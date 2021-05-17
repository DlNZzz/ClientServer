package ru.dinz;

import java.io.*;
import java.net.*;

class ServerSomething extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter writer; // поток записи в сокет
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

    public ServerSomething(Socket socket) throws IOException, ClassNotFoundException, InterruptedException {
        this.socket = socket;
        // если потоку ввода/вывода приведут к генерированию искдючения, оно проброситься дальше
        //Server.story.printStory(writer); // поток вывода передаётся для передачи истории последних 10
        // сооюбщений новому поключению
        start(); // вызываем run()
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
            System.out.println(account);
            token = new Token(account);
            Queue.add(token);
            writer.write(account.getName() + " connection!");
            writer.newLine();
            writer.flush();
            //try {
                //writer.write(word + "\n");
                //writer.flush(); // flush() нужен для выталкивания оставшихся данных
                // если такие есть, и очистки потока для дальнейших нужд
            //} catch (IOException ignored) {}
            try {
                while (true) {
                    String message = "";
                    if (Queue.getPriorityQueue().peek().equals(token)) {
                        writer.write("Write");
                        writer.newLine();
                        writer.flush();
                        message = reader.readLine();
                        writerFile.write(message + "\n");
                        writerFile.flush();
                    } else {
                        writer.write("Expect");
                        writer.newLine();
                        writer.flush();
                        message = reader.readLine();
                    }
                    if (message.equals("exit")) {
                        closeService(token);
                    }
                    System.out.println("Echoing: " + message);

                    Server.story.addStoryEl(message);
                    for (ServerSomething vr : Server.serverList) {
                        vr.send(message); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                    }

                }
            } catch (NullPointerException ignored) {

            }
        } catch (IOException | ClassNotFoundException e) {
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
            if (token != null) {
                Queue.remove(token);
            }
            if(!socket.isClosed()) {
                socket.close();
                in.close();
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
