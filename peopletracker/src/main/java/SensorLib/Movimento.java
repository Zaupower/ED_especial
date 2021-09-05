package main.java.SensorLib;

import java.io.IOException;

public class Movimento {

    private int idPessoa ;
    private String divisO;
    private String dataHora;

    public Movimento() throws IOException {
    }

    /**
     * Get person from movimento
     * @return
     */
    public int getIdPessoa() {
        return this.idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getDivisO() {
        return divisO;
    }

    public void setDivisO(String divisO) {

        this.divisO = divisO;
    }

    public String getDataHora() {
        if (this.dataHora == null){
            this.dataHora = "Nao Registrado";
        }
        return this.dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return "Movimento{" +
                "idPessoa=" + idPessoa +
                ", divisO='" + divisO + '\'' +
                ", dataHora='" + dataHora + '\'' +
                '}';
    }


}

