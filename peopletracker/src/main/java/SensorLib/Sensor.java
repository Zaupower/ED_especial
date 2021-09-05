package main.java.SensorLib;


import main.java.Estruturas.ArrayUnorderedList;

import java.io.IOException;

/**
 * Sensor Class
 * Each sensor is atached to a division
 * A sensor contains the division properties/rules
 */
public class Sensor {
    private String name;
    private int maxCapacity;
    private boolean isQuarentine = false;
    private boolean isReserved = false;
    private ArrayUnorderedList<MovimentoComplexo> entradas = new ArrayUnorderedList<>();
    private ArrayUnorderedList<MovimentoComplexo> saidas = new ArrayUnorderedList<>();
    private ArrayUnorderedList<MovimentoComplexo> entradasEsaidasCompleto = new ArrayUnorderedList<>();

    public void addEntrada(MovimentoComplexo movimento) throws IOException {
        this.entradas.addToRear(movimento);
        MovimentoComplexo entradaESaida = new MovimentoComplexo(movimento, movimento.getPerson());
        Movimento entrada = new Movimento();
        entrada.setIdPessoa(movimento.getIdPessoa());
        entrada.setDataHora(movimento.getDataHora());
        entrada.setDivisO(movimento.getDivisO());

        entradaESaida.setEntrada(entrada);
        this.entradasEsaidasCompleto.addToRear(movimento);
    }

    public void addSaida(MovimentoComplexo movimento) throws IOException {
        this.saidas.addToRear(movimento);
        for (int i = 0; i < entradasEsaidasCompleto.size(); i++){
            if(entradasEsaidasCompleto.getIndex(i).getEntrada().getIdPessoa() == movimento.getIdPessoa()){
                MovimentoComplexo tmp = entradasEsaidasCompleto.getIndex(i);
                Movimento saida = new Movimento();
                saida.setIdPessoa(movimento.getIdPessoa());
                saida.setDataHora(movimento.getDataHora());
                saida.setDivisO(movimento.getDivisO());
                tmp.setSaida(saida);
                entradasEsaidasCompleto.setByIndex(i, tmp);
            }
        }

    }

    public ArrayUnorderedList<MovimentoComplexo> getEntradasEsaidasCompleto() {
        return this.entradasEsaidasCompleto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public boolean isQuarentine() {
        return isQuarentine;
    }

    public void setQuarentine(boolean quarentine) {
        isQuarentine = quarentine;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void setReserved(boolean reserved) {
        isReserved = reserved;
    }

}
