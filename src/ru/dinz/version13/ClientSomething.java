package ru.dinz.version13;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientSomething {

    private final String serverIp;
    private final short serverPort;
    private final Account account;
    private BufferedReader readerSystemConsole;
    private ObjectOutputStream outObject;
    private SocketChannel server;
    private SocketAddress socketAddress;
    private ByteBuffer buffer;

    ClientSomething(String serverIp, short serverPort, Account account) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.account = account;
        go();
    }

    public void go() {
        try {
            server = SocketChannel.open();
            socketAddress = new InetSocketAddress(serverIp, serverPort);
            server.connect(socketAddress);
            buffer = ByteBuffer.allocate(1024);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Socket failed");
        }
        readerSystemConsole = new BufferedReader(new InputStreamReader(System.in));
        if (addAccountToServer()) {
            new ReadMessage().start();
            new WriteMessage().start();
        } else {
            closeService();
        }
    }

    private boolean addAccountToServer() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            outObject = new ObjectOutputStream(byteArrayOutputStream);
            outObject.writeObject(account);
            outObject.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            server.write(ByteBuffer.wrap(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                byteArrayOutputStream.close();
            } catch (IOException ex) {

            }
        }
        return true;
        /*
        try {
            outObject.writeObject(account);
            outObject.flush();
            reader.readLine();
            String message = reader.readLine();
            System.out.println(message);
            return message.equals("Account found!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Account failed");
            return false;
        }
         */
    }

    class ReadMessage extends Thread {
        @Override
        public void run() {
            String message;
            try {
                while (true)  {
                    server.read(buffer);
                    buffer.flip();
                    System.out.println(new String(buffer.array(), buffer.position(), buffer.remaining()));
                }
            } catch (IOException e) {
                closeService();
            }
        }
    }

    class WriteMessage extends Thread {
        @Override
        public void run() {
            while (true) {
                String messageUser;
                try {
                    messageUser = readerSystemConsole.readLine();
                    server.write(ByteBuffer.wrap(messageUser.getBytes()));
                } catch (IOException e) {
                    closeService();
                }
            }
        }
    }

    private void closeService() {
        try {
            if (server.isOpen()) {
                readerSystemConsole.close();
                outObject.close();
            }
        } catch (IOException ignored) {}
    }
}
