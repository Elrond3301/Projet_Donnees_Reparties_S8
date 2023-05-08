

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
			private int old_version;

			public Client_enquete_Slave(Client_itf c, int id, Client_itf origin, int old_version){
				this.c = c;
				this.id = id;
				this.origin = origin;
				this.old_version = old_version;
			}

			public void run(){
				try {
					Object obj = this.c.getObject(id); /* Récupère l'objet du client nous enquêtons */
					int new_version = this.c.getVersion(id); /* Récupère la version que possède le client que nous enquêtons */
					origin.addReponse(obj, this.c, this.id, new_version, this.old_version); /* Nous ajoutons la réponse de l'enquête dans le client qui l'a lancée */
				} catch (Exception e) {
				}
			}

		}
