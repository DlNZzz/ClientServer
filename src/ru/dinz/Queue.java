package ru.dinz;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Queue {
    private static Map<Token, Integer> mapDataBase = Map.of(
            new Token(new Account("1", "1")), 1,
            new Token(new Account("11", "1")), 2,
            new Token(new Account("13", "1")), 3,
            new Token(new Account("133", "1")), 12
    );
    //private static PriorityBlockingQueue priorityBlockingQueue = new PriorityBlockingQueue();
    private static PriorityBlockingQueue<Token> priorityQueue;

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

    public static Map<Token, Integer> getMap() {
        return mapDataBase;
    }
}
/*
@Override
    public int compareTo(Token o) {
        Map<Token, Integer> mapDataBase = Queue.getMap();
        int priority1 = -1, priority2 = -1;
        if (mapDataBase.containsKey(this)) {
            priority1 = mapDataBase.get(this);
        }
        if (mapDataBase.containsKey(o)) {
            priority2 = mapDataBase.get(o);
        }
        return priority1 - priority2;
    }
 */

/*
@Override
    public int compareTo(Token o) {
        Map<Token, Integer> mapDataBase = Queue.getMap();
        int priority1 = -1, priority2 = -1;
        if (mapDataBase.containsKey(this)) {
            mapDataBase.get(this);
        }
        for (Map.Entry<Token, Integer> entry : mapDataBase.entrySet()) {
            Token token = entry.getKey();
            if (token.equals(o)) {
                priority2 = entry.getValue();
            }
        }
        return priority2 - priority1;
    }
 */