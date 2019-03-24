package com.example.user.alsapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class gaitActivity extends AppCompatActivity implements SensorEventListener {

    //atrybuty klasy
    private static final String TAG = "gaitActivity";
    //czujniki i powerManager
    private SensorManager mySensorManager;
    private Sensor accelerometer;
    private PowerManager powerManager;
    private PowerManager.WakeLock myWakeLock;

    private boolean isRunning = false; //zmienna okreslajaca czy zbieramy dane czy nie

    private XYSeries seriesX = new XYSeries("X");
    private XYSeries seriesY = new XYSeries("Y");
    private XYSeries seriesZ = new XYSeries("Z");

    private StringBuilder data = new StringBuilder(); //string builder przechowujacy dane, ktore mozna nastepnie zapisac do pliku
    private int counter = 0; //licznik (do osi OX wykresu)

    //elementy potrzebne do wykresu
    private XYMultipleSeriesRenderer mrenderer;
    private LinearLayout chartLayout;
    private Button startBtn;
    private MediaPlayer mp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gait);


        //ustawienie czujnika - akcelerometru
        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mySensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //wake lock zeby aplikacja mogla dzialac, gdy telefon zostanie zablokowany
        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        myWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "myapp:test");

        startBtn=(Button) findViewById(R.id.startBtn);

        //utworzenie rendererów serii danych i doprecyzowanie wyglądu serii danych na wykresie
        XYSeriesRenderer rendererX = new XYSeriesRenderer();
        rendererX.setLineWidth(2);
        rendererX.setColor(Color.MAGENTA);
        rendererX.setPointStyle(PointStyle.DIAMOND);
        rendererX.setPointStrokeWidth(6);
        rendererX.setLineWidth(3);

        XYSeriesRenderer rendererY = new XYSeriesRenderer();
        rendererY.setLineWidth(2);
        rendererY.setColor(Color.GREEN);
        rendererY.setPointStyle(PointStyle.DIAMOND);
        rendererY.setPointStrokeWidth(6);
        rendererY.setLineWidth(3);

        XYSeriesRenderer rendererZ = new XYSeriesRenderer();
        rendererZ.setLineWidth(2);
        rendererZ.setColor(Color.BLUE);
        rendererZ.setPointStyle(PointStyle.DIAMOND);
        rendererZ.setPointStrokeWidth(6);
        rendererZ.setLineWidth(3);

        //dodanie otworzonych wczesniej rendererów do listy rendererów i ustawienie maksimów i minimów wykresu
        mrenderer = new XYMultipleSeriesRenderer();
        mrenderer.addSeriesRenderer(rendererX);
        mrenderer.addSeriesRenderer(rendererY);
        mrenderer.addSeriesRenderer(rendererZ);
        mrenderer.setYAxisMax(15);
        mrenderer.setYAxisMin(-10);
        mrenderer.setShowGrid(true);

        //zainicjowanie elementów GUI
        chartLayout = (LinearLayout) findViewById(R.id.plotLayout);
        mp = MediaPlayer.create(this, R.raw.beep);

    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        //metoda reagujaca na zmianę wartosci rejestrowanej przez czujnik

        if (isRunning) { //gdy pomiar jest wykonywany

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) { //gdy typ sensora to akcelerometr

                //zapisujemy do zmiennych skladowe mierzonego przyspieszenia oraz czas
                float aX = event.values[0];
                float aY = event.values[1];
                float aZ = event.values[2];
                float timeStamp = event.timestamp;

                counter++;// zwiekszam licznik
                //licznik jest potrzebny do wykonywania wykresu
                // (kolejne wartosci są bardziej czytelne niż timeStamp w nanosekundach)

                //wyswietlam wartosci skladowej x przysieszenia i czasu
                Log.d(TAG, "aX= " + Float.toString(aX) + " timeStamp " + Float.toString(timeStamp));


                //dodanie do serii danych wartosci skladowych przyspieszenia
                seriesX.add(counter, aX);
                seriesY.add(counter, aY);
                seriesZ.add(counter, aZ);

                //zapisanie w zmiennej typu StringBuilder wartosci, zeby mozna bylo je zapisac do pliku
                String toData = counter + ";" + aX + ";" + aY + ";" + aZ + "!";
                data.append(toData);

                if (counter > 300) {
                    startBtn.performClick();
                    drawCurrent();

                }

            }
        }
    }

    public void clearData() {
        //metoda wywoływana po kliknieciu przycisku czyszczącego dane
        //usuwanie danych z serii danych
        seriesX.clear();
        seriesY.clear();
        seriesZ.clear();
        //zerowanie licznika
        counter = 0;
        //zerowanie danych wpisywanych do pliku
        data = new StringBuilder();

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void startClicked(View view) {
        Log.d(TAG, "Button pressed");


        isRunning = !isRunning; //zmienna ktora docyduje o tym, czy dokonujemy pomiaru czy tez nie
        startBtn.setVisibility(View.GONE);
        if (isRunning) {
            myWakeLock.acquire(); //jesli pomiar ma byc wykonany musimy tez pozwolic aplikacji na pomiary przy zablokowanym telefonie
            clearData();
        } else {
            myWakeLock.release(); //gdy zatrzymujemy pomiar wylaczamy tę funkcję

        }
    }

    public void drawCurrent() {

        mp.start();



        chartLayout.removeAllViews(); //usuwanie tego co bylo na wykresie
        //dodanie serii danych do wykresu
        XYMultipleSeriesDataset mdataset = new XYMultipleSeriesDataset();
        mdataset.addSeries(seriesX);
        mdataset.addSeries(seriesY);
        mdataset.addSeries(seriesZ);

        //wyświetlenie wykresu
        GraphicalView chartView = ChartFactory.getLineChartView(this, mdataset, mrenderer);
        chartLayout.addView(chartView);
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd;HH:mm:ss");
        Date date = new Date();
        String fileName="gait;"+dateFormat.format(date);
        fileSave fileSave=new fileSave(this,fileName, data.toString());

    }

}
