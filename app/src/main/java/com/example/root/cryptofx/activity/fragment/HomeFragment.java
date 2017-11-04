package com.example.root.cryptofx.activity.fragment;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.root.cryptofx.R;
import com.example.root.cryptofx.activity.adapter.CryptoAdapter;
import com.example.root.cryptofx.activity.adapter.CountryAdapter;
import com.example.root.cryptofx.activity.others.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ImageView imgView;
    private TextView country_name;
    private ImageView cryptoIcon;
    private TextView cryptoName;
    private TextView cryptoResult;
    private TextView fiatFinalResult;
    private EditText etCurrency;
    private ProgressBar progressBar;
    private Button btnConvert;
    private int fiatIndex;
    private int cryptoIndex;
    private double inputValue;
    public View rootView;
    String REQUEST_TAG = "com.example.root.cryptofx.activity.fragment";


    //private String result[] = new String[21];


    private String[] countryNames = {
            "US Dollars", "Chinese Yuan", "Euro", "British Pound", "Japanese Yen", "Canadian Dollar",
            "Australian Dollar", "Singapore Dollar", "Hong Kong Dollar", "Polish Zloty", "Russian Ruble",
            "Indian Rupee", "Swiss Franc", "Brazilian Real", "Norwegian Krone", "Mexican Peso",
            "Nigerian Naira", "Philippines Peso", "Kenyan Shilling", "Chilean Peso"};

    private int flags[] = {
            R.drawable.usa,
            R.drawable.china,
            R.drawable.euro,
            R.drawable.british_pound,
            R.drawable.japan,
            R.drawable.canada,
            R.drawable.australia,
            R.drawable.singapore,
            R.drawable.hongkong,
            R.drawable.poland,
            R.drawable.russia,
            R.drawable.india,
            R.drawable.switzerland,
            R.drawable.brazil,
            R.drawable.norway,
            R.drawable.mexico,
            R.drawable.nigeria,
            R.drawable.philippines,
            R.drawable.kenya,
            R.drawable.chile};

    private String[] cryptoAPI = {"BTC", "ETH"};
    private String[] fiatAPI = {"USD","CNY","EUR","GBP","JPY","CAD","AUD","SGD","HKD","PLN","RUB",
            "INR","CHF","BRL","NOK","MXN","NGN","PHP","KES","CLP"};

    private String[] cryptoNAMES = {"Bitcoin", "Ethereum"};

    private int cryptoICONS[] = {R.drawable.btc,
            R.drawable.ether};

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        this.imgView = (ImageView) rootView.findViewById(R.id.iv_flag);
        this.country_name = (TextView) rootView.findViewById(R.id.tv_country);
        this.cryptoResult = (TextView) rootView.findViewById(R.id.btcResult);
        this.fiatFinalResult = (TextView) rootView.findViewById(R.id.usdResult);
        this.etCurrency = (EditText) rootView.findViewById(R.id.etCurrency);
        this.cryptoName = (TextView) rootView.findViewById(R.id.crypto_NAME);
        this.cryptoIcon = (ImageView) rootView.findViewById(R.id.crypto_LOGO);
        this.progressBar = (ProgressBar) rootView.findViewById(R.id.progress_Bar);
        this.btnConvert = (Button) rootView.findViewById(R.id.btnConvert);

        Spinner fiatSpinner = (Spinner) rootView.findViewById(R.id.countrySpinner);
        CountryAdapter countryAdapter = new CountryAdapter(getActivity(), flags, countryNames);
        fiatSpinner.setAdapter(countryAdapter);

        Spinner cryptoSpinner = (Spinner) rootView.findViewById(R.id.crypto_SPINNER);
        CryptoAdapter cryptoAdapter = new CryptoAdapter(getActivity(), cryptoICONS, cryptoNAMES);
        cryptoSpinner.setAdapter(cryptoAdapter);

        fiatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                fiatIndex = position;
                Glide.with(getActivity()).load(flags[fiatIndex])
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgView);
                country_name.setText(countryNames[fiatIndex]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        cryptoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                cryptoIndex = position;

                cryptoIcon.setImageResource(cryptoICONS[cryptoIndex]);
                cryptoName.setText(cryptoNAMES[cryptoIndex]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etCurrency.getText().toString().trim().length() > 0
                        && !etCurrency.getText().toString().trim().equals(".")) {

                    checkInternetConnection();

                    dismissKeyboard(getActivity());

                    etCurrency.setCursorVisible(false);

                    cryptoResult.setText(R.string.loading);

                    String textValue = etCurrency.getText().toString();
                    inputValue = Double.parseDouble(textValue);

                    volleyJsonObjectRequest(fiatAPI[fiatIndex], cryptoAPI[cryptoIndex], inputValue);

                } else {

                    Toast.makeText(getActivity(), "Enter a valid number", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return rootView;
    }

    public void volleyJsonObjectRequest(final String fiatValue, String cryptoValue, final double inputValue) {

        String jsonURL = "https://min-api.cryptocompare.com/data/price?fsym=" + cryptoValue + "&tsyms=" + fiatValue;

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                jsonURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                    progressBar.setVisibility(View.GONE);
                try {

                    String result = response.getString(fiatValue);
                    double fiatResult = Double.parseDouble(result) * inputValue;

                    fiatFinalResult.setText(String.valueOf(fiatResult));
                    cryptoResult.setText(String.valueOf(inputValue));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        //Volley.newRequestQueue(getActivity()).add(jsonRequest);
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonRequest, REQUEST_TAG);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = getString(R.string.connected_msg);
            color = Color.YELLOW;
        } else {
            message = getString(R.string.disconnected_mgs);
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(rootView.findViewById(R.id.fab1), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }


    public boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connectMgr = (ConnectivityManager)
                getActivity().getSystemService(getActivity().getBaseContext().CONNECTIVITY_SERVICE);

        //Check for network connections
        if (connectMgr.getNetworkInfo(0).getState() ==
                NetworkInfo.State.CONNECTED ||
                connectMgr.getNetworkInfo(0).getState() ==
                        NetworkInfo.State.CONNECTING ||
                connectMgr.getNetworkInfo(1).getState() ==
                        NetworkInfo.State.CONNECTING ||
                connectMgr.getNetworkInfo(0).getState() ==
                        NetworkInfo.State.CONNECTED) {

            showSnack(true);
            progressBar.setVisibility(View.VISIBLE);

            return true;
        } else if (
                connectMgr.getNetworkInfo(0).getState() ==
                        NetworkInfo.State.DISCONNECTED ||
                        connectMgr.getNetworkInfo(1).getState() ==
                                NetworkInfo.State.DISCONNECTED) {

           showSnack(false);

            return false;
        }
        return false;
    }

    public void dismissKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

}
