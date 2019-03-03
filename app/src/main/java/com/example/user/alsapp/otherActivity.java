package com.example.user.alsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class otherActivity extends AppCompatActivity {

    private EditText other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        other =(EditText) findViewById(R.id.otherTxt);
    }

    public void saveClicked(View view) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd;HH:mm:ss");
        Date date = new Date();
        String fileName="other;"+dateFormat.format(date);
        fileSave fileSave=new fileSave(this,fileName, other.getText().toString());
        finish();

    }
}
