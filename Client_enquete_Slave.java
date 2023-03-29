import java.rmi.RemoteException;

public class Client_enquete_Slave extends Thread {

			private Client_itf c;
			private int id;
			private Rappel_lec rappel;

			public Client_enquete_Slave(Client_itf c, int id, Rappel_lec rappel){
				this.c = c;
				this.id = id;
				this.rappel = rappel;
			}


			public void run(){
				try {
					this.rappel.reponse(this.c.getSharedObject(this.id));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		}
