package com.example.morganhoward.recycleapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;

//  sources:
// (1) https://www.thorntech.com/2016/03/parsing-json-android-using-volley-library/
// (2) https://developer.android.com/training/volley/request.html#request-json
// (3) http://www.worldbestlearningcenter.com/tips/Android-populate-listview-from-json-using-volley.htm
// (4) http://stackoverflow.com/questions/12642382/generate-listview-from-json-in-android
public class ProductSearch extends AppCompatActivity {

    TextView productLabel;
    TextView recycleInfoLabel;
    TextView product;
    EditText zipCode;
    EditText upc;
    JSONObjectAdaptor adapter;
    ArrayList<JSONObject> components;
    RequestQueue requestQueue;

    String url = "http://web.engr.oregonstate.edu/~kiesslim/recycleit/recycle" +
            ".php";

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

        // Instantiate ListView, adaptor, and arrayList objects for the ListView
        ListView listView=(ListView)findViewById(R.id.listView);
        components = new ArrayList<JSONObject>();
        adapter = new JSONObjectAdaptor(this, R.layout.item_layout,
                components);
        // Associate the adapter with the ListView. Data will be added later
        // and the adapter will refresh the listview with it's updated contents
        listView.setAdapter(adapter);
    }

    // Custom ListView adaptor class to populate a listview with an arbitrary
    // number of product components
    private class JSONObjectAdaptor extends ArrayAdapter<JSONObject> {
        private ArrayList<JSONObject> obj;
        public JSONObjectAdaptor(Context context, int textViewResourceId,
                                 ArrayList<JSONObject> objs) {
            super(context, textViewResourceId, objs);
            this.obj = objs;
        }

        // This will be called automatically to populate
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService
                        (Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.item_layout, null);
            }

            // Get the TextViews that are going to be populated in each
            // ListView entry
            JSONObject o = obj.get(position);
            TextView curb = (TextView) v.findViewById(R.id.curbside);
            TextView webs = (TextView) v.findViewById(R.id.website);
            TextView inst = (TextView) v.findViewById(R.id.instructions);
            TextView comp = (TextView) v.findViewById(R.id.component);
            TextView mat = (TextView) v.findViewById(R.id.material);

            try {
                // Set the values of the TextViews to the data retrieved from
                // the products recycling DB
                curb.setText(o.getInt("isCurbside") == 0 ? "No" : "Yes");
                webs.setText(o.getString("website"));
                inst.setText(o.getString("instructions"));
                comp.setText(o.getString("component"));
                mat.setText(o.getString("material"));
            }
            catch (JSONException e) {
                // Display error that JSON data could not be parsed
                productLabel.setVisibility(View.VISIBLE);
                recycleInfoLabel.setVisibility(View.INVISIBLE);
                product.setText("Could not parse");
            }
            return v;
        }
    }

    public void get_materials(View view) {
        // create volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // JSONArrayRequest since the database returns an array of JSON objects
        JsonArrayRequest objectRequest = new JsonArrayRequest(
            // Build query URL
            url + "?upc=" + upc.getText() + "&zip=" + zipCode.getText(),
            // Add a query response listener
            new Response.Listener<JSONArray>() {

                @Override
                public void onResponse(JSONArray response) {

                    try {
                        JSONObject obj = response.getJSONObject(0);

                        // Clear ArrayList in case this is a subsequent request,
                        // or else the ListView will continue displaying old
                        // results
                        components.clear();

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                // Add each onject in the JSON array to the
                                // ArrayList
                                components.add(response.getJSONObject(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        // Tell adapter to update listView
                        adapter.notifyDataSetChanged();

                        // Add some text labels to the layout
                        productLabel.setVisibility(View.VISIBLE);
                        recycleInfoLabel.setVisibility(View.VISIBLE);
                        product.setText(obj.getString("product"));

                    }
                    catch (JSONException e) {
                        // Display error message
                        productLabel.setVisibility(View.VISIBLE);
                        recycleInfoLabel.setVisibility(View.INVISIBLE);
                        product.setText("JSON data could not be parsed");
                        e.printStackTrace();
                    }
                }
            },
            // Error handler in case of bad response
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