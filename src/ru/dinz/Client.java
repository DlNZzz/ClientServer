package ru.dinz;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.*;

public class Client {

    private static Socket clientSocket;
    private static BufferedWriter writer;
    private static BufferedReader reader;
    private static BufferedReader readerSystem;
    private static ObjectOutputStream outObject;

    public static void main(String[] args) throws IOException {
        clientSocket = new Socket("127.0.0.1", 4721);
        writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outObject = new ObjectOutputStream(clientSocket.getOutputStream());
        readerSystem = new BufferedReader(new InputStreamReader(System.in));
        Client client = new Client();
        client.go();
    }

    public void go() throws IOException {
        Account account = createAccount();
        outObject.writeObject(account);
        outObject.flush();
        writer.flush();

        String message = reader.readLine();
        System.out.println(message);
        /*
        while (reader.ready()) {
            message = reader.readLine();
            System.out.println(message);
        }

        String request = reader.readLine();
        System.out.println(request);

        writer.write("Get me some information");
        writer.newLine();
        writer.flush();
        */
        close();
    }

    /**
     * Create new Account
     * @return Account
     * @throws IOException
     */
    public Account createAccount() throws IOException {
        System.out.print("Client start!\nEnter name: ");
        String name = readerSystem.readLine();
        System.out.print("Enter password: ");
        String password = readerSystem.readLine();
        Account account = new Account(name, password);
        return account;
    }

    /**
     * Closes streams
     * @throws IOException
     */
    private void close() throws IOException {
        clientSocket.close();
        writer.close();
        reader.close();
        readerSystem.close();
        outObject.close();
    }
}

/*
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client
{
    JTextArea incoming;
    JTextField outgoing;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;

    public void go() {
        JFrame frame = new JFrame("Ludicrously Simple Chat Client");
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        setUpNetworking();

        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.setSize(650, 500);
        frame.setVisible(true);

    }

    private void setUpNetworking() {
        try {
            sock = new Socket("127.0.0.1", 4719);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("networking established" + sock.getPort());
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }

    public class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            try {
                writer.println(outgoing.getText());
                writer.flush();

            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public static void main(String[] args) {
        new Client().go();
    }

    class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("client read " + message);
                    incoming.append(message + "\n");
                }
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
*/
