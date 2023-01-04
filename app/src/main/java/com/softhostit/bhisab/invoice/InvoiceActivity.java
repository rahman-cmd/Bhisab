package com.softhostit.bhisab.invoice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.expense.ExpenseActivity;
import com.softhostit.bhisab.product.Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class InvoiceActivity extends AppCompatActivity {

    private ArrayList<InvoiceModel> invoiceModelArrayList;
    private ArrayList<ClientDetailsModel> clientDetailsModelArrayList;
    private InvoiceAdapter invoiceAdapter;

    private RecyclerView invoiceRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        invoiceRV = findViewById(R.id.invoiceRV);

        invoiceModelArrayList = new ArrayList<>();
        clientDetailsModelArrayList = new ArrayList<>();


        loadAllInvoice();
    }

    private void loadAllInvoice() {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        int user_id = intent.getIntExtra("user_id", 0);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ALL_INVOICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    InvoiceModel invoiceModel = new InvoiceModel();
                    ClientDetailsModel clientDetailsModel = new ClientDetailsModel();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        invoiceModel.setCurrency(object.getString("currency"));
                        invoiceModel.setType(object.getString("type"));
                        invoiceModel.setInvoice_id_custom(object.getInt("invoice_id_custom"));
                        invoiceModel.setInvoice_id(object.getInt("invoice_id"));
                        invoiceModel.setDate_issue(object.getString("date_issue"));
                        invoiceModel.setClient_id(object.getInt("client_id"));
                        invoiceModel.setDiscount(object.getInt("discount"));
                        invoiceModel.setDiscount_type(object.getString("discount_type"));
                        invoiceModel.setVat(object.getInt("vat"));
                        invoiceModel.setVat_type(object.getString("vat_type"));
                        invoiceModel.setTotal(object.getInt("total"));
                        invoiceModel.setTotal_payment(object.getInt("total_payment"));
                        invoiceModel.setDue(object.getInt("due"));
                        invoiceModel.setDue_collect_date(object.getInt("due_collect_date"));
                        // get client_details
                        JSONObject client_details = object.getJSONObject("client_details");

                        String name = client_details.getString("name");
                        String cname = client_details.getString("cname");
                        String phone1 = client_details.getString("phone1");
                        int pre_due = client_details.getInt("pre_due");
                        String address = client_details.getString("address");


                        clientDetailsModel.setName(name);
                        clientDetailsModel.setCname(cname);
                        clientDetailsModel.setPhone1(phone1);
                        clientDetailsModel.setPre_due(pre_due);
                        clientDetailsModel.setAddress(address);

                        clientDetailsModelArrayList.add(clientDetailsModel);

                        Log.d("client_details", clientDetailsModel.getName());

                        invoiceModelArrayList.add(invoiceModel);
                    }

                    invoiceAdapter = new InvoiceAdapter(InvoiceActivity.this, invoiceModelArrayList);
                    invoiceRV.setAdapter(invoiceAdapter);

                    // set adapter to recyclerview
                    invoiceRV.setHasFixedSize(true);
                    invoiceRV.setLayoutManager(new LinearLayoutManager(InvoiceActivity.this));

                    if (!jsonObject.getBoolean("error")) {
                        Toasty.success(InvoiceActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.error(InvoiceActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toasty.error(InvoiceActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("domain", domain);
                params.put("user_id", user_id + "");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}