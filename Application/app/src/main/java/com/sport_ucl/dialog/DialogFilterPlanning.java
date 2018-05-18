package com.sport_ucl.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.sport_ucl.PlanningSingleton;
import com.sport_ucl.R;
import com.sport_ucl.module.Planning;

/**
 * Created by valentin on 11/03/18.
 */

public class DialogFilterPlanning extends AppCompatDialogFragment {
    private EditText etSportName;
    private EditText etSportDate;
    private EditText etSportHours;
    private Button btnApply;

    /**
     * Boite de dialogue qui s'ouvre lorsqu'on selectionne un event dans le module
     * evenement + filtrages des events.
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_filter_planning, null);

        etSportName = view.findViewById(R.id.etSportName);
        etSportDate = view.findViewById(R.id.etSportDate);
        etSportHours = view.findViewById(R.id.etSportHours);

        builder.setView(view)
                .setTitle("Filtres du planning")
                .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Filtrer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        PlanningSingleton.getINSTANCE().setParam(new String[]{etSportName.getText().toString(), etSportDate.getText().toString(),
                                etSportHours.getText().toString()});
                       Planning.changeListViewAdapter();
                    }
                });

        //AutoCompleteText creation + listener
        Bundle bundle=getArguments();
        String sportNameString[]= bundle.getStringArray("sportNameString");
        AutoCompleteTextView at=(AutoCompleteTextView)etSportName;
        at.setAdapter(new ArrayAdapter(getActivity(),R.layout.support_simple_spinner_dropdown_item,sportNameString));
        at.setThreshold(1);
        at.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                etSportName.setText(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return builder.create();
    }

    //Getters for pickers

    public EditText getEtSportDate() {
        return etSportDate;
    }

    public EditText getEtSportHours() {
        return etSportHours;
    }
}
