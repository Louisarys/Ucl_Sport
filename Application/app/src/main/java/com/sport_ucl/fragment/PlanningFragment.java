package com.sport_ucl.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;

import com.sport_ucl.DisciplineToken;
import com.sport_ucl.EvenementListAdapter;
import com.sport_ucl.PlanningSingleton;
import com.sport_ucl.R;
import com.sport_ucl.dialog.DialogFilterPlanning;
import com.sport_ucl.dialog.DialogSportPlanning;
import com.sport_ucl.module.Planning;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

/**
 * Created by valentin on 13/03/18.
 */



public class PlanningFragment extends Fragment {

    private View myFragmentView;
    private ListView mListViewPlanning;
    private ArrayList<DisciplineToken> eventList;
    private static long eventId ;
    private DialogFilterPlanning dialogFilterPlanning;
    private HashSet<String> sportNameSet = new HashSet();

    private EditText etSportDate;
    private EditText etSportHours;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        myFragmentView = inflater.inflate(R.layout.fragment_planning, container, false);

        mListViewPlanning = (ListView) myFragmentView.findViewById(R.id.listViewPlanning);

        //TODO refaire une interface plus jolie pour les tokens (pas urgent)

        eventList = PlanningSingleton.getINSTANCE().getTokenList(getContext());
        final EvenementListAdapter adapter = new EvenementListAdapter(getContext(),R.layout.adapter_view_layout, eventList);
        mListViewPlanning.setAdapter(adapter);

        /*
        CLICK sur un evement de la liste
        Lorsque l'on clique dessus, un Dialog va apparaître avec toutes les informations relatives
        à l'évenement.
         */
        mListViewPlanning.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        Button btnRecherchePlanning = myFragmentView.findViewById(R.id.btnRecherchePlanning);

        btnRecherchePlanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogFilterPlanning();

            }
        });



        return myFragmentView;


    }

    public void openDialogSportPlanning(DisciplineToken token) {
        DialogSportPlanning dialogSportPlanning = new DialogSportPlanning();
        dialogSportPlanning.setEvenement(token);
        dialogSportPlanning.show(getFragmentManager(), "exemple dialog sport");
    }

    public void openDialogFilterPlanning() {
        for(int i = 0; i < eventList.size(); i++)
        {
            sportNameSet.add(eventList.get(i).getSport());
        }
        String[] sportNameString= sportNameSet.toArray(new String[sportNameSet.size()]);

        dialogFilterPlanning = new DialogFilterPlanning();
        //create bundle to export sports
        Bundle bundle = new Bundle();
        bundle.putStringArray("sportNameString",sportNameString);
        dialogFilterPlanning.setArguments(bundle);

        dialogFilterPlanning.show(getFragmentManager(), "Exemple dialog");
    }







}
