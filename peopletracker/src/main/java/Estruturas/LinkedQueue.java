package main.java.Estruturas;

public class LinkedQueue<T> implements QueueADT<T> {

    private Node<T> front, rear;
    private T element;
    private int count;

    public LinkedQueue() {
        this.front = this.rear = null;
        this.element = null;
        this.count = 0;
    }

    @Override
    public void enqueue(T element) {
        Node<T> temp = new Node<T>(element);

        if (size() == 0) {
            this.front = temp;
            this.rear = temp;
            this.count++;
        } else {
            this.rear.setNext(temp);
            this.rear = temp;
            this.count++;
        }
    }

    @Override
    public T dequeue() throws EmptyException {
        if (isEmpty()) {
            throw new EmptyException("empty collection");
        }

        T temp = this.front.getElement();

        this.front = this.front.getNext();
        this.count--;

        return temp;
    }

    @Override
    public T first() {
        return this.front.getElement();
    }

    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public String toString() {
        String s = "";

        Node<T> current = this.front;

        while (current != null) {
            s += current.toString() + "\n";
            current = current.getNext();
        }

        return s;
    }
}