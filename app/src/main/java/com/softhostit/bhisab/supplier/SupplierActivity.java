package com.softhostit.bhisab.supplier;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softhostit.bhisab.Constant;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.coustomer.CoustomerActivity;
import com.softhostit.bhisab.coustomer.CustomerAdapter;
import com.softhostit.bhisab.coustomer.CustomerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class SupplierActivity extends AppCompatActivity {
    private Context context;
    List<SupplierModel> supplierModelList;
    private SupplierAdapter supplierAdapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;

    FloatingActionButton add_fab, add_supplier_fab, add_supplier_group_fab;

    // These are taken to make visible and invisible along with FABs
    TextView add_supplier_action_text, add_supplier_group_action_text;

    // to check whether sub FAB buttons are visible or not.
    Boolean isAllFabsVisible;

    private ArrayList<String> supplierNameArrayList, supplierIdArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier);
        getSupportActionBar().setTitle("Supplier");

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        add_fab = findViewById(R.id.add_fab);
        add_supplier_fab = findViewById(R.id.add_supplier_fab);
        add_supplier_group_fab = findViewById(R.id.add_supplier_group_fab);
        add_supplier_action_text = findViewById(R.id.add_supplier_action_text);
        add_supplier_group_action_text = findViewById(R.id.add_supplier_group_action_text);


        // Now set all the FABs and all the action name texts as GONE
        add_supplier_fab.setVisibility(View.GONE);
        add_supplier_group_fab.setVisibility(View.GONE);
        add_supplier_action_text.setVisibility(View.GONE);
        add_supplier_group_action_text.setVisibility(View.GONE);

        isAllFabsVisible = false;
        supplierList();

        add_fab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                add_supplier_fab.show();
                add_supplier_group_fab.show();
                add_supplier_action_text.setVisibility(View.VISIBLE);
                add_supplier_group_action_text.setVisibility(View.VISIBLE);
                isAllFabsVisible = true;
            } else {
                add_supplier_fab.hide();
                add_supplier_group_fab.hide();
                add_supplier_action_text.setVisibility(View.GONE);
                add_supplier_group_action_text.setVisibility(View.GONE);
                isAllFabsVisible = false;
            }
        });

        add_supplier_fab.setOnClickListener(view -> {
            loadSupplierGroup();
            // show the dialog to add customer group
            final Dialog dialog = new Dialog(SupplierActivity.this);
            dialog.setContentView(R.layout.add_supplier_dialog);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            EditText addSupplierFirstName = dialog.findViewById(R.id.addSupplierFirstName);
            EditText addSupplierLastName = dialog.findViewById(R.id.addSupplierLastName);
            EditText addSupplierPhone = dialog.findViewById(R.id.addSupplierPhone);
            EditText addSupplierCompanyName = dialog.findViewById(R.id.addSupplierCompanyName);
            EditText addSupplierAddress = dialog.findViewById(R.id.addSupplierAddress);
            TextView addSupplierGroup = dialog.findViewById(R.id.addSupplierGroup);
            TextView addSupplierId = dialog.findViewById(R.id.addSupplierId);
            Button addSupplierButton = dialog.findViewById(R.id.addSupplierButton);

            addSupplierGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // add customer category dialog start
                    // get string array of categories from arraylist
                    String[] supplier = new String[supplierNameArrayList.size()];
                    for (int i = 0; i < supplierNameArrayList.size(); i++) {
                        supplier[i] = supplierNameArrayList.get(i);
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(SupplierActivity.this);
                    builder.setTitle("Choose Category")
                            .setItems(supplier, new DialogInterface.OnClickListener() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // get selected category id
                                    String selectedSupplierId = supplierIdArrayList.get(which);
                                    // get selected category name
                                    String selectedSupplierName = supplierNameArrayList.get(which);
                                    // set category name on textview
                                    addSupplierGroup.setText(selectedSupplierName);
                                    addSupplierId.setText(selectedSupplierId);
                                }
                            })
                            .show();

                    // add customer category dialog end
                }
            });


            addSupplierButton.setOnClickListener(view1 -> {
                String supplierName = addSupplierFirstName.getText().toString();
                String supplierLastName = addSupplierLastName.getText().toString();
                String supplierPhone = addSupplierPhone.getText().toString();
                String supplierCompanyName = addSupplierCompanyName.getText().toString();
                String supplierAddress = addSupplierAddress.getText().toString();
                String supplierGroup = addSupplierGroup.getText().toString();
                String supplierId = (addSupplierId.getText().toString());

                if (supplierName.isEmpty()) {
                    addSupplierFirstName.setError("Enter Supplier Name");
                    addSupplierFirstName.requestFocus();
                } else if (supplierLastName.isEmpty()) {
                    addSupplierLastName.setError("Enter Supplier Last Name");
                    addSupplierLastName.requestFocus();
                } else if (supplierPhone.isEmpty()) {
                    addSupplierPhone.setError("Enter Supplier Phone");
                    addSupplierPhone.requestFocus();
                } else if (supplierCompanyName.isEmpty()) {
                    addSupplierCompanyName.setError("Enter Supplier Company Name");
                    addSupplierCompanyName.requestFocus();
                } else if (supplierAddress.isEmpty()) {
                    addSupplierAddress.setError("Enter Supplier Address");
                    addSupplierAddress.requestFocus();
                } else if (supplierGroup.isEmpty()) {
                    Toasty.error(SupplierActivity.this, "Select Supplier Group", Toasty.LENGTH_SHORT).show();
                } else {
                    addSupplier(supplierName, supplierLastName, supplierPhone, supplierCompanyName, supplierAddress, supplierId);
                    dialog.dismiss();
                }
            });

        });

        add_supplier_group_fab.setOnClickListener(view -> {
            // show the dialog to add customer
            final Dialog dialog = new Dialog(SupplierActivity.this);
            dialog.setContentView(R.layout.add_customer_group_dialog);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            EditText addSupplierGroup = dialog.findViewById(R.id.addGroup);
            addSupplierGroup.setHint("Add Supplier Group");
            Button addGroupBtn = dialog.findViewById(R.id.addGroupBtn);
            TextView headerTitle = dialog.findViewById(R.id.headerTitle);
            headerTitle.setText("Add Supplier Group");

            addGroupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String group = addSupplierGroup.getText().toString().trim();
                    if (group.isEmpty()) {
                        addSupplierGroup.setError("Enter Supplier Group Name");
                        addSupplierGroup.requestFocus();
                        return;
                    }
                    addSupplierGroups(group);
                    dialog.dismiss();
                }
            });
        });


    }

    private void addSupplier(String supplierName, String supplierLastName, String supplierPhone, String supplierCompanyName, String supplierAddress, String supplierId) {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int user_id = intent.getIntExtra("user_id", 0);

        // add group button progress  dialog
        ProgressDialog progressDialog = new ProgressDialog(SupplierActivity.this);
        progressDialog.setMessage("Adding Supplier Group...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ADD_SUPPLIER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        supplierList();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toasty.error(SupplierActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toasty.error(SupplierActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("domain", domain);
                params.put("username", username);
                params.put("user_id", user_id + "");
                params.put("fname", supplierName);
                params.put("lname", supplierLastName);
                params.put("phone1", supplierPhone);
                params.put("cname", supplierCompanyName);
                params.put("address", supplierAddress);
                params.put("group", supplierId);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void loadSupplierGroup() {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int user_id = intent.getIntExtra("user_id", 0);

        supplierNameArrayList = new ArrayList<>();
        supplierIdArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SUPPLIER_GROUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");
                        supplierIdArrayList.add(id + "");
                        supplierNameArrayList.add(name);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(SupplierActivity.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(SupplierActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("domain", domain);
                params.put("username", username);
                params.put("user_id", user_id + "");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void addSupplierGroups(String group) {
        // add group button progress  dialog
        ProgressDialog progressDialog = new ProgressDialog(SupplierActivity.this);
        progressDialog.setMessage("Adding Supplier Group...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int user_id = intent.getIntExtra("user_id", 0);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SUPPLIER_GROUP_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getBoolean("error")) {
                        progressDialog.dismiss();
                        Toasty.success(SupplierActivity.this, jsonObject.getString("message"), Toasty.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toasty.error(SupplierActivity.this, jsonObject.getString("message"), Toasty.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toasty.error(SupplierActivity.this, "Error: " + e.toString(), Toasty.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toasty.error(SupplierActivity.this, "Error: " + error.toString(), Toasty.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", group);
                params.put("user_id", user_id + "");
                params.put("username", username);
                params.put("domain", domain);
                return params;
            }
        };
        VolleySingleton.getInstance(SupplierActivity.this).addToRequestQueue(stringRequest);
    }

    private void supplierList() {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");

        // show data in recyclerview
        supplierModelList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SUPPLIER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        try {
                            //converting response to json array
                            JSONArray array = new JSONArray(response);
                            Log.d("supplier", response);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    //getting product object from json array
                                    JSONObject productObject = array.getJSONObject(i);
                                    SupplierModel model = new SupplierModel();


                                    model.setId((productObject.getInt("id")));
                                    model.setName(productObject.getString("fname"));
                                    model.setPhone(productObject.getString("phone1"));
                                    model.setCname(productObject.getString("cname"));
                                    model.setGroup(productObject.getString("group"));
                                    model.setAddress(productObject.getString("address"));
                                    supplierModelList.add(model);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            //setup adapter
                            supplierAdapter = new SupplierAdapter(getApplicationContext(), supplierModelList);
                            // set adapter to recyclerview
                            recyclerView.setHasFixedSize(true);
//                        product_list_recycler_view.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                            recyclerView.setAdapter(supplierAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        Toasty.error(getApplicationContext(), "Something went wrong", Toasty.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("domain", domain);
                params.put("username", username);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}