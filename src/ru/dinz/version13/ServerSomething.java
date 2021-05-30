package ru.dinz.version13;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;

class ServerSomething extends Thread {

    private SocketChannel client;
    private Selector selector;
    private ByteBuffer buffer;
    private BufferedReader readerSystem;
    private FileWriter writerFile;

    public ServerSomething(SocketChannel socketChannel) {
        this.client = socketChannel;
        try {
            selector = Selector.open();
            client.configureBlocking(false);
            buffer = ByteBuffer.allocate(1024);
            client.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    private Account deserialization() {
        Object o = null;
        try {
            client.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer.flip();
        byte[] bytes = buffer.array();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try (ObjectInput objectInput = new ObjectInputStream(byteArrayInputStream)) {
            o = objectInput.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            buffer.clear();
        }
        return (Account) o;
    }

    @Override
    public void run() {
        try {
            writerFile = new FileWriter("notes.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        readerSystem = new BufferedReader(new InputStreamReader(System.in));
        Token token = null;
        try {
            Account account = deserialization();
            token = new Token(account);
            if (Queue.getMap().containsKey(token)) {
                System.out.println(account);
                Queue.add(token);
                String recordRelevance = "";
                try {
                    while (true) {
                        int select = selector.select();
                        if (select == 0) {
                            continue;
                        }
                        Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                        while (selectionKeyIterator.hasNext()) {
                            SelectionKey next = selectionKeyIterator.next();
                            if (Objects.equals(Queue.getPriorityQueue().peek(), token)) {
                                try {
                                    ((SocketChannel) next.channel()).read(buffer);
                                    buffer.flip();
                                    System.out.println(new String(buffer.array(), buffer.position(), buffer.remaining()));
                                    buffer.clear();
                                } catch (IOException e) {

                                } finally {
                                    selectionKeyIterator.remove();
                                }
                            }
                        }
/*
                        String message = "";
                        if (Objects.equals(Queue.getPriorityQueue().peek(), token)) {
                            recordRelevance = "Write";
                            writer.write(recordRelevance);
                            writer.newLine();
                            writer.flush();
                            System.out.println("блокировка");
                            message = reader.readLine();
                            if (!"".equals(message)) {
                                System.out.println("65 message - " + message);
                                writerFile.write(message + "\n");
                                writerFile.flush();
                                System.out.println("Echoing: " + message);
                                for (ServerSomething vr : Server.serverList) {
                                    vr.send(message);
                                }
                            }
                        } else if (!"Expect".equals(recordRelevance)) {
                            recordRelevance = "Expect";
                            writer.write(recordRelevance);
                            writer.newLine();
                            writer.flush();
                        }
                        if ("exit".equals(message)) {
                            System.out.println("73 строка");
                            closeService(token);
                        }
                        */
                    }
                } catch (NullPointerException ignored) {
                }
            } else {
                System.out.println("Account not found");
                client.write(ByteBuffer.wrap("Account not found!".getBytes()));
                closeService(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("93");
            this.closeService(token);
        }
    }

    /**
     * отсылка одного сообщения клиенту по указанному потоку
     * @param msg
     */
    /*
    private void send(String msg) {
        try {
            writer.write(msg + "\n");
            writer.flush();
        } catch (IOException ignored) {

        }
    }*/

    /**
     * закрытие сервера
     * прерывание себя как нити и удаление из списка нитей
     */
    private void closeService(Token token) {
        try {
            System.out.println("114 строка token - " + token);
            if (token != null) {
                Queue.remove(token);
                System.out.println(Queue.getPriorityQueue() + " 119");
            }
            if(client.isOpen()) {
                //socket.close();
                //writer.close();
                //reader.close();
                //inObject.close();
                readerSystem.close();
                //outObject.close();
                for (ServerSomething serverSomething : Server.serverList) {
                    if(serverSomething.equals(this)) {
                        serverSomething.interrupt();
                    }
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {

        }
    }
}
