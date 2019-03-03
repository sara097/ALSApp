package com.example.user.alsapp;

import android.content.Context;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import static android.content.Context.MODE_PRIVATE;

public class fileSave {
    private Context context;
    private String name;
    private String text;

    public fileSave(Context con,String name, String text) {
        this.context=con;
        this.text=text;
        this.name=name;
        saveData();
    }

    private void saveData() {

        try {
            //utworzenie pliku do zapisu
            FileOutputStream fOut = context.openFileOutput(name,
                    MODE_PRIVATE);
            //utworzenie OutputStreamWritera
            OutputStreamWriter out = new OutputStreamWriter(fOut);
            //zapisanie do pliku
            out.write(text);
            out.flush();
            out.close();

            //wyswietlenie komunikatu, że zapisano dane
            Toast.makeText(context, "Data Saved", Toast.LENGTH_LONG).show();

        } catch (java.io.IOException e) {
            //obsluga wyjatku
            //w razie niepowodzenia zapisu do pliku zostaje wyswietlony komunikat a w konsoli zrzut stosu
            Toast.makeText(context, "Data Could not be added", Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }

    }
}
