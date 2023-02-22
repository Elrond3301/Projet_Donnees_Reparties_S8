/*
 * Interface à instancier en fonction des besoins de l'Irc : graphique, textuel
 */
public interface Observateur_itf {
    /*
     * Fonction recevant la mise à jour du compteur du sharedobject associé
     * @param : int compteur : nombre de modifications depuis la dernière lecture
     * @return : int : le compteur si besoin de propager la valeur
     */
    public int getNotification(int compteur);
}
