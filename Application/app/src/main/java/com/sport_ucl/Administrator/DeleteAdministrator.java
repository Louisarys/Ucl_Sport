package com.sport_ucl.Administrator;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bumptech.glide.load.resource.drawable.DrawableDecoderCompat;
import com.sport_ucl.DBAdapter;
import com.sport_ucl.DisciplineToken;
import com.sport_ucl.EvenementListAdapter;
import com.sport_ucl.PlanningSingleton;
import com.sport_ucl.R;
import java.util.ArrayList;

import com.sport_ucl.dialog.DialogSportDeleteEvent;

/**
 * Created by Nour-Eddine on 2/04/18.
 */


public class DeleteAdministrator extends AppCompatActivity {

    private static final String TAG = "DeleteAdministrator";
    public static String dsport = null;
    public static String dsalle = null;
    public static String ddate = null;
    public static String dheure = null;
    public static String dLieu = null;
    private static ListView dListView;
    private static ArrayList<DisciplineToken> dEventList;
    private static Context dContext;
    private static long deleteEventId ;


    /**
     * Partie Delete event (voir explications Administrateur.java)
     * On va reproduire la même fenetre que pour les event sauf que la pop-up
     * qui s'ouvre va permetre une seule option qui est le delete.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_event_del);

        dContext = DeleteAdministrator.this;
        dListView = (ListView) findViewById(R.id.deleteListView);


        dEventList = PlanningSingleton.getINSTANCE().getTokenList(DeleteAdministrator.this);
        final EvenementListAdapter adapter = new EvenementListAdapter(this, R.layout.adapter_view_layout, dEventList);
        dListView.setAdapter(adapter);
        /*
        CLICK sur un evement de la liste
        Lorsque l'on clique dessus, un Dialog va apparaître avec toutes les informations relatives
        à l'évenement.
         */
        dListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                deleteEventId = dEventList.get(position).getId();
                openDialogDeleteEvent(dEventList.get(position));

            }
        });

    }
    public void openDialogDeleteEvent(DisciplineToken token) {
        DialogSportDeleteEvent dialogDelete = new DialogSportDeleteEvent();
        dialogDelete.setEvenement(token);
        dialogDelete.show(getSupportFragmentManager(), "exemple dialog sport");
    }

    public static void changeListViewAdapter(){
         dEventList = PlanningSingleton.getINSTANCE().getTokenList(dContext);

        final EvenementListAdapter adapterFavoris = new EvenementListAdapter(dContext,R.layout.adapter_view_layout, dEventList);
        dListView.setAdapter(adapterFavoris);
    }
}
