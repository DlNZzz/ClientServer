package ru.dinz.version2;

public class Del {
    public static void main(String[] args) {
        System.out.println(new Token(new Account("11", "2")));
        System.out.println(new Token(new Account("1", "1")));
        System.out.println(new Token(new Account("13", "1")));
        System.out.println(new Token(new Account("132", "1")));
        System.out.println("----------------------");

        Queue.add(new Token(new Account("11", "2")));
        Queue.add(new Token(new Account("1", "1")));
        Queue.add(new Token(new Account("13", "1")));
        Queue.add(new Token(new Account("132", "1")));

        for (Token t : Queue.getPriorityQueue()) {
            System.out.println(t);
        }
        System.out.println("----------------------");

        Queue.remove(new Token(new Account("1", "1")));
        System.out.println(Queue.getPriorityQueue().peek());
    }
}
