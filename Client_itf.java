import java.rmi.RemoteException;

/* 
 * Interface Client  
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 18/02/2023
 * Cette interface définit les fonctions que le serveur peut appeler sur le client
*/

public interface Client_itf extends java.rmi.Remote {

	// mis à jour 
	public void majSO( int id, Object o) throws RemoteException ;

	// Enquête du client pour trouver une version récente de l'objet
	//public void enquete(int id, Rappel_lec rappel) throws RemoteException;

	//public void mise_à_jour(idObjet : entier, version : entier, valeur : Object, cbr : Rappel_ecr);

	public SharedObject getSharedObject(int id) throws RemoteException;

}
