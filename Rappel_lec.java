import java.rmi.Remote;
import java.util.*;

/* 
 * Classe Rappel_lec   
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 14/05/2023
 * La classe rappel_lecture permet de mettre à jour en écriture chaque client
*/

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

    /*
     * Fonction getObj
     * Parametres : id : identifiant de l'objet
     * Retour : l'objet souhaité
     */
    public Object getObj(int i){
        return this.lec_rep.get(i);
    }

    /*
     * Fonction getVersion
     * Parametres : i : identifiant de l'objet
     * Retour : la version de l'objet
     */
    public int getVersion(int i){
        return this.lec_rep_version.get(i);
    }

}