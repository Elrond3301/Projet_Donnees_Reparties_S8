/* 
 * Classe SharedObject   
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON G/ermain
 * Date : 18/02/2023
 * La classe SharedObject contient un objet partagé et un id
 */

public class SharedObject implements SharedObject_itf {

	private Lock lock; 
    public Object obj; 
	private int id;
	private int notif;
	private Observateur_itf obs;

	// Constructor for SharedObject with an object and an id
	public SharedObject(Object obj, int id) {
		super();
		this.obj = obj;
		this.id = id;
		this.lock = Lock.NL;
		this.notif = 0;
		
	}

	// Constructor for SharedObject with an id
	public SharedObject(int id){
		super();
		this.id = id;
		this.lock = Lock.NL;
	}
	

	// Constructor for SharedObject with an object and an id
	public SharedObject(Object obj, int id, Observateur_itf obs) {
		super();
		this.obj = obj;
		this.id = id;
		this.lock = Lock.NL;
		this.notif = 0;
		this.obs = obs;
	}

	// Constructor for SharedObject with an id
	public SharedObject(int id, Observateur_itf obs){
		super();
		this.id = id;
		this.lock = Lock.NL;
		this.obs = obs;
	}

	
	// invoked by the user program on the client node
	public void lock_read() {
		if (this.lock == Lock.NL){
			this.obj = Client.lock_read(this.id); // call the function lock_read on the client if the lock is NL
			this.lock = Lock.RLT;
		} else if(this.lock == Lock.RLC) { // if the lock is RLC, we change the lock to RLT but we don't call the function lock_read on the client
			this.lock = Lock.RLT;
		} else if(this.lock == Lock.WLC){ // same here
			this.lock = Lock.RLT_WLC;
		} 
		this.notif = 0;
		if (this.obs != null) {
			this.obs.getNotification(this.notif);
		}	
	}

	// invoked by the user program on the client node
	public void lock_write() {
		if (this.lock == Lock.RLC || this.lock == Lock.NL){ // if the lock is NL or RLC, we call the function lock_write on the client
			this.obj = Client.lock_write(this.id);
			this.lock = Lock.WLT;
		} else if (this.lock == Lock.WLC) { 
			this.lock = Lock.WLT;
		}
		
	}

	// invoked by the user program on the client node
	public synchronized void unlock() {
		switch (this.lock) {
			case RLT :
				this.lock = Lock.RLC;
				break;
			case WLT : // ici doit notifier le serveur et rendre l'état de l'objet
				this.lock = Lock.RLC;
				Slave s = new Slave(this.id, this.obj);
				s.start();
				break;
			case RLT_WLC :
				this.lock = Lock.WLC;
				break;
			default :
				break;
		}
		SharedObject.this.notify(); // Notify pour prévenir qu'on a unlock
	}


	/*
	 * Fonction Reduce Lock
	 * Cette fonction permet de réduire le lock d'un objet
	 * Retourne l'objet partagé
	 */
	public synchronized Object reduce_lock() {
		while (this.lock == Lock.WLT){ // Attente d'un notify du unlock
			try {
				SharedObject.this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		switch (this.lock) {
			case WLC:
				this.lock = Lock.RLC;
				break;
			case RLT_WLC:
				this.lock = Lock.RLT;
				break;

			default:
				break;
		}
		return this.obj;
	}

	/*
	 * Fonction Invalidate Reader
	 * Cette fonction permet d'invalider un reader
	 * Retourne l'objet partagé
	 */
	public synchronized void invalidate_reader() {
		if (this.lock == Lock.RLC) { 
			this.lock = Lock.NL;
		} else if (this.lock == Lock.RLT) {
			while (this.lock == Lock.RLT) { // Attente d'un notify du unlock
				try {
					SharedObject.this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			this.lock = Lock.NL;
			
		}

	}

	/*
	 * Fonction Invalidate Writer
	 * Cette fonction permet d'invalider un writer
	 * Retourne l'objet partagé
	 */
	public synchronized Object invalidate_writer() {
		if (this.lock == Lock.WLC) {
			this.lock = Lock.NL;
		} else if (this.lock == Lock.WLT || this.lock == Lock.RLT_WLC) {
			while (this.lock == Lock.WLT || this.lock == Lock.RLT_WLC) { // Attente d'un notify du unlock
				try {
					SharedObject.this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} //Attente active du unlock
			this.lock = Lock.NL;
		}
		return this.obj;
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
	 * Fonction Get Lock
	 * Cette fonction permet de récupérer le lock d'un objet
	 * Retourne le lock de l'objet
	 */
	public Lock getLock(){
		return this.lock;
	}


	/*
	 * Fonction Get Obs
	 * Cette fonction permet de récupérer l'observateur d'un objet
	 * Retourne l'observateur de l'objet
	 */
	public Observateur_itf getObs(){
		return this.obs;
	}



	/*
	 * Fonction getNotification qui permet de recevoir les notifications et mettre à jour le compteur de notification
	 * 
	 */
    public void getNotification(Object obj) {
		if(this.lock!=Lock.WLC){
			this.obj = obj;
			this.notif++;
			this.obs.getNotification(this.notif);	
		}
		this.lock = Lock.RLC;		
    }

	/*
	 * Fonction Set Obs
	 * Cette fonction permet de modifier l'observateur d'un objet
	 */

	public void setObs(Observateur_itf obs) {
		this.obs = obs;
	}

	private class Slave extends Thread {

		private int id;
		private Object obj;

		public Slave(int id, Object obj){
			this.id = id; 
			this.obj = obj;
		}

		public void run(){
			Client.notification(this.id, this.obj); // appel de la fonction notification sur le client
		}
	}
}
