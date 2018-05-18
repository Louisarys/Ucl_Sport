package com.sport_ucl;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sport_ucl.module.Planning;

import java.util.ArrayList;

/**
 * Created by Nour-Eddine on 17/03/18.
 */

public class Gps extends AppCompatActivity {

    /**
     * Code pour faire une liste des endroits le nom de la salle ainsi que la géolocalisation de ceux-ci
     */


    private static final String TAG = "Gps";
    public static String GpsName   = null;
    public static String GpsLieu   = null;
    public ListView mListViewGps;
    public ArrayList<GpsObject> gpsList;

    private double latitude  ;
    private double longitude ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        mListViewGps = (ListView) findViewById(R.id.listViewGps);
        DBAdapter dbAdapter = new DBAdapter(Gps.this);

        gpsList = dbAdapter.getGpsObject();

        final GpsListAdapter adapter = new GpsListAdapter(this,R.layout.adapter_view_layout_gps, gpsList);
        mListViewGps.setAdapter(adapter);

        mListViewGps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                latitude  = gpsList.get(position).getLatitude() ;
                longitude = gpsList.get(position).getLongitude();

                System.out.println("latitude: " + latitude);
                System.out.println("longitude: " + longitude);

                //Todo Pour les tests il faut modifier la localisation de l'emulateur sinon ca prend trop de temps
                //Todo car calculer a pied la distance entre les Etat-Unis et le blocry c'est long ... très long ...

                // Pour les test ==> latitude  = 50.670677;
                // Pour les test ==> longitude = 4.602341;

                /**
                 * Différentes ouvertures du GPS: Soit l'app Google Map ou Site Web
                 */

                // Choose dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(Gps.this);
                builder.setTitle("Quelle version de Maps utiliser ?");
                builder.setPositiveButton("App", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openMapsApp();
                    }
                });
                builder.setNeutralButton("Site", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/place/"+latitude+","+longitude));
                        startActivity(browserIntent);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

    }
    /**
     * Différents paramètres lors de l'ouverture de l'app GPS
     */

    private void openMapsApp(){
        //TODO Ouverture de l'app google Map => C'EST OK CA
        //   Open Google Map

        //   Obtenir un itinéraire entre un lieu et un autre.
        //   Les itinéraires peuvent être obtenus pour trois modes de transport : en voiture, à pied ou à vélo.
        //   3 params :
        //   -   Action
        //   -   URI
        //   -   Package


        // Création de l'URI pour l'intent
        // Cela peut être select par le user : &mode=b ==> velo  &mode=w ==> pied  &mode=d ==> voiture

        Uri IntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude + "&mode=w");


        // Création de l'intent lui même
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, IntentUri);

        // Appel du package google.apps.maps
        mapIntent.setPackage("com.google.android.apps.maps");


        // gestion du cas ou l'application n'éxiste pas sur le téléphone du user

        if (mapIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivity(mapIntent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Aucune application détectée ", Toast.LENGTH_LONG).show();
        }
    }
}
