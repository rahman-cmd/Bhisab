package com.softhostit.bhisab.deposit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.softhostit.bhisab.Login.SharedPrefManager;
import com.softhostit.bhisab.Login.User;
import com.softhostit.bhisab.Login.VolleySingleton;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.coustomer.CoustomerActivity;
import com.softhostit.bhisab.coustomer.CustomerAdapter;
import com.softhostit.bhisab.coustomer.CustomerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DepositActivity extends AppCompatActivity {

    private Context context;
    List<DepositModel> depositModels;
    private DepositAdapter depositAdapter;

    private ArrayList<String> customerNameArrayList, customerIdArrayList;
    private ArrayList<String> depositCategoryArrayList, depositCategoryIdArrayList, depositCategoryUserIdArrayList;

    private ArrayList<String> bankIdArrayList, bankNameArrayList;
    ProgressBar progressBar;
    ImageView noData;
    Boolean isAllFabsVisible;
    RecyclerView deposit_recycler_view;
    FloatingActionButton addCategory, add_fab, addDeposit;

    TextView add_deposit_action_text, add_category_action_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposit_activity);

        getSupportActionBar().setTitle("Deposit");

        deposit_recycler_view = findViewById(R.id.deposit_recycler_view);
        progressBar = findViewById(R.id.progressBar);
        noData = findViewById(R.id.noData);


        // FAB button
        addCategory = findViewById(R.id.addCategory);
        add_fab = findViewById(R.id.add_fab);
        addDeposit = findViewById(R.id.addDeposit);

        // Also register the action name text, of all the FABs.
        add_deposit_action_text = findViewById(R.id.add_deposit_action_text);
        add_category_action_text = findViewById(R.id.add_category_action_text);

        // Now set all the FABs and all the action name texts as GONE
        addCategory.setVisibility(View.GONE);
        addDeposit.setVisibility(View.GONE);
        add_deposit_action_text.setVisibility(View.GONE);
        add_category_action_text.setVisibility(View.GONE);

        isAllFabsVisible = false;

        add_fab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                addCategory.show();
                addDeposit.show();
                add_deposit_action_text.setVisibility(View.VISIBLE);
                add_category_action_text.setVisibility(View.VISIBLE);
                isAllFabsVisible = true;
            } else {
                addCategory.hide();
                addDeposit.hide();
                add_deposit_action_text.setVisibility(View.GONE);
                add_category_action_text.setVisibility(View.GONE);
                isAllFabsVisible = false;
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        addDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDepositDialog();
            }
        });

        depositList();


    }

    private void showAddDepositDialog() {
        loadCustomer();
        loadBankList();
        loadDepositCategory();
        // show dialog
        final Dialog dialog = new Dialog(DepositActivity.this);
        dialog.setContentView(R.layout.add_deposit_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        EditText addDepositEt = dialog.findViewById(R.id.addDepositEt);
        EditText addDepositNote = dialog.findViewById(R.id.addDepositNote);
        Button addDepositBtn = dialog.findViewById(R.id.addDepositBtn);
        TextView addDepositDate = dialog.findViewById(R.id.addDepositDate);

        TextView addDepositCustomerName = dialog.findViewById(R.id.addDepositCustomerName);
        TextView addDepositCustomerId = dialog.findViewById(R.id.addDepositCustomerId);

        TextView addDepositBankName = dialog.findViewById(R.id.addDepositBankName);
        TextView addDepositBankId = dialog.findViewById(R.id.addDepositBankId);

        TextView addDepositIncomeAccountNumber = dialog.findViewById(R.id.addDepositIncomeAccountNumber);
        TextView addDepositAccountId = dialog.findViewById(R.id.addDepositAccountId);


        addDepositIncomeAccountNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add customer category dialog start
                // get string array of categories from arraylist
                String[] depositCatAccount = new String[depositCategoryArrayList.size()];
                for (int i = 0; i < depositCategoryArrayList.size(); i++) {
                    depositCatAccount[i] = depositCategoryArrayList.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(DepositActivity.this);
                builder.setTitle("Choose Account")
                        .setItems(depositCatAccount, new DialogInterface.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // get selected category id
                                String selectedDAccountId = depositCategoryIdArrayList.get(which);
                                String selectedUserId = depositCategoryUserIdArrayList.get(which);
                                // get selected category name
                                String selectedDAccountName = depositCategoryArrayList.get(which);
                                // set category name on textview
                                addDepositIncomeAccountNumber.setText(selectedDAccountName);
                                addDepositAccountId.setText(selectedDAccountId);
                            }
                        })
                        .show();

                // add customer category dialog end
            }
        });

        addDepositCustomerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add customer category dialog start
                // get string array of categories from arraylist
                String[] categories = new String[customerNameArrayList.size()];
                for (int i = 0; i < customerNameArrayList.size(); i++) {
                    categories[i] = customerNameArrayList.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(DepositActivity.this);
                builder.setTitle("Choose Customer")
                        .setItems(categories, new DialogInterface.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // get selected category id
                                String selectedCategoryId = customerIdArrayList.get(which);
                                // get selected category name
                                String selectedCategoryName = customerNameArrayList.get(which);
                                // set category name on textview
                                addDepositCustomerName.setText(selectedCategoryName);
                                addDepositCustomerId.setText(selectedCategoryId);
                            }
                        })
                        .show();

                // add customer category dialog end

            }
        });

        addDepositBankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add customer category dialog start
                // get string array of categories from arraylist
                String[] bank = new String[bankNameArrayList.size()];
                for (int i = 0; i < bankNameArrayList.size(); i++) {
                    bank[i] = bankNameArrayList.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(DepositActivity.this);
                builder.setTitle("Choose Account")
                        .setItems(bank, new DialogInterface.OnClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // get selected category id
                                String selectedAccountId = bankIdArrayList.get(which);
                                // get selected category name
                                String selectedAccountName = bankNameArrayList.get(which);
                                // set category name on textview
                                addDepositBankName.setText(selectedAccountName);
                                addDepositBankId.setText(selectedAccountId);
                            }
                        })
                        .show();

                // add customer category dialog end
            }
        });


        addDepositDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(addDepositDate);
            }
        });


        addDepositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String deposit = addDepositEt.getText().toString().trim();
                String note = addDepositNote.getText().toString().trim();
                String date = addDepositDate.getText().toString().trim();
                String customerName = addDepositCustomerName.getText().toString().trim();
                String addDepositBank = addDepositBankName.getText().toString().trim();
                String addDepositIncomeAccount = addDepositIncomeAccountNumber.getText().toString().trim();

                int bankId = Integer.parseInt(addDepositBankId.getText().toString().trim());
                int id = Integer.parseInt(addDepositCustomerId.getText().toString().trim());
                int addDepositId = Integer.parseInt(addDepositAccountId.getText().toString().trim());

                if (customerName.isEmpty()) {
                    addDepositCustomerName.setError("Select customer");
                    addDepositCustomerName.requestFocus();
                    return;
                }

                if (addDepositBank.isEmpty()) {
                    addDepositBankName.setError("Select account");
                    addDepositBankName.requestFocus();
                    return;
                }

                if (addDepositIncomeAccount.isEmpty()) {
                    addDepositIncomeAccountNumber.setError("Select account");
                    addDepositIncomeAccountNumber.requestFocus();
                    return;
                }

                if (deposit.isEmpty()) {
                    addDepositEt.setError("Enter deposit amount");
                    addDepositEt.requestFocus();
                    return;
                }

                if (date.isEmpty()) {
                    addDepositDate.setError("Select date");
                    addDepositDate.requestFocus();
                    return;
                }


                addDeposit(deposit, note, date, id, bankId, addDepositId);
                dialog.dismiss();
            }
        });


    }

    private void loadDepositCategory() {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");

        // show list of deposit category
        depositCategoryArrayList = new ArrayList<>();
        depositCategoryIdArrayList = new ArrayList<>();
        depositCategoryUserIdArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.CATEGORY_ITEM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("categories");
//                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);

                        int id = object.getInt("id");
                        int user_id = object.getInt("user_id");
                        String cattitle = object.getString("name");

                        depositCategoryIdArrayList.add(String.valueOf(id));
                        depositCategoryUserIdArrayList.add(String.valueOf(user_id));
                        depositCategoryArrayList.add(cattitle);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DepositActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
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

    private void loadBankList() {
        // show list of customer here form database
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int user_id = intent.getIntExtra("user_id", 0);


        bankIdArrayList = new ArrayList<>();
        bankNameArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BANK_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);


                        int id = object.getInt("id");
                        String acctitle = object.getString("acctitle");

                        bankIdArrayList.add(String.valueOf(id));
                        bankNameArrayList.add(acctitle);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DepositActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("domain", domain);
                params.put("username", username);
                params.put("id", user_id + "");
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }

    // load customer data
    private void loadCustomer() {
        // show list of customer here form database
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");

        // show data in recycler view
        customerNameArrayList = new ArrayList<>();
        customerIdArrayList = new ArrayList<>();
        // add data to array list
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.CLIENT_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json array
                            JSONArray array = new JSONArray(response);
                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {
                                try {
                                    //getting product object from json array
                                    JSONObject productObject = array.getJSONObject(i);

                                    int id = productObject.getInt("id");
                                    String customerName = productObject.getString("fname");
                                    customerIdArrayList.add(String.valueOf(id));
                                    customerNameArrayList.add(customerName);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    private void addDeposit(String deposit, String note, String date, int id, int bankId, int addDepositId) {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int user_id = intent.getIntExtra("user_id", 0);
        String account = depositModels.get(0).getAccount();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.DEPOSIT_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
                        Toasty.success(DepositActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        depositList();
                    } else {
                        Toasty.error(DepositActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(DepositActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                // date convert to timestamp
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                long ts = 0;
                try {
                    ts = dateFormat.parse(date).getTime() / 1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("TAG", "onDateSet: " + ts);


                params.put("domain", domain);
                params.put("user_id", user_id + "");
                params.put("client_id", id + "");
                params.put("date", ts + "");
                params.put("account", bankId + "");
                params.put("des", note);
                params.put("amount", deposit);
                params.put("in_cat", addDepositId + "");


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void showAlertDialog() {
        // show dialog
        final Dialog dialog = new Dialog(DepositActivity.this);
        dialog.setContentView(R.layout.add_category_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        EditText addCategory = dialog.findViewById(R.id.addCategory);
        Button addCategoryBtn = dialog.findViewById(R.id.addCategoryBtn);

        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = addCategory.getText().toString().trim();
                if (category.isEmpty()) {
                    addCategory.setError("Enter Deposit Category");
                    addCategory.requestFocus();
                    return;
                }
                addCategory(category);
                dialog.dismiss();
            }
        });
    }

    private void showDatePickerDialog(TextView addDepositDate) {
        // get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(DepositActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // set date to text view
                addDepositDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void addCategory(String category) {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int user_id = intent.getIntExtra("user_id", 0);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.CATEGORY_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getBoolean("error")) {
                        Toasty.success(DepositActivity.this, jsonObject.getString("message"), Toasty.LENGTH_SHORT).show();
                        depositList();
                    } else {
                        Toasty.error(DepositActivity.this, jsonObject.getString("message"), Toasty.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasty.error(DepositActivity.this, "Error: " + e.toString(), Toasty.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toasty.error(DepositActivity.this, "Error: " + error.toString(), Toasty.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", category);
                params.put("auto_select", "");
                params.put("no_delete", "");
                params.put("user_id", user_id + "");
                params.put("ct", "");
                params.put("username", username);
                params.put("domain", domain);
                return params;
            }
        };
        VolleySingleton.getInstance(DepositActivity.this).addToRequestQueue(stringRequest);
    }

    private void depositList() {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int user_id = intent.getIntExtra("user_id", 0);

        // show data in recycler view
        depositModels = new ArrayList<>();
        // add data to array list
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.DEPOSIT_LIST,
                // show circular progress bar
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        deposit_recycler_view.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() == 0) {
                                noData.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DepositModel depositModel = new DepositModel();
                                    JSONObject dataobj = jsonArray.getJSONObject(i);
                                    depositModel.setId(dataobj.getInt("id"));
                                    depositModel.setAccount(dataobj.getString("account"));
                                    depositModel.setDate(dataobj.getInt("date"));
                                    depositModel.setAmount(dataobj.getInt("amount"));
                                    depositModel.setUser_id(dataobj.getInt("user_id"));
                                    depositModel.setPayer(dataobj.getInt("payer"));
                                    depositModel.setIn_cat(dataobj.getString("name"));
                                    depositModel.setDes(dataobj.getString("des"));
                                    depositModel.setDomain(dataobj.getString("domain"));
                                    depositModel.setFname(dataobj.getString("fname"));
                                    depositModels.add(depositModel);
                                }
                            }


                            //creating adapter object and setting it to recyclerview
                            //setup adapter
                            depositAdapter = new DepositAdapter(DepositActivity.this, depositModels);
                            // set adapter to recyclerview
                            deposit_recycler_view.setHasFixedSize(true);
                            // set LayoutManager to RecyclerView
                            deposit_recycler_view.setLayoutManager(new LinearLayoutManager(DepositActivity.this));
                            deposit_recycler_view.setAdapter(depositAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(DepositActivity.this, "Error 1: " + e.toString(), Toasty.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        deposit_recycler_view.setVisibility(View.VISIBLE);
                        Toasty.error(DepositActivity.this, "Something went wrong", Toasty.LENGTH_SHORT).show();
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
}