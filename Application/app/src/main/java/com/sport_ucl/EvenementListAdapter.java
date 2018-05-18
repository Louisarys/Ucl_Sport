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

public class EvenementListAdapter extends ArrayAdapter<DisciplineToken> {

    private static final String Tag = "EvenementListAdapter";

    private Context mContext ;
    int mResource ;

    public EvenementListAdapter(Context context, int resource, ArrayList<DisciplineToken> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource ;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Récupération des infos sur un evenement
        Long id = getItem(position).getId();
        String sport = getItem(position).getSport();
        String sexe = getItem(position).getSexe();
        String salle = getItem(position).getSalle();
        String lieu = getItem(position).getLieu();
        String jour = getItem(position).getJour();
        String date = getItem(position).getDate();
        String heureDebut = getItem(position).getHeureDebut();
        String heureFin = getItem(position).getHeureFin();
        String type = getItem(position).getType();
        String inscription = getItem(position).getInscription();
        ArrayList<DisciplineRemarque> remarques = getItem(position).getRemarques();
        Boolean active = getItem(position).getActive();


        //Création d'un objet evenement avec ses infos
        DisciplineToken event = new DisciplineToken(sport, jour, date, lieu, salle, heureDebut,
                active, heureFin, id, inscription, remarques, sexe, type);

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

        sportView.setText(sport);
        lieuView.setText(lieu);
        salleView.setText(salle);
        dateView.setText(date);
        heureDebutView.setText(heureDebut);
        heureFinView.setText(heureFin);

        return convertView;

    }
}
