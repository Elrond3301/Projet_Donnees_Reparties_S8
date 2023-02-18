import java.rmi.Remote;

/* 
 * Interface ServeurObject  
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 18/02/2023
 * Cette interface définit les fonctions que le serveur peut appeler sur l'objet partagé
*/

public interface ServerObject_itf extends Remote {

    /*
     * Fonction Subscribe
     * Parametres : client : client qui souhaite s'abonner
     * Cette fonction permet de s'abonner à un objet partagé
     */
    public void subscribe(Client_itf client);

    /*
     * Fonction notification
     * Parametres : obj : objet à notifier
     * Cette fonction permet de notifier les clients abonnés à un objet
     */

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
