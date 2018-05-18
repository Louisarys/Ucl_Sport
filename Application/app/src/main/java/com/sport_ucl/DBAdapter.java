package com.sport_ucl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/* cette classe contient tout le code necessaire a la communication entre l'appli et la
* base de donnee locale.
* elle est divisee en plusieurs sous-sections correspondant a plusieurs types d'actions.
 */

public class DBAdapter {
    private MySQLiteOpenHelper openHelper;
    private Context context;    //pour le debugging
    private Semaphore writeDb = new Semaphore(1);

    public DBAdapter(Context context){
        openHelper = new MySQLiteOpenHelper(context);
        this.context = context;
    }

    //Todo : la methode sera lente et pourra etre optimisee par la suite

    public void databaseUpgrade(DisciplineToken discipline){
        try {
            writeDb.acquire();
            SQLiteDatabase db = openHelper.getWritableDatabase();
            databasePushPlanning(db, discipline);
            db.close();
            writeDb.release();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    /*************** methodes de push *********************/

    public void databasePushPlanning(SQLiteDatabase db, DisciplineToken disciplineToken){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", disciplineToken.getId());
        contentValues.put("SPORT", disciplineToken.getSport());
        contentValues.put("SEXE", disciplineToken.getSexe());
        contentValues.put("LIEU", disciplineToken.getLieu());
        contentValues.put("SALLE", disciplineToken.getSalle());
        contentValues.put("JOUR", disciplineToken.getJour());
        contentValues.put("DATE", disciplineToken.getDate());
        contentValues.put("HEURE_DEBUT", disciplineToken.getHeureDebut());
        contentValues.put("HEURE_FIN", disciplineToken.getHeureFin());
        contentValues.put("TYPE", disciplineToken.getType());
        contentValues.put("INSCRIPTION", disciplineToken.getInscription());
        contentValues.put("ACTIVE", disciplineToken.getActive());
        db.insert("Planning", null, contentValues);
    }

    /********************** methodes de getting ***********************/

    //permet d'obtenir les Tokens utilise dans l'activitee Planning

    public boolean newEventCondition(String jour, String lieu, String salle, String sport, String date, String sexe, String type, int heureDebut, int minuteDebut, int heureFin, int minuteFin){
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String request = "SELECT * FROM Planning WHERE  LIEU = ? and SALLE =? and JOUR =? and DATE =? and SPORT=? and SEXE=? and TYPE =? ";
        Cursor cursor = db.rawQuery(request, new String[] {lieu, salle, jour, date, sport, sexe, type});
        Log.d("debbug", "nombre d'event trouver : " + cursor.getCount());
        if (cursor.getCount() == 0){
            cursor.close();
            db.close();
            return true;
        }
        else {
            Log.d("debbug", "debut de la comparaison");
            while (cursor.moveToNext()){
                Log.d("debbug", "debut cursor : " + cursor.getString(7));
                Log.d("debbug", "fin cursor : " + cursor.getString(8));
                Log.d("debbug", "fin heure : " + heureFin);
                Log.d("debbug", "fin minute : " + minuteFin);
                if (heureIntervalChevauchement(cursor.getString(7), cursor.getString(8), heureDebut, minuteDebut, heureFin, minuteFin)){
                    cursor.close();
                    db.close();
                    return false;
                }
            }
            cursor.close();
            db.close();
            return true;
        }
    }

    public ArrayList<GpsObject> getGpsObject(){
        ArrayList<GpsObject> gpsList = new ArrayList<>();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String request = "SELECT * FROM Gps";
        Cursor cursor = db.rawQuery(request, null);
        while (cursor.moveToNext()){
            gpsList.add(new GpsObject(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(4), cursor.getDouble(3)));
        }
        cursor.close();
        db.close();
        return gpsList;
    }

    //permet d'obtenir l'entierete des disciplines listees dans la base de donnee locale.

    public void getTokenPlanning(ArrayList<DisciplineToken> tokenList){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String request = "SELECT * FROM Planning";
        Cursor cursor = db.rawQuery(request, null);
        while (cursor.moveToNext()){
            tokenList.add(new DisciplineToken(cursor.getString(1), cursor.getString(5), cursor.getString(6), cursor.getString(3), cursor.getString(4), cursor.getString(7),
                    true, cursor.getString(8), cursor.getLong(0), cursor.getString(10), null, cursor.getString(2), cursor.getString(9) ));
        }
        cursor.close();
        db.close();
    }

    //les deux methodes suivantes permettent d'obtenir la liste des evenements favoris d'un user

    public ArrayList<Long> getEventId(long userId){
        ArrayList<Long> list = new ArrayList<Long>();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String request = "SELECT id_Event FROM favoris WHERE id_User == " + userId;
        Cursor cursor = db.rawQuery(request, null);
        while (cursor.moveToNext()){
            list.add(cursor.getLong(0));
        }
        cursor.close();
        db.close();
        return list;
    }

    //recherche les evenements dont les id sont passe en parametre dans la Bdd locale

    public void getTokenFavoris(ArrayList<DisciplineToken> tokenList, ArrayList<Long> eventId){
        if (eventId.size() == 0){
            return;
        }
        else {
            SQLiteDatabase db = openHelper.getWritableDatabase();
            String request = "SELECT * FROM Planning WHERE ID IN" + listIdBuilder(eventId);
            Cursor cursor = db.rawQuery(request, null);
            while (cursor.moveToNext()) {
                tokenList.add(new DisciplineToken(cursor.getString(1), cursor.getString(5), cursor.getString(6), cursor.getString(3), cursor.getString(4), cursor.getString(7),
                        true, cursor.getString(8), cursor.getLong(0), cursor.getString(10), null, cursor.getString(2), cursor.getString(9)));
            }
            cursor.close();
            db.close();
        }
    }

    //permet d'obtenir la liste de Tokens du bouton recherche de l'activitee Planning

    public ArrayList<DisciplineToken> updateListEvent(String sport, String date, String heure){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String formatSport = formatSport(sport);

        String[] toBuild = formatHeure(heure).split(":");
        ArrayList<DisciplineToken> tokenList = new ArrayList<DisciplineToken>();
        String request = requestBuilderPlanning(formatSport, date, "");//permet de créer une requête personnalisée
        Cursor cursor = db.rawQuery(request, null);
        while (cursor.moveToNext()){
            tokenList.add(new DisciplineToken(cursor.getString(1), cursor.getString(5), cursor.getString(6), cursor.getString(3), cursor.getString(4), cursor.getString(7),
                    true, cursor.getString(8), cursor.getLong(0), cursor.getString(10), null, cursor.getString(2), cursor.getString(9) ));
        }
        cursor.close();
        db.close();
        if (toBuild.length > 1) {
            return triPerHours(tokenList, Integer.parseInt(toBuild[0]), Integer.parseInt(toBuild[1]));
        }
        else {
            return tokenList;
        }
    }

    //permet de savoir si un utilisateur a deja un evenement en favoris

    public boolean isSubscribeEvent (long notreIdUser, long notreIdEvent)
    {
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String request = "SELECT * FROM favoris WHERE id_User == " + notreIdUser + " and id_Event == " + notreIdEvent ;
        Cursor cursor = db.rawQuery(request, null );
        if(cursor.getCount() >0){
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    //permet d'obtenir les id de version des differentes tables de la database

    public ArrayList<UpdateId> getUpdateId(){
        ArrayList<UpdateId> idList = new ArrayList<>();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String request = "SELECT ID,VERSION,NAME,MAXID FROM 'Update' ";
        Cursor cursor = db.rawQuery(request, null );
        while (cursor.moveToNext()){
            idList.add(new UpdateId(cursor.getLong(1), cursor.getString(0), cursor.getString(2), cursor.getLong(3)));
        }
        cursor.close();
        db.close();
        return idList;
    }

    //permet de recuperer l'id que doit avoir la prochaine discipline inseree dans la base de donnee

    public long getMaxIdFromUpdate(String table){
        long maxId = 0;
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String request = "SELECT * FROM 'Update' WHERE NAME = ?";
        Cursor cursor = db.rawQuery(request, new String[]{table});
        while (cursor.moveToNext()){
            maxId  = cursor.getLong(3);
        }
        cursor.close();
        db.close();
        return maxId;
    }

    //permet d'obtenir la liste des Id des evenements en favoris d'un user

    public ArrayList<Long> updateGetId(long max_id){
        ArrayList<Long> returnList = new ArrayList<>();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String request = "SELECT id_Event FROM favoris WHERE id_Event > ?";
        Cursor cursor = db.rawQuery(request,new String[]{String.valueOf(max_id)});
        while (cursor.moveToNext()){
            returnList.add(cursor.getLong(0));
        }
        cursor.close();
        db.close();
        return returnList;
    }

    //permet d'obtenir la liste des versions et des noms des tables contenu dans la table
    //Update
    public ArrayList<WebUpdate> updateGetUpdate(){
        ArrayList<WebUpdate> returnList = new ArrayList<>();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String request = "SELECT ID,VERSION,MAXID FROM Update";
        Cursor cursor = db.rawQuery(request, null);
        while (cursor.moveToNext()){
            returnList.add(new WebUpdate(cursor.getString(0), cursor.getLong(1), cursor.getLong(2)));
        }
        cursor.close();
        db.close();
        return returnList;

    }

    //permet de recuperer les tokens a changer dans le planning local

    public ArrayList<DisciplineToken> updateGetPlanning(long max_id){
        ArrayList<DisciplineToken> tokenList = new ArrayList<>();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String request = "SELECT * FROM Planning WHERE ID > ?";
        Cursor cursor = db.rawQuery(request, new String[]{String.valueOf(max_id)});
        while (cursor.moveToNext()){
            tokenList.add(new DisciplineToken(cursor.getString(1), cursor.getString(5), cursor.getString(6), cursor.getString(3), cursor.getString(4), cursor.getString(7),
                    true, cursor.getString(8), cursor.getLong(0), cursor.getString(10), null, cursor.getString(2), cursor.getString(9) ));
        }
        cursor.close();
        db.close();
        return tokenList;

    }
    /********************** methodes de setting ***********************/

    //cette methode est utile quand il s'agit de remplir d'un coup la database en ligne a l'aide de
    //la database locale de l'application.
    public void disciplineFill(){
        openHelper.disciplineFill(openHelper.getWritableDatabase());
        updateInsertPlanning(updateGetPlanningbis(), new UpdateIdWeb(2, "disciplines", 100));
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String request = "SELECT ID,VERSION,NAME,MAXID FROM 'Update' ";
        Cursor cursor = db.rawQuery(request, null );
        while (cursor.moveToNext()){
            Log.d("insertLocalPlanning :", "Version :" + cursor.getLong(1));
            Log.d("insertLocalPlanning :", "Name :" + cursor.getString(0));
            Log.d("insertLocalPlanning :", "Table :" + cursor.getString(2));
            Log.d("insertLocalPlanning :", "MaxId :" + cursor.getLong(3));
        }
        cursor.close();
        db.close();
    }

    //permet d'ajouter une discipline a la Table Planning de la base de donnee locale

    public void addADiscipline(DisciplineToken token){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", token.getId());
        contentValues.put("SPORT", token.getSport());
        contentValues.put("SEXE", token.getSexe());
        contentValues.put("LIEU", token.getLieu());
        contentValues.put("SALLE", token.getSalle());
        contentValues.put("JOUR", token.getJour());
        contentValues.put("DATE", token.builDate());
        contentValues.put("HEURE_DEBUT", token.getHeureDebut());
        contentValues.put("HEURE_FIN", token.getHeureFin());
        contentValues.put("TYPE", token.getType());
        contentValues.put("INSCRIPTION", token.getInscription());
        contentValues.put("ACTIVE", token.getActive());
        db.insert("Planning", null, contentValues);
        db.close();
        incrementUpdateTable("Planning", 1);
    }

    //permet d'ajouter une discipline dans la Table Favoris de la base de donnee locale

    public void insertFavoris(long notreIdUser, long notreIdEvent){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_User",(int)notreIdUser);
        contentValues.put("id_Event",(int)notreIdEvent);
        db.insert("favoris", null, contentValues);
        db.close();
    }

    public void updateInsertFavoris(ArrayList<Long> list, long idUser, UpdateIdWeb updateIdWeb){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        for (long event : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_User", (int) idUser);
            contentValues.put("id_Event", (int) event);
            db.insert("favoris", null, contentValues);
        }
        db.delete("'Update'", "ID = ?",new String[]{"favoris"});
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", "favoris");
        contentValues.put("VERSION", updateIdWeb.getVersion());
        contentValues.put("NAME", "favoris");
        contentValues.put("MAXID", updateIdWeb.getMax_id());
        db.insert("'Update'", null, contentValues);
        db.close();
    }

    //methode utilisee lors de l'update globale de la database, qui met a jour la Table Planning

    public void updateInsertPlanning(ArrayList<DisciplineToken> tokens, UpdateIdWeb updateIdWeb){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        for (DisciplineToken token : tokens) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", token.getId());
            contentValues.put("SPORT", token.getSport());
            contentValues.put("SEXE", token.getSexe());
            contentValues.put("LIEU", token.getLieu());
            contentValues.put("SALLE", token.getSalle());
            contentValues.put("JOUR", token.getJour());
            contentValues.put("DATE", token.builDate());
            contentValues.put("HEURE_DEBUT", token.getHeureDebut());
            contentValues.put("HEURE_FIN", token.getHeureFin());
            contentValues.put("TYPE", token.getType());
            contentValues.put("INSCRIPTION", token.getInscription());
            contentValues.put("ACTIVE", token.getActive());
            db.insert("Planning", null, contentValues);
        }
        db.delete("'Update'", "ID=?",new String[]{"disciplines"});
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", "disciplines");
        contentValues.put("VERSION", updateIdWeb.getVersion());
        contentValues.put("NAME", "Planning");
        contentValues.put("MAXID", updateIdWeb.getMax_id());
        db.insert("'Update'", null, contentValues);
        db.close();
    }

    //cette methode doit etre mise a jour des qu'on etant la base de donnee

    public void updateinsertUpdate(ArrayList<UpdateIdWeb> updateIdWeb){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS 'Update';");
        db.execSQL("CREATE TABLE 'Update' (ID STRING PRIMARY KEY, VERSION INTEGER, NAME STRING, MAXID INTEGER);");
        for (UpdateIdWeb idWeb : updateIdWeb){
            ContentValues contentValues = new ContentValues();
            switch (idWeb.getName()){
                case "disciplines" :
                    contentValues.put("ID", "disciplines");
                    contentValues.put("VERSION", idWeb.getVersion());
                    contentValues.put("NAME", "Planning");
                    contentValues.put("MAXID", idWeb.getMax_id());
                    db.insert("'Update'", null, contentValues);
                    break;
                case "favoris" :
                    contentValues.put("ID", "favoris");
                    contentValues.put("VERSION", idWeb.getVersion());
                    contentValues.put("NAME", "favoris");
                    contentValues.put("MAXID", idWeb.getMax_id());
                    db.insert("'Update'", null, contentValues);
                    break;
                case "update" :
                    contentValues.put("ID", "updates");
                    contentValues.put("VERSION", idWeb.getVersion());
                    contentValues.put("NAME", "Updates");
                    contentValues.put("MAXID", idWeb.getMax_id());
                    db.insert("'Update'", null, contentValues);
                    break;
                case "remarques" :
                    contentValues.put("ID", "remarques");
                    contentValues.put("VERSION", idWeb.getVersion());
                    contentValues.put("NAME", "RemarquesPlanning");
                    contentValues.put("MAXID", idWeb.getMax_id());
                    db.insert("'Update'", null, contentValues);
                    break;
            }
        }
        db.close();
    }

    //methode utilisee lors de l'update globale de la database, qui met a jour la Table Favoris

    public void updateInsertGps(ArrayList<GpsObject> gpsList, UpdateIdWeb updateIdWeb){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        for (GpsObject gps : gpsList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("id_Gps", gps.getIdGpsLieu());
            contentValues.put("nom", gps.getNom());
            contentValues.put("lieu", gps.getLieu());
            contentValues.put("longitude", gps.getLongitude());
            contentValues.put("latitude", gps.getLatitude());
            db.insert("Gps", null, contentValues);
        }
        db.delete("'Update'", "ID = ?",new String[]{"gps"});
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", "gps");
        contentValues.put("VERSION", updateIdWeb.getVersion());
        contentValues.put("NAME", "Gps");
        contentValues.put("MAXID", updateIdWeb.getMax_id());
        db.insert("'Update'", null, contentValues);
        db.close();
    }

    //permet d'inserer une liste de disciplines dans la Table Planning

    public void insertPlanning(ArrayList<DisciplineToken> tokens){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        for (DisciplineToken token : tokens) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", token.getId());
            contentValues.put("SPORT", token.getSport());
            contentValues.put("SEXE", token.getSexe());
            contentValues.put("LIEU", token.getLieu());
            contentValues.put("SALLE", token.getSalle());
            contentValues.put("JOUR", token.getJour());
            contentValues.put("DATE", token.builDate());
            contentValues.put("HEURE_DEBUT", token.getHeureDebut());
            contentValues.put("HEURE_FIN", token.getHeureFin());
            contentValues.put("TYPE", token.getType());
            contentValues.put("INSCRIPTION", token.getInscription());
            contentValues.put("ACTIVE", token.getActive());
            db.insert("Planning", null, contentValues);
        }
        db.close();
    }

    public void trashPlanning(ArrayList<DisciplineToken> tokens){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Planning;");
        db.execSQL("CREATE TABLE Planning (ID INTEGER PRIMARY KEY, SPORT STRING, SEXE STRING, LIEU STRING, SALLE STRING, JOUR STRING, DATE STRING, HEURE_DEBUT STRING, HEURE_FIN STRING, TYPE STRING, INSCRIPTION STRING, ACTIVE BOOLEAN);");
        db.close();
        insertPlanning(tokens);
    }

    /********************** methodes de delete ***********************/

    public void deleteFavoris(long discardUserId, long discardEventId){
        openHelper.deleteFavoris(openHelper.getWritableDatabase(), discardUserId, discardEventId);
    }

    public void deleteEvent(long id){
        if (isSubscribeEvent(UserSingleton.getINSTANCE().getUser().getId(), id)){
            SQLiteDatabase db = openHelper.getWritableDatabase();
            db.delete("favoris","ID=?",new String[]{Long.toString(id)});
            db.close();
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.delete("Planning","ID=?",new String[]{Long.toString(id)});

        ArrayList<ContentValues> contentValues = new ArrayList<>();

        String request = "SELECT * FROM 'Update' WHERE NAME in (?, ?)";
        Cursor cursor = db.rawQuery(request, new String[]{"Planning", "favoris"});
        while (cursor.moveToNext()){
            ContentValues contentValues1 = new ContentValues();
            contentValues1.put("ID", cursor.getString(0));
            contentValues1.put("VERSION", cursor.getLong(1)+1);
            contentValues1.put("NAME", cursor.getString(2));
            contentValues1.put("MAXID", cursor.getLong(3));
        }
        db.delete("'Update'", "ID = ?",new String[]{"favoris"});
        db.delete("'Update'", "ID = ?",new String[]{"disciplines"});
        for (int i = 0 ; i<contentValues.size() ; i++){
            db.insert("'Update'", null, contentValues.get(i));
        }
        cursor.close();
        db.close();
    }

    public void reinitialiseDisciplines(ArrayList<DisciplineToken> tokens){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS Planning;");
        db.execSQL("CREATE TABLE Planning (ID INTEGER PRIMARY KEY, SPORT STRING, SEXE STRING, LIEU STRING, SALLE STRING, JOUR STRING, DATE STRING, HEURE_DEBUT STRING, HEURE_FIN STRING, TYPE STRING, INSCRIPTION STRING, ACTIVE BOOLEAN);");
        for (DisciplineToken token : tokens){
            ContentValues contentValues = new ContentValues();
            contentValues.put("ID", token.getId());
            contentValues.put("SPORT", token.getSport());
            contentValues.put("SEXE", token.getSexe());
            contentValues.put("LIEU", token.getLieu());
            contentValues.put("SALLE", token.getSalle());
            contentValues.put("JOUR", token.getJour());
            contentValues.put("DATE", token.builDate());
            contentValues.put("HEURE_DEBUT", token.getHeureDebut());
            contentValues.put("HEURE_FIN", token.getHeureFin());
            contentValues.put("TYPE", token.getType());
            contentValues.put("INSCRIPTION", token.getInscription());
            contentValues.put("ACTIVE", token.getActive());
            db.insert("Planning", null, contentValues);
        }
        db.close();
    }
    /**************************** methodes d'aide **********************************/

    //pour l'usage de cette fonction, nous supposons que heureDebut < heureFin
    public boolean heureIntervalChevauchement(String heureDebut, String heureFin, int heureD, int minuteD, int heureF, int minuteF){
        String[] one = heureDebut.split(":");
        String[] two = heureFin.split(":");
        //dans ce cas, risque de chauvechement avec le debut
        if (Integer.parseInt(one[0]) > heureD) {
            Log.d("debbug", "debut plus grand ou = a debut");
            if (Integer.parseInt(one[0]) > heureF) {
                Log.d("debbug", "debut plus grand que fin");
                return false;
            }
            if (Integer.parseInt(one[0]) == heureF) {
                Log.d("debbug", "debut == fin");
                if (Integer.parseInt(one[1]) < minuteF) {
                    Log.d("debbug", "debut minute plus petit que fin");
                    return true;
                } else {
                    Log.d("debbug", "debut vrai == fin");
                    return false;
                }
            } else {
                Log.d("debbug", "debut faux plus petit que fin");
                return true;
            }
        }
        if (Integer.parseInt(one[0]) == heureD){
            Log.d("debbug", "debut == debut");
            if (Integer.parseInt(one[1]) > minuteD) {
                if (Integer.parseInt(one[0]) > heureF){
                    return false;
                }
                if (Integer.parseInt(one[0]) == heureF){
                    if (Integer.parseInt(one[1]) >= minuteF){
                        return false;
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
            else {
                if (Integer.parseInt(two[0]) < heureD){
                    return true;
                }
                if (Integer.parseInt(two[0]) == heureD){
                    if (Integer.parseInt(two[1]) <= minuteD){
                       return false;
                    }
                    else {
                        return true;
                    }
                }
                else {
                    return true;
                }
            }
        }
        //risque de chevauchement avec l'heure de fin
        else {
            Log.d("debbug", "debut < debut");
            if (Integer.parseInt(two[0]) < heureD) {
                return false;
            }
            if (Integer.parseInt(two[0]) == heureD) {
                if (Integer.parseInt(two[1]) <= minuteD) {
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    public ArrayList<DisciplineToken> triPerHours(ArrayList<DisciplineToken> token, int heure, int minute){
        ArrayList<DisciplineToken> returnToken = new ArrayList<>();
        String[] toBuild;
        for (int i = 0 ; i < token.size() ; i++){
            toBuild = token.get(i).getHeureDebut().split(":");
            if (Integer.parseInt(toBuild[0]) > heure) {
             returnToken.add(token.get(i));
            }
            else{
                if (Integer.parseInt(toBuild[0]) == heure) {
                    if (Integer.parseInt(toBuild[1]) >= minute){
                        returnToken.add(token.get(i));
                    }
                }
            }
        }
        return returnToken;
    }

    //construit la requete d'updateListEvent

    public String requestBuilderPlanning(String sport, String date, String heure){
        StringBuilder build = new StringBuilder();
        if (sport.equals("") && date.equals("") && heure.equals("")){
            build.append("SELECT * FROM Planning");
            return build.toString();
        }
        else {
            build.append("SELECT * FROM Planning WHERE");
            String[] append = new String[]{sport, date, heure};
            int count = 0 ;
            for (int i = 0 ; i<3 ; i++){
                if (count > 0){
                    if (!append[i].equals("")){
                        build.append(" AND " + planningSort(i) + " = '" + append[i] + "' ");
                        count++;
                    }
                }
                else {
                    if (!append[i].equals("")){
                        build.append(" " + planningSort(i) + " = '" + append[i] + "' ");
                        count++;
                    }
                }
            }
        }
        return build.toString();
    }

    public String[] argumentBuilderPlanning(String sport, String date, String heure){
        String[] format = {sport, date, heure};
        int count = 0;
        for (int i = 0 ; i < 3 ; i++){
            if (!format[i].equals("")){
                count++;
            }
        }
        String[] returnString = new String[count];
        count = 0;
        for (int i = 0 ; i < 3 ; i++){
            if (!format[i].equals("")){
                returnString[count] = format[i];
                count++;
            }
        }
        return returnString;
    }

    public String planningSort(int i){
        if (i == 0){
            return "SPORT";
        }
        if (i == 1){
            return "DATE";
        }
        if (i == 2){
            return "HEURE_DEBUT";
        }
        return null;
    }

    public String listIdBuilder(ArrayList<Long> list){
            StringBuilder build = new StringBuilder();

            build.append("(");

            for (int i = 0; i < list.size() - 1; i++) {
                build.append(list.get(i));
                build.append(",");
            }
            build.append(list.get(list.size() - 1));
            build.append(")");
            return build.toString();
    }

    public void incrementUpdateTable(String table, int increment){
        long maxID;
        long version;
        String id;
        String name;
        SQLiteDatabase db = openHelper.getReadableDatabase();
        String request = "SELECT * FROM 'Update' WHERE  NAME = ?";
        Cursor cursor = db.rawQuery(request,new String[]{table});
        cursor.moveToNext();
        id = cursor.getString(0);
        version = cursor.getLong(1);
        name = cursor.getString(2);
        maxID = cursor.getLong(3);
        cursor.close();
        db.delete("'Update'", "NAME = ?",new String[]{table});
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", id);
        contentValues.put("VERSION", version+1);
        contentValues.put("NAME", name);
        contentValues.put("MAXID", maxID+increment);
        db.insert("'Update'", null, contentValues);
        db.close();
    }

    /********************** methodes de formatage **************************/

    //met les heures renvoyer par la device a un format pouvant etre utiliser par la bdd locale

    public String formatHeure(String heure){
        StringBuilder build = new StringBuilder();
        String [] toBuild = heure.split("h");
        if (toBuild.length>1) {
            build.append(toBuild[0]);
            build.append(":");
            build.append(formatMinutes(toBuild[1]));
        }
        else {
            build.append(heure);
        }
        return build.toString();
    }

    //rajoute un 0 devant les heures écrites de la maniere 19h9 pour obtenir 19h09

    public  String formatMinutes(String minutes){
        char[] morceaux = minutes.toCharArray();
        if (morceaux.length > 1){
            return String.valueOf(morceaux);
        }
        else{
            StringBuilder build = new StringBuilder();
            build.append("0");
            build.append(String.valueOf(morceaux));
            return build.toString();
        }
    }

    //permet de mettre les dates au même format que dans la bdd

    public String formatDate(){
        return null;
    }

    //permet de modifier le format des sports pour qu'ils soient reconnus par la bdd

    public String formatSport(String inputSport){
        String outputSport = inputSport.toUpperCase();
        /*char[] charArray = outputSport.toCharArray();
        StringBuilder build = new StringBuilder();
        for (int i = 0 ; i < charArray.length ; i++){
            if (i == 0){
                build.append(Character.toUpperCase(charArray[0]));
            }
            else {
                build.append(charArray[i]);
            }
        }*/
        return outputSport;
    }

    /********************** methodes de testing **************************/

    public void testUsers(){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String request = "SELECT LOGIN FROM Users";
        Cursor cursor = db.rawQuery(request,null);
        int count = cursor.getCount();
        Log.d("Count", ""+count);
        //cursor.moveToFirst();
        while (cursor.moveToNext()){
            Log.d("LOGIN", cursor.getString(0));
        }
        cursor.close();
        db.close();
    }

    public void testPlanning(){
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String request = "SELECT DATE FROM Planning";
        Cursor cursor = db.rawQuery(request,null);
        int count = cursor.getCount();
        Log.d("Count", ""+count);
        //cursor.moveToFirst();
        while (cursor.moveToNext()){
            Log.d("SPORT", cursor.getString(0));
        }
        cursor.close();
        db.close();
    }

    //test validate

    public void testGetUpdateId(){
        ArrayList<UpdateId> idList = new ArrayList<>();
        idList = getUpdateId();

        Log.d("debbug test", "idList.size : " + idList.size());

        for (int i = 0 ; i<idList.size() ; i++){
            Log.d("debbug test", "idList.getId : " + idList.get(i).getId());
            Log.d("debbug test", "idList.getName : " + idList.get(i).getName());
            Log.d("debbug test", "idList.getTable : " + idList.get(i).getTable());
            Log.d("debbug test", "idList.getMax_id : " + idList.get(i).getMax_id());
        }
    }

    public void testGetGps(){
        ArrayList<GpsObject> idList = new ArrayList<>();

        SQLiteDatabase db = openHelper.getReadableDatabase();
        String request = "SELECT * FROM Gps";
        Cursor cursor = db.rawQuery(request,null);
        int count = cursor.getCount();
        Log.d("Count", ""+count);
        //cursor.moveToFirst();
        while (cursor.moveToNext()){
            idList.add(new GpsObject(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4)));
        }
        cursor.close();
        db.close();
        Log.d("debbug test", "idList.size : " + idList.size());

        for (int i = 0 ; i<idList.size() ; i++){
            Log.d("debbug test", "idList.getId : " + idList.get(i).getIdGpsLieu());
            Log.d("debbug test", "idList.getNom : " + idList.get(i).getNom());
            Log.d("debbug test", "idList.getLieu : " + idList.get(i).getIdGpsLieu());
            Log.d("debbug test", "idList.getLongitude : " + idList.get(i).getLongitude());
            Log.d("debbug test", "idList.getLatitude : " + idList.get(i).getLatitude());
        }
    }

    public ArrayList<DisciplineToken> updateGetPlanningbis(){
        ArrayList<DisciplineToken> tokenList = new ArrayList<>();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        String request = "SELECT * FROM planningbis";
        Cursor cursor = db.rawQuery(request, null);
        while (cursor.moveToNext()){
            tokenList.add(new DisciplineToken(cursor.getString(1), cursor.getString(5), cursor.getString(6), cursor.getString(3), cursor.getString(4), cursor.getString(7),
                    true, cursor.getString(8), cursor.getLong(0), cursor.getString(10), null, cursor.getString(2), cursor.getString(9) ));
        }
        cursor.close();
        db.close();
        return tokenList;

    }
}
