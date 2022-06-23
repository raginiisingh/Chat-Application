import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;

import java.awt.Font;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //declare components
    private JLabel heading=new JLabel("CLIENT AREA");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Times New Roman",Font.BOLD,20);


    //constructor
    public Client(){
        try{
            System.out.println("sending request to server");
            socket=new Socket("192.168.1.3",7777);
            System.out.println("connection done");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            //startWriting();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
                //System.out.println("key released"+e.getKeyCode());
                if(e.getKeyCode()==10){
                    //System.out.println("You have pressed enter button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me :"+ contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
                
            }

        });

        
    }

    private void createGUI()
    {
        this.setTitle("Client Messenger");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("logo.jpg"));
        heading.setHorizontalTextPosition(JLabel.CENTER);
        heading.setVerticalTextPosition(JLabel.BOTTOM);
        heading.setHorizontalAlignment(JLabel.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);

        this.setLayout(new BorderLayout());
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH); 
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);


    }

    public void startReading(){
        //thread read
        Runnable r1=()->{
            System.out.println(("reader started.."));
            while(true){
                try{
                String msg=br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Server has terminated the conversation");
                    JOptionPane.showMessageDialog(this,"Server has terminated the chat" );
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                //System.out.println("Server:"+msg);
                messageArea.append("Server: " + msg + "\n");
                
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        };

        new Thread(r1).start();

    }

    public void startWriting(){
        //thread send
        Runnable r2=()->{
            System.out.println("writer started...");
            while(true)
            {
                try{
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush(); 

                }catch(Exception e){
                    e.printStackTrace();
                }
            }

        };
        new Thread(r2).start();

    }
    public static void main(String[] args) {
        System.out.println("this is client");
        new Client();
        
    }

    
}
