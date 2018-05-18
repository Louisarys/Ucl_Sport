package com.sport_ucl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TestDBUpdate {
    private FirebaseDatabase database;
    private DBAdapter dbAdapter;
    private Context context;

    public TestDBUpdate(FirebaseDatabase database, DBAdapter dbAdapter, Context context){
        this.database = database;
        this.dbAdapter = dbAdapter;
        this.context = context;
    }

    /************************** methode de test *********************************/

    //test validate

    public void databaseUpdateDisciplines(UpdateId updateId, UpdateIdWeb updateIdWeb){
        final DBAdapter dbAdapter = new DBAdapter(context);
        ArrayList<DisciplineToken> disciplineTokens = dbAdapter.updateGetPlanning(updateIdWeb.getMax_id());
        for (int i = 1; i <= disciplineTokens.size() ; i++){
            Log.d("debbug online", "et de : " + i);
            database.getReference("disciplines").child(String.valueOf(disciplineTokens.get(i-1).getId())).setValue(disciplineTokens.get(i-1));
        }
        database.getReference("stock_id").child("disciplines").setValue(updateId.getMax_id()+1);
        database.getReference("update").child("disciplines").child("version").setValue(updateId.getId());
        database.getReference("update").child("disciplines").child("max_id").setValue(updateId.getMax_id());
    }

    //test validate

    public void localUpdateFavoris(final long userId, UpdateId updateId, final UpdateIdWeb updateIdWeb){
        final ArrayList<Long> eventId = new ArrayList<>();
        DatabaseReference favoris = database.getReference("favoris");
        Log.d("debbug backend", "UpdateFavoris");
        favoris.child(String.valueOf(userId)).orderByKey().startAt(String.valueOf(updateId.getMax_id()+1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d("debbug backend", "one getFavoris");
                    eventId.add(snapshot.getValue(Long.class));
                }
                dbAdapter.updateInsertFavoris(eventId, userId, updateIdWeb);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //test validate

    public void localUpdateDisciplines( UpdateId updateId, final UpdateIdWeb updateIdWeb){
        final ArrayList<DisciplineToken> tokens = new ArrayList<>();
        DatabaseReference disciplines = database.getReference("disciplines");

        disciplines.orderByKey().startAt(String.valueOf(updateId.getMax_id()+1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    tokens.add(new DisciplineToken(snapshot.child("sport").getValue(String.class),
                            snapshot.child("jour").getValue(String.class), snapshot.child("date").getValue(String.class),
                            snapshot.child("lieu").getValue(String.class), snapshot.child("salle").getValue(String.class),
                            snapshot.child("heureDebut").getValue(String.class), snapshot.child("active").getValue(Boolean.class),
                            snapshot.child("heureFin").getValue(String.class), snapshot.child("id").getValue(long.class),
                            snapshot.child("inscription").getValue(String.class), null, snapshot.child("sexe").getValue(String.class),
                            snapshot.child("type").getValue(String.class)));
                }
                dbAdapter.updateInsertPlanning(tokens, updateIdWeb);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void localUpdateRemarques(){
        //todo a implementer lorsque les remarques seront disponible
    }

    //test validate

    public void localUpdateUpdate(final long idUser){
        final ArrayList<UpdateIdWeb> updateList = new ArrayList<>();
        DatabaseReference update = database.getReference("update");

        update.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.getKey().equals("favoris")){
                        updateList.add(new UpdateIdWeb(snapshot.child(String.valueOf(idUser)).child("version").getValue(long.class), snapshot.getKey(), snapshot.child(String.valueOf(idUser)).child("max_id").getValue(long.class)));
                    }
                    else {
                        updateList.add(new UpdateIdWeb(snapshot.child("version").getValue(long.class), snapshot.getKey(), snapshot.child("max_id").getValue(long.class)));
                    }
                }
                dbAdapter.updateinsertUpdate(updateList);
                dbAdapter.testGetUpdateId();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
