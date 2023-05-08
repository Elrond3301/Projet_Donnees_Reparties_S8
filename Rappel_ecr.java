import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/* 
 * Classe Rappel_ecr   
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 03/05/2023
 * La classe rappel_ecriture permet de mettre à jour en écriture chaque client
*/

public class Rappel_ecr implements Remote {    
    
    private ArrayList<Client_itf> tabc;

    public Rappel_ecr() {
        this.tabc = new ArrayList<Client_itf>();
    }

    /*
     * Fonction reponse
     * Permet de stocker les réponses des clients
     */
    public void reponse(Client_itf c){
        this.tabc.add(c);
    }   

    /*
     * Fonction length
     * Permet de récupérer le nombre de réponses
     * A enlever si inutile
     */
    public int length(){
        return this.tabc.size();
    }

    /*
     * Fonction maj
     * Parametres : id : identifiant de l'objet
     *              o : objet 
     *              version : de l'objet
     * Actualise l'objet atomiquement en écriture pour chaque client si nécessaire selon la version qu'ils ont
     */
    public void maj(int id, Object o, int version){
        for (Client_itf c : this.tabc){
            try {
                if (c.getVersion(id) < version){
                    Client_maj_atomique_Slave sc = new Client_maj_atomique_Slave(c, o, id, version);
                    sc.start();
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}
