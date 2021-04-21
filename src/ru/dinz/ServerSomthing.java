package ru.dinz;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

class ServerSomthing extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом,
    // кроме него - клиент и сервер никак не связаны
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток завписи в сокет

    /**
     * для общения с клиентом необходим сокет (адресные данные)
     * @param socket
     * @throws IOException
     */

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        // если потоку ввода/вывода приведут к генерированию искдючения, оно проброситься дальше
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Server.story.printStory(out); // поток вывода передаётся для передачи истории последних 10
        // сооюбщений новому поключению
        start(); // вызываем run()
    }
    @Override
    public void run() {
        String word;
        try {
            // первое сообщение отправленное сюда - это никнейм
            word = in.readLine();
            try {
                out.write(word + "\n");
                out.flush(); // flush() нужен для выталкивания оставшихся данных
                // если такие есть, и очистки потока для дьнейших нужд
            } catch (IOException ignored) {}
            try {
                while (true) {
                    word = in.readLine();
                    if(word.equals("stop")) {
                        this.downService(); // харакири
                        break; // если пришла пустая строка - выходим из цикла прослушки
                    }
                    System.out.println("Echoing: " + word);
                    Server.story.addStoryEl(word);
                    for (ServerSomthing vr : Server.serverList) {
                        vr.send(word); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                    }
                }
            } catch (NullPointerException ignored) {}


        } catch (IOException e) {
            this.downService();
        }
    }

    /**
     * отсылка одного сообщения клиенту по указанному потоку
     * @param msg
     */
    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
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
                out.close();
                for (ServerSomthing vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}
