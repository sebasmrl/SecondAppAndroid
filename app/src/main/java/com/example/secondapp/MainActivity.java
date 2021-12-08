package com.example.secondapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView mTextViewRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-----------------------
        mTextViewRegister = findViewById(R.id.TextViewRegister); //accedo a un un texto que tiene ese id en el xml

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //La intencion es que MainActivity llame a Register Activity
                Intent intentRegister =  new Intent(MainActivity.this, Register_Activity.class);
                startActivity(intentRegister);

            }
        });

    }
}