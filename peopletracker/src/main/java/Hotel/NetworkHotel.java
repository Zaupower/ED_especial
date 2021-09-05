package main.java.Hotel;


import main.java.Estruturas.Network;

public class NetworkHotel<T> extends Network<T> {


    public NetworkHotel(int size) {
        super(size+1);
    }

    /**
     * método responsável por atribuir o peso em apenas um dos sentidos do caminho, devido a este só perder vida quando entrar num aposento
     * com fantasma ( o contrário não se aplica, ou seja , sair de um aponsento com fantasma )
     * @param v1
     * @param weightV1, weigthV2
     * @param v2
     */
    public void setOneDirectionWeightPath(T v1, double weightV1, double weightV2, T v2) {
        if (weightV1 < 0.0 && weightV2 < 0.0) {
            throw new IllegalArgumentException("weight must be higher than 0");
        }


        int posv1 = super.getIndex(v1);
        int posv2 = super.getIndex(v2);
        if (this.weightMatrix[posv1][posv2] == 0.0 && this.weightMatrix[posv2][posv1] == 0){

            this.weightMatrix[posv1][posv2] = weightV2;
            this.weightMatrix[posv2][posv1] = weightV1;
        }
    }


    public void addVertex(T vertex){
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

    private void expandCapacity(){
        T[] verticesTmp = ((T[]) new Object[this.vertices.length * 2]);

        double[][] weightMatrixTmp = new double[this.vertices.length*2][this.vertices.length*2];
        boolean[][] adjMatrixTmp = new boolean[this.vertices.length * 2][this.vertices.length * 2];

        for (int i = 0; i < this.vertices.length; i++) {
            for (int j = 0; j < this.vertices.length; j++) {
                weightMatrixTmp[i][j] = this.weightMatrix[i][j];
                adjMatrixTmp[i][j] = this.adjMatrix[i][j];
            }
            verticesTmp[i] = this.vertices[i];
        }

        this.vertices = verticesTmp;
        this.adjMatrix = adjMatrixTmp;
        this.weightMatrix = weightMatrixTmp;
    }

    public boolean getAdjMatrixIndex(int i, int j) {
        return this.adjMatrix[i][j];
    }
}
