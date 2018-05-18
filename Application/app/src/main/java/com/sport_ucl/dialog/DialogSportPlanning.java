package com.sport_ucl.dialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//public class ExampleDialogSportPlanning extends AppCompatDialogFragment {
import com.sport_ucl.DBAdapter;
import com.sport_ucl.DisciplineToken;
import com.sport_ucl.FireHelp;
import com.sport_ucl.R;
import com.sport_ucl.UserSingleton;
import com.sport_ucl.module.Planning;

/**
 * Created by nour-eddine on 11/03/18.
 */

/**
 * Boite de dialogue qui s'ouvre lorsqu'on selectionne un event dans le module
 * planning pour avoir plus d'info sur l'event.
 */

public class DialogSportPlanning extends AppCompatDialogFragment {

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
    private long notreIdUser;
    private long notreIdEvent;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sport_planning, null);



        builder.setView(view)
                .setTitle(token.getSport())
                .setPositiveButton("Enregister en favoris", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                                    notreIdUser = UserSingleton.getINSTANCE().getUser().getId();
                                    notreIdEvent = Planning.geteventId();
                                    DBAdapter dbAdapter = new DBAdapter(getContext());
                                    //TODO griser le btn avant le premier click dessus

                                    if (dbAdapter.isSubscribeEvent (notreIdUser, notreIdEvent))
                                    {
                                        Toast.makeText(getContext(), "Cet événement est déjà dans vos favoris", Toast.LENGTH_LONG).show();

                                        ((AlertDialog)dialog).getButton(which).setVisibility(View.INVISIBLE);
                                    }
                                    else
                                    {
                                        dbAdapter.insertFavoris(notreIdUser, notreIdEvent);
                                        FireHelp fire = new FireHelp(getContext());
                                        fire.addFavoris(notreIdUser, notreIdEvent);
                                        Toast.makeText(getContext(), "Votre inscription est réussie", Toast.LENGTH_LONG).show();
                                        ((AlertDialog)dialog).getButton(which).setVisibility(View.INVISIBLE);
                                    }

                    }
                })
                .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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



    public DisciplineToken getToken() {
        return this.token;
    }

    public void setEvenement(DisciplineToken token) {
        this.token = token;
    }
}
