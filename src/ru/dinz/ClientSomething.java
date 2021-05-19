package ru.dinz;

import java.io.*;
import java.net.Socket;

public class ClientSomething {

    private final String serverIp;
    private final short serverPort;
    private final Account account;
    private Socket clientSocket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private BufferedReader readerSystemConsole;
    private ObjectOutputStream outObject;

    ClientSomething(String serverIp, short serverPort, Account account) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.account = account;
        go();
    }

    public void go() {
        try {
            clientSocket = new Socket(serverIp, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Socket failed");
        }
        try {
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            outObject = new ObjectOutputStream(clientSocket.getOutputStream());
            readerSystemConsole = new BufferedReader(new InputStreamReader(System.in));
            if (addAccountToServer()) {
                new ReadMessage().start();
                new WriteMessage().start();
            } else {
                closeService();
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeService();
        }
    }

    private boolean addAccountToServer() {
        try {
            outObject.writeObject(account);
            outObject.flush();
            String message = reader.readLine();
            System.out.println(message);
            return message.equals("Account found!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Account failed");
            return false;
        }
    }

    class ReadMessage extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }

    class WriteMessage extends Thread {
        @Override
        public void run() {
            super.run();
        }
    }

    private void closeService() {
        try {
            if (!clientSocket.isClosed()) {
                clientSocket.close();
                writer.close();
                reader.close();
                readerSystemConsole.close();
                outObject.close();
            }
        } catch (IOException ignored) {}
    }
}
