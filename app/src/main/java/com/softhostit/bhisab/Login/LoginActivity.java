package com.softhostit.bhisab.Login;

import static com.softhostit.bhisab.R.id;
import static com.softhostit.bhisab.R.layout;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class LoginActivity extends AppCompatActivity {

    EditText domainName;
    Button domainSubmit;
    TextView showDomain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login);



        if (getSharedPreferences("domain", MODE_PRIVATE).getString("domain", null) != null) {
            // redirect to home activity
            showDomain = findViewById(id.showDomain);
            showDomain.setText(getSharedPreferences("domain", MODE_PRIVATE).getString("domain", null)); // get domain name from shared preference

        } else {
            // show dialog
           /* btnDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(LoginActivity.this);
                    dialog.setContentView(layout.dialog_login);
                    dialog.setTitle("Title...");

                    domainName = dialog.findViewById(id.domainName);
                    domainSubmit = dialog.findViewById(id.domainSubmit);

                    domainSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor editor = getSharedPreferences("domain", MODE_PRIVATE).edit();
                            editor.putString("domain", domainName.getText().toString());
                            editor.apply();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });*/
            showDialog();
        }



    }

    private void showDialog() {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(layout.dialog_login);
        dialog.setTitle("Title...");

        domainName = dialog.findViewById(id.domainName);
        domainSubmit = dialog.findViewById(id.domainSubmit);

        domainSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("domain", MODE_PRIVATE).edit();
                editor.putString("domain", domainName.getText().toString());
                editor.apply();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}