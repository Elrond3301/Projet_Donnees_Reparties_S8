

/* 
 * Classe ServerObject
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 14/05/2023
 * La classe ServerObject contient un objet partagé et un id
 */

public class ServerObject implements ServerObject_itf{

    private String name;
    public Object obj;
    private int version;



    public ServerObject(int id, Object obj, int version){
        super();
        this.obj =  obj;
        this.version = version;
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

    /*
     * Fonction getVersion
     * Permet de récupérer la version d'un objet
     * Retour : int : version de l'objet
     */
     
    public int getVersion(){
        return this.version;
    }

    /*
     * Fonction setVersion
     * Permet de modifier la version d'un objet
     * Parametres : version : nouvelle version de l'objet
     */
    public void setVersion(int version){
        this.version = version;
    }

    /*
     * Fonction newVersion
     * Retour : la version de l'objet avant actualisation
     * Actualise la version de l'objet après avoir retourné la version qu'il avait
     */
    public int newVersion(){
        return ++this.version;
    }
 
}
