package com.softhostit.bhisab.expense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.softhostit.bhisab.R;

public class ExpenseActivity extends AppCompatActivity {

    RecyclerView expenseRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);


        expenseRecyclerView = findViewById(R.id.expenseRecyclerView);

    }
}