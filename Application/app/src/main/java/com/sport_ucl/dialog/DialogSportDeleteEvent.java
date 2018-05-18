package com.sport_ucl.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.app.NotificationChannel;
import android.support.v4.app.NotificationManagerCompat;

import com.sport_ucl.Administrator.DeleteAdministrator;
import com.sport_ucl.Administrator.NotificationHelper;
import com.sport_ucl.DBAdapter;
import com.sport_ucl.DisciplineToken;
import com.sport_ucl.FireHelp;
import com.sport_ucl.R;
import android.content.Context;
import com.sport_ucl.Administrator.DeleteAdministrator;
import com.sport_ucl.UserSingleton;

import android.app.TaskStackBuilder;
import android.widget.Toast;
import android.app.NotificationManager ;
import android.content.Context;
import android.app.NotificationChannel;
import android.app.NotificationChannel;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by Nour-Eddine on 2/04/18.
 */

public class DialogSportDeleteEvent extends AppCompatDialogFragment {

    private Context mContext;

    private DisciplineToken token;
    NotificationCompat.Builder notification ;
    private static final String uniqueID  = "123456789" ;
    public int NOTIFICATION_ID = 234;
    public static final String NOTIFICATION_CHANNEL_ID = "4565";
    private NotificationHelper mNotificationHelper;

    /**
     * Boite de dialogue qui s'ouvre lorsqu'on selectionne un event dans le module
     * administrateur.
     */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_sport_planning, null);

        mNotificationHelper = new NotificationHelper(getContext());

        builder.setView(view)
                .setTitle(token.getSport())
                .setPositiveButton("Supprimer l'évenement", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        //Todo Il faut delete la ligne dans la table event ainsi que celle dans la table user/

                        /************* Delete de la table Evenement *****************/

                        //DELETE FROM Evenement WHERE id = token.getId()


                        /*************** Send notification ********************/

                            // Récupération des id des utilisateur qui sont "Inscrit à cet evenement"

                                        //Select idUser
                                        //from Favoris
                                        //where idEvent = token.getId();

                            // Envoyer une notification à tous ces users

                                        //Todo .................

                        //Small icon (left top)
                        //App name
                        //Time
                        //Large icon (right)
                        //Title
                        //Text
                        //todo a tester
                        DBAdapter dbAdapter = new DBAdapter(getContext());
                        FireHelp fire = new FireHelp(getContext());
                        Log.d("firedebbug", "début du delete");
                        fire.deleteEvent(token.getId());
                        if (dbAdapter.isSubscribeEvent(UserSingleton.getINSTANCE().getUser().getId(), token.getId())){
                            fire.deleteFavoris(UserSingleton.getINSTANCE().getUser().getId(), token.getId());
                            dbAdapter.deleteFavoris(UserSingleton.getINSTANCE().getUser().getId(), token.getId());
                        }
                        dbAdapter.deleteEvent(token.getId());
                        String message  = token.getSport() +"/"+ token.getDate() +"/"+ token.getHeureDebut();
                        sendOnChannel("Annulation", message);
                        DeleteAdministrator.changeListViewAdapter();

                        //todo supprimer l'event sur la base de donnee et en local
//
//                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                                .setSmallIcon(R.drawable.simp2)
//                                .setLargeIcon(R.drawable.simp2)
//                                .setContentTitle("Suppression d'un évenement")
//                                .setContentText("Le "+ token.getSport() + " du "+ token.getDate() + " à "+ token.getHeureDebut()+ " a été suprimé")
//                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        /***************** Delete de la table Favoris ****************/

                        // Attention ceci est la dernière étape
                        //DELETE FROM Favoris WHERE id = token.getId()






                    }
                })
                .setNegativeButton("Quitter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();

    }


    public DisciplineToken getToken() {

        return this.token;
    }

    public void setEvenement(DisciplineToken token) {

        this.token = token;
    }

    public void sendOnChannel(String title, String message){
        NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification(title,message);
        mNotificationHelper.getManager().notify(1, nb.build());

    }



}

