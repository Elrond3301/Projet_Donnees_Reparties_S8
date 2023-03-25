/* 
 * Interface Serveur  
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 25/03/2023
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
	 * Fonction addClient
	 * Parametres : client : client à ajouter
	 * Retour : Set<Client_itf> : liste des clients connectés
	 * Cette fonction permet d'ajouter un client au serveur
	 */
	public java.util.Set<Client_itf> addClient(Client_itf client) throws java.rmi.RemoteException;

	/*
	 * Fonction publish
	 * Parametres : name : nom de l'objet
	 * 				o : objet à publier
	 * 				reset : booléen qui indique si l'objet doit être réinitialisé
	 * Retour : int : identifiant de l'objet
	 * Cette fonction permet de publier un objet sur le serveur
	 */
	public int publish(String name, Object o, boolean reset) throws java.rmi.RemoteException;
	
	/*
	 * Fonction write
	 * Parametres : idObjet : identifiant de l'objet
	 * 				valeur : nouvelle valeur de l'objet
	 * Retour : int : 0 si l'écriture a réussi, -1 sinon
	 * Cette fonction permet d'écrire une nouvelle valeur dans un objet
	 */
	public int write(int idObjet, Object valeur) throws java.rmi.RemoteException;


	public int getCmptclient() throws java.rmi.RemoteException;

}
