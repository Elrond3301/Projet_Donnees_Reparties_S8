import java.rmi.Remote;

/* 
 * Interface ServeurObject  
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 * Date : 21/01/2023
 * Cette interface définit les fonctions que le serveur peut appeler sur l'objet partagé
*/

public interface ServerObject_itf extends Remote {

    public void subscribe(Client_itf client);

    public void notification(Object obj);

    /*
     * Fonction Lock Read
     * Parametres : c : client qui demande le lock
     * Retour : Object : objet à renvoyer au client pour l'actualiser
     * Cette fonction permet de demander un lock read sur un objet
     */
    public Object lock_read(Client_itf c);

    /*
     * Fonction Lock Write
     * Parametres : c : client qui demande le lock
     * Retour : Object : objet à renvoyer au client pour l'actualiser
     * Cette fonction permet de demander un lock write sur un objet
     */
    public Object lock_write(Client_itf c);
}
