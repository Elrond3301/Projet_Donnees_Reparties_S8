import java.io.FileWriter;
import java.util.Random;

/* 
 * Client Normal
 * Auteur : BESSON Germain
 *          CAMPAN Mathieu
 *          HAUTESSERRES Simon
 * Date : 03/05/2023
 * La classe ClientNormal permet de créer un client qui va écrire et lire dans un fichier en alternance
*/
public class ClientNormal {

    final static int NB_ITERATIONS = 50;

    private String myName;
    private SharedObject sentence;

	public ClientNormal(String name) {
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
                Random rand = new Random();
                int n = rand.nextInt(1000);

                if (n < 10){ // Probabilité d'être tué sur 1000
                    FileWriter fw = new FileWriter("test.txt", true);
                    fw.write(myName+" meurt\n");
                    fw.close();
                    System.exit(0);
                }
                else if (i%2==0){
                    FileWriter fw = new FileWriter("test.txt", true);
                    fw.write(myName+" avant écriture " +((SharedObject) this.sentence).getVersion() + "\n");
                    fw.close();
                    this.sentence.write("ecriture : " + i);
                    fw = new FileWriter("test.txt", true);
                    fw.write(myName+" après écriture " + ((SharedObject) this.sentence).getVersion()+ "\n");
                    fw.close();
            
                } else {
                    FileWriter fw = new FileWriter("test.txt", true);
                    fw.write(myName+" avant read " + ((SharedObject) this.sentence).getVersion()+"\n");
                    fw.close();
                    this.sentence.read();
                    fw = new FileWriter("test.txt", true);
                    fw.write(myName+" après read " + ((SharedObject) this.sentence).getVersion()+ "\n");
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
        ClientNormal c = new ClientNormal(args[0]);
        c.run();
        return;
    }
}

