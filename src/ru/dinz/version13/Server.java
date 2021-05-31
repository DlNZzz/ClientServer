package ru.dinz.version13;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/*
1 все клиенты должны видеть записанное
2 подключаться могут только те кто в списке
3 сервер должен в реальном времени оповещать статус клиента
 */
public class Server {

    public static final int PORT = 4740;
    private static SocketChannel socketChannel;
    private static ServerSocketChannel serverSocketChannel;
    /**
     * список всех нитей - экземпляров
     */
    public static List<ServerSomething> serverList = new ArrayList<>();

    public static void main(String[] args) {
        new Server().go();
    }

    public void go() {
        System.out.println("Server Started");
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            while (true) {
                socketChannel = serverSocketChannel.accept();
                System.out.println("Connection Set: " + socketChannel.getRemoteAddress());
                System.out.println("IP is: " + ((InetSocketAddress)
                        socketChannel.getRemoteAddress()).getAddress().toString().split("/")[1]);
                System.out.println("PORT is: "
                        + socketChannel.getRemoteAddress().toString().split(":")[1]);
                serverList.add(new ServerSomething(socketChannel));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}