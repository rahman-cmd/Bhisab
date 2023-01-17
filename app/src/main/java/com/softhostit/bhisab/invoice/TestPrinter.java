package com.softhostit.bhisab.invoice;

import android.content.Context;
import android.graphics.Bitmap;
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
    String names, price, qty, unite, discountS, line_total;
    double cost_total, subTotal;
    DecimalFormat f;

    String currency = "BDT: ";

    private Context context;
    List<OrderItemModel> orderDetailsList = new ArrayList<>();
    Bitmap bm;

    private String invoiceId;
    private String name;
    private String cname;
    private String phone1;
    private int pre_due;
    private String c_address;

    private int invoice_ids;
    private String date_issue;
    private String grand_quantity;
    private int subtotal;
    private int discounts;
    private int vat_tax;
    private int total_amount;
    private int payment;
    private int due_amount;

    public TestPrinter() {
    }

    public TestPrinter(Context context, String name, String cname, String phone1, int pre_due, String c_address, int invoice_ids, String date_issue, String grand_quantity, int subtotal, int discounts, int vat_tax, int total_amount, int payment, int due_amount, List<OrderItemModel> orderDetailsList) {
        this.context = context;
        this.orderDetailsList = orderDetailsList;
        this.name = name;
        this.cname = cname;
        this.phone1 = phone1;
        this.pre_due = pre_due;
        this.c_address = c_address;
        this.invoice_ids = invoice_ids;
        this.date_issue = date_issue;
        this.grand_quantity = grand_quantity;
        this.subtotal = subtotal;
        this.discounts = discounts;
        this.vat_tax = vat_tax;
        this.total_amount = total_amount;
        this.payment = payment;
        this.due_amount = due_amount;

        f = new DecimalFormat("#0.0");
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<OrderItemModel> getOrderDetailsList() {
        return orderDetailsList;
    }

    public void setOrderDetailsList(List<OrderItemModel> orderDetailsList) {
        this.orderDetailsList = orderDetailsList;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public int getPre_due() {
        return pre_due;
    }

    public void setPre_due(int pre_due) {
        this.pre_due = pre_due;
    }

    public String getC_address() {
        return c_address;
    }

    public void setC_address(String c_address) {
        this.c_address = c_address;
    }

    public int getInvoice_ids() {
        return invoice_ids;
    }

    public void setInvoice_ids(int invoice_ids) {
        this.invoice_ids = invoice_ids;
    }

    public String getDate_issue() {
        return date_issue;
    }

    public void setDate_issue(String date_issue) {
        this.date_issue = date_issue;
    }

    public String getGrand_quantity() {
        return grand_quantity;
    }

    public void setGrand_quantity(String grand_quantity) {
        this.grand_quantity = grand_quantity;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getDiscounts() {
        return discounts;
    }

    public void setDiscounts(int discounts) {
        this.discounts = discounts;
    }

    public int getVat_tax() {
        return vat_tax;
    }

    public void setVat_tax(int vat_tax) {
        this.vat_tax = vat_tax;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getDue_amount() {
        return due_amount;
    }

    public void setDue_amount(int due_amount) {
        this.due_amount = due_amount;
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

        prnMng.printStr("Bhisab", 2, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr(cname, 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr(name, 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr("Customer Receipt ", 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr("Contact: " + shopContact, 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr("Invoice ID: " + invoiceId, 1, WoosimCmd.ALIGN_CENTER);
//        prnMng.printStr(cname, 1, WoosimCmd.ALIGN_CENTER);
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
        prnMng.printStr("Sub Total " + currency + f.format(subtotal), 1, WoosimCmd.ALIGN_RIGHT);
//        prnMng.printStr("Total Tax (+): " + currency + f.format(Double.parseDouble(tax)), 1, WoosimCmd.ALIGN_RIGHT);
//        prnMng.printStr("Discount (-): " + currency + f.format(Double.parseDouble(discount)), 1, WoosimCmd.ALIGN_RIGHT);
//        prnMng.printStr("--------------------------------");
//        prnMng.printStr("Total Price: " + currency + f.format(Double.parseDouble(price)), 1, WoosimCmd.ALIGN_RIGHT);
//
        prnMng.printStr("--------------------------------");
        prnMng.printStr("Powered by Soft Host It", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("www.softhostit.com", 1, WoosimCmd.ALIGN_CENTER);

        prnMng.printStr("Thank you for your business", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printNewLine();
//
//        //print barcode
//        prnMng.printPhoto(bm);
//        prnMng.printNewLine();
//
        printEnded(prnMng);
        prnMng.printNewLine();

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
