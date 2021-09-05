package main.java.Demo;

import main.java.Estruturas.*;
import main.java.Hotel.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class testClass {
    public static void main(String[] args) throws IOException, InvalidIndexException, EmptyException, NotFoundException, NoComparableException, ParseException {
        /*
        int lotacao1,lotacao2,lotacao3,lotacao4, lotacao5;
        lotacao1 = 6;
        lotacao2 = 2;
        lotacao3 = 4;
        lotacao4 = 609;
        lotacao5 = 1;
        ArrayUnorderedList<String> c1 = new ArrayUnorderedList<>();
        c1.addToFront("Node3");
        c1.addToFront("Node4");
        DummyClass d1 = new DummyClass("Node1",c1, lotacao1 );

        ArrayUnorderedList<String> c2 = new ArrayUnorderedList<>();
        c2.addToFront("Node3");
        DummyClass d2 = new DummyClass("Node2",c2, lotacao2 );

        ArrayUnorderedList<String> c3 = new ArrayUnorderedList<>();
        c3.addToFront("Node1");
        c3.addToFront("Node2");

        DummyClass d3 = new DummyClass("Node3",c3, lotacao3 );

        ArrayUnorderedList<String> c4 = new ArrayUnorderedList<>();
        c4.addToFront("Node1");

        DummyClass d4 = new DummyClass("Node4",c4, lotacao4 );

        ArrayUnorderedList<String> c5 = new ArrayUnorderedList<>();
        c5.addToFront("Node1");
        c5.addToFront("Node2");
        DummyClass d5 = new DummyClass("Node5",c5, lotacao5 );

        NetworkGame<DummyClass> dummyClassGraph = new NetworkGame<>(5);

        dummyClassGraph.addVertex(d1);
        dummyClassGraph.addVertex(d2);
        dummyClassGraph.addVertex(d3);
        dummyClassGraph.addVertex(d4);
        dummyClassGraph.addVertex(d5);
        for (int i = 0; i < dummyClassGraph.size(); i++) {
            for (int j = 0; j < dummyClassGraph.size(); j++) {
                String vertexName = dummyClassGraph.getVertex(i).nome;
                if (dummyClassGraph.getVertex(j).getConections().contains(vertexName) ) {
                    dummyClassGraph.addEdge(dummyClassGraph.getVertex(i), dummyClassGraph.getVertex(j));

                    dummyClassGraph.setEdgeWeight(dummyClassGraph.getVertex(j), dummyClassGraph.getVertex(i).lotacao, dummyClassGraph.getVertex(j));
                }
            }
        }

        Iterator<DummyClass> it = dummyClassGraph.getShortestPath(d1, d3);

        while (it.hasNext()) {
            System.out.println(it.next());
        }

        double result  = dummyClassGraph.shortestPathWeight(d1, d3);
        System.out.println("\nTotal de pessoas contactadas : " + result);

            System.out.println("Beutyy");


            O que fazer checkList
                • permitir registar os funcionários e os hospedes.
                CHECK: TESTING -> WORKING

               Implemented On Menu  • permitir obter a informação dos contactos efetuados por determinada pessoa (funcionários e
                    hospedes) nas últimas  (parâmetro a definir) horas.
                    CHECK: TESTING (FALTA ESTA!! E DONNE TO MENU!!)

              Implemented On Menu  • permitir obter a localização atual de uma pessoa.
                    CHECK: TESTING -> WORKING
                        SensorManager2 sensorManager = new SensorManager2(m);
                        String movimentosFileName = "movimentos.json";
                        Divisao d = sensorManager.findPersonById(1, movimentosFileName);

              Implemented On Menu  • lançar um alerta quando um hospede entra numa área reservada aos funcionários.
                    CHECK: TESTING -> WORKING

             Implemented On Menu   • permitir obter a localização atual de uma pessoa, e retornar o caminho mais apropriado para se
                    deslocar para a sala de quarentena. Os caminhos devem considerar que os hospedes não podem
                    entrar nas áreas reservadas aos funcionários. Considera-se o caminho mais apropriado, aquele
                    que permitir deslocar-se para a sala de quarentena passando pelo menor número de pessoas.
                        Criar metodo getPersonPath
                        CHECK: TESTING -> WORKING!!

               Implemented On Menu  • lançar um aviso quando a lotação máxima permitida está próxima (parâmetro a definir) e um
                    alerta quando já foi ultrapassada.
                    CHECK: NOT DONNE ( FALTA DEFINIR PARAMETRO)

               Implemented On Menu  • lançar um alerta se o sensor reportar uma pessoa desconhecida pelo sistema.
                    CHECK: TESTING -> WORKING

               On work Menu  • fAZER MENU PARA TODAS AS FUNCIONALODAS.
                    CHECK: TESTING -> WORKING

        String mapPath = "mapa.json";

        Mapa m = new Mapa();
        m.lerJson(mapPath);

        SensorManager2 sensorManager = new SensorManager2(m, mapPath);
        String movimentosFileName = "movimentos.json";

        System.out.println(" ");
        System.out.println("Read moves and check Alerts");
        sensorManager.readMoves(movimentosFileName, true, m);

        System.out.println(" ");
        System.out.println("Find Person By ID...");
        Divisao d = sensorManager.findPersonById(2, movimentosFileName, m);
        if (d != null){

            System.out.println(" ");
            System.out.println("Pessoa encontrada: " +d.getNome());
        }

        System.out.println(" ");
        System.out.println("Find best path to quarentine...");
        sensorManager.findBestPAthToQuarentine(4,movimentosFileName, m);

        System.out.println(" ");
        System.out.println("Get person contactos...");
        String hora = "2020-07-14 14:40";
        FormattedDateMatcher dateMatcher = new FormattedDateMatcher();
        System.out.println("HORA MATCH? "+dateMatcher.Pattern(hora));
        int personId = 4;

        m = new Mapa();
        m.lerJson(mapPath);
        sensorManager.getPersonContactsFromLastHours( hora, personId, m);


       // String[] ligacoes = new String[3];
        //ligacoes[0] = "Sala";
        //ligacoes[1] = "Arrumos";
        //Aposento a1 = new Aposento("Quarto",ligacoes );
        //System.out.println(a1.getLigacoes());
         */
        Menu menu = new Menu();
        menu.run();
    }
}
