import java.io.FileWriter;
import java.io.IOException;

/*
 * Interface à instancier en fonction des besoins de l'Irc : graphique, textuel
 */
public class Observateur_ClientNormal_Lazy implements Observateur_itf{

    private String name;

    public Observateur_ClientNormal_Lazy(String name){
        this.name = name;
    }

    /*
     * Fonction recevant la mise à jour du compteur du sharedobject associé
     * Appelle la méthode de Irc_notif pour afficher le nombre de modifications en attente 
     * @param : int compteur : nombre de modifications depuis la dernière lecture
     * @return : int : le compteur si besoin de propager la valeur
     */


    public int getNotification(int compteur){
		System.out.println(this.name + " Notification reçue");
        try {
            FileWriter fwabonne = new FileWriter("test_abonne.txt", true);
            fwabonne.write(this.name+" a recue notification numéro " + compteur + "\n");
            fwabonne.close();                    
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
                
        return compteur;
    }

    
}
