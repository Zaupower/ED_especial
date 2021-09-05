package main.java.SensorLib;

import main.java.Enum.PersonType;
import main.java.Estruturas.*;
import main.java.Hotel.*;
import main.java.User.Person;
import main.java.User.RegisterPerson;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
//ESTE ESTA A FUNCIONAR

public class SensorManager {
    private Mapa mapa;
    private NetworkHotel<Divisao> graph;
    private String originalMapPathString;

    /**
     * Manage, store and read information from the sensors, hold the map info to
     * @param mapa
     * @param originalMapPathString
     * @throws InvalidIndexException
     * @throws EmptyException
     */
    public SensorManager(Mapa mapa, String originalMapPathString) throws InvalidIndexException, EmptyException {
        this.mapa = mapa;
        this.mapa.cleanPaths();
        this.graph = new NetworkHotel<>(mapa.numeroDivisoes());
        this.initializeGraph(this.mapa, this.graph);
        this.originalMapPathString = originalMapPathString;
    }

    /**
     * Initialize the grap structure from the map
     * @param mapa
     * @param graph
     */
    private void initializeGraph(Mapa mapa, NetworkHotel<Divisao> graph) {
        for (int i = 0; i < mapa.numeroDivisoes(); i++) {
            graph.addVertex(mapa.getDivisao(i));
        }
        for (int i = 0; i < graph.size(); i++) {
            for (int j = 1; j < graph.size(); j++) {
                String vertexName = graph.getVertex(i).getNome();
                if (hasEdge(vertexName, j, graph)) {
                    graph.addEdge(graph.getVertex(i), graph.getVertex(j));
                    graph.setOneDirectionWeightPath(graph.getVertex(i), graph.getVertex(i).getLotacao(), graph.getVertex(j).getLotacao(), graph.getVertex(j));
                }
            }
        }
    }

    /**
     * Get shortest path from one division to another
     * @param inicio
     * @param fim
     * @param graph
     * @return
     * @throws InvalidIndexException
     * @throws EmptyException
     */
    public ArrayUnorderedList<ArrayUnorderedList<Integer>> getShortestPath(Divisao inicio, Divisao fim, NetworkHotel graph) throws InvalidIndexException, EmptyException {

        return graph.getShortestPath(inicio, fim);
    }

    /*
     public double getShortestPathWeight(Divisao inicio, Divisao fim, NetworkGame graph) throws InvalidIndexException, EmptyException {
            double result  = graph.shortestPathWeight(inicio, fim) + inicio.getLotacao();
            return result;
        }
     */

    /**
     * Verify two rooms has connection
     * @param divisao
     * @param index
     * @param graph
     * @return
     */
    public boolean hasEdge(String divisao, int index, NetworkHotel<Divisao> graph) {
        if (graph.getVertex(index).getLigacoes().contains(divisao)) {
            return true;
        }
        return false;
    }

    /**
     * Adicionar string no metodo para especificar o path do json
     *
     * @throws IOException
     * @throws EmptyException
     */
    public void readMoves(String movimentosFileName, boolean toPrintAlerts, Mapa mapaUsado) throws IOException, EmptyException, NotFoundException, NoComparableException, ParseException {
        MovimentosManager movimentosManager = new MovimentosManager();
        movimentosManager.loadMovimentosFromJson(movimentosFileName);
        LinkedQueue<Movimento> movimentosRealizados;
        movimentosRealizados = movimentosManager.getMovimentosRealizados();
        ArrayUnorderedList<ArrayUnorderedList<String>> listaDeAlertas = new ArrayUnorderedList<>();
        mapaUsado.cleanMovesInRoom();
        while (!movimentosRealizados.isEmpty()) {
            listaDeAlertas.addToRear(mapaUsado.addPersonToDivision(movimentosRealizados.dequeue()));
        }
        //this.mapa.getPersonContatos(1);
        if (toPrintAlerts) {
            printAlerts(listaDeAlertas);
        }
    }

