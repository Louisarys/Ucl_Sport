package com.sport_ucl;

/* Cette classe a pour but de permettre de stocker dans un objet l'ensemble des
 * relatives a un evenement
 */

import android.text.method.DateTimeKeyListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.SimpleTimeZone;

public class DisciplineToken {
    private long id;
    private String sport;
    private String sexe;
    private String lieu;
    private String salle;
    private String jour;
    private String date;
    private String heureDebut;
    private String heureFin;
    private String type;
    private String inscription;
    private ArrayList<DisciplineRemarque> remarques;
    private Boolean active;


    public DisciplineToken(String sport,  String jour, String date, String lieu, String salle, String heureDebut,  Boolean active, String heureFin, long id, String inscription, ArrayList<DisciplineRemarque> remarques, String sexe, String type) {
        this.id = id;
        this.sport = sport;
        this.sexe = sexe;
        this.lieu = lieu;
        this.salle = salle;
        this.jour = jour;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.type = type;
        this.inscription = inscription;
        this.remarques = remarques;
        this.active = active;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getSport() {return sport;}

    public void setSport(String sport) {this.sport = sport;}

    public String getSexe() {return sexe;}

    public void setSexe(String sexe) {this.sexe = sexe;}

    public String getLieu() {return lieu;}

    public void setLieu(String lieu) {this.lieu = lieu;}

    public String getSalle() {return salle;}

    public void setSalle(String salle) {this.salle = salle;}

    public String getJour() {return jour;}

    public void setJour(String jour) {this.jour = jour;}

    public String getDate() {return date;}

    public void setDate(String date) {this.date = date;}

    public String getHeureDebut() {return heureDebut;}

    public void setHeureDebut(String heureDebut) {this.heureDebut = heureDebut;}

    public String getHeureFin() {return heureFin;}

    public void setHeureFin(String heureFin) {this.heureFin = heureFin;}

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

    public String getInscription() {return inscription;}

    public void setInscription(String inscription) {this.inscription = inscription;}

    public ArrayList<DisciplineRemarque> getRemarques() {return remarques;}

    public void setRemarques(ArrayList<DisciplineRemarque> remarques) {this.remarques = remarques;}

    public Boolean getActive() {return active;}

    public void setActive(Boolean active) {this.active = active;}

    /************** methodes de test **************************/

    public void printContenu(){
        Log.d("le token est : ", "sport : " + sport + ", jour : " + jour + ", date : " + date + ", lieu : " + lieu + ", salle : " + salle + ", heuredébut : " + heureDebut + ", id : " + id + ", active : " + active + ", fin : " + heureFin + ", inscription : " + inscription + ", remarques : " + buildRemarques() + ", sexe : " + sexe + ", type : " + type);
    }

    public String buildRemarques(){
        StringBuilder build = new StringBuilder();
        for (int i = 0 ; i<remarques.size() ; i++){
            build.append(remarques.get(i).getRemarque());
            build.append("-");
        }
        return build.toString();
    }

    public String builDate(){
        if (date.split("-").length >1){
            return date;
        }
        else {
            String[] morceaux = date.split("/");
            if (morceaux.length > 1) {
                StringBuilder builder = new StringBuilder();
                if (morceaux[0].toCharArray().length>1){
                    builder.append(morceaux[0]);
                }
                else {
                    builder.append("0"+morceaux[0]);
                }
                builder.append("-");
                builder.append(morceaux[1]);
                builder.append("-");
                if (morceaux[2].toCharArray().length >2) {
                    builder.append(morceaux[2]);
                }
                else {
                    builder.append("20" + morceaux[2]);
                }
                return builder.toString();
            }
            return "";
        }
    }
}
