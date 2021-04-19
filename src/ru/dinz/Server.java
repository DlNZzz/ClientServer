package ru.dinz;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {

    private static Socket clientSocket;
    private static ServerSocket serverSocket;
    private static BufferedWriter writer;
    private static BufferedReader reader;
    private static ObjectInputStream inObject;

    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(4719);
        new Server().go(serverSocket);
    }

    public void go(ServerSocket serverSocket) throws IOException {
        int count = 0;
        while (true) {
            clientSocket = serverSocket.accept();
            inObject = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println(((InetSocketAddress) clientSocket.getRemoteSocketAddress()).getAddress());
            System.out.println(clientSocket.getRemoteSocketAddress().toString().split(":")[0]);
            System.out.println("Accepted");

            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String request = reader.readLine();
            String response = "#" + count + ", Your message length is " + request.length();
            writer.write(response);
            writer.newLine();
            writer.write("HTTP/1.0 200 OK\r\n"
                    + "Content-type: text/html\r\n"
                    + "\r\n"
                    + "<h1>Java " + count++ + "</h1>\r\n");
            writer.flush();

            close();
        }
    }

    private void close() throws IOException {
        clientSocket.close();
        writer.close();
        reader.close();
        inObject.close();
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
