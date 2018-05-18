package com.sport_ucl;

// Cette classe permet le transfert de listes de DisciplineToken au travers de l'application

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

public class PlanningSingleton {

    private static PlanningSingleton INSTANCE = null;
    private ArrayList<DisciplineToken> tokenArrayList= null;
    private ArrayList<DisciplineToken> tokenFavoris = null;
    private String[] param = null;
    private DisciplineToken token;

    private PlanningSingleton(){
    }

    public static PlanningSingleton getINSTANCE(){
        if (INSTANCE == null){
            INSTANCE = new PlanningSingleton();
        }
        return(INSTANCE);
    }

    public ArrayList<DisciplineToken> getTokenList(Context context){

        if (param == null){
            tokenArrayList = new ArrayList<>();
            DBAdapter dbAdapter = new DBAdapter(context);
            dbAdapter.getTokenPlanning(tokenArrayList);
        }
        else{
            DBAdapter dbAdapter = new DBAdapter(context);
            tokenArrayList = dbAdapter.updateListEvent(param[0], param[1], param[2]);
            param = null;
        }
        return tokenArrayList;
    }

    public ArrayList<DisciplineToken> getFavorisList(Context context){
            tokenFavoris = new ArrayList<>();
            DBAdapter dbAdapter = new DBAdapter(context);
            dbAdapter.getTokenFavoris(tokenFavoris, dbAdapter.getEventId(UserSingleton.getINSTANCE().getUser().getId()));
        return tokenFavoris;
    }

    public void setParam(String[] list){
        this.param = list;
    }

    public void setToken(DisciplineToken token){
        this.token = token;
    }

    public DisciplineToken getToken() {
        return token;
    }
}
