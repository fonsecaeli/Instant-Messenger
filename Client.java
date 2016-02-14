import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.math.*;
import java.util.*;





public class Client extends JFrame {

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String message = "";
	private String serverIP;
	private Socket connection;
	private int height;
	private int length;
	private int port;
   
   
   //fields for encryption
   private boolean sendEncrypted;
   private boolean receiveEncrypted;
   private Cipher encrypter;
   private Cipher decrypter;
   private String outKey1;//public exponent
   private String outKey2; //public modulus 
   private String inKey1;
   private String inKey2;

   private String userName;
   private String otherName;
   private int bitLength;
   
   //sets up the public key encryption
   private void setupEncryption() {
      decrypter = new Cipher(bitLength);
      outKey1 = decrypter.getPublicExponent();
      outKey2 = decrypter.getModulus();
      
      sendMessage(outKey1, false);
      sendMessage(outKey2, false);
   }
   
   //proccess the keys sent from the other user
   private void getPublicKeys() throws IOException {
		for(int ii = 0;  ii < 2;  ii++) { //two keys should be sent
			try {
				ArrayList<String> message = (ArrayList<String>) input.readObject(); //all messages will be string[]
            if(ii == 0) {
               inKey1 = message.get(1);
            } 
            else {
               inKey2 = message.get(1);
            }
            otherName = message.get(0);
			}
			catch(ClassNotFoundException e) {
				showMessage("\n I don't know that object type!");
			}
      }
      encrypter = new Cipher(inKey1, inKey2);
	}

