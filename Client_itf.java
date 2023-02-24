/* 
 * Interface Client  
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 18/02/2023
 * Cette interface définit les fonctions que le serveur peut appeler sur le client
*/

public interface Client_itf extends java.rmi.Remote {

	/* 
	 * Fonction Reduce Lock
	 * Parametres : id : identifiant de l'objet
	 * Retour : Object : objet à renvoyer au client pour l'actualiser
	 * Cette fonction est appelee par le serveur pour changer le verrou du client lors d'un lock read
	 */
	public Object reduce_lock(int id) throws java.rmi.RemoteException;

	/* 
	 * Fonction Invalidate Reader
	 * Parametres : id : identifiant de l'objet
	 * Cette fonction est appelee par le serveur pour invalider un/des client(s) en lecture lors d'un lock write
	 */
	public void invalidate_reader(int id) throws java.rmi.RemoteException;

	/* 
	 * Fonction Invalidate Writer
	 * Parametres : id : identifiant de l'objet
	 * Retour : Object : objet à renvoyer au client pour l'actualiser
	 * Cette fonction est appelee par le serveur pour invalider un client en écriture lors d'un lock write
	 */
	public Object invalidate_writer(int id) throws java.rmi.RemoteException;


	/*
	 * Fonction getNotification
	 * Parametres : id : identifiant de l'objet
	 * 				obj : objet 
	 * Cette fonction est  appelee par le serveur pour notifier un client abonné à l'objet qu'un message a èté envoyé, mise à jour...
	 */
    public void getNotification(int id, Object obj) throws java.rmi.RemoteException;
}
