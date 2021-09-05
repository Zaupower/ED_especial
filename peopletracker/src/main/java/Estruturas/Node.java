package main.java.Estruturas;

public class Node<T> {

    private Node next;
    private T element;

    public Node(T element) {
        this.next = null;
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "Node{" +
                "element=" + element +
                '}';
    }
}
