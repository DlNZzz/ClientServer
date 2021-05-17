package ru.dinz;

import java.io.*;
import java.net.*;
import java.util.*;
/*
1 все клиенты должны видеть записанное
2 подключаться могут только те кто в списке
3 сервер должен в реальном времени оповещать статус клиента
 */
public class Server {

    public static final int PORT = 4729;
    private static Socket clientSocket;
    private static ServerSocket serverSocket;
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
        try {
            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Accepted");
                System.out.println("IP is: " + ((InetSocketAddress) clientSocket.getRemoteSocketAddress()).getAddress().toString().split("/")[1]);
                System.out.println("PORT is: " + clientSocket.getRemoteSocketAddress().toString().split(":")[1]);
                try {
                    serverList.add(new ServerSomething(clientSocket));
                } catch (IOException e) {
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
