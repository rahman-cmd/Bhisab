package com.softhostit.bhisab.invoice;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.DeviceListActivity;
import com.softhostit.bhisab.Login.SharedPrefManager;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.pdf_report.BarCodeEncoder;
import com.softhostit.bhisab.pdf_report.PDFTemplate;
import com.softhostit.bhisab.utils.IPrintToPrinter;
import com.softhostit.bhisab.utils.PrefMng;
import com.softhostit.bhisab.utils.Tools;
import com.softhostit.bhisab.utils.WoosimPrnMng;
import com.softhostit.bhisab.utils.printerFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class InvoiceDetailsActivity extends AppCompatActivity {

    RecyclerView recycler;
    TextView txt_total_price, txt_tax, txt_discount, txt_total_cost;


    Button btn_thermal_printer, disconnect_printer, btn_pdf_receipt;
    private static final int REQUEST_CONNECT = 100;

    private PDFTemplate templatePDF;
    Bitmap bm = null;
    String longText, shortText, userName;
    private String[] header = {"Description", "Price"};

    private WoosimPrnMng mPrnMng = null;

    private ArrayList<OrderItemModel> orderItemModelArrayList;
    private OrderDetailsAdapter orderDetailsAdapter;


    String invoiceId, shopName, orderDate, orderPrice, customerName, tax, discounts, shopAddress, shopEmail, shopContact;
    double calculatedTotalPrice;


    DecimalFormat f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);

        recycler = findViewById(R.id.recycler);
        txt_total_price = findViewById(R.id.txt_total_price);
        txt_tax = findViewById(R.id.txt_tax);
        txt_discount = findViewById(R.id.txt_discount);
        txt_total_cost = findViewById(R.id.txt_total_cost);
        btn_pdf_receipt = findViewById(R.id.btn_pdf_receipt);
        btn_thermal_printer = findViewById(R.id.btn_thermal_printer);
        disconnect_printer = findViewById(R.id.disconnect_printer);

        f = new DecimalFormat("#0.00");

        Intent intent = getIntent();
        int invoice_id = intent.getIntExtra("invoice_id", 0);
        int invoice_id_customer = intent.getIntExtra("invoice_id_customer", 0);
        String formatted = intent.getStringExtra("formatted");
        int client_id = intent.getIntExtra("client_id", 0);
        int discount = intent.getIntExtra("discount", 0);
        String discount_type = intent.getStringExtra("discount_type");
        int vat = intent.getIntExtra("vat", 0);
        String vat_type = intent.getStringExtra("vat_type");
        int total = intent.getIntExtra("total", 0);
        int total_payment = intent.getIntExtra("total_payment", 0);
        int due = intent.getIntExtra("due", 0);
        String name = intent.getStringExtra("name");
        String cname = intent.getStringExtra("cname");
        String phone1 = intent.getStringExtra("phone1");
        int pre_due = intent.getIntExtra("pre_due", 0);
        String address = intent.getStringExtra("address");

        txt_discount.setText("Discount: " + discount);
        txt_tax.setText("VAT/TAX: " + vat);
        txt_total_price.setText("Total Price: " + total);
        txt_total_cost.setText("Total Payment: " + total_payment);



        loadInvoiceItems();

        orderItemModelArrayList = new ArrayList<>();

        //for Android 11
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent1.setData(uri);
                startActivity(intent1);
            }
        }


        // storage permission for Android 10 and below devices (API level 29 and below)
        Dexter.withActivity(this)
                .withPermissions(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new com.karumi.dexter.listener.multi.MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            Toasty.success(InvoiceDetailsActivity.this, "Permissions granted!", Toast.LENGTH_SHORT, true).show();
                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Toasty.error(InvoiceDetailsActivity.this, "Permissions are denied!", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                    }
                }).check();


        BarCodeEncoder qrCodeEncoder = new BarCodeEncoder();
        try {
            bm = qrCodeEncoder.encodeAsBitmap("www.bhisab.com", BarcodeFormat.CODE_128, 600, 300);
        } catch (WriterException e) {
            Log.d("Data", e.toString());
        }


        //for pdf report
        shortText = "Customer Name: " + name;
        longText = "< Have a nice day. Visit again >";


        btn_thermal_printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if the Bluetooth is available and on.
                if (!Tools.isBlueToothOn(InvoiceDetailsActivity.this)) return;

               /* PrefMng.saveActivePrinter(DepositDetailsActivity.this, PrefMng.PRN_WOOSIM_SELECTED);

                //Pick a Bluetooth device
                Intent i = new Intent(DepositDetailsActivity.this, DeviceListActivity.class);
                startActivityForResult(i, REQUEST_CONNECT);*/

                // get bluetooth printer address
                // get address from shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("bluetooth_address", MODE_PRIVATE);
                String address = sharedPreferences.getString("address", "");


                // get printer instance

                if (address.equals("")) {
                    // if address is null, then open device list activity
                    //Print to the printer
                    Intent intent = new Intent(InvoiceDetailsActivity.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT);

                } else {
                    // if address is not null, then print to the printer
                    PrefMng.saveActivePrinter(InvoiceDetailsActivity.this, PrefMng.PRN_WOOSIM_SELECTED);


                    IPrintToPrinter testPrinter = new TestPrinter(InvoiceDetailsActivity.this, orderItemModelArrayList);
                    //Connect to the printer and after successful connection issue the print command.
                    mPrnMng = printerFactory.createPrnMng(getApplicationContext(), address, testPrinter);
                }
            }
        });

        disconnect_printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // clear shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("bluetooth_address", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                Toasty.success(InvoiceDetailsActivity.this, "Printer Disconnected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadInvoiceItems() {
        // get domain name and user_id from sher prefarence
        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        String domain = sharedPrefManager.getUser().getDomain();
        int user_id = sharedPrefManager.getUser().getId();

        Intent intent = getIntent();
        int invoice_id = intent.getIntExtra("invoice_id", 0);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.INVOICE_VIEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        OrderItemModel orderItemModel = new OrderItemModel();
                        orderItemModel.setItem_id(jsonArray.getJSONObject(i).getInt("item_id"));
                        orderItemModel.setName(jsonArray.getJSONObject(i).getString("name"));
                        orderItemModel.setDescription(jsonArray.getJSONObject(i).getString("description"));
                        orderItemModel.setQuantity(jsonArray.getJSONObject(i).getInt("quantity"));
                        orderItemModel.setReturn_quantity(jsonArray.getJSONObject(i).getInt("return_quantity"));
                        orderItemModel.setPrice(jsonArray.getJSONObject(i).getInt("price"));
                        orderItemModel.setDiscount(jsonArray.getJSONObject(i).getInt("discount"));
                        orderItemModel.setUnit(jsonArray.getJSONObject(i).getString("unit"));
                        orderItemModel.setLine_total(jsonArray.getJSONObject(i).getInt("line_total"));
                        orderItemModelArrayList.add(orderItemModel);
                    }

                    orderDetailsAdapter = new OrderDetailsAdapter(InvoiceDetailsActivity.this, orderItemModelArrayList);
                    recycler.setAdapter(orderDetailsAdapter);
                    recycler.setHasFixedSize(true);
                    recycler.setLayoutManager(new LinearLayoutManager(InvoiceDetailsActivity.this));

                    Log.d("jsonArray", jsonArray.toString());


                    if (!jsonObject.getBoolean("error")) {

                        Toasty.success(InvoiceDetailsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {

                        Toasty.error(InvoiceDetailsActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(InvoiceDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("domain", domain);
                params.put("user_id", user_id + "");
                params.put("invoice_id", invoice_id + "");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CONNECT && resultCode == RESULT_OK) {
            try {
                //Get device address to print to.
                String blutoothAddr = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                // Save the address to the preferences
                SharedPreferences pref = getSharedPreferences("bluetooth_address", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("address", blutoothAddr);
                editor.apply();

            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (mPrnMng != null) mPrnMng.releaseAllocatoins();
        super.onDestroy();
    }
}