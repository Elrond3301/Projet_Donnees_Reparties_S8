import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.*;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.*;
import java.util.*;

/* 
 * Classe Server   
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 14/05/2023
 * La classe Server permet de gérer les objets partagés
 */

public class Server extends UnicastRemoteObject implements Server_itf {

    public static int NB_CLIENTS = 10;
    private Set<Client_itf> tabC = new HashSet<Client_itf>(); 
    private int cmptclient = 0;

    private HashMap<Integer,ServerObject> mapSO; // Map qui associe un ServerObject à son id
    private HashMap<String, Integer> mapName; // Map qui associe un nom à un id

    public Server() throws RemoteException {
		super();
        this.mapSO = new HashMap<Integer, ServerObject>();
        this.mapName = new HashMap<String, Integer>();
	}

    @Override
    public void addClient(Client_itf client) throws RemoteException {
        // faire un slave qui ajoute et attend jusqu'à this.cmptclient == NB_CLIENT
        if (this.cmptclient < Server.NB_CLIENTS){
            this.tabC.add(client);
            this.cmptclient++;
        } 
    }

    public Set<Client_itf> getClients() throws RemoteException {
        return this.tabC;
    }

    @Override
    public int publish(String name, Object o, boolean reset) throws RemoteException {
        int id;
        if (mapName.containsKey(name)){
            if (reset){
                id = mapName.get(name);
                ServerObject so = new ServerObject(id, o,0);
                mapSO.put(id, so);
                // Propagation
                for (Client_itf client : this.tabC){
                    client.majSO(id,o);
                }
            } else {
                id = mapName.get(name);
            }
        } else {
            id = mapSO.size();
            ServerObject so = new ServerObject(id,o,0);
            mapSO.put(id,so);
            mapName.put(name, id);
            // Propagation
            for (Client_itf client : this.tabC){
                    client.majSO(id,o);
            }
        }
        return id;
    }

    @Override
    public synchronized int write(int idObjet, Object obj) throws RemoteException { /* synchronized pour Atomique */
        ServerObject so =  this.mapSO.get(idObjet);
        int version = so.newVersion();
        so.obj = obj;
        this.mapSO.put(idObjet, so);
		for(Client_itf c : this.tabC){ /* On propage la mise à jour aux autres clients de façon asynchrone */
			Client_maj_Slave s = new Client_maj_Slave(c, so.obj, idObjet, version);
			s.start();	
		}		
        return version;
    }

    // return the name of the ServerObject associated to the id
    public String getName(int id) throws RemoteException {
        return mapSO.get(id).getName();
    }
    
    public int getCmptclient() throws RemoteException{
        return this.cmptclient;
    }

    public static void main(String [] args){
        System.out.println("== Server launch ==");

        if (args != null && args.length == 1){              /* On peut changer le nombre de clients en paramètres */
            Server.NB_CLIENTS = Integer.parseInt(args[0]);
            System.out.println("Nombre de clients : " + Server.NB_CLIENTS);
        } else if (args != null && args.length > 1){
            System.out.println("Usage : java Server [nb_clients]");
            System.exit(1);
        }

        // initialize the system
		Server s = null;
        try {
            s = new Server();
            
            // look up the server
		    // if not found, create it
            LocateRegistry.createRegistry(1099);
            String URL = null;
            try {
                URL = "//" + InetAddress.getLocalHost().getHostName() + "/Server";
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            Naming.rebind(URL, s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }	
    }
    




}