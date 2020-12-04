package jalaleddine.abdelbaset.onlinecurrencyconverter;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.MobileAds;


public class MainActivity extends AppCompatActivity {


    String first,second,temp,firstOnlyCurrency,secondOnlyCurrency;
    double value;
    int counter22 =0;
    String firstCurrencyName = "";
    String secondCurrencyName = "";
   String [] apiKeys = {"bcbffbad221110b17baf","abd525275226373b63b0","0ae90034db378c5d98d3","444985feb8b80b822d60","92ea5078dcd92dda1ee3","93510ac30f3d08358047","553e7ca78f9c742d74dd"};
    Random rand =  new Random();
    int pickApi = rand.nextInt(apiKeys.length-1);
    String api = apiKeys[pickApi];
   EditText Result;
   String Sresult;
   String currencySymbol,currencyFullName;
    ProgressDialog progressDialog;

    PublisherAdRequest adRequest;
    AutoCompleteTextView mySpinner;
    AutoCompleteTextView mySpinner2;
    ArrayList<String> AbedList;
    ArrayList<String> currencySymbolList,keysList;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;


    Runnable getCurrencyVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       MobileAds.initialize(this, "ca-app-pub-6045747739342601~6799954717");// inter biggie
       mInterstitialAd = new InterstitialAd(this);
       mInterstitialAd.setAdUnitId("ca-app-pub-6045747739342601/6238991908");
       mInterstitialAd.loadAd(new AdRequest.Builder().build());


