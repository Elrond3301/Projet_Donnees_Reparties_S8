import java.util.concurrent.*;

public class Lancement_Test  {

    
	
	public static void main(String argv[]) {
		
		if (argv.length != 2) {
			System.out.println("java Lancement_Test <NbClientsNormaux> <NbClientsLazy>");
			return;
		}

        // Initialisation des variables
        int nbClientsNormaux = Integer.parseInt(argv[0]);
        int nbClientsLazy = Integer.parseInt(argv[1]);

        ClientNormal [] clientsNormaux = new ClientNormal[nbClientsNormaux];
        ClientLazy [] clientsLazy = new ClientLazy[nbClientsLazy];
        

        // Lancement des clients lazy
        for (int i = 0; i < nbClientsLazy; i++) {
            clientsLazy[i] = new ClientLazy("ClientLazy" + i);
        }

        // Lancement des clients normaux
        for (int i = 0; i < nbClientsNormaux; i++) {
            clientsNormaux[i] = new ClientNormal("ClientNormal" + i);
        }

        
        // Run des clients normaux
    
        
        return;
        
	}

	
}




