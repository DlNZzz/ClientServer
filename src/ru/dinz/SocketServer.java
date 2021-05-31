package ru.dinz;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SocketServer {
    private Selector selector;
    private InetSocketAddress address;
    private Set<SocketChannel> session;

    public SocketServer(String host, int port) {
        this.address = new InetSocketAddress(host, port);
        this.session = new HashSet<SocketChannel>();
    }

    public static void main(String[] args) throws IOException {
        new SocketServer("localhost", 8001).go();
    }

    public void go() throws IOException {
        this.selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(address);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server started...");
        while(true) {
            this.selector.select();
            Iterator keys = this.selector.selectedKeys().iterator();
            while(keys.hasNext()) {
                SelectionKey key = (SelectionKey) keys.next();
                keys.remove();
                if (!key.isValid()) {
                    continue;
                }
                if (key.isAcceptable()) {
                    accept(key);
                }
                else if (key.isReadable()) {
                    read(key);
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_READ);
        this.session.add(channel);
        broadcast("System:user new: " + channel.socket().getRemoteSocketAddress() + "\n");
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int numRead = channel.read(byteBuffer);
        if (numRead == -1) {
            this.session.remove(channel);
            broadcast("System:user left: " + channel.socket().getRemoteSocketAddress() + "\n");
            channel.close();
            key.cancel();
            return;
        }
        byte[] data = new byte[numRead];
        System.arraycopy(byteBuffer.array(), 0, data, 0, numRead);
        String gotData = new String(data);
        System.out.println("Got: " + gotData);
        broadcast(channel.socket().getRemoteSocketAddress() + ": " + gotData);
    }

    // SSL TSL
    private void broadcast(String data) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(data.getBytes());
        byteBuffer.flip();
        this.session.forEach(socketChannel -> {
            try {
                socketChannel.write(byteBuffer);
                byteBuffer.flip();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}