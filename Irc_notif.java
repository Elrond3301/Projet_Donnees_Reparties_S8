import java.awt.*;
import java.awt.event.*;

import javax.swing.JLabel;


public class Irc_notif extends Frame {
	public TextArea		text;
	public static JLabel       notif;
	public TextField	data;
	SharedObject		sentence;
	static String		myName;

	public static void main(String argv[]) {
		
		if (argv.length != 1) {
			System.out.println("java Irc <name>");
			return;
		}
		myName = argv[0];
	
		// initialize the system
		Client.init();
		
		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookupAndSubscribe("IRC");
		if (s == null) {
			s = Client.create(new Sentence());
			Client.registerAndSubscribe("IRC", s);
		}
		// create the graphical part
		new Irc_notif(s);
	}

	public Irc_notif(SharedObject s) {
	
		setLayout(new FlowLayout());
	
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);

		notif = new JLabel();
		notif.setText("nbNotif : 0 ");
		add(notif);
	
		data=new TextField(60);
		add(data);
	
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListener_notif(this));
		add(write_button);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListener_notif(this));
		add(read_button);
		
		setSize(470,300);
		text.setBackground(Color.black); 
		show();
		
		sentence = s;
	}

	public  static void  MajNotif(int nbNotif){

		notif.setText("nbNotif : "+ nbNotif);

	}
}



class readListener_notif implements ActionListener {
	Irc_notif irc;
	public readListener_notif (Irc_notif i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// lock the object in read mode
		irc.sentence.lock_read();
		
		// invoke the method
		String s = ((Sentence)(irc.sentence.obj)).read();
		
		// unlock the object
		irc.sentence.unlock();
		
		// display the read value
		irc.text.append(s+"\n");
	}
}

class writeListener_notif implements ActionListener {
	Irc_notif irc;
	public writeListener_notif (Irc_notif i) {
        	irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// get the value to be written from the buffer
        String s = irc.data.getText();
        	
        // lock the object in write mode
		irc.sentence.lock_write();
		
		// invoke the method
		((Sentence)(irc.sentence.obj)).write(Irc_notif.myName+" wrote "+s);
		irc.data.setText("");
		
		// unlock the object
		irc.sentence.unlock();
	}
}


