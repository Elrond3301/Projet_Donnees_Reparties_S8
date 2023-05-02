import java.io.FileWriter;
import java.io.IOException;

/* 
 * Client Lazy
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 * Date : 21/01/2023
 * La classe ClientLazy permet de créer un client qui va écrire et lire dans un fichier en alternance
 * en attendant 5 secondes entre chaque écriture/lecture
*/

public class ClientLazy{

    final static int NB_ITERATIONS = 5;

    private String myName;
    private SharedObject sentence;

	public ClientLazy(String name) {
        this.myName = name;
        // initialize the system
		Client.init();
		
		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		
		// créer et diffuser un nouvel objet partagé
        this.sentence = Client.publish("IRC", new String(""),false);
    }

    /*
     * Méthode run du client
     */
	public void run() {
        try {
            for(int i = 0; i < ClientNormal.NB_ITERATIONS; i++) {
                FileWriter fw = new FileWriter("test.txt", true);
                if (i%2==0){

                    System.out.println("Client " + this.myName + " veut écrire");
                    fw.write(myName+" avant écriture iteration " + i + " " + ((SharedObject) this.sentence).getVersion() + "\n");
                    this.sentence.write("ecriture : " + i);
                    fw.write(myName+" après écriture " + ((SharedObject) this.sentence).getVersion()+"\n");
                    // Attente de 5 secondes
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }


            
                } else {
                    System.out.println("Client " + this.myName + " veut lire");
                    fw.write(myName+" avant read iteration " + i + " " + ((SharedObject) this.sentence).getVersion()+"\n");
                    fw.write(myName+" après read " + ((SharedObject) this.sentence).getVersion()+"\n");

                    // Attente de 5 secondes
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

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

