package main.java.SensorLib;

import com.google.gson.Gson;
import main.java.Estruturas.LinkedQueue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.IOException;

public class MovimentosManager {
    JSONParser parser = new JSONParser();
    private JSONArray Movimentos;
    private LinkedQueue<Movimento> movimentosRealizados = new LinkedQueue<>();

        public void loadMovimentosFromJson(String s) throws IOException {
            Gson gson = new Gson();
            String json = "./movimentos/" + s;

            try{
                Object obj = parser.parse(new FileReader(json));
                JSONObject jsonObject = (JSONObject) obj;
                Movimentos = (JSONArray)  jsonObject.get("Movimentos");
                for (int i = 0; i< Movimentos.size(); i++){
                    Movimento m = new Movimento();
                    JSONObject move = (JSONObject) Movimentos.get(i);
                   //
                    Long tmpId = (Long) move.get("idPessoa");
                    m.setIdPessoa(tmpId.intValue());
                    m.setDivisO((String) move.get("Divisão"));
                    m.setDataHora((String) move.get("DataHora"));
                    this.movimentosRealizados.enqueue(m);
                }
            } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public LinkedQueue<Movimento>  getMovimentosRealizados(){
            return this.movimentosRealizados;
    }
}
