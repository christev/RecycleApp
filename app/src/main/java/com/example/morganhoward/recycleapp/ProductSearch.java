package com.example.morganhoward.recycleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//  sources:
// (1) https://www.thorntech.com/2016/03/parsing-json-android-using-volley-library/
// (2) https://developer.android.com/training/volley/request.html#request-json
public class ProductSearch extends AppCompatActivity {

    TextView productLabel;
    TextView recycleInfoLabel;
    TextView product;
    EditText zipCode;
    EditText upc;

    //String url = "https://python-tutorial-155402.appspot.com/books";
    String url = "http://web.engr.oregonstate.edu/~kiesslim/recycleit/recycle" +
            ".php";
//    String url = "https://raw.githubusercontent.com/ianbar20/JSON-Volley-Tutorial/master/Example-JSON-Files/Example-Object.JSON";
    String data = "";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        // Input and output fields
        recycleInfoLabel = (TextView) findViewById(R.id.recycleInfoLabel);
        productLabel = (TextView) findViewById(R.id.productLabel);
        product = (TextView) findViewById(R.id.product);
        zipCode = (EditText) findViewById(R.id.EditTextZip);
        upc = (EditText) findViewById(R.id.EditTextUPC);
    }

    public void get_materials(View view) {
        // create volley request queue
        requestQueue = Volley.newRequestQueue(this);


        JsonArrayRequest objectRequest = new JsonArrayRequest(
//            Request.Method.GET,
            url + "?upc=" + upc.getText() + "&zip=" + zipCode.getText(),
//            null,
            new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {
                    //String r = response.toString();
                    //results.setText(r);

                    try {

                        JSONObject obj = response.getJSONObject(0);
//                        String component = obj.getString("component");
//                        String material = obj.getString("material");
//                        data += "Component: " + component +
//                                "\nMaterial: " + material;
//
//                        obj = response.getJSONObject(1);
//                        component = obj.getString("component");
//                        material = obj.getString("material");
//                        data += "\nComponent: " + component +
//                                "\nMaterial: " + material;

                        productLabel.setVisibility(View.VISIBLE);
                        recycleInfoLabel.setVisibility(View.VISIBLE);
                        product.setText(obj.getString("product"));

                    }
                    catch (JSONException e) {
                        productLabel.setVisibility(View.VISIBLE);
                        recycleInfoLabel.setVisibility(View.INVISIBLE);
                        product.setText("Could not be retrieved");
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    productLabel.setVisibility(View.VISIBLE);
                    recycleInfoLabel.setVisibility(View.INVISIBLE);
                    product.setText("Could not be retrieved");
                    Log.e("Volley", "Error");
                }
            });
        requestQueue.add(objectRequest);
    }
}