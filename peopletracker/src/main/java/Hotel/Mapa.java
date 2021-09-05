package main.java.Hotel;


import com.google.gson.Gson;
import main.java.Estruturas.ArrayUnorderedList;
import main.java.Estruturas.EmptyException;
import main.java.Estruturas.NoComparableException;
import main.java.Estruturas.NotFoundException;
import main.java.SensorLib.Movimento;
import main.java.SensorLib.MovimentoComplexo;
import main.java.SensorLib.Sensor;
import main.java.User.Person;
import main.java.User.RegisterPerson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;

public class Mapa {

    private String hotel;
    private Integer versao;
    private Divisao[] divisoes = null;
    private String[][] ligacoes = null;
    private ArrayUnorderedList<Divisao> quarentenaRooms = new ArrayUnorderedList<>();
    private ArrayUnorderedList<Divisao> reservadoRooms = new ArrayUnorderedList<>();


    /**
     * Get Hotel Name
     * @return
     */
    public String getHotel() {
        return hotel;
    }

    /**
     * Set Hotel name
     * @param hotel
     */
    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    /**
     * Get Version
     * @return
     */
    public Integer getVersao() {
        return versao;
    }

    /**
     * Set Version
     * @param versao
     */
    public void setVersao(Integer versao) {
        this.versao = versao;
    }

    /**
     * Get divisao[] from this map
     * @return
     */
    public Divisao[] getDivisoes() {
        return divisoes;
    }

    /**
     * Get divisao by index
     * @param i
     * @return
     */
    public Divisao getDivisao(int i) {
        if (i <= divisoes.length) {

            return divisoes[i];
        }
        return null;
    }

    /**
     * Set all divisoes
     * @param divisoes
     */
    public void setDivisoes(Divisao[] divisoes) {
        this.divisoes = divisoes;
    }

    /**
     * Get all divisoes
     * @return
     */
    public String[][] getLigacoes() {
        return ligacoes;
    }

    /**
     * Set all ligacoes[][]
     * @param ligacoes
     */
    public void setLigacoes(String[][] ligacoes) {
        this.ligacoes = ligacoes;
    }

