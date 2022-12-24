package com.softhostit.bhisab.expense;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ExpenseActivity extends AppCompatActivity {

    RecyclerView expenseRecyclerView;
    List<ExpenseModel> expenseModelList;
    private ExpenseAdapter expenseAdapter;

    private ArrayList<String> bankIdArrayList, bankNameArrayList;
    private ArrayList<String> categoryIdArrayList, categoryNameArrayList;

    FloatingActionButton add_fab, addCategory, addExpense, addExpenseBankAccount;

    TextView add_expense_action_text, add_expense_bank_text, add_category_action_text;


    // to check whether sub FAB buttons are visible or not.
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        getSupportActionBar().setTitle("Expense");


        expenseRecyclerView = findViewById(R.id.expenseRecyclerView);
        expenseList();

        add_fab = findViewById(R.id.add_fab);
        addCategory = findViewById(R.id.addCategory);
        addExpense = findViewById(R.id.addExpense);
        addExpenseBankAccount = findViewById(R.id.addExpenseBankAccount);


        add_expense_action_text = findViewById(R.id.add_expense_action_text);
        add_expense_bank_text = findViewById(R.id.add_expense_bank_text);
        add_category_action_text = findViewById(R.id.add_category_action_text);

        isAllFabsVisible = false;

        // Now set all the FABs and all the action name texts as GONE
        addCategory.setVisibility(View.GONE);
        addExpense.setVisibility(View.GONE);
        addExpenseBankAccount.setVisibility(View.GONE);
        add_expense_action_text.setVisibility(View.GONE);
        add_category_action_text.setVisibility(View.GONE);
        add_expense_bank_text.setVisibility(View.GONE);


        add_fab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                addCategory.show();
                addExpense.show();
                addExpenseBankAccount.show();
                add_expense_bank_text.setVisibility(View.VISIBLE);
                add_category_action_text.setVisibility(View.VISIBLE);
                add_expense_action_text.setVisibility(View.VISIBLE);
                isAllFabsVisible = true;
            } else {
                addCategory.hide();
                addExpense.hide();
                addExpenseBankAccount.hide();
                add_expense_action_text.setVisibility(View.GONE);
                add_category_action_text.setVisibility(View.GONE);
                add_expense_bank_text.setVisibility(View.GONE);
                isAllFabsVisible = false;
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryAlertDialog();
            }
        });

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExpenseDialog();
            }
        });

        addExpenseBankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDepositBankAccountDialog();
            }
        });




    }

    private void showAddExpenseDialog() {
        loadBankList();
        loadSector();
        final Dialog dialog = new Dialog(ExpenseActivity.this);
        dialog.setContentView(R.layout.add_expense_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        TextView addExpenseName = dialog.findViewById(R.id.addExpenseName);
        TextView addExpenseNameId = dialog.findViewById(R.id.addExpenseNameId);

        TextView addExpenseBankName = dialog.findViewById(R.id.addExpenseBankName);
        TextView addExpenseBankId = dialog.findViewById(R.id.addExpenseBankId);

        EditText addExpenseAmount = dialog.findViewById(R.id.addExpenseAmount);
        TextView addExpenseDate = dialog.findViewById(R.id.addExpenseDate);
        EditText addExpenseDescription = dialog.findViewById(R.id.addExpenseDescription);
        Button addExpenseButton = dialog.findViewById(R.id.addExpenseButton);

        addExpenseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(addExpenseDate);
            }
        });

        addExpenseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] category = new String[categoryNameArrayList.size()];
                for (int i = 0; i < categoryNameArrayList.size(); i++) {
                    category[i] = categoryNameArrayList.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseActivity.this);
                builder.setTitle("Select Category");
                builder.setItems(category, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // get selected category id
                        String selectedExpenseId = categoryIdArrayList.get(which);
                        // get selected category name
                        String selectedExpenseName = categoryNameArrayList.get(which);
                        // set category name on textview
                        addExpenseName.setText(selectedExpenseName);
                        addExpenseNameId.setText(selectedExpenseId);

                    }
                });
                builder.show();
            }
        });

        addExpenseBankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] bank = new String[bankNameArrayList.size()];
                for (int i = 0; i < bankNameArrayList.size(); i++) {
                    bank[i] = bankNameArrayList.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseActivity.this);
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
                                addExpenseBankName.setText(selectedAccountName);
                                addExpenseBankId.setText(selectedAccountId);
                            }
                        })
                        .show();

                // add customer category dialog end
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseList();
                String expenseName = addExpenseName.getText().toString();
                String expenseBankName = addExpenseBankName.getText().toString();
                String expenseDate = addExpenseDate.getText().toString();
                String expenseDescription = addExpenseDescription.getText().toString();

                String addExpenseId = addExpenseNameId.getText().toString().trim();
                String addExpenseBankAccountId = addExpenseBankId.getText().toString().trim();
                String addExpenseAmountInt = addExpenseAmount.getText().toString().trim();

                if (expenseName.isEmpty()) {
                    Toasty.error(ExpenseActivity.this, "Sector Name is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (expenseBankName.isEmpty()) {
                    Toasty.error(ExpenseActivity.this, "Expense Bank Name is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (addExpenseAmountInt.isEmpty()) {
                    addExpenseAmount.setError("Expense Amount is required");
                    addExpenseAmount.requestFocus();
                    return;
                }

                if (expenseDate.isEmpty()) {
                    Toasty.error(ExpenseActivity.this, "Expense Date is required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (expenseDescription.isEmpty()) {
                    addExpenseDescription.setError("Expense Description is required");
                    addExpenseDescription.requestFocus();
                    return;
                }

                addExpense(addExpenseId, addExpenseBankAccountId, addExpenseAmountInt, expenseDate, expenseDescription);
                dialog.dismiss();
            }
        });
    }

    private void showDatePicker(TextView addExpenseDate) {
        // get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(ExpenseActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // set date to text view
                addExpenseDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void addExpense(String addExpenseId, String addExpenseBankAccountId, String addExpenseAmountInt, String expenseDate, String expenseDescription) {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int user_id = intent.getIntExtra("user_id", 0);

        // show progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(ExpenseActivity.this);
        progressDialog.setMessage("Adding Expense...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.EXPENSE_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (!jsonObject.getBoolean("error")) {
//                        Toasty.success(ExpenseActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        expenseList();
                    } else {
                        progressDialog.dismiss();
                        Toasty.error(ExpenseActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toasty.error(ExpenseActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                // date convert to timestamp
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                long ts = 0;
                try {
                    ts = dateFormat.parse(expenseDate).getTime() / 1000;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                params.put("domain", domain);
                params.put("username", username);
                params.put("user_id", user_id + "");
                params.put("date", ts + "");
                params.put("account", addExpenseBankAccountId + "");
                params.put("des", expenseDescription);
                params.put("amount", addExpenseAmountInt + "");
                params.put("in_cat", addExpenseId + "");


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void showAddCategoryAlertDialog() {
        Dialog dialog = new Dialog(ExpenseActivity.this);
        dialog.setContentView(R.layout.add_expense_cat_dialog);

        EditText sector_name = dialog.findViewById(R.id.sector_name);
        Button addSectorBtn = dialog.findViewById(R.id.addSectorBtn);

        addSectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sector = sector_name.getText().toString();

                if (sector.isEmpty()) {
                    sector_name.setError("Sector Name is required");
                    sector_name.requestFocus();
                    return;
                }


                addSector(sector);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addSector(String sector) {
    }

    private void showAddDepositBankAccountDialog() {
        Dialog dialog = new Dialog(ExpenseActivity.this);
        dialog.setContentView(R.layout.add_deposit_bank_account_dialog);

        EditText bank_name = dialog.findViewById(R.id.addBankName);
        Button addBankBtn = dialog.findViewById(R.id.addBankBtn);

        addBankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankName = bank_name.getText().toString();

                if (bankName.isEmpty()) {
                    bank_name.setError("Bank Name is required");
                    bank_name.requestFocus();
                    return;
                }


                addExpenseBankAccount(bankName);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addExpenseBankAccount(String bankName) {
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");
        int userId = intent.getIntExtra("user_id", 0);

        // show the loading progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(ExpenseActivity.this);
        progressDialog.setMessage("Adding Bank Account...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.BANK_CREATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (!jsonObject.getBoolean("error")) {
//                        Toasty.success(ExpenseActivity.this, jsonObject.getString("message"), Toasty.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
                        progressDialog.dismiss();
                        Toasty.error(ExpenseActivity.this, jsonObject.getString("message"), Toasty.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toasty.error(ExpenseActivity.this, "Error: " + e.toString(), Toasty.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toasty.error(ExpenseActivity.this, "Error: " + error.toString(), Toasty.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("acctitle", bankName);
                params.put("user_id", userId + "");
                params.put("des", "");
                params.put("ini_balan", "");
                params.put("acc_number", "");
                params.put("con_per", "");
                params.put("phone", "");
                params.put("username", username);
                params.put("domain", domain);
                return params;
            }
        };
        VolleySingleton.getInstance(ExpenseActivity.this).addToRequestQueue(stringRequest);
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
                Toast.makeText(ExpenseActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void loadSector(){
        // show list of customer here form database
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");


        categoryIdArrayList = new ArrayList<>();
        categoryNameArrayList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.EXPENSE_CATEGORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("categories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String name = object.getString("name");

                        categoryIdArrayList.add(String.valueOf(id));
                        categoryNameArrayList.add(name);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ExpenseActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void expenseList() {
        // show list of customer here form database
        Intent intent = getIntent();
        String domain = intent.getStringExtra("domain");
        String username = intent.getStringExtra("username");

        expenseModelList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.EXPENSE_LIST,
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
                                    ExpenseModel model = new ExpenseModel();
                                    model.setIn_cat(productObject.getInt("in_cat"));
                                    model.setAccount(productObject.getInt("account"));
                                    model.setDate(productObject.getInt("date"));
                                    model.setDes(productObject.getString("des"));
                                    model.setAmount(productObject.getInt("amount"));

                                    expenseModelList.add(model);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            //creating adapter object and setting it to recyclerview
                            //setup adapter
                            expenseAdapter = new ExpenseAdapter(getApplicationContext(), expenseModelList);
                            // set adapter to recyclerview
                            expenseRecyclerView.setHasFixedSize(true);
                            expenseRecyclerView.setLayoutManager(new LinearLayoutManager(ExpenseActivity.this));
                            expenseRecyclerView.setAdapter(expenseAdapter);

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
}