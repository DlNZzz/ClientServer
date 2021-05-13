package ru.dinz;

import java.io.*;
import java.net.*;

class ServerSomething extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter writer; // поток записи в сокет
    private ObjectInputStream inObject;
    private static ObjectOutputStream outObject;
    private static BufferedReader readerSystem;

    /**
     * для общения с клиентом необходим сокет (адресные данные)
     * @param socket
     * @throws IOException
     */

    public ServerSomething(Socket socket) throws IOException, ClassNotFoundException, InterruptedException {
        this.socket = socket;
        // если потоку ввода/вывода приведут к генерированию искдючения, оно проброситься дальше
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        inObject = new ObjectInputStream(socket.getInputStream());
        outObject = new ObjectOutputStream(socket.getOutputStream());
        readerSystem = new BufferedReader(new InputStreamReader(System.in));
        Server.story.printStory(writer); // поток вывода передаётся для передачи истории последних 10
        // сооюбщений новому поключению
        start(); // вызываем run()
    }
    @Override
    public void run() {
        String word;
        try {
            // первое сообщение отправленное сюда - это никнейм
            //word = in.readLine();
            Account account = (Account) inObject.readObject();
            System.out.println(account);
            Token token = new Token(account);
            Queue.add(token);

            //outObject.writeObject(token);
            //outObject.flush();

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
                    //!!!
                    //Поставить условие проверки очередии
                    boolean isEmpty = false;
                    if (Queue.getPriorityQueue().peek().equals(token)) {
                        isEmpty = true;
                    }
                    if (isEmpty) {
                        writer.write("Write");
                        writer.newLine();
                        writer.flush();
                        String message = readerSystem.readLine();
                        System.out.println(message);
                    }
                    /*
                    word = in.readLine();
                    if(word.equals("exit")) {
                        this.downService();
                        break;
                    }
                    System.out.println("Echoing: " + word);
                    Server.story.addStoryEl(word);
                    for (ServerSomething vr : Server.serverList) {
                        vr.send(word); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                    }
                    */
                }
            } catch (NullPointerException ignored) {

            }
        } catch (IOException | ClassNotFoundException e) {
            this.downService();
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
        } catch (IOException ignored) {}

    }

    /**
     * закрытие сервера
     * прерывание себя как нити и удаление из списка нитей
     */
    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                writer.close();
                for (ServerSomething vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}
