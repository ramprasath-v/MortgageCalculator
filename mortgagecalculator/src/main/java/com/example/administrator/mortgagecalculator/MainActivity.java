package com.example.administrator.mortgagecalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends ActionBarActivity {
    private EditText amountText;
    private SeekBar interestRate;
    private double amountBorrowed,rate = 5.0;
    private RadioGroup termRadioGroup;
    private RadioButton radioButton7,radioButton15,radioButton30,termRadioButton;
    private CheckBox taxCheck,insuranceCheck;
    private Button calculateButton;
    private TextView paymentValue,rateTextValue;
    private int termValue;
    private boolean taxChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountText = (EditText) findViewById(R.id.amountEdit);
        interestRate = (SeekBar) findViewById(R.id.rateSeek);
        paymentValue = (TextView) findViewById(R.id.monthlyPaymentText);
        termRadioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);
        taxCheck = (CheckBox) findViewById(R.id.checkTax);
        calculateButton = (Button) findViewById(R.id.calculate);

        //gets values from seekbar
        interestRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rateTextValue = (TextView) findViewById(R.id.rateID);
                String progress1 = String.valueOf(progress);

                rateTextValue.setText(progress1 +".0%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rate = interestRate.getProgress();
            }
        });

        //calculates when user clicks calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountTextValue = amountText.getText().toString();
                //validates amount borrowed field
                if (amountTextValue.matches("")){
                    displayErrorMessage("Enter value in Amount Field");
                }

                else {
                    amountBorrowed = Double.parseDouble(amountTextValue);
                    int selectedId = termRadioGroup.getCheckedRadioButtonId();
                    termRadioButton = (RadioButton) findViewById(selectedId);
                    termValue = Integer.parseInt(termRadioButton.getText().toString());
                    if (((CheckBox) taxCheck).isChecked()) {
                        taxChecked = true;
                    } else {
                        taxChecked = false;
                    }

                    double payment = calculateMonthlyPayment(amountBorrowed, rate, termValue, taxChecked);
                    paymentValue.setTextColor(Color.parseColor("#48b427"));
                    paymentValue.setText("Your Monthly Payment is $" + new DecimalFormat("##.##").format(payment));
                }
            }
        });
    }

    /**
     * Method for calculating monthly payment based on values entered by user
     * @param amount
     * @param rate
     * @param term
     * @param taxCheck
     * @return
     */
    public double calculateMonthlyPayment(double amount, double rate, int term, boolean taxCheck){
        double monthlyPayment;
        double tax = (0.1 * amount) / 100;
        int termInMonths = term * 12;

        if (rate>0) {
            double monthlyInterest = rate/1200;
            double r= Math.pow(1+monthlyInterest,termInMonths);

            if (taxCheck == true) {
                monthlyPayment = (amount * (monthlyInterest / (1-(1/r)))) +tax;
            } else {
                monthlyPayment = (amount * (monthlyInterest / (1-(1/r))));
            }
        }
        else{
            if (taxCheck == true) {
                monthlyPayment = (amount/term) + tax;
            } else {
                monthlyPayment = (amount/term);
            }
        }
        return monthlyPayment;
    }

    /**
     * Method to display error message
     * @param message
     */
    public void displayErrorMessage(String message){
        Toast.makeText(this, message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
