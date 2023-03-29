import java.rmi.Remote;
import java.util.*;

public class Rappel_lec implements Remote {

    private ArrayList<SharedObject_itf> lec_rep;

    public Rappel_lec() {
        this.lec_rep = new ArrayList<SharedObject_itf>();
    }

    /*
     * Fonction reponse
     * Permet de stocker les réponses des clients
     */
    public void reponse(SharedObject val){
        this.lec_rep.add(val);
    }   

    /*
     * Fonction length
     * Permet de récupérer le nombre de réponses
     */
    public int length(){
        return this.lec_rep.size();
    }


    public SharedObject_itf get(int i){
        return this.lec_rep.get(i);
    }
}