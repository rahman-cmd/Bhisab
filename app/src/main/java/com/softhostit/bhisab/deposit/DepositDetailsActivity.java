package com.softhostit.bhisab.deposit;

import static com.softhostit.bhisab.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.softhostit.bhisab.DeviceListActivity;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.utils.IPrintToPrinter;
import com.softhostit.bhisab.utils.PrefMng;
import com.softhostit.bhisab.utils.Tools;
import com.softhostit.bhisab.utils.WoosimPrnMng;
import com.softhostit.bhisab.utils.printerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class DepositDetailsActivity extends AppCompatActivity {
    TextView total_amount, deposit_info;
    Button btn_thermal_printer, disconnect_printer;
    private static final int REQUEST_CONNECT = 100;

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
        getSupportActionBar().setTitle("Deposit Details");

        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        int time = intent.getIntExtra("time", 0);
        String account = intent.getStringExtra("account");
        int date = intent.getIntExtra("date", 0);
        int amount = intent.getIntExtra("amount", 0);
        int user_id = intent.getIntExtra("user_id", 0);
        int payer = intent.getIntExtra("payer", 0);
        String in_cat = intent.getStringExtra("in_cat");
        String des = intent.getStringExtra("des");
        String domain = intent.getStringExtra("domain");

        // Timestamp To Date Converter
        Date date1 = new Date(date * 1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/LLL/yyyy");
        String formatted = simpleDateFormat.format(date1);


        total_amount.setText("টাকার পরিমানঃ " + amount);
        deposit_info.setText("কাস্টমার নাম : " + payer + "\nতারিখ: " + formatted + "\nভা.নং : " + id + "\nবিবরণ : " + des + "\nখাত : " + in_cat);

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
                    int time = intent.getIntExtra("time", 0);
                    String account = intent.getStringExtra("account");
                    int date = intent.getIntExtra("date", 0);
                    int amount = intent.getIntExtra("amount", 0);
                    int user_id = intent.getIntExtra("user_id", 0);
                    int payer = intent.getIntExtra("payer", 0);
                    String in_cat = intent.getStringExtra("in_cat");
                    String des = intent.getStringExtra("des");
                    String domain = intent.getStringExtra("domain");

                    IPrintToPrinter testPrinter = new DepositModel(id, time, account, date, amount, user_id, payer, in_cat, des, domain, getApplicationContext());
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