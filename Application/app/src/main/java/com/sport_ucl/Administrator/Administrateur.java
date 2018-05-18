package com.sport_ucl.Administrator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sport_ucl.R;

/**
 * Created by Nour-Eddine on 2/04/18.
 */

public class Administrateur extends AppCompatActivity {

    /**
     * La partie administrateur consciste en un module qui permet à l'administrateur (UCL SPORT) de mettre
     * de delete un évènement ou en ajouter un.
     * L'ajout se fait par de simple champs a complèter (en cours d'implémentation).
     * Pour ce qui est de la suppression, cela est fonctionnel.
     * De plus pour chaque suppression d'évènement une notification est envoyé aux personnes qui se sont
     * enregistré aux évènements concernés.
     */

    private Button add ;
    private Button delete ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        add = (Button) findViewById(R.id.addEvent_btn);
        delete = (Button) findViewById(R.id.deleteEvent_btn);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(Administrateur.this, AddAdministrator.class);
                startActivity(addIntent);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent deleteIntent = new Intent(Administrateur.this, DeleteAdministrator.class);
                startActivity(deleteIntent);
            }
        });

    }

}
