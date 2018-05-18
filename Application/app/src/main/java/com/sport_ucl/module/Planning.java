package com.sport_ucl.module;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.sport_ucl.DisciplineToken;
import com.sport_ucl.EvenementListAdapter;
import com.sport_ucl.dialog.DialogFilterPlanning;
import com.sport_ucl.dialog.DialogSportPlanning;
import com.sport_ucl.PlanningSingleton;
import com.sport_ucl.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

/**
 * Created by Nour-Eddine on 01/03/18.
 */

public class Planning extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static String esport   = null;
    public static String esalle   = null;
    public static String edate    = null;
    public static String eheure   = null;
    public static String eLieu = null;
    private static ListView mListView;
    private static ArrayList<DisciplineToken> eventList;
    private static Context context;
    public static Button btnRecherchePlanning;
    private EditText etSportDate;
    private EditText etSportHours;
    private DialogFilterPlanning dialogFilterPlanning;
    private HashSet<String> sportNameSet = new HashSet();
    private static long eventId ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);

        context = Planning.this;
        mListView = (ListView) findViewById(R.id.listView);

        //TODO refaire une interface plus jolie pour les tokens (pas urgent)

        eventList = PlanningSingleton.getINSTANCE().getTokenList(Planning.this);
        final EvenementListAdapter adapter = new EvenementListAdapter(this,R.layout.adapter_view_layout, eventList);
        mListView.setAdapter(adapter);
        /*
        CLICK sur un evement de la liste
        Lorsque l'on clique dessus, un Dialog va apparaître avec toutes les informations relatives
        à l'évenement.
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                eventId = eventList.get(position).getId();
                openDialogSportPlanning(eventList.get(position));

            }
        });



        /*
        *   name:       btnRecherchePlanning
        *   fonction:   Ce bouton a pour but d'afficher un dialog permettant d'afficher
        *               des options de filtres sur l'affichage du planning
        * */
        btnRecherchePlanning = (Button)findViewById(R.id.btnRecherchePlanning);

        btnRecherchePlanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFilterPlanning();

            }
        });

    }

    public void openDialogSportPlanning(DisciplineToken token) {
        DialogSportPlanning dialogSportPlanning = new DialogSportPlanning();
        dialogSportPlanning.setEvenement(token);
        dialogSportPlanning.show(getSupportFragmentManager(), "exemple dialog sport");
    }

    public void openDialogFilterPlanning() {
        for(int i = 0; i < eventList.size(); i++)
        {
            sportNameSet.add(eventList.get(i).getSport());
        }
        String[] sportNameString= sportNameSet.toArray(new String[sportNameSet.size()]);

        dialogFilterPlanning = new DialogFilterPlanning();

        //Create bundle to export sports
        Bundle bundle = new Bundle();
        bundle.putStringArray("sportNameString",sportNameString);
        dialogFilterPlanning.setArguments(bundle);

        dialogFilterPlanning.show(getSupportFragmentManager(), "Exemple dialog");
    }


    public void showDatePicker(View v) {
        //Get current date
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        //Create datePicker with current date
        DatePickerDialog pickerDialog=new DatePickerDialog(Planning.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                //Get the input et fill it with the picked date
                etSportDate = dialogFilterPlanning.getEtSportDate();

                Calendar calendar = Calendar.getInstance();
                calendar.set(i, i1, i2);
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                String dateShown = format.format(calendar.getTime());
                etSportDate.setText(dateShown);
            }
        },year,month,day);
        //Show the picker
        pickerDialog.show();
    }

    public void showTimePicker(View v){
        //get current hour/minute
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        //Create datePicker with current hour/minute
        TimePickerDialog timePickerDialog= new TimePickerDialog(Planning.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                //Get the input et fill it with the picked hour/minute
                etSportHours = dialogFilterPlanning.getEtSportHours();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, i);
                calendar.set(Calendar.MINUTE, i1);
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                String dateShown = format.format(calendar.getTime());
                etSportHours.setText(dateShown);
            }
        },hour,minute,true);
        //Show the picker
        timePickerDialog.show();
    }

    public static long geteventId() {
        return eventId;
    }

    public static void changeListViewAdapter(){

        eventList = PlanningSingleton.getINSTANCE().getTokenList(context);
        final EvenementListAdapter adapter = new EvenementListAdapter(context,R.layout.adapter_view_layout, eventList);
        mListView.setAdapter(adapter);
    }
}
