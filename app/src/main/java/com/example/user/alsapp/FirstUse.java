package com.example.user.alsapp;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import java.util.Calendar;

public class FirstUse extends AppCompatActivity {

    //atrybuty klasy, które są elementami aplikacji
    private Spinner genderSpinner;
    private EditText age;
    private EditText name;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_use);

        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);
        name=(EditText) findViewById(R.id.nameTxt);
        age = (EditText) findViewById(R.id.ETAge);
        age.setInputType(InputType.TYPE_NULL);

        //dodanie do pola tekstowego wieku OnFocusListenera
        age.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) { //jesli jest focus na tym polu tekstowym

                    Calendar mcurrentDate = Calendar.getInstance(); //utworzenie instancji kalendarza
                    //finalne zmienne na rok, miesiąc i dzień
                    final int mYear = mcurrentDate.get(Calendar.YEAR);
                    final int mMonth = mcurrentDate.get(Calendar.MONTH);
                    final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                    //dialog z datePickerem
                    DatePickerDialog datePicker = new DatePickerDialog(FirstUse.this, new DatePickerDialog.OnDateSetListener() {
                        //metoda jesli data jest wybrana
                        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                            //zmienna na przechowywanie wieku
                            int ageCalculated = mYear - selectedyear; //różnica w latach
                            //jesli wybrany miesiac jest wiekszy od obecnego lub jesli wybrany miesiac jest równy
                            // ale wybrany dzien jest wiekszy od obecnego to zmniejszam rok
                            if (selectedmonth > mMonth || (selectedmonth == mMonth && selectedday >= mDay)) {
                                ageCalculated--;
                            }
                            if(ageCalculated>0) {
                                //ustawienie zeby wiek wyswietlał sie w polu tekstowym
                                age.setText(String.valueOf(ageCalculated));
                                age.clearFocus(); //usuniecie focusu z pola tekstowego, zeby po ponowym kliknieciu pojawiło się znowu wybieranie daty
                            } else{
                                age.setText(getString(R.string.invalidTxt));
                                age.clearFocus();
                            }
                        }
                    }, mYear, mMonth, mDay);
                    datePicker.setTitle(getString(R.string.birthdateTxt)); //tytuł date pickera
                    datePicker.show(); //pokazanie date pickera
                }
            }
        });
    }

    public void okClicked(View view) {
        user=name.getText().toString()+";"+ String.valueOf(genderSpinner.getSelectedItem())+";"+age.getText().toString();
        fileSave fileSave=new fileSave(this,"dane",user);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
        finish();


    }


}
