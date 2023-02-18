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
 * Date : 18/02/2023
 * La classe Server permet de gérer les objets partagés
 */

public class Server extends UnicastRemoteObject implements Server_itf {

    private HashMap<Integer,ServerObject> mapSO; // Map qui associe un ServerObject à son id
    private HashMap<String, Integer> mapName; // Map qui associe un nom à un id

    public Server() throws RemoteException {
		super();
        this.mapSO = new HashMap<Integer, ServerObject>();
        this.mapName = new HashMap<String, Integer>();
	}

    @Override 
    public int lookup(String name) throws RemoteException {
        Integer res = this.mapName.get(name);
        if (res == null){
            return -1;
        }
        return res;
    }

    @Override
    public int lookupAndSubscribe(String name, Client_itf client) throws RemoteException{
        int id = lookup(name);
        if (id != -1){
            mapSO.get(mapName.get(name)).subscribe(client);
        }
        return id;
    }

    @Override
    public synchronized void register(String name, int id) throws RemoteException {
        ServerObject so = mapSO.get(id); 
        so.setName(name);
        mapSO.put(id, so);
        mapName.put(name, id);
    }

    @Override
    public void registerAndSubscribe(String name, int id, Client_itf client) throws RemoteException{
        register(name,id);
        mapSO.get(id).subscribe(client);
    }  

    @Override
    public int create(Object o) throws RemoteException {
        int id = 1;
        while (mapSO.containsKey(id)){
            id++;
        }
        ServerObject so = new ServerObject(id, o);
        mapSO.put(id, so);
        return id;
    }

    @Override
    public void notification(int id, Object obj) throws java.rmi.RemoteException {
        mapSO.get(id).notification(obj);
    }


    @Override // return the object associated to the id after calling the method lock_read
    public Object lock_read(int id, Client_itf client) throws RemoteException {
        return mapSO.get(id).lock_read(client);
    }

    @Override // return the object associated to the id after calling the method lock_write
    public Object lock_write(int id, Client_itf client) throws RemoteException {
        return mapSO.get(id).lock_write(client);
    }

    // return the name of the ServerObject associated to the id
    public String getName(int id) throws RemoteException {
        return mapSO.get(id).getName();
    }

    public static void main(String [] args){
        System.out.println("Hello world !");
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