package main.java.Estruturas;

public class ArrayOrderedList<T> extends ArrayList<T> implements OrderedListADT<T> {

    public ArrayOrderedList() {
        super();
    }

    public ArrayOrderedList(int size) {
        super(size);
    }

    @Override
    public void add(T element) throws NoComparableException {
        if (!(element instanceof Comparable)) {
            throw new NoComparableException("not comparable");
        }

        Comparable<T> obj = (Comparable<T>) element;

        if (this.isEmpty()) {
            this.list[0] = element;
            this.rear++;
        } else {

            if (this.size() == this.list.length) {
                super.expandCapacity();
            }

            int i = 0;

            while (i < this.rear && obj.compareTo(list[i]) > 0) {
                i++;
            }

            for (int j = this.rear; j > i; j--) {

                this.list[j] = this.list[j - 1];

            }

            this.list[i] = element;
            this.rear++;
            this.modCount++;
        }
    }
}
