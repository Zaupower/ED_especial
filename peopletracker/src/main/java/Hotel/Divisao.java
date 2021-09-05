package main.java.Hotel;

import main.java.Enum.PersonType;
import main.java.Estruturas.ArrayUnorderedList;
import main.java.Estruturas.EmptyException;
import main.java.Estruturas.NoComparableException;
import main.java.Estruturas.NotFoundException;
import main.java.SensorLib.MovimentoComplexo;
import main.java.SensorLib.Sensor;
import main.java.User.Person;

import java.io.IOException;
import java.util.Iterator;

public class Divisao {

    private String nome;
    private Integer lotacao;
    private Boolean reservado = false;
    private Boolean quarentena = false;
    private Sensor sensor = new Sensor();
    private ArrayUnorderedList<Person> currentPersonsOnRoom = new ArrayUnorderedList<>();
    private ArrayUnorderedList<String> ligacoes = new ArrayUnorderedList<>();

    /**
     * Clean all sensors data and list of persons in the room
     */
    public void cleanMoves(){
        this.sensor = new Sensor();
        this.currentPersonsOnRoom = new ArrayUnorderedList<>();
    }

    /**
     * Get divisao name
     * @return String name
     */
    public String getNome() {
        return nome;
    }

    /**
     * Set divisao name
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Get divisao lotacao
     * @return
     */
    public Integer getLotacao() {
        return lotacao;
    }

    /**
     * Set divisao lotacao
     * @param lotacao
     */
    public void setLotacao(Integer lotacao) {
        this.lotacao = lotacao;
    }

    /**
     * Get boolean to know if the room is reserved to staff
     * @return
     */
    public Boolean getReservado() {
        if (this.reservado == true){
            return reservado;
        }else{
            this.reservado = false;
            return false;
        }
    }

    /**
     * Set reservado
     * @param reservado
     */
    public void setRservado(Boolean reservado) {
        this.reservado = reservado;
    }

    /**
     * Get boolean to know if the divisao is quarentena
     * @return
     */
    public Boolean getQuarentena() {
        if (this.quarentena == true){
            return quarentena;
        }else{
            this.quarentena = false;
            return false;
        }
    }

    /**
     * Set quarentena
     * @param quarentena
     */
    public void setQuarentena(Boolean quarentena) {
        this.quarentena = quarentena;
    }

    /**
     * Get all connections from thisdivisao
     * @return
     */
    public ArrayUnorderedList<String> getLigacoes() {
        return ligacoes;
    }

    /**
     * Add Connection
     * @param ligacao
     */
    public void addLigacao(String ligacao) {
        this.ligacoes.addToFront(ligacao);
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setLigacoes(ArrayUnorderedList<String> ligacoes) {
        this.ligacoes = ligacoes;
    }

    public boolean isPersonOnTheRoom(int id){

        for (int i = 0; i< currentPersonsOnRoom.size(); i++){
            if (currentPersonsOnRoom.getIndex(i).getIdPessoa() == id){
                return true;
            }
        }
        return false;
    }

    /**
     * Add current prson to the room
     * @param movimento
     * @return
     * @throws NoComparableException
     * @throws IOException
     */
    public ArrayUnorderedList<String> addCurrentPerson(MovimentoComplexo movimento) throws NoComparableException, IOException {
        /**
         * Add Sensor alerts!!
         */
        ArrayUnorderedList<String> sensorAlerts = new ArrayUnorderedList<>();
        this.currentPersonsOnRoom.addToFront(movimento.getPerson());

        if (currentPersonsOnRoom.size() > this.lotacao){
            int overload = currentPersonsOnRoom.size()-this.lotacao;
            String alert1 = "AELRT Max capacity of "+this.getNome()+" was ultrapassed by: "+ overload + " hora: "+movimento.getDataHora();
            //System.out.println("AELRT Max capacity of "+this.getNome()+" was ultrapassed by: "+ overload);
        }else if (currentPersonsOnRoom.size() > this.lotacao - 6){
            String alert2 = "Warning max capacity of "+this.getNome()+" room is reaching the max "+ " hora: "+movimento.getDataHora();
            String alert3 = "Current ocuppied size: "+ currentPersonsOnRoom.size()+ " hora: "+movimento.getDataHora();
            //System.out.println("Warning max capacity of "+this.getNome()+" room is reaching the max ");
            sensorAlerts.addToRear(alert2);
            sensorAlerts.addToRear(alert3);
            //System.out.println("Current ocuppied size: "+ currentPersonsOnRoom.size());
        }

        if (this.reservado){
            if (movimento.getPerson().getRole() == PersonType.HOSPEDE){
                String sensor4 = "Alerta Pessoa: "+ movimento.getPerson().getName()+" , id: "+movimento.getPerson().getIdPessoa()+"nao autorizada a entrar "+this.getNome()+"!!"+ " hora: "+movimento.getDataHora();
                sensorAlerts.addToRear(sensor4);
                //System.out.println("Alerta Pessoa: "+ movimento.getPerson().getName()+" , id: "+movimento.getPerson().getIdPessoa()+"nao autorizada a entrar "+this.getNome()+"!!");
            }
        }

        this.sensor.addEntrada(movimento);
        sensorAlerts.addToFront("Divisao: "+this.nome+ " ");
        return sensorAlerts;
    }

    @Override
    public String toString() {
        return "Divisao{" +
                "nome='" + nome + '\'' +
                ", lotacao=" + lotacao +
                ", reservado=" + reservado +
                ", quarentena=" + quarentena +
                ", ligacoes=" + ligacoes +
                '}';
    }

    /**
     * Remove personFrom Room
     * @param movimento
     * @throws EmptyException
     * @throws NotFoundException
     * @throws IOException
     */
    public void removePersonFromRoom(MovimentoComplexo movimento) throws EmptyException, NotFoundException, IOException {
        if (!this.currentPersonsOnRoom.isEmpty()){
            for ( int i = 0; i < this.currentPersonsOnRoom.size(); i++){
                if (this.currentPersonsOnRoom.getIndex(i).getIdPessoa() == movimento.getIdPessoa()){
                    this.sensor.addSaida(movimento);
                    this.currentPersonsOnRoom.removeByIndex(i);
                }
            }
        }

    }

    /**
     * Remove conection from map
     * @param conctionToRemove
     */
    public void removeConection(String conctionToRemove){
        ArrayUnorderedList<String> newConectionsList = new ArrayUnorderedList<>();
        Iterator<String> it = this.ligacoes.iterator();
        while (it.hasNext()){
            String conectionsTmp = it.next();
            if (!conectionsTmp.equals(conctionToRemove)){
                newConectionsList.addToRear(conectionsTmp);
            }
        }

        this.ligacoes = newConectionsList;
    }

    /**
     * Get index of unfinished moviment to add exit
     * @param idPessoa
     */
    public void getUnfinishedMovimentIndexByPersonId(int idPessoa) {
        //System.out.printf("blaaa");
    }

    /**
     * Print this division content
     */
    public void printDivision() {
        System.out.println("Divisao: "+this.getNome()+ " ");
        ArrayUnorderedList<MovimentoComplexo> currentPersons = this.getSensor().getEntradasEsaidasCompleto();
        if (!this.currentPersonsOnRoom.isEmpty()){
            Iterator<Person> it = this.currentPersonsOnRoom.iterator();
            System.out.println(" Current persons on the room");
            while (it.hasNext()){
                Person p = it.next();
                System.out.println("  "+p.getName() + " id: " + p.getIdPessoa() + " role: "+p.getRole());
            }
        }
    }
}