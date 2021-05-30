package ru.dinz;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Queue {
    private static Map<Token, Integer> mapDataBase = Map.of(
            new Token(new Account("Admin", "1")), 1,
            new Token(new Account("2", "2")), 2,
            new Token(new Account("3", "3")), 3,
            new Token(new Account("4", "4")), 12
    );
    private volatile static BlockingQueue<Token> priorityQueue = new PriorityBlockingQueue<>();

    public static Map<Token, Integer> getMap() {
        return mapDataBase;
    }

    public synchronized static BlockingQueue<Token> getPriorityQueue() {
        return priorityQueue;
    }

    public static void add(Token token) {
        priorityQueue.add(token);
    }

    public static Token peek() {
        return priorityQueue.peek();
    }

    public static Token poll() {
        return priorityQueue.poll();
    }

    public static void remove(Token token) {
        priorityQueue.remove(token);
    }

    @Override
    public String toString() {
        return "Queue{}";
    }
}

/*
public static void main(String[] args) {
        priorityQueue = new PriorityBlockingQueue<>();
        priorityQueue.add(new Token(new Account("1", "1")));
        priorityQueue.add(new Token(new Account("111", "1")));
        priorityQueue.add(new Token(new Account("13", "1")));
        priorityQueue.add(new Token(new Account("133", "1")));

        System.out.println(mapDataBase.get(priorityQueue.poll()));
        System.out.println(mapDataBase.get(priorityQueue.poll()));
        System.out.println(mapDataBase.get(priorityQueue.poll()));
        if (mapDataBase.get(priorityQueue.peek()) == null) {
            System.out.println(-1);
        } else {
            System.out.println(mapDataBase.get(priorityQueue.poll()));
        }
        //priorityQueue.peek();
    }
 */
