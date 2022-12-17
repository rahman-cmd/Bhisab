package com.softhostit.bhisab.deposit;

import static android.os.Build.VERSION.SDK_INT;
import static com.softhostit.bhisab.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.DeviceListActivity;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.pdf_report.BarCodeEncoder;
import com.softhostit.bhisab.pdf_report.PDFTemplate;
import com.softhostit.bhisab.utils.IPrintToPrinter;
import com.softhostit.bhisab.utils.PrefMng;
import com.softhostit.bhisab.utils.Tools;
import com.softhostit.bhisab.utils.WoosimPrnMng;
import com.softhostit.bhisab.utils.printerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class DepositDetailsActivity extends AppCompatActivity {
    TextView total_amount, deposit_info, btn_pdf_receipt;
    Button btn_thermal_printer, disconnect_printer;
    private static final int REQUEST_CONNECT = 100;

    private PDFTemplate templatePDF;
    Bitmap bm = null;
    String longText, shortText, userName;
    private String[] header = {"Description", "Price"};

    private WoosimPrnMng mPrnMng = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit_details);

        total_amount = findViewById(R.id.total_amount);
        deposit_info = findViewById(R.id.deposit_info);
        btn_thermal_printer = findViewById(R.id.btn_thermal_printer);
        disconnect_printer = findViewById(R.id.disconnect_printer);
        btn_pdf_receipt = findViewById(R.id.btn_pdf_receipt);

        getSupportActionBar().setTitle("Deposit Details");

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String account = intent.getStringExtra("account");
        int date = intent.getIntExtra("date", 0);
        int amount = intent.getIntExtra("amount", 0);
        int user_id = intent.getIntExtra("user_id", 0);
        int payer = intent.getIntExtra("payer", 0);
        String in_cat = intent.getStringExtra("name");
        String des = intent.getStringExtra("des");
        String domain = intent.getStringExtra("domain");
        String fname = intent.getStringExtra("fname");

        // Timestamp To Date Converter
        Date date1 = new Date(date * 1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/LLL/yyyy");
        String formatted = simpleDateFormat.format(date1);



        //for Android 11
        if (SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent1 = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent1.setData(uri);
                startActivity(intent1);
            }
        }

        BarCodeEncoder qrCodeEncoder = new BarCodeEncoder();
        try {
            bm = qrCodeEncoder.encodeAsBitmap(domain, BarcodeFormat.CODE_128, 600, 300);
        } catch (WriterException e) {
            Log.d("Data", e.toString());
        }


        //for pdf report
        shortText = "Customer Name: " + fname;
        longText = "< Have a nice day. Visit again >";
        templatePDF = new PDFTemplate(getApplicationContext());


        total_amount.setText("টাকার পরিমানঃ " + amount);
        deposit_info.setText("কাস্টমার নাম : " + fname + "\nতারিখ: " + formatted + "\nভা.নং : " + id + "\nবিবরণ : " + des + "\nখাত : " + in_cat);

        btn_pdf_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                templatePDF.openDocument();
                templatePDF.addMetaData(Constant.ORDER_RECEIPT, Constant.ORDER_RECEIPT, "Bhisab");
                templatePDF.addTitle(fname, formatted+ "\n Email: " + id + "\nContact: " + des + "\nInvoice ID:" + in_cat, "Total Amount: " + amount);
                templatePDF.addParagraph(shortText);
                templatePDF.createTable(header, getPDFReceipt());
                templatePDF.addImage(bm);
                templatePDF.addRightParagraph(longText);
                templatePDF.closeDocument();
                templatePDF.viewPDF();
            }
        });



        btn_thermal_printer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if the Bluetooth is available and on.
                if (!Tools.isBlueToothOn(DepositDetailsActivity.this)) return;

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
                    Intent intent = new Intent(DepositDetailsActivity.this, DeviceListActivity.class);
                    startActivityForResult(intent, REQUEST_CONNECT);

                } else {
                    // if address is not null, then print to the printer
                    PrefMng.saveActivePrinter(DepositDetailsActivity.this, PrefMng.PRN_WOOSIM_SELECTED);

                    //The interface to print text to thermal printers.
                    Intent intent = getIntent();
                    int id = intent.getIntExtra("id", 0);
                    String account = intent.getStringExtra("account");
                    int date = intent.getIntExtra("date", 0);
                    int amount = intent.getIntExtra("amount", 0);
                    int user_id = intent.getIntExtra("user_id", 0);
                    int payer = intent.getIntExtra("payer", 0);
                    String in_cat = intent.getStringExtra("name");
                    String des = intent.getStringExtra("des");
                    String domain = intent.getStringExtra("domain");
                    String fname = intent.getStringExtra("fname");

                    IPrintToPrinter testPrinter = new DepositModel(id, account, date, amount, user_id, payer, in_cat, des, domain, fname, getApplicationContext());
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

                Toasty.success(DepositDetailsActivity.this, "Printer Disconnected", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private ArrayList<String[]> getPDFReceipt() {
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Customer Name"});
        return rows;
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