/* 
 * Interface Serveur  
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 18/02/2023
 * Cette interface définit les fonctions que le client peut appeler sur le serveur
*/

public interface Server_itf extends java.rmi.Remote {
	/*
	 * Fonction Lookup
	 * Parametres : name : nom de l'objet
	 * Retour : int : identifiant de l'objet
	 * Cette fonction permet de rechercher un objet dans le serveur
	 */
	public int lookup(String name) throws java.rmi.RemoteException;

	/*
	 * Fonction LookupAndSubscribe
	 * Parametres : name : nom de l'objet
	 * Retour : int : identifiant de l'objet
	 * Cette fonction permet de rechercher un objet dans le serveur et de s'abonner à celui-ci
	 */
	public int lookupAndSubscribe(String name, Client_itf client) throws java.rmi.RemoteException;


	/*
	 * Fonction Register
	 * Parametres : name : nom de l'objet
	 *              id : identifiant de l'objet
	 * Cette fonction permet d'enregistrer un objet dans la map du serveur
	 */
	public void register(String name, int id) throws java.rmi.RemoteException;


	/*
	 * Fonction Register
	 * Parametres : name : nom de l'objet
	 *              id : identifiant de l'objet
	 * 			    client : client qui souhaite s'abonner
	 * Cette fonction permet d'enregistrer un objet dans la map du serveur et de s'abonner à celui-ci
	 */
	public void registerAndSubscribe(String name, int id, Client_itf client) throws java.rmi.RemoteException;


	/*
	 * Fonction Create
	 * Parametres : o : objet à créer
	 * Retour : int : identifiant de l'objet
	 * Cette fonction permet de créer un ServerObject et de l'enregistrer dans la map du serveur
	 */
	public int create(Object o) throws java.rmi.RemoteException;

	/*
	 * Fonction Notification
	 * Parametres : id : identifiant de l'objet
	 * 			    obj : objet à notifier
	 * Cette fonction permet de notifier les clients abonnés à un objet
	 */
	public void notification(int id, Object obj) throws java.rmi.RemoteException;


	/*
	 * Fonction Lock Read
	 * Parametres : id : identifiant de l'objet
	 *              client : client qui demande le lock
	 * Retour : Object : objet à renvoyer au client pour l'actualiser
	 * Cette fonction permet de demander un lock read sur un objet
	 */
	public Object lock_read(int id, Client_itf client) throws java.rmi.RemoteException;

	/*
	 * Fonction Lock Write
	 * Parametres : id : identifiant de l'objet
	 *              client : client qui demande le lock
	 * Retour : Object : objet à renvoyer au client pour l'actualiser
	 * Cette fonction permet de demander un lock write sur un objet
	 */
	public Object lock_write(int id, Client_itf client) throws java.rmi.RemoteException;



}
