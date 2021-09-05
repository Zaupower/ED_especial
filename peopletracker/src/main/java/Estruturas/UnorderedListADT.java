package main.java.Estruturas;

public interface UnorderedListADT<T> extends ListADT<T> {

    /**
     * Adds an element to the front of the list
     *
     * @param element the element to be added to this list
     */
    public void addToFront(T element);

    /**
     * Adds an element to the rear of the list
     *
     * @param element the element to be added to this list
     */
    public void addToRear(T element);

    /**
     * Adds an element after a particular element already in the list
     *
     * @param element the element to be added to this list
     */
    public void addAfter(T element, T target) throws NotFoundException;
}
