import java.rmi.RemoteException;

/* 
* Classe Client Maj Slave 
* Auteur : CAMPAN Mathieu
*          HAUTESSERRES Simon
*          BESSON Germain
* Date : 03/05/2023
* Cette classe permet d'effectuer une mise à jour asynchrone pour un client cible
*/
public class Client_maj_Slave extends Thread {

	private Client_itf c;
	private Object o;
	private int id;
	private int version;

	public Client_maj_Slave(Client_itf c, Object o, int id, int version){
		this.c = c;
		this.id = id;
		this.o = o;
		this.version = version;
	}


	public void run(){
		try {
			c.majAsynchrone(this.o, this.id, this.version);
		} catch (RemoteException e) {
		}
	}

}
