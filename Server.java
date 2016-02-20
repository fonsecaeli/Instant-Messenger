import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.EOFException;
import java.net.Socket;
import java.net.ServerSocket;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import java.math.BigInteger;
import java.util.ArrayList;





public class Server extends JFrame {

   private JTextField userText;
   private JTextArea chatWindow;
   private ObjectOutputStream output;
   private ObjectInputStream input;
   private ServerSocket server;
   private Socket connection;
   private int height;
   private int length;
   private int port;
   
   //fields for encryption 
   private boolean isEncrypted;
   private Cipher encrypter;
   private Cipher decrypter;
   private String outKey1;
   private String outKey2;
   private String inKey1;
   private String inKey2;

   private String userName;
   private int bitLength;
   private String otherName;

   
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
   public Server(int height, int length, int port, String userName, int bitLength) {
      super("Instant Messenger-Server");
      this.userName = userName;
      this.bitLength = bitLength;
      isEncrypted = true;
      this.height = height;
      this.length = length;
      this.port = port;
      setupMenus();
      userText = new JTextField();
      userText.setEditable(false); //before you are connected you cannot type anything in message box 
      userText.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent event) {
               String test = event.getActionCommand();
               if(!test.equals("")) {
                  sendMessage(test);
               } 
               userText.setText(""); //turns message box blank after message is sent
            
            }
         }
         );
      add(userText, BorderLayout.NORTH);
      chatWindow = new JTextArea();
      add(new JScrollPane(chatWindow));
      setSize(height, length); //size of window
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


	//set up and run the server 
   public void startRunning() {
      try {
         server = new ServerSocket(port, 100); //port #, backlog(how many people can wait to access this instant messenger)
         while(true) {
            try {
            	//connect and have conversation
               waitForConnection();
               setupStreams();
               setupEncryption();
               getPublicKeys();
               whileChatting();
            }
            catch(EOFException ex) {
               showMessage("\n Server ended the connection!");
            }
            finally {
               closeStuff();
            }
         }
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }

	//wait for connection, then once connected give prompt to user
   private void waitForConnection() throws IOException {
      showMessage(" Waiting for someone to connect... \n");
      connection = server.accept(); //once someone asks to connect this accepts the connection to the socket, once connected a connection is created bewteen server and client 
      showMessage(" Now connected to "+connection.getInetAddress().getHostName());
   
   }

	//get stream to send and receive data
   private void setupStreams() throws IOException {
      output = new ObjectOutputStream(connection.getOutputStream()); //creates pathway that allows to connect to another computer
      output.flush(); // clears the stream so there is no left over data
      input = new ObjectInputStream(connection.getInputStream()); //sets pathway to receive messages 
      showMessage("\nStreams are now setup! \n");
   }

	//during the chat conversation 
   private void whileChatting() throws IOException {
      String notification = "You are now connected! ";
      sendMessage(notification);
      String decryptedMessage = "";
      ableToType(true);
      do {
      	//have conversation
         try {
            ArrayList<String> message = (ArrayList<String>) input.readObject();
            decryptedMessage = decrypter.decrypt(message);
				showMessage("\n" + decryptedMessage);
         }
         catch(ClassNotFoundException e) {
            showMessage("\n idk wtf that user sent!");
         }
      }
      while(!decryptedMessage.equals(otherName+" - END")); //if the client wants end then use this string 
   }

	//close streams and sockets after you are done chating 
   private void closeStuff() {
      showMessage("\n Closing connections... \n");
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
      ArrayList<String> message = new ArrayList<String>(2);
      message.add(userName+" - ");
      message.add(m); 
		try {
			output.writeObject(message); //sends the message to the server
			output.flush();
         if(visible) {
			   showMessage("\n"+userName+" - " + m); //shows message in the users chat window
         }
		}
		catch(IOException e) {
			chatWindow.append("\n something messed up sending message");
		}
	}
   
   //sends messages sends them as ArrayList<string> as param instead of string 
   private void sendMessage(String message) {
      ArrayList<String> encryptedMessage = encrypter.encryptString(userName+" - ");
      encryptedMessage.addAll(encrypter.encryptString(message));
      //System.out.println(encryptedMessage);
		try {
			output.writeObject(encryptedMessage); //sends the message to the server
			output.flush();
			showMessage("\n"+userName+" - " + message); //shows message in the users chat window
		}
		catch(IOException e) {
			chatWindow.append("\nsomething messed up sending message");
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
