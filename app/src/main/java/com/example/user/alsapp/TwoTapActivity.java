package com.example.user.alsapp;

import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TwoTapActivity extends AppCompatActivity {

    private Date date;
    private long firstTime;
    private long time;
    private ArrayList<Long> milsLeft = new ArrayList<>();
    private ArrayList<Long> milsRight = new ArrayList<>();
    private float width;
    private float x;
    private TextView info;
    private ConstraintLayout layout;
    private StringBuilder data = new StringBuilder(); //string builder przechowujacy dane, ktore mozna nastepnie zapisac do pliku
    private int counter=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_tap);

        info = (TextView) findViewById(R.id.twoTapInfo);
        layout = findViewById(R.id.layout);
        layout.setOnTouchListener(handleTouch);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

    }


    private View.OnTouchListener handleTouch = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (counter < 20) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        //wziac czas dotkniecia i po ktorej stronie
                        date = new Date();
                        if(counter==0) {
                            firstTime = date.getTime();
                            time = firstTime - firstTime;
                            if (x > (width / 2)) { //prawa strona
                                String toData = time + ";" + 1 +";"+0+"!";
                                data.append(toData);

                            } else { //lewa strona

                                String toData = time + ";" + 0 +";"+1+"!";
                                data.append(toData);
                            }
                        }
                        else{
                            time = date.getTime() - firstTime;
                            if (x > (width / 2)) { //prawa strona
                                String toData = time + ";" + 10 +";"+0+"!";
                                data.append(toData);

                            } else { //lewa strona

                                String toData = time + ";" + 0 +";"+10+"!";
                                data.append(toData);
                            }
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        //wziac czas puszczenia i po ktorej stronie

                        x = event.getX();
                        time = date.getTime() - firstTime;
                        if (x > (width / 2)) { //prawa strona
                            String toData = time + ";" + 10 +";"+0+"!";
                            data.append(toData);

                        } else { //lewa strona

                            String toData = time + ";" + 0 +";"+10+"!";
                            data.append(toData);
                        }
                        counter++;
                        break;
                }
            } else if (counter==20){
                endOfMeasure();
                counter++;
            }else{

            }



                return true;

            }

    };

    private void endOfMeasure(){

            info.setText("Badanie zakończone!");
            DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd;HH:mm:ss");
            Date date = new Date();
            String fileName = "twoTap;" + dateFormat.format(date);
            fileSave fileSave = new fileSave(this, fileName, data.toString());

    }


}
