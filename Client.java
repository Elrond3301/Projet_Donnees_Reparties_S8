import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

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
				me = new Client();
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
	public static SharedObject lookupAndSubscribe(String name) {
		SharedObject so = null;
		try {
			// On récupère l'id du SharedObject
			int id = server.lookupAndSubscribe(name, Client.me);
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
	 * Fonction statique d'enregistrement d'un SharedObject dans le serveur
	 * Parametres : name : nom du SharedObject
	 *              so : SharedObject à enregistrer
	 */
	public static void registerAndSubscribe(String name, SharedObject_itf so) {
		try {
			server.registerAndSubscribe(name, ((SharedObject) so).getId(), Client.me); // On enregistre le SharedObject dans le serveur avec un ID associé à son nom
		} catch (RemoteException e) {
			Client.lookup(name); // On regarde si le SharedObject existe déjà
		}
	}



	/*
	 * Fonction statique d'enregistrement d'un SharedObject dans le serveur
	 * Parametres : name : nom du SharedObject
	 *              so : SharedObject à enregistrer
	 */
	public static void register(String name, SharedObject_itf so) {
		try {
			server.register(name, ((SharedObject) so).getId()); // On enregistre le SharedObject dans le serveur avec un ID associé à son nom
		} catch (RemoteException e) {
			Client.lookup(name); // On regarde si le SharedObject existe déjà
		}
	}

	/*
	 * Fonction statique de création d'un SharedObject dans le serveur
	 * Parametres : o : objet à partager
	 * Retour : SharedObject : SharedObject créé
	 */
	public static SharedObject create(Object o) {
		int id = 0;
		try {
			id = server.create(o); // On demande au serveur de créer un SharedObject avec l'objet o
		} catch (RemoteException e) {
			e.printStackTrace();

		}
		SharedObject so = new SharedObject(o, id); // On crée un SharedObject avec l'objet o et l'id récupéré
		Client.mapSO.put(id, so);
		return so;
	}
	
	public static void notification(int id, Object obj){
		try {
			Client.server.notification(id, obj);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


/////////////////////////////////////////////////////////////
//    Interface to be used by the consistency protocol
////////////////////////////////////////////////////////////

	/*
	 * Fonction statique de demande d'un verrou en lecture au serveur
	 * Parametres : id : id du SharedObject
	 * Retour : Object : objet partagé
	 */
	public static Object lock_read(int id) {
		try {
			return server.lock_read(id, me); // On demande au serveur de nous donner un verrou en lecture
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}

	}

	/*
	 * Fonction statique de demande d'un verrou en écriture au serveur
	 * Parametres : id : id du SharedObject
	 * Retour : Object : objet partagé
	 */
	public static Object lock_write (int id) {
		try {
			return server.lock_write(id, me); // On demande au serveur de nous donner un verrou en écriture
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Object reduce_lock(int id) throws java.rmi.RemoteException {
		return Client.mapSO.get(id).reduce_lock();
	}

	@Override
	public void invalidate_reader(int id) throws java.rmi.RemoteException {
		Client.mapSO.get(id).invalidate_reader();
	}

	@Override
	public Object invalidate_writer(int id) throws java.rmi.RemoteException {
		return Client.mapSO.get(id).invalidate_writer();
	}

}
