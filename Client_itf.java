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


}
