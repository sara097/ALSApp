package com.example.user.alsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private String user;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        header=(TextView) findViewById(R.id.headerTxt);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            Intent i = new Intent(getBaseContext(), FirstUse.class);
            startActivity(i);
        }

        readData("dane");
        header.setText(user);



    }

    public void readData(String name) {


        try {
            //utworzenie pliku a nastepnie InputStreamReadera i BufferedReadera
            FileInputStream fis = openFileInput(name);
            InputStreamReader reader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String strLine = null;
            //jesli istnieje linia odczytanie jej zawartosci
            if ((strLine = bufferedReader.readLine()) != null) {
                String[] lines = strLine.split("!");
                String[] line = null;

                for (String line1 : lines) {
                    //wszystkie pomiary mialy po kilka wartosci rozdzielonych sredniakmi, wiec je tez rodzielamy
                    line = line1.split(";");
                }
                user ="Witaj, "+ line[0];


                bufferedReader.close();
                reader.close();
                fis.close();
            }

        } catch (java.io.IOException e) {
            //obsługa wyjątku wraz z wyswietleniem uzytkownikowi komunikatu
            Toast.makeText(this, "Cannot read data", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }


    public void gaitClicked(View view) {
        Intent i=new Intent(getBaseContext(), gaitActivity.class);
        startActivity(i);
    }

    public void handClicked(View view) {
        Intent i=new Intent(getBaseContext(), handActivity.class);
        startActivity(i);
    }

    public void tapClicked(View view) {
        Intent i=new Intent(getBaseContext(), tapActivity.class);
        startActivity(i);
    }

    public void otherClicked(View view) {
        Intent i=new Intent(getBaseContext(), otherActivity.class);
        startActivity(i);
    }

    public void browseClicked(View view) {
        Intent i=new Intent(getBaseContext(), browseActivity.class);
        startActivity(i);
    }

    public void tapTwoClicked(View view) {
        Intent i=new Intent(getBaseContext(), TwoTapActivity.class);
        startActivity(i);
    }
}
