package com.softhostit.bhisab.deposit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.pdf_report.BarCodeEncoder;
import com.softhostit.bhisab.utils.IBixolonCanvasConst;
import com.softhostit.bhisab.utils.IPrintToPrinter;
import com.softhostit.bhisab.utils.WoosimPrnMng;
import com.softhostit.bhisab.utils.printerFactory;
import com.softhostit.bhisab.utils.printerWordMng;
import com.woosim.printer.WoosimCmd;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class DepositModel implements IPrintToPrinter {
    private int id;
    private String account;
    private int date;
    private int amount;
    private int user_id;
    private int payer;
    private String in_cat;
    private String des;
    private String domain;
    private String fname;
    private Context context;
    Bitmap bm;

    public DepositModel() {
    }

    public DepositModel(int id, String account, int date, int amount, int user_id, int payer, String in_cat, String des, String domain, String fname, Context context) {
        this.id = id;
        this.account = account;
        this.date = date;
        this.amount = amount;
        this.user_id = user_id;
        this.payer = payer;
        this.in_cat = in_cat;
        this.des = des;
        this.domain = domain;
        this.fname = fname;
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPayer() {
        return payer;
    }

    public void setPayer(int payer) {
        this.payer = payer;
    }

    public String getIn_cat() {
        return in_cat;
    }

    public void setIn_cat(String in_cat) {
        this.in_cat = in_cat;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void printContent(WoosimPrnMng prnMng) {

        //Generate barcode
        BarCodeEncoder qrCodeEncoder = new BarCodeEncoder();
        bm = null;

        try {
            bm = qrCodeEncoder.encodeAsBitmap(domain, BarcodeFormat.CODE_128, 400, 48);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Date date1 = new Date(date * 1000L);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/LLL/yyyy");
        String formatted = simpleDateFormat.format(date1);

        Bitmap image = Bitmap.createScaledBitmap(bm, 400, 48, false);


        prnMng.getDeviceAddr();
        // Print text as bitmap image
        prnMng.printStr("Soft Host It", 2, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr(domain, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("Customer Receipt ", 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr("Contact: " + "01770991502", 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr("MD ABDUR RAHMAN", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printNewLine();
        prnMng.printStr("Print at: " + new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault()).format(new Date()), 1, WoosimCmd.ALIGN_LEFT);
        prnMng.printStr("Date: " + formatted, 1, WoosimCmd.ALIGN_LEFT);
        prnMng.printStr("Name: " + fname, 1, WoosimCmd.ALIGN_LEFT);
        prnMng.printStr("Invoice ID: " + id, 1, WoosimCmd.ALIGN_LEFT);
        prnMng.printStr("Account: " + account, 1, WoosimCmd.ALIGN_LEFT);
        prnMng.printStr("Amount: " + amount, 1, WoosimCmd.ALIGN_LEFT);
        prnMng.printStr("--------------------------------");



        prnMng.printStr("Category: " + in_cat, 1, WoosimCmd.ALIGN_LEFT);
//        prnMng.printStr("Description: " + des, 1, WoosimCmd.ALIGN_LEFT);
//        prnMng.printStr("User ID: " + user_id, 1, WoosimCmd.ALIGN_LEFT);
//
        prnMng.printStr("--------------------------------");
        prnMng.printStr("  Deposit        RECEIPT", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("--------------------------------");
//
        prnMng.printStr("Thank you for your business", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printNewLine();
        prnMng.printStr("--------------------------------");
        prnMng.printStr("Powered by Soft Host It", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("www.softhostit.com", 1, WoosimCmd.ALIGN_CENTER);
//
//
        prnMng.printNewLine();
        prnMng.printStr("Bhisab", 1, WoosimCmd.ALIGN_CENTER);
//        //print barcode
        prnMng.printPhoto(image);
        prnMng.printNewLine();
        printEnded(prnMng);
    }

    @Override
    public void printEnded(WoosimPrnMng prnMng) {
        //Do any finalization you like after print ended.
        if (prnMng.printSucc()) {
            Toasty.success(context, R.string.print_succ, Toast.LENGTH_LONG).show();
        } else {
            Toasty.error(context, R.string.print_error, Toast.LENGTH_LONG).show();
        }
    }
}
