package com.sashaermolenko.fastorder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Optional;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.r0adkll.slidr.Slidr;
import com.sashaermolenko.fastorder.Adapters.CartRecyclerAdapter;
import com.sashaermolenko.fastorder.Fragments.CartFragment;
import com.sashaermolenko.fastorder.Google.GooglePay;
import com.sashaermolenko.fastorder.Google.MapsActivity;
import com.sashaermolenko.fastorder.Items.HistoryItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class OrderPayActivity extends AppCompatActivity {

    private PaymentsClient mPaymentsClient;
    private View mGooglePayButton;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 42;

    private TextView price, time;
    public static Spinner spinner, spinnerO;

    private String date;

    private String data[] = {"Out of restaurant", "In restaurant"};
    private String spots[];
    public static int type_id = 0, spot_index = 0;
    public static HashMap<Integer, Integer> ids = new HashMap<>();
    public static HashMap<String, Integer> indexes = new HashMap<>();

    private Context context;

    private Button timeD;
    private ImageButton map;
    private String pri;
    private static final int Time_id = 1;
    private static final int Date_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        context = this;
        try {
            spots = new String[MainActivity.spots.size()];
            MainActivity.spots.toArray(spots);
            ArrayList<JSONObject> aj = MainActivity.spotsObjects;
            for (int i = 0; i < aj.size(); ++i) {
                try {

                    ids.put(i, Integer.parseInt(aj.get(i).get("id").toString()));
                    indexes.put(aj.get(i).get("name").toString(), i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }


       pri = CartRecyclerAdapter.getTotalPrice() + '₴';

        mPaymentsClient =
                Wallet.getPaymentsClient(
                        this,
                        new Wallet.WalletOptions.Builder()
                                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                                .build());

        possiblyShowGooglePayButton();

        Slidr.attach(this);

        bind();

        //Toast.makeText(context, Integer.toString(MainActivity.cartItems.size()), Toast.LENGTH_SHORT).show();
    }

    private void possiblyShowGooglePayButton() {
        final Optional<JSONObject> isReadyToPayJson = GooglePay.getIsReadyToPayRequest();
        if (!isReadyToPayJson.isPresent()) {
            return;
        }
        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
        if (request == null) {
            return;
        }
        Task<Boolean> task = mPaymentsClient.isReadyToPay(request);
        task.addOnCompleteListener(
                new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        try {
                            boolean result = task.getResult(ApiException.class);
                            if (result) {
                                // show Google as a payment option
                                mGooglePayButton = findViewById(R.id.googlepay_button);
                                mGooglePayButton.setOnClickListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                requestPayment(view);
                                                //TODO send request
                                            }
                                        });
                                mGooglePayButton.setVisibility(View.VISIBLE);
                            }
                        } catch (ApiException exception) {
                            // handle developer errors
                        }
                    }
                });
    }

    public void requestPayment(View view) {
        Optional<JSONObject> paymentDataRequestJson = GooglePay.getPaymentDataRequest();
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }
        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());
        if (request != null) {
            AutoResolveHelper.resolveTask(
                    mPaymentsClient.loadPaymentData(request), this, LOAD_PAYMENT_DATA_REQUEST_CODE);
        }
        // Get the calander
        Calendar c = Calendar.getInstance();

        // From calander get the year, month, day, hour, minute
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        date = Integer.toString(year) + "." + Integer.toString(month) + "." + Integer.toString(day);

        try {
            if(Integer.valueOf(CartRecyclerAdapter.getTotalPrice()) != 0 && MainActivity.historyItems.indexOf(new HistoryItem(date, pri, MainActivity.cartItems)) == -1)
                MainActivity.addItem(date, pri, MainActivity.cartItems);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }

        //TODO отправлять запрос на оплату

        MainActivity.resumeFromCart = true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        String json = paymentData.toJson();
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        // Log the status for debugging.
                        // Generally, there is no need to show an error to the user.
                        // The Google Pay payment sheet will present any account errors.
                        break;
                    default:
                        // Do nothing.
                }
                break;
            default:
                // Do nothing.
        }
    }



    public void bind() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner = (Spinner) findViewById(R.id.spinRes);

        spinner.setAdapter(adapter);

        spinner.setPrompt("Place");

        spinner.setSelection(type_id);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                //Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            ArrayAdapter<String> adapterO = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spots);
            adapterO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerO = (Spinner) findViewById(R.id.spinResO);
            spinnerO.setAdapter(adapterO);
        } catch (Exception e) {
//            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

        try {
            spinnerO.setPrompt("Place");
            spinnerO.setSelection(spot_index);
            spinnerO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    // показываем позиция нажатого элемента
                    //Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } catch (Exception e) {
//            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

        TextView forFont = (TextView) findViewById(R.id.orderGet);
        forFont.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));

        forFont = (TextView) findViewById(R.id.placeChoose);
        forFont.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));

        forFont = (TextView) findViewById(R.id.fullPrice);
        forFont.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));

        price = (TextView) findViewById(R.id.priceO);
        price.setText(pri);
        time = (TextView) findViewById(R.id.timeO);
        map = (ImageButton) findViewById(R.id.mapB);
        timeD = (Button) findViewById(R.id.timeD);
        price.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));
        time.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));
        timeD.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Thin.ttf"));
        timeD.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Show time dialog
                showDialog(Time_id);
            }
        });
        map.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(context, MapsActivity.class);
                startActivity(i);
            }
        });
    }
    protected Dialog onCreateDialog(int id) {

        // Get the calander
        Calendar c = Calendar.getInstance();

        // From calander get the year, month, day, hour, minute
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        switch (id) {
            case Date_id:

                // Open the datepicker dialog
                return new DatePickerDialog(OrderPayActivity.this, date_listener, year,
                        month, day);
            case Time_id:

                // Open the timepicker dialog
                return new TimePickerDialog(OrderPayActivity.this, time_listener, hour,
                        minute, false);

        }
        return null;
    }

    // Date picker dialog
    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            String date1 = String.valueOf(month) + "/" + String.valueOf(day)
                    + "/" + String.valueOf(year);
            time.setText(date1);
        }
    };
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String time1 = String.valueOf(hour) + ":" + String.valueOf(minute);
            time.setText(time1);
        }
    };


}
