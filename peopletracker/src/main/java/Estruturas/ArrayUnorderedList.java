package main.java.Estruturas;

public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {

    @Override
    public void addToFront(T element) {
        if (this.size() == this.list.length) {
            this.expandCapacity();
        }

        for (int i = this.size(); i > 0; i--) {
            this.list[i] = this.list[i - 1];
        }

        this.rear++;
        this.list[0] = element;
        this.modCount++;
    }

    @Override
    public void addToRear(T element) {
        if (this.size() == this.list.length) {
            super.expandCapacity();
        }

        this.list[this.rear] = element;
        this.rear++;
        this.modCount++;
    }

    @Override
    public void addAfter(T element, T target) throws NotFoundException {
        try {
            int pos = this.find(target);

            if (this.size() == this.list.length) {
                super.expandCapacity();
            }

            pos++;

            for (int i = this.size(); i > pos; i--) {
                this.list[i] = this.list[i - 1];
            }

            this.list[pos] = element;
            this.rear++;
            this.modCount++;
        } catch (NotFoundException e) {
            throw e;
        }
    }
}