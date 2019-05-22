package com.example.kasir;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button kasir;
    Button bos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kasir = (Button) findViewById(R.id.button);
        kasir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kas = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(kas);
            }
        });
        bos = (Button) findViewById(R.id.button2);
        bos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent boss = new Intent(MainActivity.this, Main3Activity.class);
                startActivity(boss);
            }
        });


    }}
