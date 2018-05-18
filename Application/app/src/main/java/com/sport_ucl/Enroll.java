package com.sport_ucl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.sport_ucl.module.Planning;

/**
 * Created by Nour-Eddine on 12/03/18.
 */

public class Enroll extends AppCompatActivity {

    private TextView eventSportSelected   ;
    private TextView eventSalleSelected   ;
    private TextView eventDateSelected    ;
    private TextView eventHeureSelected   ;
    private TextView eventFavorisSelected ;

    private Button enroll_btn_event ;
    private Button unsubscribe_btn_event ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        enroll_btn_event     = (Button) findViewById(R.id.event_enroll_btn);
        unsubscribe_btn_event= (Button) findViewById(R.id.event_unsubscribe_btn);
        eventSportSelected   = (TextView) findViewById(R.id.eventSportSelected_tv)  ;
        eventSalleSelected   = (TextView) findViewById(R.id.eventSalleSelected_tv)  ;
        eventDateSelected    = (TextView) findViewById(R.id.eventDateSelected_tv)   ;
        eventHeureSelected   = (TextView) findViewById(R.id.eventHeureSelected_tv)  ;
        eventFavorisSelected = (TextView) findViewById(R.id.eventFavorisSelected_tv);

        eventSportSelected.setText(Planning.esport);
        eventSalleSelected.setText(Planning.esalle);
        eventDateSelected.setText(Planning.edate);
        eventHeureSelected.setText(Planning.eheure);
        eventFavorisSelected.setText(Planning.eLieu);

        enroll_btn_event.setEnabled(true);
        unsubscribe_btn_event.setEnabled(false);
/*
        enroll_btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enroll_btn_event.setEnabled(false);
                enroll_btn_event.setBackgroundColor(Color.DKGRAY);
                unsubscribe_btn_event.setBackgroundColor(Color.BLUE);
                unsubscribe_btn_event.setEnabled(true);
                Toast t1 = Toast.makeText(getApplicationContext(), "You are enroll for this event" , Toast.LENGTH_LONG);
                t1.show();
                // Add une personne à l'event Avec son nom et prenom
            }
        });
        unsubscribe_btn_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enroll_btn_event.setEnabled(true);
                unsubscribe_btn_event.setEnabled(false);
                unsubscribe_btn_event.setBackgroundColor(Color.DKGRAY);
                enroll_btn_event.setBackgroundColor(Color.BLUE);
                Toast t2 = Toast.makeText(getApplicationContext(), "You are unsubscribe for this event" , Toast.LENGTH_LONG);
                t2.show();
                // -1 personne à l'event Avec son nom et son prenom
            }
        });
*/
        //@Pre
        // Champ de recherche ==> Uniquement disponnible quand bdd okok
        //@Post
        // Maintenant que l'event est affiché qu'est-ce qu'on en fait ?
        // D'autres variables a ajouter en base de donnée ? nombre de personnes inscrit ?
        // getion du button enroll ???
        // Un btn "My events" pour voir les events ou il est inscrit en haut a droit
        // Un titre au dessus au milieu
        // En haut à gauche un btn back
        // en bas un btn recherche




    }



}
