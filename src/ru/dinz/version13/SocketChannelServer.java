package ru.dinz.version13;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class SocketChannelServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress(9000));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            int select = selector.select();
            if (select == 0) {
                continue;
            }
            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey next = selectionKeyIterator.next();
                try {
                    if (next.channel() == serverSocket) {
                        SocketChannel client = serverSocket.accept();
                        client.configureBlocking(false);
                        System.out.println("Connection Set: " + client.getRemoteAddress());
                        client.register(selector, SelectionKey.OP_READ);
                    } else {
                        ((SocketChannel) next.channel()).read(buffer);
                        buffer.flip();
                        System.out.println(new String(buffer.array(), buffer.position(), buffer.remaining()));
                        buffer.clear();
                    }
                } catch (IOException e) {

                } finally {
                    selectionKeyIterator.remove();
                }
            }
        }
    }
}