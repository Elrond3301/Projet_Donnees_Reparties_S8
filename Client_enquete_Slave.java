import java.rmi.RemoteException;

/* 
 * Classe Client_enquete_Slave   
 * Auteur : CAMPAN Mathieu
 *          HAUTESSERRES Simon
 *          BESSON Germain
 * Date : 03/05/2023
 * La classe Client_enquete_Slave lance une enquete pour savoir la version et l'objet actuel que possède un client précis
*/

public class Client_enquete_Slave extends Thread {

			private Client_itf c;		/* Le client qu'on souhaite enquêter */
			private Client_itf origin;  /* Le client qui lane l'enquête */
			private int id;

			public Client_enquete_Slave(Client_itf c, int id, Client_itf origin){
				this.c = c;
				this.id = id;
				this.origin = origin;
			}

			public void run(){
				try {
					Object obj = this.c.getObject(id); /* Récupère l'objet du client nous enquêtons */
					int version = this.c.getVersion(id); /* Récupère la version que possède le client que nous enquêtons */
					origin.addReponse(obj, this.c, version); /* Nous ajoutons la réponse de l'enquête dans le client qui l'a lancée */
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		}