	//constructor 
	public Client(String host, int height, int length, int port, String userName, int bitLength) {
		super("Instant Messenger-Client");
      sendEncrypted = true;
      receiveEncrypted = true;
      this.height = height;
      this.length = length;
      this.port = port;
      this.bitLength = bitLength;
      this.userName = userName;
      setupMenus();
		serverIP = host;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent event) {
               String test = event.getActionCommand();
               if(!test.equals("")) {
                  if (sendEncrypted)
                  {
                     sendMessage(test);
                  }
                  else
                  {
                     sendMessage(test, true);
                  }
               } 
					userText.setText("");
				}
			}
		);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(height,length);
		setVisible(true);
		chatWindow.setFont(new Font("Serif", Font.PLAIN, 30));
		userText.setFont(new Font("Serif", Font.PLAIN, 30));
	}
   
   //sets up the menus for the gui
   private void setupMenus() {
      JMenuBar menubar = new JMenuBar();
   	
   
      JMenu options = new JMenu("Options");
   	
   
      JMenuItem exit = new JMenuItem("Exit");
      JMenuItem encrypt = new JMenuItem("Encyption on/off");
      options.add(exit);
      options.add(encrypt);
      menubar.add(options);
   
      JMenu help = new JMenu("Help");
   	
      JMenuItem about = new JMenuItem("About");
      help.add(about);
      menubar.add(help);
   
      exit.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               System.exit(0);
            }
         }
         );
   
      about.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               openReadme();
            }
         }
         );
      encrypt.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               if (sendEncrypted)
               {
                  sendMessage("SET_ENCRYPTION");
               }
               else
               {
                  sendMessage("SET_ENCRYPTION", false);
               }
               sendEncrypted = !sendEncrypted;   // changes the encryption setting
            }
         }
      
         );
   
      setJMenuBar(menubar);
   
   	
   	
   }	

   //opens readme file
   private void openReadme() {
      try {
         ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "Readme.txt");
         pb.start();
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }


	//connect to server 
	public void startRunning() {
		try {
			connectToServer();
			setupStreams();
         setupEncryption();
         getPublicKeys();
			whileChatting();
		}
		catch(EOFException e) {
			showMessage("\nClient terminated connection");

		}
		catch(IOException e2) {
			e2.printStackTrace();
		}
		finally {
			closeStuff();
		}
	}

	//connect to a server
	private void connectToServer() throws IOException {
		showMessage("Attempting connection... \n");
		connection = new Socket(InetAddress.getByName(serverIP), port);
		showMessage("Connected to: " + connection.getInetAddress().getHostName());

	}

	//setting up streams for the client 
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\nThe streams are set up and good to go! \n");
	}

	//while chatting with server 
	private void whileChatting() throws IOException {
		ableToType(true);
      String decryptedMessage = "";
      String plainMessage = "";
      do
      {
         System.out.println(receiveEncrypted);
         if (receiveEncrypted == true)
         {
            do {
            	//have conversation
   
               try {
                  ArrayList<String> message = (ArrayList<String>) input.readObject();
                  decryptedMessage = decrypter.decrypt(message);
                  if (decryptedMessage.equals(otherName + " - SET_ENCRYPTION"))
                  {
                     receiveEncrypted = !receiveEncrypted;
                  
                  }

   
      				showMessage("\n" + decryptedMessage);
                  
               }
               catch(ClassNotFoundException e) {
                  showMessage("\n idk wtf that user sent!");
               }
            }
            while(!decryptedMessage.equals(otherName + " - END") && !decryptedMessage.equals(otherName + " - SET_ENCRYPTION")); //if the client wants end then use this string 
         }
         else
         {
            do
            {
         	   try {
         			ArrayList<String> message = (ArrayList<String>) input.readObject(); // is ArrayList the message type?
         		   plainMessage = message.get(0) + message.get(1);
                  if (plainMessage.equals(otherName + " - SET_ENCRYPTION"))
                  {
                     receiveEncrypted = !receiveEncrypted;
                  }

                  showMessage("\n" + plainMessage);
                  
         		}
         		catch(ClassNotFoundException e) {
         			showMessage("\n I don't know that object type!");
         		}
            }
            while (!plainMessage.equals(otherName + " - END") && !plainMessage.equals(otherName + " - SET_ENCRYPTION"));
         }
      }
      while (!plainMessage.equals(otherName + " - END") && !decryptedMessage.equals(otherName + " - END"));
   }
	//house keeping, closing all the streams and sockets down
	private void closeStuff() {
		showMessage("\nClosing stuff down");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	//send messages to server 
	private void sendMessage(String m, boolean visible) {
      if (m.equals("SET_ENCRYPTED"))
      {
         sendEncrypted = !sendEncrypted;
      }
      ArrayList<String> message = new ArrayList<String>(2); 
      message.add(userName + " - ");
      message.add(m);
		try {
			output.writeObject(message); //sends the message to the server
			output.flush();
         if(visible) {
			   showMessage("\n" + userName + " - " + m); //shows message in the users chat window
         }
		}
		catch(IOException e) {
			chatWindow.append("\n something messed up sending message");
		}
	}
   
   //sends messages sends them as ArrayList<string> as param instead of string 
   private void sendMessage(String message) {
      if (message.equals("SET_ENCRYPTED"))
      {
         sendEncrypted = !sendEncrypted;
      }
      ArrayList<String> encryptedMessage = encrypter.encryptString(userName + " - ");
      encryptedMessage.addAll(encrypter.encryptString(message));
      System.out.println(encryptedMessage);
		try {
			output.writeObject(encryptedMessage); //sends the message to the server
			output.flush();
			showMessage("\n" + userName + " - " + message); //shows message in the users chat window
		}
		catch(IOException e) {
			chatWindow.append("\nSomething messed up sending message");
		}
	}


	//updates chatWindow
	private void showMessage(final String text) {
		//setting aside a thread to update a gui, so we dont have to create an entire new gui when we just want to update part
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					chatWindow.append(text); //adds message to the chatWindow
				}
			}
		);
	}

	//lets the user type stuff into there chat box
	private void ableToType(final boolean trueOrFalse) {
		SwingUtilities.invokeLater(
			new Runnable() {
				public void run() {
					userText.setEditable(trueOrFalse);
				}
			}
		);
	} 
 
}