    /**
     * método responsável por carregar o ficheiro json do divisoes, ou seja, pontuação, hotel e aposentos
     *
     * @param s
     */
    public void lerJson(String s) {
        try {
            Gson gson = new Gson();

            String json = "./Maps/" + s;

            Reader reader = Files.newBufferedReader(Paths.get(json));

            Mapa divisoes = gson.fromJson(reader, Mapa.class);
            this.setHotel(divisoes.getHotel());
            this.setVersao(divisoes.getVersao());
            this.setDivisoes(divisoes.getDivisoes());
            this.setLigacoes(divisoes.getLigacoes());

            reader.close();
            setLigacoes(this, this.getLigacoes());
            setSensors(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Set ligacoes
     * @param mapa Class
     * @param ligacoes matrix of strings
     */
    public void setLigacoes(Mapa mapa, String[][] ligacoes) {
        for (int j = 0; j < ligacoes.length; j++) {
            for (int i = 0; i < mapa.numeroDivisoes(); i++) {
                if (mapa.divisoes[i].getNome().equals(ligacoes[j][0])) {
                    mapa.divisoes[i].addLigacao(ligacoes[j][1]);
                } else if (mapa.divisoes[i].getNome().equals(ligacoes[j][1])) {
                    mapa.divisoes[i].addLigacao(ligacoes[j][0]);
                }
            }
        }
    }

    /**
     * Set Sensors values of each Room and populate this.quarentenaRooms and reservadoRooms
     *
     * @param mapa
     */
    public void setSensors(Mapa mapa) {
        for (int i = 0; i < mapa.numeroDivisoes(); i++) {
            Sensor sensor = new Sensor();
            sensor.setName(mapa.divisoes[i].getNome());
            sensor.setMaxCapacity(mapa.divisoes[i].getLotacao());
            if (mapa.divisoes[i].getReservado() == true) {
                this.reservadoRooms.addToFront(divisoes[i]);
                sensor.setReserved(true);
            }
            if (mapa.divisoes[i].getQuarentena() == true) {
                this.quarentenaRooms.addToFront(divisoes[i]);
                sensor.setQuarentine(true);
            }
            mapa.divisoes[i].setSensor(sensor);
        }
    }

    public ArrayUnorderedList<Divisao> getQuarentenaRooms() {
        return quarentenaRooms;
    }

    public ArrayUnorderedList<Divisao> getReservadoRooms() {
        return reservadoRooms;
    }

    @Override
    public String toString() {
        return "Mapa2{" +
                "hotel='" + hotel + '\'' +
                ", versao=" + versao +
                ", divisoes=" + Arrays.toString(divisoes) +
                ", ligacoes=" + Arrays.toString(ligacoes) +
                '}';
    }

    public int numeroDivisoes() {
        return divisoes.length;
    }

    /**
     * Method to add a person to a division
     * Steps
     * -verify if this person is registerd
     * if not sensor alert
     * -verify if the person is currelty in another room
     * if in another room, remove it
     * - in the add method on the room
     * Verify Auth
     * if Room.Auth != person.Auth
     * Launch Alert
     * Verify capacity
     * if max capacity reached Alert Max Capacity
     * if max capacity ultrapassed Max Capacity Ultrapassed
     *
     * @param movimento Movimento
     * @return
     */
    public ArrayUnorderedList<String> addPersonToDivision(Movimento movimento) throws IOException, EmptyException, NotFoundException, NoComparableException, ParseException {

        RegisterPerson registerPerson = new RegisterPerson();

        Person p = registerPerson.getPersonById(movimento.getIdPessoa());

        MovimentoComplexo mc = new MovimentoComplexo(movimento, p);

        if (p.getName().equals("NotFound")) {
            System.out.println("This Person is not registered");
        }
        int divisionId = this.getDisionIdByName(movimento.getDivisO());
        int saidaDivisoesIndex = verifyIfPersonIsAlreadyInAnotherRoom(mc);
        if (saidaDivisoesIndex != -1) {
            this.divisoes[saidaDivisoesIndex].getUnfinishedMovimentIndexByPersonId(movimento.getIdPessoa());
        }
        mc.setEntrada(movimento);
        return this.divisoes[divisionId].addCurrentPerson(mc);
    }

    /**
     * Serach for person in other room if found remove it
     * @param movimento
     * @return
     * @throws EmptyException
     * @throws NotFoundException
     * @throws IOException
     */
    private int verifyIfPersonIsAlreadyInAnotherRoom(MovimentoComplexo movimento) throws EmptyException, NotFoundException, IOException {
        for (int i = 0; i < this.divisoes.length; i++) {
            if (this.divisoes[i].isPersonOnTheRoom(movimento.getIdPessoa())) {
                movimento.setDivisaoObj(this.divisoes[i]);
                this.divisoes[i].removePersonFromRoom(movimento);
                return i;
            }
        }
        return -1;
    }

    /**
     * Parse date String to seconds
     * @param dateTime
     * @return
     * @throws ParseException
     */
    private int parseDateTimeToSeconds(String dateTime) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        long ts = dateFormat.parse(dateTime).getTime() / 1000;
        return (int) ts;
    }

    /**
     * Clear this Mapa paths in all divisoes
     */
    public void cleanPaths() {
        boolean exists = false;
        ArrayUnorderedList<Divisao> newDivisions = new ArrayUnorderedList<>();
        ArrayUnorderedList<Integer> positionstorRemoveFromConections = new ArrayUnorderedList<>();
        for (int i = 0; i < this.divisoes.length; i++) {
            exists = false;
            Divisao currentDivison = this.divisoes[i];
            String divisionName = currentDivison.getNome();
            for (int j = 0; j < this.divisoes.length; j++) {
                ArrayUnorderedList<String> listaDeLigacoes = this.divisoes[j].getLigacoes();
                for (int k = 0; k < listaDeLigacoes.size(); k++) {
                    if (divisionName.equals(listaDeLigacoes.getIndex(k))) {
                        exists = true;
                    }
                }
            }

            if (!exists) {
                positionstorRemoveFromConections.addToRear(i);
                System.out.println(this.divisoes[i].getNome() + " nao tem ligacao");
            } else {
                newDivisions.addToRear(this.divisoes[i]);
            }

            //vERIFICAR SE TODAS AS DIVISOES TEEM UMA LIGAÇAO
            //CASO NAO TENHAM RETURN FALSE PARA UTILIZAR O MAPA ORIGINAL
        }

        if (!positionstorRemoveFromConections.isEmpty()) {

            refoctorDivisoes(newDivisions);
        }
    }

    /**
     * Get division from this mapa by name
     * @param name
     * @return
     */
    private int getDisionIdByName(String name) {
        for (int i = 0; i < this.divisoes.length; i++) {
            if (this.divisoes[i].getNome().equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Refactor divisions
     * @param newDivisions
     */
    private void refoctorDivisoes(ArrayUnorderedList<Divisao> newDivisions) {
        this.divisoes = new Divisao[newDivisions.size()];
        for (int i = 0; i < newDivisions.size(); i++) {
            divisoes[i] = newDivisions.getIndex(i);
        }
    }

    /**
     * Refactor Ligacoes
     * @param divisionToRemove
     */
    private void refoctorLigacoes(ArrayUnorderedList<Integer> divisionToRemove) {
        //System.out.println( "rEFACTOR LIGACOES" );
        ArrayUnorderedList<String[]> newConections = new ArrayUnorderedList<>();

        for (int i = 0; i < ligacoes.length; i++) {
            boolean found = false;
            for (int j = 0; j < divisionToRemove.size(); j++) {
                String ligacao1 = ligacoes[i][0];
                String testSt = divisoes[divisionToRemove.getIndex(j)].getNome();
                String ligacao2 = ligacoes[i][1];
                if (ligacao1.equals(testSt) || ligacao2.equals(testSt)) {
                    found = true;
                }
            }
            if (!found) {
                newConections.addToRear(ligacoes[i]);
            }
        }

        this.ligacoes = new String[newConections.size()][];
        int counter = 0;
        Iterator<String[]> it = newConections.iterator();
        while (it.hasNext()) {
            this.ligacoes[counter] = it.next();
            counter++;
        }
    }

    /**
     * Search person on this room
     * @param id
     * @return
     */
    public Divisao searchPerson(int id) {
        Divisao d = this.divisoes[0];
        //d.setNome("NotFound");
        for (int i = 0; i < this.divisoes.length; i++) {
            if (this.divisoes[i].isPersonOnTheRoom(id)) {
                return this.divisoes[i];
            }
        }
        return null;
    }

    /**
     * Get all contacts methe in this room
     * @return
     */
    public ArrayUnorderedList<ArrayUnorderedList<MovimentoComplexo>> getallContacts() {
        ArrayUnorderedList<ArrayUnorderedList<MovimentoComplexo>> allMoves = new ArrayUnorderedList<>();
        for (int i = 0; i < this.divisoes.length; i++) {
            ArrayUnorderedList<MovimentoComplexo> mcList = divisoes[i].getSensor().getEntradasEsaidasCompleto();
            allMoves.addToRear(mcList);
        }
        return allMoves;
    }

    /**
     * Return all MovimentoComplexo from all people extept the referentieted
     *
     * @param id person id
     * @throws IOException
     */
    public void getPersonContatos(int id, String hora) throws IOException, ParseException {
        //System.out.println("GET CONTATOS");
        int horaInt = parseDateTimeToSeconds(hora);
        RegisterPerson registerPerson = new RegisterPerson();

        Person p = registerPerson.getPersonById(id);
        if (p.getName().equals("NotFound")) {
            System.out.println("This Person is not registered");
        }

        for (int i = 0; i < this.divisoes.length; i++) {
            ArrayUnorderedList<MovimentoComplexo> mcList = divisoes[i].getSensor().getEntradasEsaidasCompleto();

            if (!mcList.isEmpty()) {
                String divisaoName = divisoes[i].getNome();
                System.out.println(" ");
                System.out.println("Divisao: " + divisaoName);

                for (int j = 0; j < mcList.size(); j++) {
                    String divisionName = mcList.getIndex(j).getEntrada().getDivisO();
                    System.out.println("SENSOR DIVISION: " + divisionName);


                    //Definir hora de entrada
                    int idPessoa = mcList.getIndex(j).getPerson().getIdPessoa();
                    if (idPessoa != p.getIdPessoa()) {
                        //
                        String horaEntrada = "Nao Registrado";
                        if (mcList.getIndex(j).getEntrada() != null) {
                            horaEntrada = mcList.getIndex(j).getEntrada().getDataHora();
                        }
                        String horaSaida = "Nao Registrado";
                        if (mcList.getIndex(j).getSaida() != null) {
                            horaSaida = mcList.getIndex(j).getSaida().getDataHora();
                        }
                        System.out.println("main.java.User: " + idPessoa + " entrou as: " + horaEntrada + " saiu as: " + horaSaida);
                    }

                }
            }
        }
    }

    public void cleanPathsToHospedes() {
        ArrayUnorderedList<Divisao> newDivisions = new ArrayUnorderedList<>();
        ArrayUnorderedList<Integer> positionstorRemoveFromConections = new ArrayUnorderedList<>();
        for (int i = 0; i < this.divisoes.length; i++) {
            Divisao currentDivison = this.divisoes[i];

            if (currentDivison.getReservado()) {
                positionstorRemoveFromConections.addToRear(i);
            } else {
                newDivisions.addToRear(this.divisoes[i]);
            }
        }

        if (!positionstorRemoveFromConections.isEmpty()) {
            refoctorLigacoes(positionstorRemoveFromConections);
            removeFromConectionsList(positionstorRemoveFromConections);
            refoctorDivisoes(newDivisions);

        }
    }

    private void removeFromConectionsList(ArrayUnorderedList<Integer> positionsToRemove) {
        Iterator<Integer> it = positionsToRemove.iterator();
        while (it.hasNext()) {
            String conectionToRemove = divisoes[it.next()].getNome();
            for (int i = 0; i < divisoes.length; i++) {
                divisoes[i].removeConection(conectionToRemove);
            }
        }
    }

    /**
     * Clear all moves and divisions
     */
    public void cleanMovesInRoom() {
        for (int i = 0; i < divisoes.length; i++) {
            divisoes[i].cleanMoves();
        }
    }

    /**
     * Verify if Map is connected
     * @return
     */
    public boolean isMapConected() {
        boolean isMapConected = true;
        for (int i = 0; i < divisoes.length; i++) {
            String divisionName = divisoes[i].getNome();
            for (int j = 0; j < ligacoes.length; j++) {
                String ligacao1 = ligacoes[j][0];
                String ligacao2 = ligacoes[j][1];
                if (!divisionName.equals(ligacao1) || !divisionName.equals(ligacao2)) {
                    isMapConected = true;
                }
            }

            if (!isMapConected) {
                return false;
            }
        }
        return true;
    }

    public void cleanConnectionsAndRooms() {
        //para cada ligacao se essa ligaçao nao existir no novo array de Divisoes adicionala
        ArrayUnorderedList<Divisao> novasDivisoes = new ArrayUnorderedList<>();
        for (int i = 0; i < ligacoes.length; i++) {
            if (novasDivisoes.isEmpty()) {
                novasDivisoes.addToRear(this.getDivisaoByName(ligacoes[i][0]));
                novasDivisoes.addToRear(this.getDivisaoByName(ligacoes[i][1]));
            } else {
                if (!this.containsDivisao(novasDivisoes, ligacoes[i][0])) {
                    novasDivisoes.addToRear(this.getDivisaoByName(ligacoes[i][0]));
                }
                if (!this.containsDivisao(novasDivisoes, ligacoes[i][1])) {
                    novasDivisoes.addToRear(this.getDivisaoByName(ligacoes[i][1]));
                }
            }
        }
        this.divisoes = new Divisao[novasDivisoes.size()];
        Iterator<Divisao> it = novasDivisoes.iterator();
        int counter = 0;
        while (it.hasNext()) {
            this.divisoes[counter] = it.next();
            counter++;
        }
    }

    /**
     * Search for a division if found return true
     * @param listaDivisoes
     * @param name
     * @return
     */
    public boolean containsDivisao(ArrayUnorderedList<Divisao> listaDivisoes, String name) {
        Iterator<Divisao> it = listaDivisoes.iterator();
        while (it.hasNext()) {
            Divisao tmp = it.next();
            if (tmp.getNome().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get division by name
     * @param name
     * @return
     */
    public Divisao getDivisaoByName(String name) {
        for (int i = 0; i < divisoes.length; i++) {
            if (divisoes[i].getNome().equals(name)) {
                return divisoes[i];
            }
        }
        return null;
    }
}