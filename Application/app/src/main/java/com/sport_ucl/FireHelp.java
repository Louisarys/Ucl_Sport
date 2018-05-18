package com.sport_ucl;

/* cette classe contient la presque totalite des methodes utilisee dans l'application pour
 * pour communiquer avec la base de donnee Firebase
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sport_ucl.Administrator.NotificationHelper;

import java.util.ArrayList;

public class FireHelp {
    private FirebaseDatabase database ;
    private final Context context;
    //constructeur

    public FireHelp(Context context)
    {
        this.database = FirebaseDatabase.getInstance();
        this.context = context;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    /************** methodes n'utilisant pas la db locale **************/

    //valide ou non le password pour le bouton sign-in
    //si le password est valide, lance l'activitee du menu principal

    public void validatePassword(String login, final String password){

        DatabaseReference users = database.getReference("users");

        users.orderByKey().equalTo(login).addListenerForSingleValueEvent(new ValueEventListener() {
            //adaptation du Listener au besoin de la situation
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //si data Snapshot != null c'est que le login est connu
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // on verifie que le mot de passe est correct
                        checkPassword(password,postSnapshot.child("password").getValue(String.class), context,postSnapshot.child("id").getValue(long.class));
                    }
                }
                //le login est incorrect, on previent l'utilisateur
                else{
                    Toast.makeText(context, "Votre Login est inconnu, veuillez réessayer ou vous inscrire", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //verifie le paswword obtenu ci-dessus
    //todo mettre l'entièreté des informations du user

    public void checkPassword(final String loginPassword, final String password, final Context context, final long idUser){
        if (loginPassword.equals(password)){
            DatabaseReference ref = database.getReference("user_profil");

            ref.orderByKey().equalTo(String.valueOf(idUser)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dataSnapshot = dataSnapshot.child(String.valueOf(idUser));
                    UserSingleton.getINSTANCE().setUser(new User(dataSnapshot.child("firstName").getValue(String.class), dataSnapshot.child("lastName").getValue(String.class), null, null, dataSnapshot.child("email").getValue(String.class), null, idUser, dataSnapshot.child("admin").getValue(Boolean.class)));
                    Toast.makeText(context, "Connexion réussie", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, home.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    //updatePlanningTable();
                    globalDatabaseUpdate(idUser);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else Toast.makeText(context, "Le login/mot de passe est incorrect", Toast.LENGTH_SHORT).show();
    }

    //permet de verifier l'existance d'un login dans la database et le rajoute avec un mot de passe
    //s'il n'existe pas.

    public void validateLogin(final String login, final String password){

        final DatabaseReference users = database.getReference("users");

        users.orderByKey().equalTo(login).addListenerForSingleValueEvent(new ValueEventListener() {
            //adaptation du Listener au besoin de la situation
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //si data Snapshot != null c'est que le login existe déjà
                if (dataSnapshot.hasChildren()) {
                    Toast.makeText(context, "Votre Login est déjà utilisé, veuillez en choisir un autre", Toast.LENGTH_SHORT).show();
                }
                //le login est libre on inscrit le user.
                else{
                    users.child(login).child("password").setValue(password);
                    final DatabaseReference getId = database.getReference("stock_id");

                    getId.orderByKey().equalTo("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long id = dataSnapshot.child("users").getValue(long.class);
                            users.child(login).child("id").setValue(id);
                            getId.child("users").setValue(id+1);
                            loginUserRegister(id);
                            //MainActivity.switchLayout2();
                            Toast.makeText(context, "Votre enregistrement c'est passé correctement", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, home.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(context, "Votre profil n'a pas été correctement enregistré ; veuillez réessayer", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //enregistre le user dans la bdd

    public void loginUserRegister(final long id){
        final DatabaseReference userProfil = database.getReference();

        userProfil.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserSingleton.getINSTANCE().getUser().setId(id);
                userProfil.child("user_profil").child(String.valueOf(id)).setValue(UserSingleton.getINSTANCE().getUser());
                Toast.makeText(context, "Votre enregistrement c'est passé correctement", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Login register aren't normally execute", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /********************** methodes d'ajout de donnee dans la database *********************/

    //permet d'ajouter un favori a la database
    //necessite que la methode de delete fasse un refractor pour que les keys soit ordonnee de
    // 1 ... n par increment de 1

    public void addFavoris(final long userId, final long eventId){
        final DatabaseReference dataRef = database.getReference().child("favoris").child(String.valueOf(userId)).getRef();
        dataRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long greatestFavoris = dataSnapshot.getChildrenCount();
                dataRef.child(String.valueOf(greatestFavoris+1)).setValue(eventId);
                database.getReference().child("update").child("favoris").child(String.valueOf(userId)).child("max_id").setValue(greatestFavoris+1);
                database.getReference("update").child("favoris").orderByKey().equalTo(String.valueOf(userId)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            if (dataSnapshot.child("version").exists()){
                                dataSnapshot.child("version").getRef().setValue(dataSnapshot.child("version").getValue(long.class)+1);
                            }
                            else {
                                dataSnapshot.child("version").getRef().setValue(1);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw databaseError.toException();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void addADiscipline(final DisciplineToken token){
        final DatabaseReference dataRef = database.getReference().getRef();
        dataRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long max_id = dataSnapshot.child("update").child("disciplines").child("max_id").getValue(Long.class);
                token.setId(max_id);
                database.getReference().child("update").child("disciplines").child("max_id").setValue(max_id + 1);
                database.getReference().child("update").child("disciplines").child("version").setValue(dataSnapshot.child("update").child("disciplines").child("version").getValue(Long.class) + 1);
                dataSnapshot.child("disciplines").child(Long.toString(max_id)).getRef().setValue(token);
                DBAdapter dbAdapter = new DBAdapter(context);
                dbAdapter.addADiscipline(token);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }
    /*********  methodes pour deleter des donnees dans la database online ***************/

    public void deleteFavoris(final long userId, long eventId){
        final DatabaseReference dataRef = database.getReference().child("favoris").child(String.valueOf(userId)).getRef();
        dataRef.orderByValue().equalTo(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    snapshot.getRef().removeValue();
                    Toast.makeText(context, "votre favori a bien été enlevé", Toast.LENGTH_SHORT).show();
                    doRefractorFavoris(userId, Integer.parseInt(snapshot.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void deleteEvent(final long eventId){
        final DatabaseReference dataRef = database.getReference().child("disciplines").getRef();
        dataRef.orderByKey().equalTo(Long.toString(eventId)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("debbugfire", "nombre de delete a faire : " + Long.toString(dataSnapshot.getChildrenCount()));
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    snapshot.getRef().removeValue();
                    Toast.makeText(context, "la discipline a été supprimée", Toast.LENGTH_SHORT).show();
                    doRefractorEvent(Integer.parseInt(snapshot.getKey()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    /**************************** methodes d'aide ****************************/

    //remet les nombres de favoris correctement.

    public void doRefractorFavoris(long userId, final int position){
        final DatabaseReference dataRef = database.getReference().child("favoris").child(String.valueOf(userId)).getRef();
        dataRef.orderByKey().startAt(String.valueOf(position+1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = position ; i < position+dataSnapshot.getChildrenCount() ; i++){
                    dataSnapshot.child(String.valueOf(i)).getRef().setValue(dataSnapshot.child(String.valueOf(i+1)).getValue());
                }
                Toast.makeText(context, "refractor done", Toast.LENGTH_SHORT).show();
                dataSnapshot.child(String.valueOf(position+dataSnapshot.getChildrenCount())).getRef().removeValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void doRefractorEvent(final int position){
        final DatabaseReference dataRef = database.getReference().child("disciplines").getRef();
        dataRef.orderByKey().startAt(String.valueOf(position+1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = position ; i < position+dataSnapshot.getChildrenCount() ; i++){
                    dataSnapshot.child(String.valueOf(i)).getRef().setValue(dataSnapshot.child(String.valueOf(i+1)).getValue());
                }
                Toast.makeText(context, "refractor done", Toast.LENGTH_SHORT).show();
                dataSnapshot.child(String.valueOf(position+dataSnapshot.getChildrenCount())).getRef().removeValue();
                removeAnElemUpdate("evenements");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    public void removeAnElemUpdate(final String table){
        final DatabaseReference dataRef = database.getReference().child("update").getRef();
        dataRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    if (snap.getKey().equals(table)){
                        snap.child("max_id").getRef().setValue(snap.child("max_id").getValue(Long.class)-1);
                        snap.child("version").getRef().setValue(snap.child("version").getValue(Long.class)+1);
                    }
                    if (snap.getKey().equals("update")){
                        snap.child("version").getRef().setValue(snap.child("version").getValue(Long.class)+1);
                    }
                }
                Toast.makeText(context, "update done", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    //permet de cast un long en boolean

    public boolean activeCast(long active){
        if (active == 1){
            return true;
        }
        else {
            return false;
        }
    }

    //on suppose que les bases de donnees sont faites de maniere a ce que
    // idList.size == idWebList.size et que les tables d'update ont les bonnes reference(version<>id)

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
                    Log.d("debbug GPS", "Un IdWeb generated");
                    if (snapshot.getKey().equals("favoris")) {
                        if(snapshot.child(String.valueOf(idUser)).exists()) {
                            idWebList.add(new UpdateIdWeb(snapshot.child(String.valueOf(idUser)).child("version").getValue(long.class), snapshot.getKey(), snapshot.child(String.valueOf(idUser)).child("max_id").getValue(long.class)));
                        }
                        else{
                            idWebList.add(new UpdateIdWeb(1, "favoris", 0));
                            snapshot.child(String.valueOf(idUser)).child("version").getRef().setValue(1);
                            snapshot.child(String.valueOf(idUser)).child("max_id").getRef().setValue(0);
                        }
                    }
                    else{
                        idWebList.add(new UpdateIdWeb(snapshot.child("version").getValue(long.class), snapshot.getKey(), snapshot.child("max_id").getValue(long.class)));
                    }
                }
                ArrayList<UpdateId> idList = dbAdapter.getUpdateId();
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
        DBUpdate update = new DBUpdate(database, dbAdapter, context);

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
                        //update.localUpdateUpdate();
                        break;
                    case "Gps" :
                        Log.d("debbug Gps", "Gps mise a jour");
                        update.localUpdateGps(idWebList.get(i));
                        break;
                }
            }
            else {
                   //update partielle de la database en ligne
                if (idList.get(i).getId() > idWebList.get(i).getVersion()) {
                    switch (idWebList.get(i).getName()) {
                        case "disciplines":
                            update.databaseUpdateDisciplines(idList.get(i), idWebList.get(i));
                            break;
                        case "favoris":
                            update.databaseUpdateFavoris(UserSingleton.getINSTANCE().getUser().getId(), idList.get(i), idWebList.get(i));
                            break;
                        case "remarques":
                            update.databaseUpdateRemarques(idList.get(i), idWebList.get(i));
                            break;
                        case "update":
                            //update.databaseUpdateUpdate(UserSingleton.getINSTANCE().getUser().getId());
                            break;
                            //todo activer la methode
                        case "gps" :
                            //update.databaseUpdateGps();
                            break;
                    }
                }
            }
        }
    }

    public void eventModifyAlert(){
        final DatabaseReference dataRef = database.getReference().child("disciplines").getRef();
        dataRef.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {
                getAllDisciplines();
                Toast.makeText(context, "un event a disparu", Toast.LENGTH_LONG).show();
                DisciplineToken token = new DisciplineToken(snapshot.child("sport").getValue(String.class),
                        snapshot.child("jour").getValue(String.class), snapshot.child("date").getValue(String.class),
                        snapshot.child("lieu").getValue(String.class), snapshot.child("salle").getValue(String.class),
                        snapshot.child("heureDebut").getValue(String.class), snapshot.child("active").getValue(Boolean.class),
                        snapshot.child("heureFin").getValue(String.class), snapshot.child("id").getValue(long.class),
                        snapshot.child("inscription").getValue(String.class), null, snapshot.child("sexe").getValue(String.class),
                        snapshot.child("type").getValue(String.class));
                NotificationHelper mNotificationHelper = new NotificationHelper(context);
                NotificationCompat.Builder nb = mNotificationHelper.getChannelNotification("annulation",token.getSport() +"/"+ token.getDate() +"/"+ token.getHeureDebut());
                mNotificationHelper.getManager().notify(1, nb.build());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAllDisciplines(){
        final ArrayList<DisciplineToken> tokens = new ArrayList<>();
        final DatabaseReference dataRef = database.getReference().child("disciplines").getRef();
        dataRef.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
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
                DBAdapter dbAdapter = new DBAdapter(context);
                dbAdapter.trashPlanning(tokens);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "error to connect database", Toast.LENGTH_SHORT).show();
                throw databaseError.toException();
            }
        });
    }
}
