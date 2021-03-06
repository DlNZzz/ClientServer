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
            System.out.println("account" + account);
            selector = Selector.open();
            client.register(selector, SelectionKey.OP_READ);
            token = new Token(account);
            if (Queue.getMap().containsKey(token)) {
                System.out.println(account);
                Queue.add(token);
                String recordRelevance = "";
                try {
                    while (true) {
                        System.out.println(selector + " select51");
                        int select = selector.select();
                        System.out.println(select);
                        System.out.println(selector + " select53");
                        if (select == 0) {
                            continue;
                        }
                        System.out.println("selector2");
                        Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                        while (selectionKeyIterator.hasNext()) {
                            SelectionKey next = selectionKeyIterator.next();
                            if (Objects.equals(Queue.getPriorityQueue().peek(), token)) {
                                try {
                                    int numRead = ((SocketChannel) next.channel()).read(buffer);

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
                            System.out.println("????????????????????");
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
                            System.out.println("73 ????????????");
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

    private Account deserialization() {
        while (true) {
            int select = 0;
            System.out.println(selector + " select");
            try {
                select = selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (select == 0) {
                continue;
            }
            System.out.println("deser");
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
                try {
                    byteArrayInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("return");
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(selector + " select2");
            return (Account) o;
        }
    }

    /**
     * ?????????????? ???????????? ?????????????????? ?????????????? ???? ???????????????????? ????????????
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
     * ???????????????? ??????????????
     * ???????????????????? ???????? ?????? ???????? ?? ???????????????? ???? ???????????? ??????????
     */
    private void closeService(Token token) {
        try {
            System.out.println("114 ???????????? token - " + token);
            if (token != null) {
                Queue.remove(token);
                System.out.println(Queue.getPriorityQueue() + " 119");
            }
            if(client.isOpen()) {
                readerSystem.close();
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
