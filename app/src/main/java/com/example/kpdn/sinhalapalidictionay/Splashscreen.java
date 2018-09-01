package com.example.kpdn.sinhalapalidictionay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Thread thread=new Thread() {


            @Override
            public void run() {
                try{

                    sleep(1000);
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();


                }catch (Exception ex){
                    ex.printStackTrace();

                }
            }
        };

        thread.start();


    }


}
