package com.softhostit.bhisab.invoice;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.softhostit.bhisab.R;
import com.softhostit.bhisab.pdf_report.BarCodeEncoder;
import com.softhostit.bhisab.utils.IPrintToPrinter;
import com.softhostit.bhisab.utils.WoosimPrnMng;
import com.woosim.printer.WoosimCmd;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class TestPrinter implements IPrintToPrinter {
    String name, price, qty, unite, discount, line_total;
    double cost_total, subTotal;
    DecimalFormat f;

    String currency = "BDT: ";

    private Context context;
    List<OrderItemModel> orderDetailsList = new ArrayList<>();
    String servedBy, shopName, shopAddress, shopEmail, shopContact, invoiceId, orderDate, customerName, footer, tax, discounts;
    Bitmap bm;

    public TestPrinter(Context context, List<OrderItemModel> orderDetailsList) {
        this.context = context;
        this.orderDetailsList = orderDetailsList;

        f = new DecimalFormat("#0.0");
    }


//    public TestPrinter(Context context, String shopName, String shopAddress, String shopEmail, String shopContact, String invoiceId, String orderDate, String customerName, String footer, double subTotal, String line_total, String tax, String discounts, String served_by, List<OrderItemModel> orderDetailsList) {
//        this.context = context;
//        this.shopName = shopName;
//        this.shopAddress = shopAddress;
//        this.shopEmail = shopEmail;
//        this.shopContact = shopContact;
//        this.invoiceId = invoiceId;
//        this.orderDate = orderDate;
//        this.customerName = customerName;
//        this.footer = footer;
//        this.subTotal = subTotal;
//        this.line_total = line_total;
//        this.tax = tax;
//        this.discounts = discounts;
//        this.servedBy = served_by;
//        this.orderDetailsList = orderDetailsList;
//
//        f = new DecimalFormat("#0.00");
//    }


    @Override
    public void printContent(WoosimPrnMng prnMng) {
        //Generate barcode
        BarCodeEncoder qrCodeEncoder = new BarCodeEncoder();
        bm = null;

        try {
            bm = qrCodeEncoder.encodeAsBitmap(invoiceId, BarcodeFormat.CODE_128, 400, 48);
        } catch (WriterException e) {
            e.printStackTrace();
        }

//        prnMng.printStr(shopName, 2, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr(shopAddress, 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr("Customer Receipt ", 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr("Contact: " + shopContact, 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr("Invoice ID: " + invoiceId, 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr(customerName, 1, WoosimCmd.ALIGN_CENTER);
//
//        prnMng.printStr("Email: " + shopEmail, 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr("Served By: " + servedBy, 1, WoosimCmd.ALIGN_CENTER);

        prnMng.printStr("--------------------------------");

        prnMng.printStr("  Items        Price  Qty  Total", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("--------------------------------");

        double getItemPrice;
        int slNumber = 1;
        for (int i = 0; i < orderDetailsList.size(); i++) {
            name = orderDetailsList.get(i).getName();
            int price = orderDetailsList.get(i).getPrice();
            getItemPrice = price;
            int qty = orderDetailsList.get(i).getQuantity();
            unite = orderDetailsList.get(i).getUnit();
            cost_total = qty * price;
            prnMng.printStr(slNumber + "." + name + " " + f.format(getItemPrice) + "x" + qty + "=" + f.format(cost_total), 1, WoosimCmd.ALIGN_CENTER);
            slNumber += 1;
//            prnMng.leftRightAlign(first10 + " " + f.format(getItemPrice) + "x" + qty, "=" + f.format(cost_total));

        }

//
//        prnMng.printStr("--------------------------------");
//        prnMng.printStr("Sub Total: " + currency + f.format(subTotal), 1, WoosimCmd.ALIGN_RIGHT);
//        prnMng.printStr("Total Tax (+): " + currency + f.format(Double.parseDouble(tax)), 1, WoosimCmd.ALIGN_RIGHT);
//        prnMng.printStr("Discount (-): " + currency + f.format(Double.parseDouble(discount)), 1, WoosimCmd.ALIGN_RIGHT);
//        prnMng.printStr("--------------------------------");
//        prnMng.printStr("Total Price: " + currency + f.format(Double.parseDouble(price)), 1, WoosimCmd.ALIGN_RIGHT);
//
//        prnMng.printNewLine();
//        prnMng.printStr(footer, 1, WoosimCmd.ALIGN_CENTER);

        prnMng.printNewLine();
//
//        //print barcode
//        prnMng.printPhoto(bm);
//        prnMng.printNewLine();
//
//        printEnded(prnMng);
//        prnMng.printNewLine();

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