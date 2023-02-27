import java.util.*;
import java.rmi.*;

/* 
 * Classe ServerObject
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 18/02/2023
 * La classe ServerObject contient un objet partagé et un id
 */

public class ServerObject implements ServerObject_itf{

    private int id; 
    private String name;
    private Verrou verrou; // true if write, false if read
    private List <Client_itf> sites; //List of clients that have a lock on the object
    private List <Client_itf> abonnes; //List of clients subscribed to the object
    public Object obj;

    public ServerObject(int id, Object obj){
        super();
        this.obj =  obj;
        this.id = id;
        this.verrou = Verrou.NL;
        this.sites = new ArrayList<Client_itf>(); //List -> for (elem : list) if elem.id == id then lock...
        this.abonnes = new ArrayList<Client_itf>(); 
    }

    @Override
    public synchronized void subscribe(Client_itf client){
        this.abonnes.add(client);
    } 

    @Override
    public synchronized void unsubscribe(Client_itf client){
        this.abonnes.remove(client);
    } 

    @Override
    public synchronized void notification(Object obj){
        this.obj = obj;
        for(Client_itf clientAbo : this.abonnes){
            if(!this.sites.contains(clientAbo)){
                this.sites.add(clientAbo);
                Slave s = new Slave(clientAbo, id, obj); // Pour éviter le blocage lors de la propagation de l'objet
                s.start();
            }
            
                
        }
        this.verrou = Verrou.RL;
    }

    @Override
    public synchronized Object lock_read(Client_itf c) {
        if (this.verrou == Verrou.WL) { //if the object is locked in write
            for (Client_itf client : this.sites) {
                try {
                    this.obj = client.reduce_lock(this.id); //reduce the lock of the unique writing client to read
                } catch (RemoteException e) {
                    e.printStackTrace();
                    }
            }
        }
        this.verrou = Verrou.RL; //lock the server object in read
        this.sites.add(c); //add the client to the list of clients that have a lock on the object
        return this.obj;
    }

    @Override
    public synchronized Object lock_write(Client_itf c) {
        
        if(this.verrou == Verrou.WL){ //if the object is already locked in write
            for (Client_itf client : this.sites) {
                try {
                    this.obj = client.invalidate_writer(this.id); //invalidate the unique writing client
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (this.verrou == Verrou.RL) { //if the object is locked in read
            for (Client_itf client : this.sites) {
                try {
                    client.invalidate_reader(this.id); //invalidate all the reading clients

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        this.verrou = Verrou.WL; //lock the server object in write
        this.sites.clear(); // On clear la liste des clients qui ont un lock sur l'objet
        this.sites.add(c); // On ajoute le nouveau client qui a le verrou
        return this.obj;
    }

    /*
     * Fonction setName
     * Permet de modifier le nom d'un objet
     * Parametres : name : nouveau nom de l'objet
     */
    public void setName(String name){
        this.name = name;
    }

    /*
     * Fonction getName
     * Permet de récupérer le nom d'un objet
     * Retour : String : nom de l'objet
     */
    public String getName(){
        return this.name;
    }
 
    private class Slave extends Thread {
        private Client_itf c;
        private int id;
        private Object obj;

        public Slave(Client_itf client, int id, Object obj){
            this.c = client;
            this.id = id;
            this.obj = obj;
        }

        public void run() { 
            try {
                this.c.getNotification(this.id, this.obj);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
