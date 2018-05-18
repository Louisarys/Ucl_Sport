package com.sport_ucl;

//cette classe a pour fonction de mettre a jour la base de donnee locale a partir
//de la base de donnee en ligne

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DBUpdate {
    private FirebaseDatabase database;
    private DBAdapter dbAdapter;
    private Context context;

    public DBUpdate(FirebaseDatabase database, DBAdapter dbAdapter, Context context){
        this.database = database;
        this.dbAdapter = dbAdapter;
        this.context = context;
    }

    //update la branche favoris de la database en ligne a l'aide de la database locale

    public void databaseUpdateFavoris(long idUser, UpdateId updateId, UpdateIdWeb updateIdWeb){
        DBAdapter dbAdapter = new DBAdapter(context);
        ArrayList<Long> favorisId = dbAdapter.updateGetId(updateIdWeb.getMax_id());
        for (int i = 1  ; i <= favorisId.size() ; i++){
            database.getReference("favoris").child(String .valueOf(idUser)).child(String.valueOf(i+updateIdWeb.getMax_id())).setValue(favorisId.get(i-1));
        }
        database.getReference("update").child("favoris").child("version").setValue(updateId.getId());
        database.getReference("update").child("favoris").child("max_id").setValue(updateId.getMax_id());
    }

    //update la branche disciplines de la database en ligne a l'aide de la database locale

    public void databaseUpdateDisciplines(UpdateId updateId, UpdateIdWeb updateIdWeb){
        final DBAdapter dbAdapter = new DBAdapter(context);
        ArrayList<DisciplineToken> disciplineTokens = dbAdapter.updateGetPlanning(updateIdWeb.getMax_id());
        for (int i = 1; i <= disciplineTokens.size() ; i++){
            database.getReference("disciplines").child(String.valueOf(disciplineTokens.get(i-1).getId())).setValue(disciplineTokens.get(i-1));
        }
        database.getReference("stock_id").child("disciplines").setValue(updateId.getMax_id()+1);
        database.getReference("update").child("disciplines").child("version").setValue(updateId.getId());
        database.getReference("update").child("disciplines").child("max_id").setValue(updateId.getMax_id());
    }

    //update la branche remarques de la database en ligne a l'aide de la database locale

    public void databaseUpdateRemarques(UpdateId updateId, UpdateIdWeb updateIdWeb){
        //todo a implementer lorsque les remarques seront disponible
    }

    //update la branche update de la database en ligne a l'aide de la database locale

    public void databaseUpdateUpdate(final long userId){
        DatabaseReference update = database.getReference("update");

        update.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //on efface tout
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    snapshot.getRef().removeValue();
                }
                //on rajoute les valeurs à partir de la db locale dans la db online
                DBAdapter dbAdapter = new DBAdapter(context);
                ArrayList<WebUpdate> webUpdates = dbAdapter.updateGetUpdate();
                for (int i = 1 ; i <= webUpdates.size() ; i++){
                    if (webUpdates.get(i).getBranche().equals("favoris")){
                        dataSnapshot.getRef().child(webUpdates.get(i).getBranche()).child(String.valueOf(userId)).child("version").setValue(webUpdates.get(i).getVersion());
                        dataSnapshot.getRef().child(webUpdates.get(i).getBranche()).child(String.valueOf(userId)).child("max_id").setValue(webUpdates.get(i).getMax_id());
                    }
                    else {
                        dataSnapshot.getRef().child(webUpdates.get(i).getBranche()).child("version").setValue(webUpdates.get(i).getVersion());
                        dataSnapshot.getRef().child(webUpdates.get(i).getBranche()).child("max_id").setValue(webUpdates.get(i).getMax_id());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*************************** methodes d'updates locales *****************************/

    //update la Table Favoris de la database locale a l'aide de la database en ligne

    public void localUpdateFavoris(final long userId, UpdateId updateId, final UpdateIdWeb updateIdWeb){
        final ArrayList<Long> eventId = new ArrayList<>();
        DatabaseReference favoris = database.getReference("favoris");

        favoris.child(String.valueOf(userId)).orderByKey().startAt(String.valueOf(updateId.getMax_id()+1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
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

    //update la Table Planning de la database locale a l'aide de la database en ligne

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

    //update la Table Remarques de la database locale a l'aide de la database en ligne

    public void localUpdateRemarques(){
        //todo a implementer lorsque les remarques seront disponible
    }

    //update la Table Update de la database locale a l'aide de la database en ligne

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
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //update la Table Gps de la database locale a l'aide de la database en ligne

    public void localUpdateGps(final UpdateIdWeb updateIdWeb){
        final ArrayList<GpsObject> gpsList = new ArrayList<>();
        DatabaseReference update = database.getReference("gps");

        update.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    gpsList.add(new GpsObject(Long.valueOf(snapshot.getKey()), snapshot.child("nom")
                            .getValue(String.class), snapshot.child("lieu").getValue(String.class),
                            snapshot.child("longitude").getValue(Double.class), snapshot.child("latitude").getValue(Double.class)));
                }
                dbAdapter.updateInsertGps(gpsList, updateIdWeb);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
