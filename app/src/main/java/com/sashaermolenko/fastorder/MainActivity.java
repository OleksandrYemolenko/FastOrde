package com.sashaermolenko.fastorder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sashaermolenko.fastorder.Fragments.CartFragment;
import com.sashaermolenko.fastorder.Fragments.HistoryFragment;
import com.sashaermolenko.fastorder.Fragments.MenuFragment;
import com.sashaermolenko.fastorder.Items.CartItem;
import com.sashaermolenko.fastorder.Items.HistoryItem;
import com.sashaermolenko.fastorder.Login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static int flag = 0;
    public static boolean resumeFromCart = false;
    public static final String STORAGE_NAME = "STORAGE";
    private static Context context;
    private FrameLayout frameLayout;
    private BottomNavigationView navigation;
    public static ArrayList<CartItem> cartItems = new ArrayList<>();
    public static ArrayList<HistoryItem> historyItems = new ArrayList<>();
    public static ArrayList<String> spots = new ArrayList<>();
    public static ArrayList<JSONObject> spotsObjects = new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction;
            switch (item.getItemId()) {
                case R.id.navigation_menu:
                    MenuFragment menuFragment = new MenuFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, menuFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_cart:
                    CartFragment cartFragment = new CartFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, cartFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_history:
                    HistoryFragment historyFragment = new HistoryFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame_layout, historyFragment);
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bind();

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences sp = this.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        //editor.putBoolean("isLogined", false);
        //editor.commit();

        boolean logined = sp.getBoolean("isLogined", false);

        if(!logined) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(resumeFromCart){
            save();
            resumeFromCart = false;
        }
    }

    private void bind() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);

        FragmentTransaction fragmentTransaction;
        MenuFragment menuFragment = new MenuFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, menuFragment );
        fragmentTransaction.commit();

        historyItems = getHistoryItems();

        if(historyItems == null) {
            addItem("asd", "asd", null);
        }

        context = this;

        new AsyncRequest().execute();
    }

    public ArrayList<HistoryItem> getHistoryItems() {
        //importing all data into JSON objects array(phones) from database
        ArrayList<HistoryItem> historyItems = JSONHelper.importFromJSON(this);
        return historyItems;
    }

    public static void addItem(String date, String price, ArrayList<CartItem> list) {

            HistoryItem hi = new HistoryItem(date, price, list);
            ArrayList<HistoryItem> his = new ArrayList<>();
            his.add(hi);
            if(historyItems != null) {
                his.addAll(historyItems);
            }
            historyItems = his;

        for(int i = 1; i < historyItems.size(); ++i) {
            if(historyItems.get(i).getDate().length() == 3) {
                historyItems.remove(i);
                break;
            }
        }
    }

    public void save(){

        //exporting all received data into JSON object in database

        Toast.makeText(this, Integer.toString(historyItems.size()), Toast.LENGTH_LONG).show();

        boolean result = JSONHelper.exportToJSON(this, historyItems);

//        if(result){
//            Toast.makeText(this, "Данные сохранены", Toast.LENGTH_LONG).show();
//        }
//        else{
//            Toast.makeText(this, "Не удалось сохранить данные", Toast.LENGTH_LONG).show();
//        }
    }

    class AsyncRequest extends AsyncTask<Void, Void, ArrayList<JSONObject> > {

        @Override
        protected ArrayList<JSONObject> doInBackground(Void... voids) {
            ArrayList<JSONObject> aj = new ArrayList<>();
            try {
                String link = Handler.createLink("access.getSpots");
                String content = Handler.sendRequest(link, "GET");

                JSONObject obj = new JSONObject(content);
                JSONArray arr = obj.getJSONArray("response");
                for(int i = 0; i < arr.length(); ++i) {
                    String id = (String)arr.getJSONObject(i).get("spot_id");
                    String name = (String)arr.getJSONObject(i).get("spot_name");
                    String address = (String) arr.getJSONObject(i).get("spot_adress");

                    String helper = address.replace(" ", "+");
                    link = "https://maps.google.com/maps/api/geocode/json?address="+helper+"&key=AIzaSyDjgfR1P5MpP8BUoFvJcrqTA_1xBJ-TVhE";
                    JSONArray res = new JSONObject(Handler.sendRequest(link, "GET")).getJSONArray("results");
                    JSONObject loc = res.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                    String lng = loc.getString("lng");
                    String lat = loc.getString("lat");

                    JSONObject o = new JSONObject();
                    o.put("id", id);
                    o.put("name", name);
                    o.put("address", address);
                    o.put("lng", lng);
                    o.put("lat", lat);
                    aj.add(o);
                }
            } catch (JSONException e) {
                System.out.println(e);
            }

            return aj;
        }

        @Override
        protected void onPostExecute(ArrayList<JSONObject> aj) {
            spotsObjects = aj;
            for (int i = 0; i < aj.size(); ++i) {
                try {
                    spots.add(aj.get(i).get("name") + " " + aj.get(i).get("address"));
                } catch (JSONException e) {
                    System.out.println(e);
                }
            }
        }
    }

}