    /**
     * Print Sensors Alerts
     * @param listaDeAlertas
     */
    private void printAlerts(ArrayUnorderedList<ArrayUnorderedList<String>> listaDeAlertas) {
        Iterator<ArrayUnorderedList<String>> it = listaDeAlertas.iterator();
        while (it.hasNext()) {
            ArrayUnorderedList<String> alerts = it.next();
            printAlert(alerts);
        }
    }

    private void printAlert(ArrayUnorderedList<String> alerts) {
        Iterator<String> it = alerts.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    private RegisterPerson registerPerson = new RegisterPerson();
    boolean movesNotRead = false;

    /**
     * Find person By Id
     * @param id
     * @param movimentos
     * @param mapaUsado
     * @return
     * @throws IOException
     * @throws EmptyException
     * @throws NotFoundException
     * @throws NoComparableException
     * @throws ParseException
     */
    public Divisao findPersonById(int id, String movimentos, Mapa mapaUsado) throws IOException, EmptyException, NotFoundException, NoComparableException, ParseException {
        this.readMoves(movimentos, false, mapaUsado);
        movesNotRead = true;
        Person p = registerPerson.getPersonById(id);
        Divisao d;
        if (!p.getName().equals("NotFound")) {
            d = mapaUsado.searchPerson(id);
            return d;
        }
        return null;
    }

    /**
     * Find best path to quarentine for an initial path
     * @param id
     * @param movimentosFileName
     * @param newMapa
     * @throws EmptyException
     * @throws NotFoundException
     * @throws IOException
     * @throws ParseException
     * @throws NoComparableException
     * @throws InvalidIndexException
     */
    public void findBestPAthToQuarentine(int id, String movimentosFileName, Mapa newMapa) throws EmptyException, NotFoundException, IOException, ParseException, NoComparableException, InvalidIndexException {
        Person p = registerPerson.getPersonById(id);

        Divisao start = findPersonById(id, movimentosFileName, newMapa);
        if (start == null) {
        } else {
            ArrayUnorderedList<ArrayUnorderedList<Divisao>> allPathsDivisoes;

            if (!start.getReservado() && p.getRole() != PersonType.FUNCIONARIO && newMapa.isMapConected()) {
                NetworkHotel<Divisao> newGraph = new NetworkHotel<>(newMapa.numeroDivisoes());
                this.initializeGraph(newMapa, newGraph);

                ArrayUnorderedList<Divisao> reserverdRooms = newMapa.getReservadoRooms();
                Iterator<Divisao> it = reserverdRooms.iterator();
                while (it.hasNext()){
                    newGraph.removeVertex(it.next());
                }
                if ( newGraph.isConnected()){
                    ArrayUnorderedList<Divisao> quarentenaRooms = newMapa.getQuarentenaRooms();
                    ArrayUnorderedList<ArrayUnorderedList<Integer>> allpathsInteger = this.getShortestPath(start, quarentenaRooms.first(), newGraph);

                    allPathsDivisoes = this.getAllPathsDivisions(allpathsInteger, newMapa);

                    this.calculateBestPathToQuarentine(allPathsDivisoes, start, quarentenaRooms);
                }else {
                    System.out.println("Need to override rules to give an optimal path");
                    this.mapa = new Mapa();
                    mapa.lerJson(this.originalMapPathString);
                    ArrayUnorderedList<Divisao> quarentenaRooms = newMapa.getQuarentenaRooms();
                    ArrayUnorderedList<ArrayUnorderedList<Integer>> indexRommsPath = this.getShortestPath(start, quarentenaRooms.first(), this.graph);
                    ArrayUnorderedList<ArrayUnorderedList<Divisao>> allPathsDivisoesSingle = this.getAllPathsDivisions(indexRommsPath, this.mapa);

                    this.calculateBestPathToQuarentine(allPathsDivisoesSingle, start, quarentenaRooms);
                }

            } else {
                this.mapa = new Mapa();
                mapa.lerJson(this.originalMapPathString);
                ArrayUnorderedList<Divisao> quarentenaRooms = newMapa.getQuarentenaRooms();
                ArrayUnorderedList<ArrayUnorderedList<Integer>> indexRommsPath = this.getShortestPath(start, quarentenaRooms.first(), this.graph);
                ArrayUnorderedList<ArrayUnorderedList<Divisao>> allPathsDivisoesSingle = this.getAllPathsDivisions(indexRommsPath, this.mapa);

                this.calculateBestPathToQuarentine(allPathsDivisoesSingle, start, quarentenaRooms);

            }
        }

    }

    /**
     * Calculate best path to Quarentine
     * @param allPathsDivisoes
     * @param start
     * @param quarentenaRooms
     * @throws EmptyException
     */
    private void calculateBestPathToQuarentine(ArrayUnorderedList<ArrayUnorderedList<Divisao>> allPathsDivisoes, Divisao start, ArrayUnorderedList<Divisao> quarentenaRooms) throws EmptyException {
        ArrayUnorderedList<ArrayUnorderedList<Divisao>> allQuarentenaPaths = this.getAllQuarentenaPaths(allPathsDivisoes, quarentenaRooms);
        if (allQuarentenaPaths.isEmpty() || allQuarentenaPaths == null){
            System.out.println("No paths Found");
        }else {
            Iterator<ArrayUnorderedList<Divisao>> it = allQuarentenaPaths.iterator();
            double bestValue = Double.MAX_VALUE;
            ArrayUnorderedList<Divisao> bestPathEver;
            int index = 0;
            double tmpValue = 0;
            int bestIndex = -1;
            while (it.hasNext()) {
                ArrayUnorderedList<Divisao> nextPath = it.next();
                tmpValue = this.getPathSum(nextPath);
                if (tmpValue < bestValue) {
                    bestValue = tmpValue;
                    bestIndex = index;
                }
                index++;
            }
            ArrayUnorderedList<Divisao> bestQuarentenaPath;
            if (allQuarentenaPaths.size() >= 1){
                bestQuarentenaPath  = allQuarentenaPaths.first();
            }else {
                bestQuarentenaPath = allQuarentenaPaths.getIndex(bestIndex);
            }

            System.out.println("Best Path: ");
            int pessoasContactadas = 0;
            Iterator<Divisao> itBestPath = bestQuarentenaPath.iterator();
            while (itBestPath.hasNext()) {
                Divisao tmp = itBestPath.next();
                pessoasContactadas += tmp.getLotacao();
                System.out.printf(" -> " + tmp.getNome());
            }
            System.out.println(" ");
            System.out.println("Total de pessoas contactadas : " + pessoasContactadas);
        }
    }

    /**
     * Get path sum of all persons contacted
     * @param nextPath
     * @return
     */
    private double getPathSum(ArrayUnorderedList<Divisao> nextPath) {
        Iterator<Divisao> it = nextPath.iterator();
        double sum = 0;
        while (it.hasNext()) {
            sum += it.next().getLotacao();
        }
        return sum;
    }

    /**
     * Falta aqui qualquer coisa
     * @param allPathsDivisoes
     * @param quarentenaRooms
     * @return
     * @throws EmptyException
     */
    private ArrayUnorderedList<ArrayUnorderedList<Divisao>> getAllQuarentenaPaths(ArrayUnorderedList<ArrayUnorderedList<Divisao>> allPathsDivisoes, ArrayUnorderedList<Divisao> quarentenaRooms) throws EmptyException {
        Iterator<ArrayUnorderedList<Divisao>> it = allPathsDivisoes.iterator();
        ArrayUnorderedList<ArrayUnorderedList<Divisao>> quarentenaPaths = new ArrayUnorderedList<>();
        boolean isQuarentena;
        String quarentenaName;
        while (it.hasNext()) {
            ArrayUnorderedList<Divisao> pathtmp = it.next();
            isQuarentena = false;
            Iterator<Divisao> itQuarentena = quarentenaRooms.iterator();
            while (itQuarentena.hasNext()) {
                Divisao quarentenaTmp = itQuarentena.next();
                String lastNomeDivision = pathtmp.last().getNome();
                String firstNomeDivision = pathtmp.first().getNome();
                quarentenaName = quarentenaTmp.getNome();
                if (lastNomeDivision.equals(quarentenaName) /*  Caso de erro */|| firstNomeDivision.equals(quarentenaName)
                ) {
                    isQuarentena = true;
                }
            }
            if (isQuarentena) {
                quarentenaPaths.addToFront(pathtmp);
            }
        }

        return quarentenaPaths;
    }

    /**
     * Get list of lists containning all division path
     * @param allPaths
     * @param mapUSed
     * @return
     */
    private ArrayUnorderedList<ArrayUnorderedList<Divisao>> getAllPathsDivisions(ArrayUnorderedList<ArrayUnorderedList<Integer>> allPaths, Mapa mapUSed) {
        Iterator<ArrayUnorderedList<Integer>> it = allPaths.iterator();
        ArrayUnorderedList<ArrayUnorderedList<Divisao>> allPathsList = new ArrayUnorderedList<>();
        while (it.hasNext()) {
            allPathsList.addToRear(getDivisaoPath(it.next(), mapUSed));
        }
        return allPathsList;
    }

    /**
     * Parse ArrayUnorderedList of Integer to ArrayUnorderedList<Divisao>
     *
     * @param printList
     * @return
     */
    public ArrayUnorderedList<Divisao> getDivisaoPath(ArrayUnorderedList<Integer> printList, Mapa mapUsed) {
        Iterator<Integer> it = printList.iterator();
        ArrayUnorderedList<Divisao> bestPath = new ArrayUnorderedList<>();
        while (it.hasNext()) {
            Divisao divisaoTmp = mapUsed.getDivisao(it.next());
            bestPath.addToFront(divisaoTmp);
        }
        return bestPath;
    }

    /**
     * Get person contact from last given hours
     * @param hora
     * @param personId
     * @param mapaUsado
     * @throws IOException
     * @throws EmptyException
     * @throws NotFoundException
     * @throws ParseException
     * @throws NoComparableException
     */
    public void getPersonContactsFromLastHours(String hora, int personId, Mapa mapaUsado) throws IOException, EmptyException, NotFoundException, ParseException, NoComparableException {
        Person p = getPerson(personId);
        String movesFile = "movimentos.json";
        this.readMoves(movesFile, false, mapaUsado);
        ArrayUnorderedList<ArrayUnorderedList<MovimentoComplexo>> todosMovimentosDoMapa = mapaUsado.getallContacts();
        Iterator<ArrayUnorderedList<MovimentoComplexo>> it = todosMovimentosDoMapa.iterator();
        while (it.hasNext()) {
            ArrayUnorderedList<MovimentoComplexo> listaDeMovimentos = it.next();
            if (!listaDeMovimentos.isEmpty()) {

                printMovimentos(listaDeMovimentos, personId, hora);
            }
        }

    }

    /**
     * Print person moviments
     * @param listaDeMovimentos
     * @param id
     * @param hora
     * @throws ParseException
     * @throws IOException
     * @throws EmptyException
     */
    private void printMovimentos(ArrayUnorderedList<MovimentoComplexo> listaDeMovimentos, int id, String hora) throws ParseException, IOException, EmptyException {
        ArrayUnorderedList<String[]> horaEntradaSaida = getHoraEntradaSaidaNaDivisao(listaDeMovimentos, id);

        if (!horaEntradaSaida.isEmpty()) {

            Iterator<String[]> inOuts = horaEntradaSaida.iterator();
            while (inOuts.hasNext()) {

                String[] inOutsE = inOuts.next();

                calculateContactos(listaDeMovimentos, inOutsE, id, hora);


            }
        }
    }

    /**
     * Calculate contacts made by a person after moving
     * @param listaDeMovimentos
     * @param inOutsE
     * @param id
     * @param hora
     * @throws IOException
     * @throws EmptyException
     * @throws ParseException
     */
    private void calculateContactos(ArrayUnorderedList<MovimentoComplexo> listaDeMovimentos, String[] inOutsE, int id, String hora) throws IOException, EmptyException, ParseException {
        for (int i = 0; i < listaDeMovimentos.size(); i++) {
            MovimentoComplexo movimentoComplexoNovo = listaDeMovimentos.getIndex(i);

            Person p = movimentoComplexoNovo.getPerson();
            int idPessoa = p.getIdPessoa();
            if (idPessoa != id) {
                String horaEntrada = "Nao Registrado";
                if (movimentoComplexoNovo.getEntrada() != null) {
                    horaEntrada = movimentoComplexoNovo.getEntrada().getDataHora();
                }
                String horaSaida = "Nao Registrado";
                if (movimentoComplexoNovo.getSaida() != null) {
                    horaSaida = movimentoComplexoNovo.getSaida().getDataHora();
                }
                System.out.println(listaDeMovimentos.first().getEntrada().getDivisO());

                System.out.println("Todos os movimentos que envolvem outra pessoa");
                System.out.println("Hora de entrada do zezinho");
                System.out.println("Hora de Pesquisa: " + hora);

                System.out.println("Entrou: " + inOutsE[0]);
                System.out.println("Saiu: " + inOutsE[1]);
                System.out.println("main.java.User: " + idPessoa + " entrou as: " + horaEntrada + " saiu as: " + horaSaida);

                if (parseDateTimeToSeconds(inOutsE[0]) > parseDateTimeToSeconds(hora) || parseDateTimeToSeconds(horaEntrada) < parseDateTimeToSeconds(inOutsE[0])) {
                    System.out.println("Houve contacto com a pessoa: " + p.getName() + " id: " + idPessoa);
                }

                System.out.println(" ");
            }

        }
    }

    /**
     * Parse date to time
     * @param dateTime
     * @return
     * @throws ParseException
     */
    private long parseDateTimeToSeconds(String dateTime) throws ParseException {

        if (!dateTime.equals("Nao Registrado")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
            long ts = dateFormat.parse(dateTime).getTime() / 1000;
            return (int) ts;
        }
        return -1;
    }

    /**
     * Parse time seconds to string
     * @param timeInt
     * @return
     */
    private String timeParserToString(long timeInt) {
        Date currentDate = new Date(timeInt * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        String date = dateFormat.format(currentDate);
        return date;
    }

    /**
     * Lista de Horas de entrada e saida de uma dada divissao compostas por int[0] -> entrada e int[1] saida
     *
     * @param next
     * @return
     */
    private ArrayUnorderedList<String[]> getHoraEntradaSaidaNaDivisao(ArrayUnorderedList<MovimentoComplexo> next, int id) throws ParseException, IOException {
        Iterator<MovimentoComplexo> it = next.iterator();
        ArrayUnorderedList<String[]> horasDoUtilizador = new ArrayUnorderedList<>();
        String[] horasEntradaSaida = new String[2];
        while (it.hasNext()) {
            MovimentoComplexo mc = it.next();
            int idPessoa = mc.getPerson().getIdPessoa();
            if (idPessoa == id) {

                if (mc.getEntrada().getDataHora() == null) {
                    System.out.println("null");
                } else {
                    String horaEntrada = mc.getEntrada().getDataHora();
                    horasEntradaSaida[0] = horaEntrada;
                }


                if (mc.getSaida().getDataHora() == null) {
                    System.out.println("null");
                } else {
                    String horaSaida = mc.getSaida().getDataHora();
                    horasEntradaSaida[1] = horaSaida;
                }
                //Hora de entrada
                //Hora de saida

                horasDoUtilizador.addToRear(horasEntradaSaida);
            }
        }
        return horasDoUtilizador;
    }

    /**
     * Find person by id
     * @param personId
     * @return
     * @throws IOException
     */
    private Person getPerson(int personId) throws IOException {
        RegisterPerson registerPerson = new RegisterPerson();

        Person p = registerPerson.getPersonById(personId);
        if (p.getName().equals("NotFound")) {
            System.out.println("This Person is not registered");
        }
        return p;
    }

    /**
     * Print all divisions
     */
    public void printDivisions() {
        Divisao[] divisaos = this.mapa.getDivisoes();
        if (divisaos == null){
            System.out.println("Mapa vazio");
        }else {
            for (int i = 0; i < divisaos.length; i++){
                divisaos[i].printDivision();
            }
        }

    }
}
