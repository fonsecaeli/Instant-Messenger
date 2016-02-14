import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.SpringLayout;



public class Login extends JFrame {
   
   private String[] labels;
   private JTextField[] textField;
   private JTextArea notifications;
   private JButton submit;
   private int height;
   private int length;
   private String userName;
   private String IP;
   private String bitNumber;
   private boolean submitted = false;
   
   public boolean forumSubmitted() {
      return this.submitted;
   }
   
   public String getIP() {
      return this.IP;
   }
   
   public String getBitNumber() {
      return this.bitNumber;
   }
   
   public Login(int height, int length, boolean isClient) {
      super("User - Login");
      if(isClient) {
         labels = new String[3];
         labels[0] = "UserName: "; 
         labels[1] = "Key BitLength: "; 
         labels[2] = "IP Address: ";
      }
      else {
         labels = new String[2];
         labels[0] = "UserName: ";  
         labels[1] = "Key BitLength: ";
      }
      int labelsLength = labels.length;
      textField = new JTextField[labelsLength]; 
      JPanel p = new JPanel(new SpringLayout());
      for(int i = 0; i < labelsLength; i++) {
         JLabel l = new JLabel(labels[i], JLabel.TRAILING);
         p.add(l);
         textField[i] = new JTextField(10); //sets the default number of spaces each TextField takes up to 10 
         l.setLabelFor(textField[i]);
         p.add(textField[i]);
      }
      JButton button = new JButton("Submit");
      p.add(new JLabel());
      p.add(button);
    
    //Lay out the panel.
      SpringUtilities.makeCompactGrid(p,
                                    labelsLength + 1, 2, //rows, cols
                                    7, 7,        //initX, initY
                                    7, 7);       //xPad, yPad
   
   
      
      button.addActionListener(
         new ActionListener() {
         
            public void actionPerformed(ActionEvent e)
            {
               userName = textField[0].getText();
               bitNumber = textField[1].getText(); //this causes problmes if it is entered as a blakc string 
                                                   //because it will be processed into an int in the main program
               if(isClient) {
                  IP = textField[2].getText();
               }
               submitted = true;
               //TODO
               //add error correction for input from this form, like if they enter nothing for one of the feilds
            }
         });  
    //Set up the content pane.
      p.setOpaque(true);  //content panes must be opaque
      setContentPane(p);
   
      this.height = height;
      this.length = length;
      
      
      
      
      notifications = new JTextArea();
      add(new JScrollPane(notifications));
      add(notifications);
      setSize(height, length); //size of window
      setVisible(true);
      notifications.setFont(new Font("Serif", Font.PLAIN, 30));
      //userText.setFont(new Font("Serif", Font.PLAIN, 30));   
   }
   
   public String getUserName() {
      return this.userName;
   }
   
   //closes the login window
   public void close() {
      setVisible(false);
      //System.exit(0);
   }
      
   //shows messages on the JFrame
   private void showMessage(final String text) {
   	//setting aside a thread to update a gui, so we dont have to create an entire new gui when we just want to update part
      SwingUtilities.invokeLater(
         new Runnable() {
            public void run() {
               notifications.append(text); //adds message to the chatWindow
            }
         }
         );
   }

   
   
}
