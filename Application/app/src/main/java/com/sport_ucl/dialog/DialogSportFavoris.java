package com.sport_ucl.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sport_ucl.DBAdapter;
import com.sport_ucl.DisciplineToken;
import com.sport_ucl.FireHelp;
import com.sport_ucl.PlanningSingleton;
import com.sport_ucl.R;
import com.sport_ucl.UserSingleton;
import com.sport_ucl.module.SportFavoris;

/**
 * Created by Nour-Eddine on 17/03/18.
 */

/**
 * Boite de dialogue qui s'ouvre lorsqu'on selectionne un event dans le module
 * favoris ==> Permet de delete un event de ses favoris.
 */

public class DialogSportFavoris extends AppCompatDialogFragment {

     private long discardUserId;
     private long discardEventId;
    private TextView txtSportSexe;
    private TextView txtSportLieu;
    private TextView txtSportSalle;
    private TextView txtSportJour;
    private TextView txtSportDate;
    private TextView txtSportHeureDebut;
    private TextView txtSportHeureFin;
    private TextView txtSportType;
    private TextView txtSportInscription;
    private TextView txtSportRemarques;
    private TextView txtSportActive;
    private DisciplineToken token;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sport_planning, null);

        builder.setView(view)
                .setTitle(token.getSport())
                .setPositiveButton("Supprimer des favoris", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DBAdapter dbAdapter = new DBAdapter(getContext());
                        discardUserId = UserSingleton.getINSTANCE().getUser().getId();
                        discardEventId = PlanningSingleton.getINSTANCE().getToken().getId();
                        dbAdapter.deleteFavoris(discardUserId, discardEventId);
                        FireHelp fire = new FireHelp(getContext());
                        fire.deleteFavoris(discardUserId, discardEventId);
                        SportFavoris.changeListViewAdapter();
                    }
                });

        txtSportSexe = view.findViewById(R.id.txtSportSexe);
        txtSportLieu = view.findViewById(R.id.txtSportLieu);
        txtSportSalle = view.findViewById(R.id.txtSportSalle);
        txtSportJour = view.findViewById(R.id.txtSportJour);
        txtSportDate = view.findViewById(R.id.txtSportDate);
        txtSportHeureDebut = view.findViewById(R.id.txtSportHeureDebut);
        txtSportHeureFin = view.findViewById(R.id.txtSportHeureFin);
        txtSportType = view.findViewById(R.id.txtSportType);
        txtSportInscription = view.findViewById(R.id.txtSportInscription);
        txtSportRemarques = view.findViewById(R.id.txtSportRemarques);
        txtSportActive = view.findViewById(R.id.txtSportActive);

        txtSportSexe.setText(token.getSexe());
        txtSportLieu.setText(token.getLieu());
        txtSportSalle.setText(token.getSalle());
        txtSportJour.setText(token.getJour());
        txtSportDate.setText(token.getDate());
        txtSportHeureDebut.setText(token.getHeureDebut());
        txtSportHeureFin.setText(token.getHeureFin());
        txtSportType.setText(token.getType());
        txtSportInscription.setText(token.getInscription());
        //Todo changer ça en récursion
        if (token.getRemarques() != null) {
            txtSportRemarques.setText(token.getRemarques().get(0).getRemarque());
        }
        else{
            txtSportRemarques.setText("Aucunes remarques");
        }
        txtSportActive.setText(token.getActive().toString());


        return builder.create();
    }
    public void setEvenement(DisciplineToken token) {
        this.token = token;
    }
}
