package com.sport_ucl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nour-Eddine on 2/03/18.
 */


/**
 * Gestion de la récupération des différents paramètres pour
 * l'affichage de la liste des lieux pour le GPS
 */

public class GpsListAdapter extends ArrayAdapter<GpsObject> {

    private static final String Tag = "GpsListAdapter";

    private Context mContext ;
    int mResource ;

    public GpsListAdapter(Context context, int resource, ArrayList<GpsObject> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource ;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération des infos sur un evenement
        Long idGpsLieu = getItem(position).getIdGpsLieu();
        String nom = getItem(position).getNom();
        String lieu = getItem(position).getLieu();
        double longitude = getItem(position).getLongitude();
        double latitude = getItem(position).getLatitude();

        //Création d'un objet evenement avec ses infos
        GpsObject gpsObject = new GpsObject(idGpsLieu, nom, lieu,longitude, latitude);

        LayoutInflater inflater = LayoutInflater.from(mContext);

        //Variable de retour de la fonction getView()
        convertView = inflater.inflate(mResource,parent,false);

        // Récupération des champs du Xml
        TextView sportView   = (TextView) convertView.findViewById(R.id.sportVw)  ;
        TextView lieuView = (TextView) convertView.findViewById(R.id.lieuVw);
        TextView salleView   = (TextView) convertView.findViewById(R.id.salleVw)  ;
        TextView dateView    = (TextView) convertView.findViewById(R.id.dateVw)   ;
        TextView heureDebutView   = (TextView) convertView.findViewById(R.id.heureDebutVw)  ;
        TextView heureFinView   = (TextView) convertView.findViewById(R.id.heureFinVw)  ;


        // Remplissage des champs du Xml

        sportView.setText(String.valueOf(idGpsLieu));
        lieuView.setText(nom);
        salleView.setText(lieu);
        dateView.setText(String.valueOf(longitude));
        heureDebutView.setText(String.valueOf(latitude));

        return convertView;

    }
}
