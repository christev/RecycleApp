package com.example.morganhoward.recycleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

//  sources:
// (1) https://www.thorntech.com/2016/03/parsing-json-android-using-volley-library/
// (2) https://developer.android.com/training/volley/request.html#request-json
public class ProductSearch extends AppCompatActivity {

    TextView results;

    //String url = "https://python-tutorial-155402.appspot.com/books";
    String url = "https://raw.githubusercontent.com/ianbar20/JSON-Volley-Tutorial/master/Example-JSON-Files/Example-Object.JSON";
    String data = "";
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);

        // create volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Cast results into TextView w/ id jsondata
        results = (TextView) findViewById(R.id.jsondata);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                //String r = response.toString();
                //results.setText(r);

                try {

                    JSONObject obj = response.getJSONObject("colorObject");
                    String color = obj.getString("colorName");
                    String desc = obj.getString("description");

                    data += "Color Name: " + color +
                            "nDescription : " + desc;

                    results.setText(data);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Error");
            }
        });
        requestQueue.add(objectRequest);
    }
}
