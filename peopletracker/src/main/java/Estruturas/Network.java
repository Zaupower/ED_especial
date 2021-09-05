package main.java.Estruturas;

import java.util.Iterator;

public class Network<T> extends Graph<T> implements NetworkADT<T> {

    protected double[][] weightMatrix;
    private static final int NO_PARENT = -1;
    private ArrayUnorderedList<Integer> optimalPath = new ArrayUnorderedList<>();

    public Network(int size) {
        super(size);
        this.weightMatrix = new double[size][size];
    }

    @Override
    public String toString() {

        String s = "";
        String result = "";

        for (int i = 0; i < this.size(); i++) {
            s += vertices[i].toString() + "\n";
        }

        result += "Adjacency Matrix\n";
        result += "----------------\n";

        result += "    ";
        for (int i = 0; i < numVertices; i++) {
            result += "" + i;
            if (i < 10)
                result += " ";
        }
        result += "\n\n";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i + "\t";

            for (int j = 0; j < numVertices; j++) {
                result += this.weightMatrix[i][j] + "\t";
            }
            result += "\n";
        }


        return result;
    }

    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {

        if (weight < 0.0) {
            throw new IllegalArgumentException("weight must be higher than 0");
        }
        super.addEdge(vertex1, vertex2);
        this.setEdgeWeight(vertex1, weight, vertex2);
    }

    @Override
    public double shortestPathWeight(T vertex1, T vertex2) throws InvalidIndexException, EmptyException {
        return 0;
    }

    public void setEdgeWeight(T v1, double weight, T v2) {
        if (weight < 0.0) {
            throw new IllegalArgumentException("weight must be higher than 0");
        }

        int posv1 = super.getIndex(v1);
        int posv2 = super.getIndex(v2);

        this.weightMatrix[posv1][posv2] = weight;
        this.weightMatrix[posv2][posv1] = weight;
    }

    /*
    @Override
    public double shortestPathWeight(T vertex1, T vertex2) throws InvalidIndexException, EmptyException {
        int index1 = super.getIndex(vertex1);
        int index2 = super.getIndex(vertex2);

        if (!super.indexIsValid(index1) || !super.indexIsValid(index2)) {
            throw new InvalidIndexException("Invalid Index");
        }

        Iterator<T> iterator = this.MyDijkstra(index1, index2);
        System.out.println("shortestPathWeight");
        int oldIndex = 0;
        int newIndex;
        int sum = 0;

        while (iterator.hasNext()) {
            newIndex = getIndex(iterator.next());
            sum += this.weightMatrix[oldIndex][newIndex];
            oldIndex = newIndex;
        }

        return sum;
    }
     */


    public ArrayUnorderedList<ArrayUnorderedList<Integer>> getShortestPath(T vertex1, T vertex2) {
        int index1 = super.getIndex(vertex1);
        int index2 = super.getIndex(vertex2);

        try {
            return this.MyDijkstra(index1, index2);
        } catch (InvalidIndexException e) {
            System.out.println(e);
            return null;
        }
    }

    private ArrayUnorderedList<ArrayUnorderedList<Integer>> MyDijkstra(int startIndex, int endIndex) throws  InvalidIndexException {
        this.optimalPath = new ArrayUnorderedList<>();
        if (!super.indexIsValid(startIndex) ) {
            throw new InvalidIndexException("Invalid Index");
        }

        int nVertices = this.numVertices;

        // shortestDistances[i] will hold the
        // shortest distance from src to i
        int[] shortestDistances = new int[nVertices];

        // added[i] will true if vertex i is
        // included / in shortest path tree
        // or shortest distance from src to
        // i is finalized
        boolean[] added = new boolean[nVertices];

        // Initialize all distances as
        // INFINITE and added[] as false
        for (int vertexIndex = 0; vertexIndex < nVertices;
             vertexIndex++) {
            shortestDistances[vertexIndex] = Integer.MAX_VALUE;
            added[vertexIndex] = false;
        }

        // Distance of source vertex from
        // itself is always 0
        shortestDistances[startIndex] = 0;

        // Parent array to store shortest
        // path tree
        int[] parents = new int[nVertices];

        // The starting vertex does not
        // have a parent
        parents[startIndex] = NO_PARENT;

        // Find shortest path for all
        // vertices
        for (int i = 1; i < nVertices; i++) {

            // Pick the minimum distance vertex
            // from the set of vertices not yet
            // processed. nearestVertex is
            // always equal to startNode in
            // first iteration.
            int nearestVertex = -1;
            int shortestDistance = Integer.MAX_VALUE;
            for (int vertexIndex = 0;
                 vertexIndex < nVertices;
                 vertexIndex++) {
                if (!added[vertexIndex] &&
                        shortestDistances[vertexIndex] <
                                shortestDistance) {
                    nearestVertex = vertexIndex;
                    shortestDistance = shortestDistances[vertexIndex];
                }
            }

            // Mark the picked vertex as
            // processed
            if (nearestVertex> -1){
                added[nearestVertex] = true;

                // Update dist value of the
                // adjacent vertices of the
                // picked vertex.
                for (int vertexIndex = 0;
                     vertexIndex < nVertices;
                     vertexIndex++) {
                    double edgeDistance = this.weightMatrix[nearestVertex][vertexIndex];

                    if (edgeDistance > 0
                            && ((shortestDistance + edgeDistance) <
                            shortestDistances[vertexIndex])) {
                        parents[vertexIndex] = nearestVertex;
                        shortestDistances[vertexIndex] = (int) (shortestDistance +
                                edgeDistance);
                    }
                }
            }

        }

        ArrayUnorderedList<ArrayUnorderedList<Integer>> allPathsList =  printSolution(startIndex, shortestDistances, parents);

        return allPathsList;
    }

    private Iterator<T> getIteratorList(int startVertex,
                                        int[] distances,
                                        int[] parents,int endVertex){
        ArrayUnorderedList<T> pathList = new ArrayUnorderedList<>();

        int nVertices = distances.length;

        for (int vertexIndex = 0; vertexIndex < nVertices; vertexIndex++) {
            if (vertexIndex == endVertex) {
                getPathChild(vertexIndex, parents);
            }
        }

            for (int i = 0; i < optimalPath.size(); i++){
                pathList.addToFront(vertices[optimalPath.getIndex(i)]);
            }
        return pathList.iterator();
    }

    /**
     * Creata an ArrayList of ArrayLists of the indexs of the de best paths given an Start point index
     *
     * @param startVertex
     * @param distances
     * @param parents
     */

    private ArrayUnorderedList<ArrayUnorderedList<Integer>> allPaths = new ArrayUnorderedList<>();

    private ArrayUnorderedList<ArrayUnorderedList<Integer>>  printSolution(int startVertex,
                                      int[] distances,
                                      int[] parents) {
        int nVertices = distances.length;
        //System.out.print("Vertex\t Distance\tPath");

        for (int vertexIndex = 0;
             vertexIndex < nVertices;
             vertexIndex++) {
            if (vertexIndex != startVertex) {
                //System.out.print("\n" + startVertex + " -> ");
                //System.out.print(vertexIndex + " \t\t ");
                //System.out.print(distances[vertexIndex] + "\t\t");
                if (!pathTMP.isEmpty()){

                    allPaths.addToRear(pathTMP);
                    pathTMP = new ArrayUnorderedList<>();
                }
                printPath(vertexIndex, parents);
            }
        }
        return allPaths;
    }

    private void getPathChild(int currentVertex,
                                    int[] parents) {

        // Base case : Source node has
        // been processed
        if (currentVertex == NO_PARENT) {
            return;
        }else {
            getPathChild(parents[currentVertex], parents);
            optimalPath.addToFront(currentVertex);
        }

    }
    // Function to print shortest path
    // from source to currentVertex
    // using parents array
    private ArrayUnorderedList<Integer> pathTMP = new ArrayUnorderedList<>();
    private void printPath(int currentVertex,
                                  int[] parents) {

        // Base case : Source node has
        // been processed
        if (currentVertex == NO_PARENT) {
            return;
        }
        addToPath(currentVertex);
        printPath(parents[currentVertex], parents);
       // System.out.print(currentVertex + " ");
    }

    public void addToPath(Integer index){
        this.pathTMP.addToRear(index);
    }
    public void setOneDirectionWeightPath(T v1, double weight, T v2) {
        if (weight < 0.0) {
            throw new IllegalArgumentException("weight must be higher than 0");
        }

        int posv1 = super.getIndex(v1);
        int posv2 = super.getIndex(v2);

        this.weightMatrix[posv1][posv2] = weight;
    }
}