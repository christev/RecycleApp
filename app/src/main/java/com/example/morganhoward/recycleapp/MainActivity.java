package com.example.morganhoward.recycleapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goto_product(View view) {
        Intent intent = new Intent(this, ProductSearch.class);
        startActivity(intent);
    }

    public void goto_zipcode(View view) {
        Intent intent = new Intent(this, Zipcode.class);
        startActivity(intent);
    }

    public void goto_barcode(View view) {
        Intent intent = new Intent(this, Barcode.class);
        startActivity(intent);
    }
}
