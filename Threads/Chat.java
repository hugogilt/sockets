package Threads;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.JScrollPane;

public class Chat extends JFrame {
	private final static int PORT = 5005;
	private  static String serverip="10.2.1.94";
	private String rol;
    //Client Socket
    Socket client;
    ServerSocket server;
    Scanner tec = new Scanner(System.in);
    String msg;
	private JPanel contentPane;
	private JTextField Barra;
	BufferedReader input;
	PrintStream output;
	ReadingFromStream t;
	//BUTTONS
	JButton sendButton;
	JButton clientButton;
	JButton serverButton;
	JButton disconnectButton;
	private final int WITH_CONNECTION = 1;
	private final int WITHOUT_CONNECTION = 0;
	private final int CASTIGADO = 3;
	private int state=WITHOUT_CONNECTION;
	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Chat frame = new Chat();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the frame.
	 */
	public Chat() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Barra = new JTextField();
		Barra.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendButton.doClick();
			}
		});
		Barra.setBounds(38, 194, 257, 26);
		contentPane.add(Barra);
		Barra.setColumns(10);
		
		JTextPane statusBar = new JTextPane();
		statusBar.setBounds(41, 244, 254, 23);
		contentPane.add(statusBar);
		statusBar.setText("");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 15, 250, 150);
		contentPane.add(scrollPane);
		
		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		
		
		serverButton = new JButton("Server");
		serverButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rol="Server";
				 try {
					 
				//Server Socket to wait for network requests
	            server = new ServerSocket(PORT);
	            //System.out.println("Server started");    
	              
	
	            statusBar.setText("Server started, waiting for a client...");
	            //System.out.println("Server waiting for a client...");  
	            client = server.accept();
	            statusBar.setText("Connected to client: " + client.getInetAddress());
	           // System.out.println("connected to client: " + client.getInetAddress());
	            //setSoLinger closes the socket giving 10mS to receive the remaining data
	            client.setSoLinger (true, 10);
	            //an input reader to read from the socket
	            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
	            t = new ReadingFromStream(input, textArea);
	            t.start();
	            //to print data out                
	            output = new PrintStream(client.getOutputStream());
	            t.setOut(output);
	            textArea.setText("");
	            state=WITH_CONNECTION;
	            enableEdition();
				 }catch (IOException ex) {
			            System.err.println(ex.getMessage());
			        }
				 	t.setbClient(clientButton);
					t.setDisconnect(disconnectButton);
					t.setSend(sendButton);
					t.setbServer(serverButton);
					t.setClient(client);
					t.setServer(server);
					t.setRol(rol);
			}
				 
		});
		
		serverButton.setBounds(307, 23, 105, 27);
		contentPane.add(serverButton);
		
		clientButton = new JButton("Client");
		clientButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rol="Client";
				 String ipAddress = showIPInputDialog();
			 //    System.out.println("La dirección IP ingresada es: " + ipAddress);
			//	serverip = JOptionPane.showInputDialog(contentPane, 	"A que servidor te quieres conectar?");
				serverip=ipAddress;

			//	Socket socket;//Socket para la comunicacion cliente servidor
				   try {            
			            client = new Socket(serverip, PORT);//open socket                
			            //To read from the server      
			            input = new BufferedReader( new InputStreamReader(client.getInputStream()));  
			            t = new ReadingFromStream(input, textArea);
			            t.start();       
			            //to write to the server
			            output = new PrintStream(client.getOutputStream()); 
			            t.setOut(output);
			            textArea.setText("");
			            statusBar.setText("Connected to server: "+serverip);
			            state=WITH_CONNECTION;
			            enableEdition();                                      
			       } catch (IOException ex) {        
			         System.err.println("Client -> " + ex.getMessage());   
			       }
				   
				   if (t != null) {
					
				
				   	t.setbClient(clientButton);
					t.setDisconnect(disconnectButton);
					t.setSend(sendButton);
					t.setbServer(serverButton);
					t.setClient(client);
					t.setServer(server);
					t.setRol(rol);
				   }
			}
			
		});
		
		
		sendButton = new JButton("Send");
		sendButton.setForeground(new Color(255, 255, 255));
		sendButton.setBackground(new Color(78, 154, 6));
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String line = Barra.getText();
				Barra.setText("");
				
					if (line.equals("Milagros")) {
						state=CASTIGADO;
						line="Mila***s\nEl usuario ha sido expulsado del chat";
					}
					
					output.println(line); 
					textArea.setText(textArea.getText()+"\nMe: "+line);
					output.flush();//empty contents 
					enableEdition();
   
				
			
					
			}
		});
		
		
		
		
		
		clientButton.setBounds(307, 78, 105, 27);
		contentPane.add(clientButton);
		
		disconnectButton = new JButton("Disconnect");
		disconnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					output.println("Conexión cortada");
					textArea.setText(textArea.getText()+"\nHas cortado la conexión");
					output.println("$$$***...");
					
					state=WITHOUT_CONNECTION;
		            enableEdition();
		            client.close();
		            if (rol.equals("Server")) {
		            	server.close();
		            }
					} catch (IOException ex) { 
						ex.printStackTrace();
				         System.err.println("Client -> " + ex.getMessage());   
				       }
			}
		});
		disconnectButton.setBounds(307, 132, 105, 27);
		contentPane.add(disconnectButton);
		
		
		sendButton.setBounds(307, 193, 105, 27);
		contentPane.add(sendButton);
		
		enableEdition();
		
	}
	
	private void enableEdition() {
		switch (state) {
		case WITH_CONNECTION:
			serverButton.setEnabled(false);
			clientButton.setEnabled(false);
			sendButton.setEnabled(true);
			disconnectButton.setEnabled(true);
			break;
		case WITHOUT_CONNECTION:
			clientButton.setEnabled(true);
			serverButton.setEnabled(true);
			sendButton.setEnabled(false);
			disconnectButton.setEnabled(false);
			break;
		case CASTIGADO:
			System.exit(0);
			break;
		default:
			break;
		}
		
	}
	
	 private static boolean isValidIP(String ipAddress) {
	        String[] octets = ipAddress.split("\\.");

	        if (octets.length != 4) {
	            return false;
	        }

	        for (String octet : octets) {
	            try {
	                int value = Integer.parseInt(octet);
	                if (value < 0 || value > 255) {
	                    return false;
	                }
	            } catch (NumberFormatException e) {
	                return false;
	            }
	        }

	        return true;
	    }
	 
	 private static String showIPInputDialog() {
	        String ipAddress = null;
	        boolean validIP = false;

	        while (!validIP) {
	            ipAddress = JOptionPane.showInputDialog(null, "Ingrese una dirección IP:", "Entrada de dirección IP", JOptionPane.QUESTION_MESSAGE);

	            if (ipAddress == null) {
	                // El usuario hizo clic en Cancelar
	                System.exit(0);
	            }

	            validIP = isValidIP(ipAddress);

	            if (!validIP) {
	                JOptionPane.showMessageDialog(null, "La dirección IP ingresada no es válida. Por favor, intente nuevamente.", "Error de formato", JOptionPane.ERROR_MESSAGE);
	            }
	        }

	        return ipAddress;
	    }
	
	
}

