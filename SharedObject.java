/* 
 * Classe SharedObject   
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON G/ermain
 * Date : 03/05/2023
 * La classe SharedObject contient un objet partagé et un id
 */

public class SharedObject implements SharedObject_itf {

    public Object obj; 
	private int id;
	private int version;

	// Constructor for SharedObject with an object and an id
	public SharedObject(Object obj, int id) {
		super();
		this.obj = obj;
		this.id = id;		
	}

	// Constructor for SharedObject with an id
	public SharedObject(int id){
		super();
		this.id = id;
	}


	/*
	 * Fonction Get Id
	 * Cette fonction permet de récupérer l'id d'un objet
	 * Retourne l'id de l'objet
	 */
	public int getId(){
		return this.id;
	}

	/*	
	 * Fonction Get Version
	 * Cette fonction permet de récupérer la version d'un objet
	 * Retourne le numero de version  de l'objet
	 */
	public int getVersion(){
		return this.version;
		
	}

	/*
	 * Fonction setVersion
	 * Parametres : version de l'objet
	 * Modifie la version de l'objet
	 */
	public void setVersion(int version){
		this.version = version;
	}

	/*
	 * Fonction read
	 * Cette fonction permet de lire l'objet
	 * Retourne l'objet
	 */
	public Object read(){
		SharedObject s = Client.read(this.id);
		if (s.obj != null){
			this.version = s.getVersion();
			this.obj = s.obj;
			return s.obj;
		} else {
			return this.obj;
		} 
	}

	/*
	 * Fonction write
	 * Parametres : o : objet à écrire
	 * Cette fonction permet d'écrire dans l'objet
	 */
	public void write(Object o){
		this.version = Client.mise_a_jour(o, this.id);
	}
}
