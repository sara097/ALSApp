package com.example.user.alsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class tapActivity extends AppCompatActivity {

    private Date date;
    private ArrayList<Long> mils=new ArrayList<>();
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap);
        info=(TextView) findViewById(R.id.tapInfo);
        info.setText("");

    }


    public void screenTapped(View view) {
        if(mils.size()<10) {
            date = new Date();
            mils.add(date.getTime());
        }else if(mils.size()==10){
            calculateTap();
        }
    }

    private void calculateTap(){
        long avgTap=0;
        for(int i=1; i<mils.size(); i++){
            avgTap+=mils.get(i)-mils.get(i-1);
        }
        avgTap=avgTap/(mils.size()-1);
       info.setText("Średnio między kolejnymi kliknięciami upłynęło "+String.valueOf(avgTap)+"milisekund");

        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd;HH:mm:ss");
        Date date = new Date();
        String fileName="tap;"+dateFormat.format(date);
        fileSave fileSave=new fileSave(this,fileName, String.valueOf(avgTap));
    }
}
