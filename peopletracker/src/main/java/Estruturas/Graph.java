package main.java.Estruturas;

import java.util.Iterator;

public class Graph<T> implements GraphADT<T> {
    protected final int DEFAULT_CAPACITY = 10;
    protected int numVertices;
    protected boolean[][] adjMatrix;
    public T[] vertices;

    public Graph(int capacity) {
        this.numVertices = 0;
        this.adjMatrix = new boolean[capacity][capacity];
        this.vertices = (T[]) (new Object[capacity]);
    }

    public void setAdjMatrix(boolean[][] adjMatrix) {
        this.adjMatrix = adjMatrix;
    }

    public String toString(){

        String s = "";
        String result = "";

        for(int i = 0 ; i < this.size() ; i++){
            s += vertices[i].toString() + "\n";
        }

        result += "Adjacency Matrix\n";
        result += "----------------\n";

        result += "    ";
        for (int i = 0; i < numVertices; i++)
        {
            result += "" + i;
            if (i < 10)
                result += " ";
        }
        result += "\n\n";

        for (int i = 0; i < numVertices; i++)
        {
            result += "" + i + "\t";

            for (int j = 0; j < numVertices; j++)
            {
                if (adjMatrix[i][j])
                    result += "1 ";
                else
                    result += "0 ";
            }
            result += "\n";
        }


        return result;
    }

    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    @Override
    public Iterator iteratorBFS(T startVertex) throws EmptyException {
        return iteratorBFS(this.getIndex(startVertex));
    }

    @Override
    public Iterator iteratorDFS(T startVertex) throws EmptyCollectionException {
        return iteratorDFS(this.getIndex(startVertex));
    }

