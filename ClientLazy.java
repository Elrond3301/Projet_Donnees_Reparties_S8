import java.io.FileWriter;
import java.io.IOException;

/* 
 * Client Lazy
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 18/02/2023
 * La classe ClientLazy permet de créer un client qui va écrire et lire dans un fichier en alternance
 * en attendant 5 secondes entre chaque écriture/lecture
*/

public class ClientLazy{
    
    final static int NB_ITERATIONS = 5;

    private String myName;
    private SharedObject sentence;
    private boolean abonne = false;

	public ClientLazy(String name) {
        this.myName = name;
        // initialize the system
		Client.init();
		
		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("IRC");
		if (s == null) {
            System.out.println("Création de l'objet partagé");
			s = Client.create(new Sentence());
			Client.register("IRC", s);
		}
        this.sentence = s;
    }
    /*
     * Méthode run du client
     */
	public void run() {
        try {
            for(int i = 0; i < ClientLazy.NB_ITERATIONS; i++) {
                FileWriter fw = new FileWriter("test.txt", true);
                                
                // Abonnement et désabonnement sporadique
                if (i%5 == 0){
                    if (this.abonne){
                        Client.unsubscribe(this.sentence.getId());
                    } else {
                        Client.subscribe(this.sentence.getId(), new Observateur_ClientNormal_Lazy(this.myName));
                    }
                }


                if (i%2==0){
                    
                    // lock the object in write mode
                    System.out.println("Client " + this.myName + " veut écrire");
                    fw.write(myName+" avant Lockwrite " + this.sentence.getLock()+"\n");
                    this.sentence.lock_write();
                    fw.write(myName+" après Lockwrite " + this.sentence.getLock()+"\n");

                    // Attente de 5 secondes
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
            
                    // unlock the object
                    this.sentence.unlock();
                    fw.write(myName+" après unlock " + this.sentence.getLock()+"\n");

                } else {
                    // lock the object in read mode
                    System.out.println("Client " + this.myName + " veut lire");
                    fw.write(myName+" avant Lockread " + this.sentence.getLock()+"\n");
                    this.sentence.lock_read();
                    fw.write(myName+" après Lockread " + this.sentence.getLock()+"\n");

                    // Attente de 5 secondes
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    // unlock the object
                    this.sentence.unlock();
                    fw.write(myName+" après unlock " + this.sentence.getLock()+"\n");
                }
                fw.close();
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
	}


    public static void main(String[] args) {
        ClientLazy c = new ClientLazy(args[0]);
        c.run();
        return;
    }
}

