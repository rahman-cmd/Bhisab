package com.softhostit.bhisab.invoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class InvoiceActivity extends AppCompatActivity {

    private ArrayList<InvoiceModel> invoiceModelArrayList;
    private ArrayList<ClientDetailsModel> clientDetailsModelArrayList;
    private InvoiceAdapter invoiceAdapter;

    private RecyclerView invoiceRV;


    private int start = 0;
    private int perPage = 10;
    private boolean isLoading = false;
    private LinearLayoutManager layoutManager;
    private int VISIBLE_THRESHOLD = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        invoiceRV = findViewById(R.id.invoiceRV);

        invoiceModelArrayList = new ArrayList<>();
        clientDetailsModelArrayList = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);

        loadAllInvoice();

        // Implement the OnScrollListener for the RecyclerView
        invoiceRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    start += perPage;
                    loadAllInvoice();
                    isLoading = true;
                }
            }
        });


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
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        String currency = object.getString("currency");
                        String type = object.getString("type");
                        int invoice_id_custom = object.getInt("invoice_id_custom");
                        int invoice_id = object.getInt("invoice_id");
                        String date_issue = object.getString("date_issue");
                        int client_id = object.getInt("client_id");
                        int discount = object.getInt("discount");
                        String discount_type = object.getString("discount_type");
                        int vat = object.getInt("vat");

                        // VAT TYPE is null in some cases so we need to check it first before adding NULL = 0
                        String vat_type = object.getString("vat_type");
                        if (vat_type.equals("null")) {
                            vat_type = "0";
                        } else {
                            vat_type = object.getString("vat_type");
                        }

                        int total = object.getInt("total");
                        int total_payment = object.getInt("total_payment");
                        int due = object.getInt("due");
                        int due_collect_date = object.getInt("due_collect_date");

                        // get client_details

                        JSONObject client_details = object.getJSONObject("client_details");
                        String name = client_details.getString("name");
                        String cname = client_details.getString("cname");
                        String phone1 = client_details.getString("phone1");
                        // pre_due is null in some cases so we need to check it first before adding NULL = 0
                        int pre_due = 0;
                        if (client_details.isNull("pre_due")) {
                            pre_due = 0;
                        } else {
                            pre_due = client_details.getInt("pre_due");
                        }
                        String address = client_details.getString("address");

                        clientDetailsModelArrayList.add(new ClientDetailsModel(name, cname, phone1, pre_due, address));
                        invoiceModelArrayList.add(new InvoiceModel(currency, type, invoice_id_custom, invoice_id, date_issue, client_id, discount, discount_type, vat, vat_type, total, total_payment, due, due_collect_date));

                    }

                    invoiceAdapter = new InvoiceAdapter(InvoiceActivity.this, invoiceModelArrayList, clientDetailsModelArrayList);
                    invoiceRV.setAdapter(invoiceAdapter);

                    // set adapter to recyclerview
                    invoiceRV.setHasFixedSize(true);
                    invoiceRV.setLayoutManager(layoutManager);

                    if (!jsonObject.getBoolean("error")) {
                        Toasty.success(InvoiceActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.error(InvoiceActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    // Notify the adapter that the data has changed
                    invoiceAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                isLoading = false;

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
                params.put("start", String.valueOf(start));
                params.put("per_page", String.valueOf(perPage));
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}