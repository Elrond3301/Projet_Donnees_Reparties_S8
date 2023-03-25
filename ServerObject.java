import java.util.*;
import java.rmi.*;

/* 
 * Classe ServerObject
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 18/02/2023
 * La classe ServerObject contient un objet partagé et un id
 */

public class ServerObject implements ServerObject_itf{

    private int id; 
    private String name;
    private List <Client_itf> sites; //List of clients that have a lock on the object
    public Object obj;



    public ServerObject(int id, Object obj){
        super();
        this.obj =  obj;
        this.id = id;
        this.sites = new ArrayList<Client_itf>(); //List -> for (elem : list) if elem.id == id then lock...
    }



    /*
     * Fonction setName
     * Permet de modifier le nom d'un objet
     * Parametres : name : nouveau nom de l'objet
     */
    public void setName(String name){
        this.name = name;
    }

    /*
     * Fonction getName
     * Permet de récupérer le nom d'un objet
     * Retour : String : nom de l'objet
     */
    public String getName(){
        return this.name;
    }
 
}
