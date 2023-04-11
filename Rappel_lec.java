import java.rmi.Remote;
import java.util.*;

public class Rappel_lec implements Remote {

    private ArrayList<Object> lec_rep;
    private ArrayList<Integer> lec_rep_version;

    public Rappel_lec() {
        this.lec_rep = new ArrayList<Object>();
        this.lec_rep_version = new ArrayList<Integer>();
    }

    /*
     * Fonction reponse
     * Permet de stocker les réponses des clients
     */
    public void reponse(Object val, int version){
        this.lec_rep.add(val);
        this.lec_rep_version.add(version);
    }   

    /*
     * Fonction length
     * Permet de récupérer le nombre de réponses
     */
    public int length(){
        return this.lec_rep.size();
    }


    public Object getObj(int i){
        return this.lec_rep.get(i);
    }

    public int getVersion(int i){
        return this.lec_rep_version.get(i);
    }

}