package ru.dinz;

import java.io.*;
import java.net.*;
import java.util.Objects;

class ServerSomething extends Thread {

    private Socket socket;
    private BufferedWriter writer;
    private ObjectInputStream inObject;
    private BufferedReader reader;
    private ObjectOutputStream outObject;
    private BufferedReader readerSystem;
    private FileWriter writerFile;

    /**
     * для общения с клиентом необходим сокет (адресные данные)
     * @param socket
     * @throws IOException
     */

    public ServerSomething(Socket socket) {
        this.socket = socket;
        //Server.story.printStory(writer); // поток вывода передаётся для передачи истории последних 10
        // сообщений новому поключению
        start();
    }

    @Override
    public void run() {
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            inObject = new ObjectInputStream(socket.getInputStream());
            outObject = new ObjectOutputStream(socket.getOutputStream());
            writerFile = new FileWriter("notes.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        readerSystem = new BufferedReader(new InputStreamReader(System.in));
        Token token = null;
        try {
            Account account = (Account) inObject.readObject();
            token = new Token(account);
            if (Queue.getMap().containsKey(token)) {
                System.out.println(account);
                Queue.add(token);
                writer.write("");
                writer.newLine();
                writer.flush();
                writer.write("Account found!");
                writer.newLine();
                writer.flush();
                String recordRelevance = "";
                try {
                    while (true) {
                        String message = "";
                        //System.out.println(Queue.getPriorityQueue().peek());
                        if (Objects.equals(Queue.getPriorityQueue().peek(), token)) {
                            recordRelevance = "Write";
                            writer.write(recordRelevance);
                            writer.newLine();
                            writer.flush();
                            System.out.println("блокировка");
                            message = reader.readLine();
                            System.out.println("65 message - " + message);
                            writerFile.write(message + "\n");
                            writerFile.flush();
                            System.out.println("Echoing: " + message);
                            for (ServerSomething vr : Server.serverList) {
                                vr.send(message);
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
                    }
                } catch (NullPointerException ignored) {
                }
            } else {
                System.out.println("Account not found");
                writer.write("Account not found!");
                writer.newLine();
                writer.flush();
                closeService(token);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("93");
            this.closeService(token);
        }
    }

    /**
     * отсылка одного сообщения клиенту по указанному потоку
     * @param msg
     */
    private void send(String msg) {
        try {
            writer.write(msg + "\n");
            writer.flush();
        } catch (IOException ignored) {

        }
    }

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
            if(!socket.isClosed()) {
                socket.close();
                writer.close();
                reader.close();
                inObject.close();
                readerSystem.close();
                outObject.close();
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
