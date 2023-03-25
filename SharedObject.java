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

	public Object read(){
		System.out.println("je lis");
		// maj obj
		return this.obj;
	}

	public void write(Object o){
		System.out.println("j'ecris");
		this.obj = o;
		// propagation maj obj
	}
}
