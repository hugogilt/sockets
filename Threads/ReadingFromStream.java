package Threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JTextArea;

public class ReadingFromStream extends Thread {
	private BufferedReader in;
	private PrintStream out;
	private JTextArea area;
	private JButton bsend;
	private JButton bdisconnect;
	ServerSocket server;
	private JButton bserver;
	private JButton bclient;
	private Socket client;
	private String rol;
	public ReadingFromStream(BufferedReader in, JTextArea area) {
		this.in=in;
		this.area=area;
	}
	
	public void run() {
		while (true) {
			try {
			String t = in.readLine();
			if (t != null && t.equals("$$$***...")) {
				out.println(t);
				client.close();
				if (rol.equals("Server")) {   
	            	server.close();
	            }
				bdisconnect.setEnabled(false);
				bsend.setEnabled(false);
				bserver.setEnabled(true);
				bclient.setEnabled(true);
				break;
			}
			if( t != null ) {
			area.setText(area.getText()+"\nOther: "+t);
			}
		}  catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
		}
	}

	

	public void setSend(JButton send) {
		this.bsend = send;
	}

	public void setDisconnect(JButton disconnect) {
		this.bdisconnect = disconnect;
	}

	public void setbServer(JButton bserver) {
		this.bserver = bserver;
	}

	public void setClient(Socket client) {
		this.client = client;
	}
	
	public JButton getbClient() {
		return bclient;
	}
	
	public void setbClient(JButton bclient) {
		this.bclient = bclient;
	}

	public void setOut(PrintStream out) {
		this.out = out;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public void setServer(ServerSocket server) {
		this.server = server;
	}

	
	
	
}
