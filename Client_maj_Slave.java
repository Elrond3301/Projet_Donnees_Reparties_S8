import java.rmi.RemoteException;

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
					c.maj_asynchrone(this.o, this.id, this.version);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}

		}
