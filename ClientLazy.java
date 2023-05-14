import java.io.FileWriter;
import java.util.Random;

/* 
 * Client Lazy
 * Auteur : BESSON Germain
 *          CAMPAN Mathieu
 *          HAUTESSERRES Simon
 * Date : 14/05/2023
 * La classe ClientLazy permet de créer un client qui va écrire et lire dans un fichier en alternance
 * en attendant 5 secondes entre chaque écriture/lecture
*/

public class ClientLazy{

    final static int NB_ITERATIONS = 50;

    private String myName;
    private SharedObject sentence;

	public ClientLazy(String name) {
        this.myName = name;
        // initialize the system
		Client.init();
		
		// créer et diffuser un nouvel objet partagé
        this.sentence = Client.publish("IRC", new String(""),false);
    }

    /*
     * Méthode run du client
     */
	public void run() {
        try {
            for(int i = 0; i < ClientNormal.NB_ITERATIONS; i++) {
                Random rand = new Random();
                int n = rand.nextInt(1000);

                if (n < 100){ // Probabilité d'être tué sur 1000
                    FileWriter fw = new FileWriter("test.txt", true);
                    fw.write(myName+" meurt\n");
                    fw.close();
                    System.exit(0);
                }
                if (i%2==0){
                // if (false){      à décommenter pour registre régulier                    
                    FileWriter fw = new FileWriter("test.txt", true);
                    fw.write(myName+" avant écriture " + ((SharedObject) this.sentence).getVersion() + "\n");
                    fw.close();
                    this.sentence.write("ecriture : " + i);                    
                    // Attente de 5 secondes
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    fw = new FileWriter("test.txt", true);
                    fw.write(myName+" après écriture " + ((SharedObject) this.sentence).getVersion()+ "\n");
                    fw.close();


            
                } else {
                    FileWriter fw = new FileWriter("test.txt", true);
                    fw.write(myName+" avant read " + ((SharedObject) this.sentence).getVersion()+"\n");
                    fw.close();
                    this.sentence.read();

                    // Attente de 5 secondes
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    fw = new FileWriter("test.txt", true);
                    fw.write(myName+" après read " + ((SharedObject) this.sentence).getVersion() +"\n");
                    fw.close();

                }
            }
            FileWriter fw = new FileWriter("test.txt", true);
            fw.write(myName+" a fini\n");
            fw.close();
        } catch (Exception e) { 
            //e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientLazy c = new ClientLazy(args[0]);
        c.run();
        return;
    }
}

