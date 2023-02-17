import java.awt.*;
import java.awt.event.*;


public class Irc_lazy extends Frame {
	public TextArea		text;
	public TextField	data;
	SharedObject		sentence;
	static String		myName;

	
	public static void main(String argv[]) {
		
		if (argv.length != 1) {
			System.out.println("java Irc_lazy <name>");
			return;
		}
		myName = argv[0];
	
		// initialize the system
		Client.init();
		
		// look up the IRC object in the name server
		// if not found, create it, and register it in the name server
		SharedObject s = Client.lookup("IRC");
		if (s == null) {
			s = Client.create(new Sentence());
			Client.register("IRC", s);
		}
		// create the graphical part
		new Irc_lazy(s);
	}

	public Irc_lazy(SharedObject s) {
		
		setLayout(new FlowLayout());
	
		text=new TextArea(10,60);
		text.setEditable(false);
		text.setForeground(Color.red);
		add(text);
	
		data=new TextField(60);
		add(data);
	
		Button write_button = new Button("write");
		write_button.addActionListener(new writeListenerLazy(this));
		add(write_button);
		Button read_button = new Button("read");
		read_button.addActionListener(new readListenerLazy(this));
		add(read_button);
		
		setSize(470,300);
		text.setBackground(Color.black); 
		show();
		
		sentence = s;
	}
}



class readListenerLazy implements ActionListener {
    Irc_lazy irc;
	public readListenerLazy (Irc_lazy i) {
		irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// lock the object in read mode
        System.out.println("Avant Lockread " + irc.sentence.getLock());
		irc.sentence.lock_read();
		System.out.println("Après Lockread " + irc.sentence.getLock());
		// invoke the method
		String s = ((Sentence)(irc.sentence.obj)).read();

        try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		System.out.println("Avant Unlock ");
		// unlock the object
		irc.sentence.unlock();
        System.out.println("Après Unlock " + irc.sentence.getLock());
		
		// display the read value
		irc.text.append(s+"\n");
	}
}

class writeListenerLazy implements ActionListener {
    Irc_lazy irc;
	public writeListenerLazy (Irc_lazy i) {
        	irc = i;
	}
	public void actionPerformed (ActionEvent e) {
		
		// get the value to be written from the buffer
        String s = irc.data.getText();
        	
        // lock the object in write mode
        System.out.println("Avant Lockwrite " + irc.sentence.getLock());
		irc.sentence.lock_write();
        System.out.println("Après Lockwrite " + irc.sentence.getLock());
        
        
		// invoke the method
		((Sentence)(irc.sentence.obj)).write(Irc_lazy.myName+" wrote "+s);

		System.out.println(((Sentence)(irc.sentence.obj)).read());

        try {
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}


		irc.data.setText("");

		// unlock the object
		System.out.println("Avant Unlock ");
		irc.sentence.unlock();
        System.out.println("Après Unlock " + irc.sentence.getLock());
	}
}