    public void removeEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            this.adjMatrix[index1][index2] = false;
            this.adjMatrix[index2][index1] = false;
        }
    }

    public Iterator iteratorBFS(int startIndex) throws EmptyException {
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();

        if (!indexIsValid(startIndex)) {
            return resultList.iterator();
        }

        boolean[] visited = new boolean[this.numVertices];
        for (int i = 0; i < this.numVertices; i++)
            visited[i] = false;

        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        while (!traversalQueue.isEmpty()) {
            x = traversalQueue.dequeue();
            resultList.addToRear(this.vertices[x]);

            for (int i = 0; i < this.numVertices; i++) {
                if (this.adjMatrix[x][i] && !visited[i]) {
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }

        return resultList.iterator();
    }

    public Iterator iteratorDFS(int startIndex) throws EmptyCollectionException {
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        boolean[] visited = new boolean[this.numVertices];

        if (!indexIsValid(startIndex)) {
            return resultList.iterator();
        }

        for (int i = 0; i < this.numVertices; i++) {
            visited[i] = false;
        }

        traversalStack.push(startIndex);
        resultList.addToRear(this.vertices[startIndex]);
        visited[startIndex] = true;

        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;

            for (int i = 0; (i < this.numVertices) && !found; i++) {
                if (this.adjMatrix[x][i] && !visited[i]) {
                    traversalStack.push(i);
                    resultList.addToRear(this.vertices[i]);
                    visited[i] = true;
                    found = true;
                }
            }

            if (!found && !traversalStack.isEmpty())
                traversalStack.pop();
        }
        return resultList.iterator();
    }

    private Iterator<Integer> iteratorShortestPathIndices(int startIndex, int targetIndex) throws EmptyException, EmptyCollectionException {
        int index = startIndex;
        int[] pathLength = new int[this.numVertices];
        int[] predecessor = new int[this.numVertices];
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<>();

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex) || (startIndex == targetIndex)) {
            return resultList.iterator();
        }

        boolean[] visited = new boolean[this.numVertices];

        for (int i = 0; i < this.numVertices; i++) {
            visited[i] = false;
        }

        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;
        pathLength[startIndex] = 0;
        predecessor[startIndex] = -1;

        while (!traversalQueue.isEmpty() && (index != targetIndex)) {
            index = traversalQueue.dequeue();

            for (int i = 0; i < this.numVertices; i++) {
                if (this.adjMatrix[index][i] && !visited[i]) {
                    pathLength[i] = pathLength[index] + 1;
                    predecessor[i] = index;
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }

        if (index != targetIndex)
            return resultList.iterator();

        LinkedStack<Integer> stack = new LinkedStack<>();
        index = targetIndex;
        stack.push(index);

        do {
            index = predecessor[index];
            stack.push(index);
        } while (index != startIndex);

        while (!stack.isEmpty()) {
            resultList.addToRear(stack.pop());
        }

        return resultList.iterator();
    }

    public Iterator<T> iteratorShortestPath(int startIndex, int targetIndex) throws EmptyException, EmptyCollectionException {

        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex))
            return resultList.iterator();

        Iterator<Integer> it = iteratorShortestPathIndices(startIndex,
                targetIndex);
        while (it.hasNext())
            resultList.addToRear(this.vertices[it.next()]);
        return resultList.iterator();
    }

    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) throws EmptyException, EmptyCollectionException {
        return iteratorShortestPath(getIndex(startVertex), getIndex(targetVertex));
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean isConnected() throws EmptyException {
        if (isEmpty()) {
            return false;
        }

        Iterator<T> it = iteratorBFS(0);
        int count = 0;

        while (it.hasNext()) {
            it.next();
            count++;
        }

        return count == this.numVertices;
    }

    @Override
    public int size() {
        return this.numVertices;
    }

    public void addEdge(int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            this.adjMatrix[index1][index2] = true;
            this.adjMatrix[index2][index1] = true;
        }
    }

    public void addVertex(T vertex) {
        if (this.numVertices == this.vertices.length) {
            expandCapacity();
        }

        this.vertices[this.numVertices] = vertex;

        for (int i = 0; i <= this.numVertices; i++) {
            this.adjMatrix[this.numVertices][i] = false;
            this.adjMatrix[i][this.numVertices] = false;
        }

        this.numVertices++;
    }

    @Override
    public void removeVertex(T vertex) {
        for (int i = 0; i < this.numVertices; i++) {
            if (vertex.equals(this.vertices[i])) {
                removeVertex(i);
            }
        }
    }

    public void removeVertex(int index) {
        if (indexIsValid(index)) {
            this.numVertices--;

            for (int i = index; i < this.numVertices; i++) {
                this.vertices[i] = this.vertices[i + 1];
            }

            for (int i = index; i < this.numVertices; i++) {
                for (int j = 0; j <= this.numVertices; j++) {
                    this.adjMatrix[i][j] = this.adjMatrix[i + 1][j];
                }
            }

            for (int i = index; i < this.numVertices; i++) {
                for (int j = 0; j < this.numVertices; j++) {
                    this.adjMatrix[j][i] = this.adjMatrix[j][i + 1];
                }
            }
        }
    }

    protected int getIndex(T vertex) {
        for (int i = 0; i < this.vertices.length; i++) {
            if (this.vertices[i].equals(vertex)) {
                return i;
            }
        }

        return -1;
    }

    protected boolean indexIsValid(int index) {
        return index != -1;
    }

    private void expandCapacity() {
        T[] verticesTmp = ((T[]) new Object[this.vertices.length * 2]);

        boolean[][] adjMatrixTmp = new boolean[this.vertices.length * 2][this.vertices.length * 2];

        for (int i = 0; i < this.vertices.length; i++) {
            for (int j = 0; j < this.vertices.length; j++) {
                adjMatrixTmp[i][j] = this.adjMatrix[i][j];
            }
            verticesTmp[i] = this.vertices[i];
        }

        this.vertices = verticesTmp;
        this.adjMatrix = adjMatrixTmp;
    }

    public T getVertex(int num){
        return this.vertices[num];
    }
}
