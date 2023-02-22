/*
 * Interface à instancier en fonction des besoins de l'Irc : graphique, textuel
 */
public class Observateur_Irc_notif implements Observateur_itf{
    /*
     * Fonction recevant la mise à jour du compteur du sharedobject associé
     * Appelle la méthode de Irc_notif pour afficher le nombre de modifications en attente 
     * @param : int compteur : nombre de modifications depuis la dernière lecture
     * @return : int : le compteur si besoin de propager la valeur
     */
    public int getNotification(int compteur){
		Irc_notif.MajNotif(compteur);
        return compteur;
    }
}
