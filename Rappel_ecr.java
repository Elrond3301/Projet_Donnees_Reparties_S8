import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public class Rappel_ecr implements Remote {    
    
    private ArrayList<Client_itf> tabc;

    public Rappel_ecr() {
        this.tabc = new ArrayList<Client_itf>();
    }

    /*
     * Fonction reponse
     * Permet de stocker les réponses des clients
     */
    public void reponse(Client_itf c){
        this.tabc.add(c);
    }   

    /*
     * Fonction length
     * Permet de récupérer le nombre de réponses
     * A enlever si inutile
     */
    public int length(){
        return this.tabc.size();
    }

    public void maj(int id, Object o, int version){
        for (Client_itf c : this.tabc){
            try {
                if (c.getVersion(id) < version){
                    Client_maj_atomique_Slave sc = new Client_maj_atomique_Slave(c, o, id, version);
                    sc.start();
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /* A enlever si inutile */
    public Object getClient(int i){
        return this.tabc.get(i);
    }

}
