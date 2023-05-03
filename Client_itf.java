import java.rmi.RemoteException;

/* 
 * Interface Client  
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 03/05/2023
 * Cette interface définit les fonctions que le serveur peut appeler sur le client
*/

public interface Client_itf extends java.rmi.Remote {

	/*
	 * Fonction majSo
	 * Parametres : id : identifiant du sharedobject
	 * 				o  : objet à mettre à jour
	 * Fonction qui ajoute à la map de sharedobject l'objet o avec son id 
	 */
	public void majSO(int id, Object o) throws RemoteException ;

	/*
	 * Fonction getObject
	 * Parametres : id : identifiant de l'objet
	 * Retour : l'objet demandé
	 */
	public Object getObject(int id) throws RemoteException;

	/*
	 * Fonction getVersion
	 * Parametres: id : identifiant de l'objet
	 * Retour : la version de l'objet associé à son id
	 */
	public int getVersion(int id) throws RemoteException;

	/*
	 * Fonction addReponse
	 * Parametres : o : objet 
	 * 				c : client qui ajoute la réponse
	 * 				version : version que possède le client
	 * Ajoute une réponse pour le rappel en écriture et en lecture
	 * Si plus de la moitié des réponses sont reçus, il se débloque
	 */
	public void addReponse(Object o, Client_itf c, int version) throws RemoteException ;
	
	/*
	 * Fonction enquete 
	 * Parametres : id : identifiant
	 * 				rappel : rappel en écriture
	 * Retour: le sharedobject le plus récent
	 * Demande une enquête auprès de tous les clients pour obtenir le SO le plus récent et le mettre à jour si nécessaire
	 */
	public SharedObject enquete(int id, Rappel_lec rappel) throws RemoteException;

	/*
	 * Fonction majAsynchrone
	 * Parametres : o : objet 
	 * 				id : identifiant
	 * 				version : version que possède le client
	 * Met à jour de manière asynchrone si une nouvelle version est arrivée
	 */
	public void majAsynchrone(Object o, int id, int version) throws RemoteException;

}
