package com.softhostit.bhisab.invoice;

import android.content.Context;
import android.widget.Toast;

import com.softhostit.bhisab.R;
import com.softhostit.bhisab.utils.IPrintToPrinter;
import com.softhostit.bhisab.utils.WoosimPrnMng;
import com.woosim.printer.WoosimCmd;

import es.dmoral.toasty.Toasty;

public class OrderItemModel implements IPrintToPrinter {
    private Context context;
    private int item_id;
    private String name;
    private String description;
    private int quantity;
    private int return_quantity;
    private int price;
    private String unit;
    private int discount;
    private String discount_type;
    private int line_total;

    public OrderItemModel() {
    }

    public OrderItemModel(Context context, int item_id, String name, String description, int quantity, int return_quantity, int price, String unit, int discount, String discount_type, int line_total) {
        this.context = context;
        this.item_id = item_id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.return_quantity = return_quantity;
        this.price = price;
        this.unit = unit;
        this.discount = discount;
        this.discount_type = discount_type;
        this.line_total = line_total;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReturn_quantity() {
        return return_quantity;
    }

    public void setReturn_quantity(int return_quantity) {
        this.return_quantity = return_quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(String discount_type) {
        this.discount_type = discount_type;
    }

    public int getLine_total() {
        return line_total;
    }

    public void setLine_total(int line_total) {
        this.line_total = line_total;
    }

    @Override
    public void printContent(WoosimPrnMng prnMng) {

        prnMng.printStr("  Items        Price  Qty  Total", 1, WoosimCmd.ALIGN_CENTER);
        prnMng.printStr("--------------------------------");

        double getItemPrice;

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
