package com.sport_ucl;

/*cette classe est encore inutilisee dans notre application mais a ete mise ici
 * en prevision de la future implementation des remarques
 */


public class DisciplineRemarque {
    private long disciplineId;
    private String remarque;
    private long numberRemarque;

    public DisciplineRemarque(long disciplineId, String remarque, long numberRemarque){
        this.disciplineId = disciplineId;
        this.remarque = remarque;
        this.numberRemarque = numberRemarque;
    }

    public long getDisciplineId() {
        return disciplineId;
    }

    public void setDisciplineId(long disciplineId) {
        this.disciplineId = disciplineId;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public long getNumberRemarque() {
        return numberRemarque;
    }

    public void setNumberRemarque(long numberRemarque) {
        this.numberRemarque = numberRemarque;
    }

    /********************************* methodes de test *********************************/

}
