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

public class TestFireHelp {
    private FirebaseDatabase database ;
    private final Context context;

    public TestFireHelp(Context context)
    {
        this.database = FirebaseDatabase.getInstance();
        this.context = context;
    }

    /**************************** methodes de testing *********************************/

    //test validate

    public void testGlobalDatabaseUpdate(final long idUser){
        final ArrayList<UpdateIdWeb> idWebList = new ArrayList<>();
        final DBAdapter dbAdapter = new DBAdapter(context);
        DatabaseReference update = database.getReference("update");

        update.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            //adaptation du Listener au besoin de la situation
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int j = 1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Log.d("compte a rebours", "boucle numero :" + j);
                    j++;
                    if (snapshot.getKey().equals("favoris")) {
                        Log.d("compte a rebours", "favoris trouvé" + j);
                        idWebList.add(new UpdateIdWeb(snapshot.child(String.valueOf(idUser)).child("version").getValue(long.class), snapshot.getKey(), snapshot.child(String.valueOf(idUser)).child("max_id").getValue(long.class)));
                    }
                    else{
                        idWebList.add(new UpdateIdWeb(snapshot.child("version").getValue(long.class), snapshot.getKey(), snapshot.child("max_id").getValue(long.class)));
                    }
                }
                /*ArrayList<UpdateId> idList = new ArrayList<>();
                idList = dbAdapter.getUpdateId();
                updateConnection(idList, sortArrayId(idList, idWebList), dbAdapter);
                */
                Log.d("debbug GlobalDatabase", "idWebList.size : " + idWebList.size());
                for (int i = 0 ; i < idWebList.size() ; i++){
                    Log.d("debbug GlobalDatabase", "idWebList.getName : " + idWebList.get(i).getName());
                    Log.d("debbug GlobalDatabase", "idWebList.getVersion : " + idWebList.get(i).getVersion());
                    Log.d("debbug GlobalDatabase", "idWebList.getMax_id : " + idWebList.get(i).getMax_id());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //test validate

    public void testUpdateConnection (ArrayList<UpdateId> idList, ArrayList<UpdateIdWeb> idWebList, DBAdapter dbAdapter){

        DBUpdate update = new DBUpdate(database, dbAdapter, context);

        for (int i = 0 ; i<idList.size() ; i++){
            //update partielle de la database locale
            if (idList.get(i).getId() < idWebList.get(i).getVersion()){
                switch (idList.get(i).getTable()){
                    case "Planning" :
                        Log.d("Test UpgradeData", "lance l'upgrade locale du planning");
                        break;
                    case "favoris" :
                        Log.d("Test UpgradeData", "lance l'upgrade locale des favoris");
                        break;
                    case "RemarquesPlanning" :
                        Log.d("Test UpgradeData", "lance l'upgrade locale des remarques");
                        break;
                    case "Update" :
                        Log.d("Test UpgradeData", "lance l'upgrade locale des updates");
                        break;
                }
            }
            else {
                //update partielle de la database en ligne
                if (idList.get(i).getId() > idWebList.get(i).getVersion()) {
                    switch (idWebList.get(i).getName()) {
                        case "disciplines":
                            Log.d("Test UpgradeData", "lance l'upgrade online du planning");
                            break;
                        case "favoris":
                            Log.d("Test UpgradeData", "lance l'upgrade online des favoris");
                            break;
                        case "remarques":
                            Log.d("Test UpgradeData", "lance l'upgrade online des remarques");
                            break;
                        case "update":
                            Log.d("Test UpgradeData", "lance l'upgrade online des updates");
                            break;
                    }
                }
            }
        }
    }

    //test validate

    public void testSortArrayId(ArrayList<UpdateId> idList, ArrayList<UpdateIdWeb> idWebList){
        ArrayList<UpdateIdWeb> idWebListReturn = new ArrayList<>();
        boolean breakLoop;
        for (int i = 0 ; i < idList.size() ; i++){
            breakLoop = false;
            int j = 0;
            while (!breakLoop){
                if (idList.get(i).getName().equals(idWebList.get(j).getName())){
                    idWebListReturn.add(idWebList.get(j));
                    idWebList.remove(j);
                    breakLoop = true;
                }
                j++;
            }
        }
        Log.d("Test SortArrayId", "idWebLisReturn.size : " + idWebListReturn.size());
        for (int i = 0 ; i<idWebListReturn.size() ; i++){
            Log.d("Test SortArrayId", "idWebLisReturn.getName : " + idWebListReturn.get(i).getName());
            Log.d("Test SortArrayId", "idList.getName : " + idList.get(i).getName());
            Log.d("Test SortArrayId", "idWebLisReturn.getVersion : " + idWebListReturn.get(i).getVersion());
            Log.d("Test SortArrayId", "idList.getVersion : " + idList.get(i).getId());
        }
    }

    //idUser == 1 pour utiliser la partie de test de la database online
    //test validate

    public void testUpdateConnectionRealCondition(final long idUser){
        final ArrayList<UpdateIdWeb> idWebList = new ArrayList<>();
        final DBAdapter dbAdapter = new DBAdapter(context);
        DatabaseReference update = database.getReference("update");

        update.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            //adaptation du Listener au besoin de la situation
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.getKey().equals("favoris")) {
                        idWebList.add(new UpdateIdWeb(snapshot.child(String.valueOf(idUser)).child("version").getValue(long.class), snapshot.getKey(), snapshot.child(String.valueOf(idUser)).child("max_id").getValue(long.class)));
                    }
                    else{
                        idWebList.add(new UpdateIdWeb(snapshot.child("version").getValue(long.class), snapshot.getKey(), snapshot.child("max_id").getValue(long.class)));
                    }
                }
                ArrayList<UpdateId> idList = new ArrayList<>();
                idList = dbAdapter.getUpdateId();
                //test de la methode updateConnection
                testUpdateConnection(idList, sortArrayId(idList, idWebList), dbAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /******************* methodes tiree de la classe initiale ******************/

    public ArrayList<UpdateIdWeb> sortArrayId(ArrayList<UpdateId> idList, ArrayList<UpdateIdWeb> idWebList){
        ArrayList<UpdateIdWeb> idWebListReturn = new ArrayList<>();
        boolean breakLoop;
        for (int i = 0 ; i < idList.size() ; i++){
            breakLoop = false;
            int j = 0;
            while (!breakLoop){
                if (idList.get(i).getName().equals(idWebList.get(j).getName())){
                    idWebListReturn.add(idWebList.get(j));
                    breakLoop = true;
                }
                j++;
            }
        }
        return idWebListReturn;
    }

    /**************************  methodes d'update globale des databases *****************************/

    //methode permettant d'obtenir la liste des id d'update de la database online et de lancer l'update

    public void globalDatabaseUpdate(final long idUser){
        final ArrayList<UpdateIdWeb> idWebList = new ArrayList<>();
        final DBAdapter dbAdapter = new DBAdapter(context);
        DatabaseReference update = database.getReference("update");

        update.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            //adaptation du Listener au besoin de la situation
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.getKey().equals("favoris")) {
                        idWebList.add(new UpdateIdWeb(snapshot.child(String.valueOf(idUser)).child("version").getValue(long.class), snapshot.getKey(), snapshot.child(String.valueOf(idUser)).child("max_id").getValue(long.class)));
                    }
                    else{
                        idWebList.add(new UpdateIdWeb(snapshot.child("version").getValue(long.class), snapshot.getKey(), snapshot.child("max_id").getValue(long.class)));
                    }
                }
                ArrayList<UpdateId> idList = new ArrayList<>();
                idList = dbAdapter.getUpdateId();
                updateConnection(idList, sortArrayId(idList, idWebList), dbAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //cette methode permet de choisir quels parties des databases sont a updater

    public void updateConnection(ArrayList<UpdateId> idList, ArrayList<UpdateIdWeb> idWebList, DBAdapter dbAdapter){
        TestDBUpdate update = new TestDBUpdate(database, dbAdapter, context);

        for (int i = 0 ; i<idList.size() ; i++){
            //update partielle de la database locale
            if (idList.get(i).getId() < idWebList.get(i).getVersion()){
                switch (idList.get(i).getTable()){
                    case "Planning" :
                        update.localUpdateDisciplines(idList.get(i), idWebList.get(i));
                        break;
                    case "favoris" :
                        update.localUpdateFavoris(UserSingleton.getINSTANCE().getUser().getId(), idList.get(i), idWebList.get(i));
                        break;
                    case "RemarquesPlanning" :
                        update.localUpdateRemarques();
                        break;
                    case "Update" :
                        update.localUpdateUpdate(1);
                        break;
                }
            }
            else {
                //update partielle de la database en ligne
                if (idList.get(i).getId() > idWebList.get(i).getVersion()) {
                    switch (idWebList.get(i).getName()) {
                        case "disciplines":
                       //     update.databaseUpdateDisciplines(idList.get(i), idWebList.get(i));
                            break;
                        case "favoris":
                         //   update.databaseUpdateFavoris(UserSingleton.getINSTANCE().getUser().getId(), idList.get(i), idWebList.get(i));
                            break;
                        case "remarques":
                           // update.databaseUpdateRemarques(idList.get(i), idWebList.get(i));
                            break;
                        case "update":
                            //update.databaseUpdateUpdate(UserSingleton.getINSTANCE().getUser().getId());
                            break;
                    }
                }
            }
        }
    }
}
