package ru.dinz;

import java.io.*;

public class Client {

    private static final String serverIp;
    private static final short serverPort;
    private static final BufferedReader readerSystem;

    static {
        serverIp = "127.0.0.1";
        serverPort = 4729;
        readerSystem = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) {
        Client client = new Client();
        Account account = client.createAccount();
        new ClientSomething(serverIp, serverPort, account);
    }

    private Account createAccount() {
        System.out.print("Client start!\nEnter name: ");
        String name = null, password = null;
        try {
            name = readerSystem.readLine();
            System.out.print("Enter password: ");
            password = readerSystem.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Account(name, password);
    }
}
