import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.management.RuntimeErrorException;

/* 
 * Classe Client   
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 18/02/2023
 * La classe Client permet de gérer les objets partagés
*/

public class Client extends UnicastRemoteObject implements Client_itf {

	private static Server_itf server; // L'objet serveur
	private static Client_itf me; // L'objet client
	private static HashMap<Integer, SharedObject> mapSO =  new HashMap<Integer, SharedObject>(); // Map qui associe un SharedObject à son id
	
	private  static Set<Client_itf> tabC = new HashSet<Client_itf>(); //liste des clients_itf utile dans le addClient
	
	public Client() throws RemoteException {
		super(); // On appelle le constructeur de UnicastRemoteObject
	}

///////////////////////////////////////////////////
//         Interface to be used by applications
///////////////////////////////////////////////////

	/*
	 * Fonction statique d'initialisation du client
	 */
	public static void init() {
		try {
			try {
				// On récupère l'objet serveur
				String URL = null;
				try {
					URL = "//" + InetAddress.getLocalHost().getHostName() + "/Server";
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				// On récupère l'objet serveur
				server = (Server_itf) Naming.lookup(URL); 
				// Create a new client
				if (server.getCmptclient() == Server.NB_CLIENTS){
					throw new RuntimeErrorException(null);
				}
				me = new Client();
				tabC =  server.addClient(me); // ajout du client
				while ( server.getCmptclient() != Server.NB_CLIENTS){/* Attente*/}
				
			} catch (AccessException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	
		
	}
	/*
	 * Fonction statique de recherche d'un SharedObject dans le serveur
	 * Parametres : name : nom du SharedObject
	 * Retour : SharedObject : SharedObject trouvé, null sinon
	 */
	public static SharedObject lookup(String name) {
		SharedObject so = null;
		try {
			// On récupère l'id du SharedObject
			int id = server.lookup(name);
			if (id != -1){ // Si l'id est différent de -1, on a trouvé le SharedObject
				so = new SharedObject(id); // On crée un SharedObject avec l'id récupéré
				Client.mapSO.put(id, so); // On ajoute le SharedObject à la map
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return so;
	}		

	/*
	 * Fonction statique d'enregistrement et du publication d'un SharedObject dans le serveur
	 * Parametres : name : nom du SharedObject
	 *              o : SharedObject à enregistrer
	 * 			    reset : booléen qui indique si on doit réinitialiser le SharedObject
	 */
	public static SharedObject publish(String name, Object o, boolean reset) {
		SharedObject so = null;
		try {
			int id = server.publish(name, o, reset); // On enregistre le SharedObject dans le serveur avec un ID associé à son nom
			
			so = new SharedObject(o,id); // On crée un SharedObject avec l'id récupéré
			mapSO.put(id, so);
		} catch (RemoteException e) {
			 // On regarde si le SharedObject existe déjà
			 
		}
		return so;
	}
	
	/*
	 * Fonction de mise à jour d'un SharedObject dans la map
	 * Parametres : id : id du SharedObject
	 * 			o : SharedObject à mettre à jour
	 */
	 
	public void majSO(int id,Object o) throws RemoteException {
		mapSO.put(id, new SharedObject(o, id));
	}

	/*
	 * Fonction de récupération d'un SharedObject dans la map
	 * Parametres : id : id du SharedObject
	 * Retour : SharedObject : SharedObject trouvé, null sinon
	 */
	public SharedObject getSharedObject(int id) throws RemoteException {
		return mapSO.get(id);
	}

	

	

	

/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	public static SharedObject enquete(int id, Rappel_lec rappel){

		for (Client_itf c : Client.tabC){
			Client_enquete_Slave s = new Client_enquete_Slave(c, id, rappel);
			s.start();
		}
		while (rappel.length() < Server.NB_CLIENTS/2){}

		SharedObject obj_cour = null;
		int length = rappel.length();
		int ver_min = 0;
		for (int i = 0; i < length; i++){
			int ver_test = ((SharedObject) rappel.get(i)).getVersion();
			if ( ver_test > ver_min){
				ver_min = ver_test;
				obj_cour = (SharedObject) rappel.get(i);
			}
		}
		if (obj_cour != null){
			Client.mapSO.put(id, obj_cour);
		}
		return obj_cour;
	}

	


}