        MobileAds.initialize(this, "ca-app-pub-6045747739342601~6799954717");// banner
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        Log.e("MobileAds Initialize","Yups");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.e("Load Ad", "True");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //mInterstitialAd.loadAd(new AdRequest.Builder().build());
                Log.e("Load Ad", "Failed");
            }

            @Override
            public void onAdOpened() {
                Log.e("Opened Ad", "True");
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                Log.e("Left Ad", "True");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                Log.e("Closed Ad", "True");
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.e("Loaded Banner Ad", "True");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //mAdView.loadAd(adRequest);
                Log.e("Loaded Banner Ad", "Failed");
            }

            @Override
            public void onAdOpened() {
                Log.e("Opened Banner Ad", "True");
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                Log.e("Left Banner Ad", "True");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                Log.e("Closed Banner Ad", "True");
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        Result = (EditText) findViewById(R.id.Result);



        mySpinner = (AutoCompleteTextView) findViewById(R.id.spinner);
        mySpinner2 = (AutoCompleteTextView) findViewById(R.id.spinner2);





        progressDialog.setMessage("Updating Currencies....");
        showDialog();

        Runnable getAllKeys = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url2 = new URL("https://free.currencyconverterapi.com/api/v6/currencies?apiKey=" + api);
                    InputStream is2 = url2.openStream();

                    BufferedReader bf2 = new BufferedReader(new InputStreamReader(is2));

                    StringBuilder sb2 = new StringBuilder();
                    String line = bf2.readLine();
                    while (line != null) {
                        sb2.append(line);
                        line = bf2.readLine();
                    }

                    Log.e("JSON", sb2.toString());

                    JSONTokener tokener2 = new JSONTokener(sb2.toString());

                    JSONObject mainObj2 = new JSONObject(tokener2);

                    JSONObject ResultsObj = mainObj2.getJSONObject("results");



                 Iterator<String> keys = ResultsObj.keys();


                 AbedList = new ArrayList<>();

                    currencySymbolList = new ArrayList<>();
                    keysList = new ArrayList<>();

            while(keys.hasNext()){
                String key;
                key = keys.next();

                JSONObject keysObj = ResultsObj.getJSONObject(key);
                try{
                    currencySymbol = keysObj.getString("currencySymbol");

                } catch(Exception e){

                }
                currencySymbolList.add(currencySymbol);
                keysList.add(key);
                AbedList.add(key + " " + "(" + currencySymbol + ")");

            }


                    runOnUiThread(new Runnable(){

                                      @Override
                                      public void run() {
                                          Collections.sort(AbedList);


                                              
                                              mySpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_dropdown, AbedList));
                                              mySpinner2.setAdapter(new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_dropdown, AbedList));

                                              hideDialog();


                                      }
                                  });


                }catch (Exception e){
                    e.printStackTrace();

                    Log.e("Error","BITCH KEY WENT OUT");

                }
            }
        };

        new Thread(getAllKeys).start();

        //Initialize Runnable
         getCurrencyVal = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://free.currencyconverterapi.com/api/v6/convert?q="+ firstOnlyCurrency.toUpperCase() +"_" + secondOnlyCurrency.toUpperCase() + "&compact=y&apiKey=" + api);
                    InputStream is = url.openStream();

                    BufferedReader bf = new BufferedReader(new InputStreamReader(is));

                    StringBuilder sb = new StringBuilder();
                    String line = bf.readLine();
                    while(line != null) {
                        sb.append(line);
                        line = bf.readLine();
                    }


                    JSONTokener tokener = new JSONTokener(sb.toString());
                    JSONObject mainObj = new JSONObject(tokener);
                    JSONObject ValObj = mainObj.getJSONObject(firstOnlyCurrency.toUpperCase() + "_" + secondOnlyCurrency.toUpperCase());
                    final double abed = ValObj.getDouble("val");

                    Log.e("Result",abed + "");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //Toast.makeText(MainActivity.this,  "Value is: " + value, Toast.LENGTH_SHORT).show();

                           // Toast.makeText(MainActivity.this, "abed is: " + abed, Toast.LENGTH_SHORT).show();

                            //Sresult = (value * abed) + "";
                            Double Doubleresult = (value * abed);
                            try{
                                Result.setText(NumberFormat.getInstance(Locale.getDefault()).format(Doubleresult));


                            }catch(Exception e){
                                Toast.makeText(MainActivity.this, "Error Occurred", Toast.LENGTH_SHORT).show();
                            }



                        }
                    });



                } catch (Exception e) {



                    //Change UI
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,  "Value is: " + value, Toast.LENGTH_SHORT).show();


                            Toast.makeText(MainActivity.this, "Couldn't connect to the server", Toast.LENGTH_SHORT).show();

                        }
                    });

                    Log.e("Error",e.getMessage() == null ? "" : e.getMessage());

                }

            }
        };






    }
    public void checkFirstRun() {

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            new AlertDialog.Builder(this).setTitle("Welcome!").setMessage("Using the app is easy you enter 2 currencies and the number that should be converted from currency 1 to currency 2. \nFor any questions or problems email me on abed.apps.help@gmail.com").setNeutralButton("OK", null).show();

            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                   .putBoolean("isFirstRun", false)
                    .apply();
        }
    }
   public void convert(View view) {

       AdRequest adRequest = new AdRequest.Builder().build();
       mAdView.loadAd(adRequest);
        if(counter22 == 5){
            mInterstitialAd.show();
                counter22 = 0;
        }
       counter22++;
            try {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                Log.e("Convert","Didn't hide keyboard");
            }
            EditText editText = (EditText) findViewById(R.id.editText);

            if (!editText.getText().toString().equals(""))
                value = Double.parseDouble(editText.getText().toString());
            else {
                value = 1.0;
            }
            boolean nully = mySpinner.getText() == null;


                      if(mySpinner.getText().toString().equals("")){
                          mySpinner.setText("USD");
                      }

                 first = mySpinner.getText().toString().toUpperCase();
                 firstOnlyCurrency = first.substring(0,3);



          if(mySpinner2.getText().toString().equals("")){
           mySpinner2.setText("USD");
            }
                    second = mySpinner2.getText().toString().toUpperCase();
                    secondOnlyCurrency = second.substring(0, 3);

            boolean fcnB = false;
            boolean scnB = false;

            for(int k = 0; k<keysList.size(); k++){

                if(first.contains(keysList.get(k))){
                    firstCurrencyName = keysList.get(k);
                    fcnB = true;
                }
                if(second.contains(keysList.get(k))){
                    secondCurrencyName = keysList.get(k);
                    scnB = true;
                }
                if(fcnB && scnB){
                    break;
                }

            }
            //Toast.makeText(this, "Made the loop", Toast.LENGTH_SHORT).show();


            try{
                 if(!fcnB || !scnB){
                    Toast.makeText(this, "Please enter a valid currency", Toast.LENGTH_SHORT).show();
                }

                if (fcnB && scnB) {
                    //Running It
                    new Thread(getCurrencyVal).start();

                    //getConverted();

                }}catch(Exception e){
                Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
            }
        }




    public void switcher (View view){


        //getCurrencies();

        temp = mySpinner.getText().toString();


        mySpinner.setText(mySpinner2.getText().toString());
        mySpinner2.setText(temp);


   }
   private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
            checkFirstRun();
        }


    }
}



