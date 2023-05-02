import java.rmi.RemoteException;

public class Client_enquete_Slave extends Thread {

			private Client_itf c;
			private Client_itf origin;
			private int id;

			public Client_enquete_Slave(Client_itf c, int id, Client_itf origin){
				this.c = c;
				this.id = id;
				this.origin = origin;
			}


			public void run(){
				try {
					Object obj = this.c.getObject(id);
					int version = this.c.getVersion(id);
					origin.add_reponse(obj, this.c, version);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		}
