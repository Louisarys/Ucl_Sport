package com.sport_ucl.Administrator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sport_ucl.DBAdapter;
import com.sport_ucl.DisciplineToken;
import com.sport_ucl.FireHelp;
import com.sport_ucl.R;

/**
 * Created by Nour-Eddine on 2/04/18.
 */

public class AddAdministrator extends AppCompatActivity {

    private EditText addId_et ;
    private EditText addSport_et ;
    private EditText addSexe_et ;
    private EditText addLieu_et ;
    private EditText addSalle_et ;
    private EditText addJour_et ;
    private EditText addDate_et ;
    private EditText addHD_et ;
    private EditText addHF_et ;
    private EditText addType_et ;
    private EditText addInscription_et ;
    private EditText addRemarques_et ;
    private EditText addActive_et ;

    private Button add_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_add);

        add_btn = (Button) findViewById(R.id.addEvent_btn);

        addId_et = (EditText) findViewById(R.id.addId);
        addSport_et = (EditText) findViewById(R.id.addSport);
        addSexe_et = (EditText) findViewById(R.id.addSexe);
        addLieu_et = (EditText) findViewById(R.id.addLieu);
        addSalle_et = (EditText) findViewById(R.id.addSalle);
        addJour_et = (EditText) findViewById(R.id.addJour);
        addDate_et = (EditText) findViewById(R.id.addDate);
        addHD_et = (EditText) findViewById(R.id.addHD);
        addHF_et = (EditText) findViewById(R.id.addHF);
        addType_et = (EditText) findViewById(R.id.addType);
        addInscription_et = (EditText) findViewById(R.id.addInscription);
        addRemarques_et = (EditText) findViewById(R.id.addRemarques);
        addActive_et = (EditText) findViewById(R.id.addActive);


        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 //Todo dans le if il faut aussi que les heures soit différente EEETTTTTTT ne se chevauchent pas
                DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
                if (addJour_et.getText().toString().equals("") || addLieu_et.getText().toString().equals("") || addSalle_et.getText().toString().equals("")
                        || addSport_et.getText().toString().equals("") || addDate_et.getText().toString().equals("") || addHD_et.getText().toString().equals("") || addHF_et.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Veuillez compléter tous les champs comportant des étoiles", Toast.LENGTH_LONG).show();
                }
                else{
                    String[] one = addHD_et.getText().toString().split(":");
                    String[] two = addHF_et.getText().toString().split(":");

                    if (dbAdapter.newEventCondition(addJour_et.getText().toString(), addLieu_et.getText().toString().toUpperCase(), addSalle_et.getText().toString().toUpperCase()
                            , addSport_et.getText().toString().toUpperCase(), addDate_et.getText().toString(), addSexe_et.getText().toString(), addType_et.getText().toString()
                            ,Integer.valueOf(one[0])
                            , Integer.valueOf(one[1]), Integer.valueOf(two[0]), Integer.valueOf(two[1])))
                    {
                        //ajout de la discipline dans la db locale
                        DisciplineToken token = new DisciplineToken(addSport_et.getText().toString().toUpperCase(), addJour_et.getText().toString(), addDate_et.getText().toString()
                                , addLieu_et.getText().toString().toUpperCase(), addSalle_et.getText().toString().toUpperCase(), addHD_et.getText().toString()
                                , Boolean.valueOf(addActive_et.getText().toString()), addHF_et.getText().toString(), dbAdapter.getMaxIdFromUpdate("Planning"), addInscription_et.getText().toString(), null, addSexe_et.getText().toString(), addType_et.getText().toString());
                        //ajout de la discipline dans la db en ligne
                        FireHelp fire = new FireHelp(getApplicationContext());
                        fire.addADiscipline(token);
                        Toast.makeText(getApplicationContext(), "L'évenement a été ajouté avec succès", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Un événement du même type a déjà été créer", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }


}

