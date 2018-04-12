package com.example.anala.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    /** Variables */
    int quantity = 1;
    double price = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder (View view){
        EditText text = findViewById(R.id.name_field);
        String name = text.getText().toString();

        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean shouldHaveWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean shouldHaveChocolate = chocolateCheckBox.isChecked();

        double price = calculatePrice(quantity, shouldHaveWhippedCream, shouldHaveChocolate);

       createOrderSummary(price, shouldHaveWhippedCream, shouldHaveChocolate, name);
    }

    /**
     * Create summary of the order.
     *
     * @param name of customer
     * @param shouldHaveWhippedCream is whether or not the user wants whipped cream topping
     * @param shouldHaveChocolate is whether or not the user wants chocolate topping
     * @param price of the order
     * @return text summary
     */
    public void createOrderSummary(double price,
                                     boolean shouldHaveWhippedCream,
                                     boolean shouldHaveChocolate,
                                     String name){
        // Check if user wants whipped cream or chocolate or both
        String WCStatus;
        String ChocStatus;
        if(shouldHaveWhippedCream == true){
            WCStatus = getString(R.string.yes);
        }else{WCStatus = getString(R.string.no);}

        if(shouldHaveChocolate == true){
            ChocStatus = getString(R.string.yes);
        }else{ChocStatus= getString(R.string.no);}

        // order summary message
        String orderSummary = getString(R.string.name) + ": "+ name +
                getString(R.string.order_summary_whipped_cream) + " " + WCStatus +
                getString(R.string.order_summary_choc)+ " " + ChocStatus +
                getString(R.string.order_summary_quantity)+ ": " + quantity +
                getString(R.string.order_summary_total)+ ": $" + price +
                "";


        // begin an email intent to send the order summary
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.email_subject) + " " + name);
        emailIntent.putExtra(Intent.EXTRA_TEXT,orderSummary );
        if (emailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(emailIntent);
        }
    }

    /**
     * This method is called when the + button is clicked.
     */
    public void increment (View view){
        if(quantity < 100) {
            quantity = quantity + 1;
            display(quantity);
        }else{
            Toast.makeText(getApplicationContext(), "Cannot order over 100 coffees.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement (View view){
        if(quantity > 1) {
            quantity = quantity - 1;
            display(quantity);
        }else{
            Toast.makeText(getApplicationContext(), "Cannot order less than 1 coffee.", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int num) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + num);
    }

     /**
     * This method rounds a double to n decimal places
     */
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Calculates the price of the order.
     *
     * @param quantity is the number of cups of coffee ordered
     * @param whippedCream is whether or not the user wants whipped cream topping
     * @param chocolate is whether or not the user wants chocolate topping
     * @return double price
     */
    private double calculatePrice(int quantity, boolean whippedCream, boolean chocolate) {
        price = 4.89;
        if(whippedCream){
            price = price + 1;
        }
        if (chocolate){
            price = price + 2;
        }

        return round(quantity * price, 2);
    }
}