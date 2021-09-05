package main.java.SensorLib;

import main.java.Hotel.Divisao;
import main.java.User.Person;

import java.io.IOException;

public class MovimentoComplexo extends Movimento {
    private Person person;
    private Divisao divisao;
    private String divisaoName;
    private Movimento entrada;
    private Movimento saida;
    public MovimentoComplexo(Movimento movimento, Person p) throws IOException {
        this.setPerson(p);
        this.setIdPessoa(p.getIdPessoa());
        this.setDataHora(movimento.getDataHora());
        this.divisao = new Divisao();
    }

    public Movimento getEntrada() {
        return entrada;
    }

    public void setEntrada(Movimento entrada) {
        this.entrada = entrada;
    }

    public Movimento getSaida() throws IOException {
        if (this.saida == null){
            saida = new Movimento();
        }
        return saida;
    }

    public void setSaida(Movimento saida) {
        this.saida = saida;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person p) {
        this.person = p;
    }

    public Divisao getDivisaoObj(){
        return this.divisao;
    }
    public void setDivisaoObj(Divisao divisao){
        this.divisao = divisao;
        this.setDivisO(divisao.getNome());
    }
}
