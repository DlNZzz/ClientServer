package ru.dinz;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    public static final int PORT = 4727;
    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    //private static BufferedWriter writer;
    //private static BufferedReader reader;
    //private static ObjectInputStream inObject;
    /**
     * список всех нитей - экземпляров
     */
    public static List<ServerSomething> serverList = new ArrayList<>();
    public static Story story;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        System.out.println("Server Started");
        serverSocket = new ServerSocket(PORT);
        story = new Story();
        new Server().go(serverSocket);
    }

    public void go(ServerSocket serverSocket) throws IOException, ClassNotFoundException, InterruptedException {
        int count = 0;
        /*
        while (true) {

            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String request = reader.readLine();
            String response = "#" + ++count + ", Your message is " + request;
            writer.write(response);
            writer.newLine();
            writer.flush();

            Account account = (Account) inObject.readObject();
            System.out.println(account);

            writer.write(account.getName() + " connection!");
            writer.newLine();
            close();
        }
        */
        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                clientSocket = serverSocket.accept();
                System.out.println("Accepted");
                System.out.println("IP is: " + ((InetSocketAddress) clientSocket.getRemoteSocketAddress()).getAddress().toString().split("/")[1]);
                System.out.println("PORT is: " + clientSocket.getRemoteSocketAddress().toString().split(":")[1]);
                try {
                    serverList.add(new ServerSomething(clientSocket)); // добавить новое соединенние в список
                } catch (IOException e) {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его:
                    clientSocket.close();
                }
            }
        } finally {
            close();
        }
    }

    private void close() throws IOException {
        //clientSocket.close();
        //writer.close();
        //reader.close();
        //inObject.close();
        //serverSocket.close();
    }
}


/*
public class Server {
    ArrayList clientOutputStreams;

    public class ClientHandler implements Runnable {
        BufferedReader reader;
        Socket sock;

        public ClientHandler(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);

            } catch (Exception ex) { ex.printStackTrace(); }
        }

        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    tellEveryone(message);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    public static void main(String[] args) {
        new Server().go();
    }

    public void go() {
        clientOutputStreams = new ArrayList();
        try {
            ServerSocket serverSock = new ServerSocket(4719);
            while(true) {
                Socket clientSocket = serverSock.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
                System.out.println("got a connection");
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    public void tellEveryone(String message) {
        Iterator it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}
*/
