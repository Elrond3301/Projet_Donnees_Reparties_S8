/* 
 * Classe SharedObject   
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON G/ermain
 * Date : 18/02/2023
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

	public void setVersion(int version){
		this.version = version;
	}

	/*
	 * Fonction read
	 * Cette fonction permet de lire l'objet
	 * Retourne l'objet
	 */
	public Object read(){
		System.out.println("je lis");
		SharedObject s = Client.read(this.id, new Rappel_lec());
		if (s.obj != null){
			this.version = s.getVersion();
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
		System.out.println("j'ecris");
		this.obj = o;
		this.version = Client.mise_à_jour(this.obj, this.id);
	}
}
