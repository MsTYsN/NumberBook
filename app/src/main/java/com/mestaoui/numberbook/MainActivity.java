package com.mestaoui.numberbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private CountryCodePicker picker;
    private EditText searchTxt;
    private RadioButton number, name;
    private Button search, add;
    private PhoneNumberUtil phoneNumberUtil;
    RequestQueue requestQueue;
    String url = "http://10.0.2.2:9090/contacts/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNumberUtil = PhoneNumberUtil.getInstance();
        picker = findViewById(R.id.picker);
        searchTxt = findViewById(R.id.searchTxt);
        number = findViewById(R.id.number);
        name = findViewById(R.id.name);
        search = findViewById(R.id.search);
        add = findViewById(R.id.add);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!number.isChecked() && !name.isChecked()) {
                    Toast.makeText(MainActivity.this, "Veuillez choisir une option de recherche", Toast.LENGTH_SHORT).show();
                } else {
                    if (number.isChecked()) {
                        loadContactByNumber(searchTxt.getText().toString(), picker.getSelectedCountryNameCode());
                    } else {
                        loadContactByName(searchTxt.getText().toString());
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View popup = LayoutInflater.from(MainActivity.this).inflate(R.layout.contact_add, null,
                        false);
                final EditText fullname = popup.findViewById(R.id.fullname);
                final CountryCodePicker ccpA = popup.findViewById(R.id.pickerA);
                final EditText phone = popup.findViewById(R.id.phone);
                final Button addContact = popup.findViewById(R.id.addContact);
                final Button cancel = popup.findViewById(R.id.cancel);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("New contact")
                        .setView(popup)
                        .create();
                dialog.show();
                addContact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fullname.getText().toString().isEmpty() || phone.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs!", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Phonenumber.PhoneNumber pn = phoneNumberUtil.parse(phone.getText().toString(), ccpA.getSelectedCountryNameCode());
                                if (!phoneNumberUtil.isValidNumber(pn)) {
                                    Toast.makeText(MainActivity.this, "Numéro de telephone invalide!", Toast.LENGTH_SHORT).show();
                                } else {
                                    requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    Map<String, String> postParam = new HashMap<String, String>();
                                    postParam.put("fullname", fullname.getText().toString());
                                    postParam.put("code", ccpA.getSelectedCountryCodeWithPlus());
                                    postParam.put("phone", String.valueOf(pn.getNationalNumber()));
                                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                                            url, new JSONObject(postParam),
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Log.d(TAG, "onResponse: ADDED POG");
                                                    Toast.makeText(MainActivity.this, "Ajout réussi!", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            }, new Response.ErrorListener() {

                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d(TAG, "onErrorResponse: SAAADGE " + error.getMessage());
                                        }
                                    }) {
                                        @Override
                                        public Map<String, String> getHeaders() throws AuthFailureError {
                                            HashMap<String, String> headers = new HashMap<String, String>();
                                            headers.put("Content-Type", "application/json");
                                            return headers;
                                        }
                                    };
                                    requestQueue.add(jsonObjReq);
                                }
                            } catch (NumberParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    public void loadContactByNumber(String number, String region) {
        try {
            Phonenumber.PhoneNumber pn = phoneNumberUtil.parse(number, region);
            if (!phoneNumberUtil.isValidNumber(pn)) {
                Toast.makeText(MainActivity.this, "Numéro de telephone invalide!", Toast.LENGTH_SHORT).show();
            } else {
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                        url + "number/" + pn.getNationalNumber(), null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if(response != null) {
                                    try {
                                        String n = response.getString("fullname");
                                        String c = response.getString("code");
                                        String p = String.valueOf(response.getLong("phone"));
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                        alertDialogBuilder.setMessage(n + " " + c + p);

                                        alertDialogBuilder.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + (c + p)));
                                                startActivity(call);
                                            }
                                        });
                                        alertDialogBuilder.setNegativeButton("SMS", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent msg = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + (c + p)));
                                                startActivity(msg);
                                            }
                                        });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Numéro de telephone introuvable!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onErrorResponse: " + error.getMessage());
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        return headers;
                    }
                };
                requestQueue.add(jsonObjReq);
            }
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
    }

    public void loadContactByName(String name) {
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url + "name/" + name, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response != null) {
                            try {
                                String n = response.getString("fullname");
                                String c = response.getString("code");
                                String p = String.valueOf(response.getLong("phone"));
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                                alertDialogBuilder.setMessage(n + " " + (c + p));

                                alertDialogBuilder.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + (c + p)));
                                        startActivity(call);
                                    }
                                });
                                alertDialogBuilder.setNegativeButton("SMS", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent msg = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + (c + p)));
                                        startActivity(msg);
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Nom introuvable!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onErrorResponse: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjReq);
    }
}