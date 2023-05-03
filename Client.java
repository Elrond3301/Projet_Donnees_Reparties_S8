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
 * Date : 03/05/2023
 * La classe Client permet de gérer les objets partagés
*/

public class Client extends UnicastRemoteObject implements Client_itf {

	private static Server_itf server; // L'objet serveur
	private static Client_itf me; // L'objet client
	private static HashMap<Integer, SharedObject> mapSO =  new HashMap<Integer, SharedObject>(); // Map qui associe un SharedObject à son id
	private static Set<Client_itf> tabC = new HashSet<Client_itf>(); //liste des clients_itf utile dans le addClient

	private static Rappel_lec rappel_lec; 
	private static Rappel_ecr rappel_ecr;
	
	public Client() throws RemoteException {
		super(); // On appelle le constructeur de UnicastRemoteObject
	}

///////////////////////////////////////////////////
//         Fonction statique
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
	 * Fonction statique de lecture 
	 * Parametres : id : identifiant de l'objet
	 * 					 rappel : permet au client de transmettre sa version courant de l'objet partagé
	 * Retour : l'objet partagé le plus récent ou null si erreur
	 * Fait une enquête auprès des autres clients pour récupérer la version de l'objet la plus récente
	 */
	public static SharedObject read(int id, Rappel_lec rappel){
		try {
			return Client.me.enquete(id, rappel);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Fonction statique mise à jour
	 * Parametres : obj : l'objet à mettre à jour
	 * 				idObjet : l'id de l'objet en question
	 * Retour : version : la nouvelle version de l'objet mis à jour
	 * Met à jour un objet avec la version la plus récente et actualise la version
	 */
	public static int mise_à_jour(Object obj, int idObjet){
		int version = 0;
		try {
			version = server.write(idObjet); /* On demande au serveur d'écrire dans l'objet et de nous donner le bon numéro de version */
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		System.out.println("Nouvelle version : " + version);
		SharedObject so = new SharedObject(obj, idObjet); /* Création du nouvel objet modifié */
		so.setVersion(version);
		Client.mapSO.put(idObjet, so);
		for(Client_itf c : Client.tabC){ /* On propage la mise à jour aux autres clients de façon asynchrone */
			Client_maj_Slave s = new Client_maj_Slave(c, so.obj, idObjet, so.getVersion());
			s.start();	
		}
		return version;
	}
	

/////////////////////////////////////////////////////////////
//    Fonction de l'interface
////////////////////////////////////////////////////////////


	public void majSO(int id,Object o) throws RemoteException {
		mapSO.put(id, new SharedObject(o, id));
	}

	public Object getObject(int id) throws RemoteException {
		return mapSO.get(id).obj;
	}

	public int getVersion(int id) throws RemoteException {
		return mapSO.get(id).getVersion();
	}

	public synchronized void addReponse(Object o, Client_itf c, int version){
		Client.rappel_lec.reponse(o, version);
		Client.rappel_ecr.reponse(c);
		System.out.println("Client add réponse" + Client.rappel_lec.length() + " Besoin de " + Server.NB_CLIENTS/2 + " clients");
		if (Client.rappel_lec.length() >= Server.NB_CLIENTS/2){
			System.out.println("je me réveille");
			Client.me.notify();
		}
	}

	public synchronized SharedObject enquete(int id, Rappel_lec rappel){
		Client.rappel_lec = rappel;
		Client.rappel_ecr = new Rappel_ecr();
		System.out.println("debut enquete");
		for (Client_itf c : Client.tabC){ /* Labce un slave d'enquête pour chaque client */
			System.out.println("lancement client ");
			Client_enquete_Slave s = new Client_enquete_Slave(c, id, me);
			s.start();
		}
		try {
			System.out.println("attente");
			Client.this.wait(); 	/* Attend qu'au moins la moitié des clients aient répondus */
		} catch (InterruptedException e) {
			e.printStackTrace();    /* Sinon on a un problème */
		}
		System.out.println("réveil");	

		SharedObject obj_cour = null;
		int length = Client.rappel_lec.length();
		int ver_min = 0;
		System.out.println("test taille : " + Client.rappel_lec.length());
		for (int i = 0; i < length; i++){
			System.out.println("test :" + i);
			SharedObject s = new SharedObject(Client.rappel_lec.getObj(i), id);
			s.setVersion(Client.rappel_lec.getVersion(i));
			System.out.println("comparaison : " + ver_min + ", " + s.getVersion() + " ->" + s.obj);
			int ver_test = s.getVersion();
			if (ver_test >= ver_min){  /* On vérifie que la version actuelle est la plus récente, on la modifie sinon */
				ver_min = ver_test;
				obj_cour = s;
			}
		}
		System.out.println("bonsoir : " + obj_cour);
		if (obj_cour != null){ /* Si on a un retour, c'est que l'objet est plus récent, donc on doit mettre à jour */
			Client.mapSO.put(id, obj_cour);
			Client.rappel_ecr.maj(id, obj_cour.obj,obj_cour.getVersion());
		}
		return obj_cour;
	}

	
	public void majAsynchrone(Object o, int id, int version) throws RemoteException {
		if (Client.mapSO.get(id).getVersion() < version){ /* Si on a une version plus vieille que la version actuelle */
			SharedObject so = new SharedObject(o, id);
			so.setVersion(version);		/* On met à jour la version et l'objet */
			Client.mapSO.put(id, so);
		}
	}

}

