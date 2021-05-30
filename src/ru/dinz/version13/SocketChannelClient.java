package ru.dinz.version13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelClient {
    public static void main(String[] args) throws IOException {
        SocketChannel server = SocketChannel.open();
        SocketAddress socketAddr = new InetSocketAddress("localhost", 9001);
        server.connect(socketAddr);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            buffer.put(reader.readLine().getBytes());
            buffer.flip();
            server.write(buffer);
            buffer.clear();
        }
    }
}