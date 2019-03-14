package br.com.bornsolutions.bsreembolso.WebService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by guilherme on 24/04/2016.
 */
public class WebService_Cep  {

    public String prcRetornaArrayCEP(String _psCEP)
    {
        JSONArray _jsonArrayCEP = null;
        //BUSCA A CIDADE E ESTADO PELO WEB SERVICE DE CEP
        String _strURL = "https://viacep.com.br/ws/" + _psCEP + "/json/";
        HttpURLConnection c = null;
        try {
            URL u = new URL(_strURL);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            //c.setConnectTimeout(timeout);
            //c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    _jsonArrayCEP = new JSONArray("[" + sb.toString().replace("\n","") + "]");

                    JSONObject object = _jsonArrayCEP.getJSONObject(0);
                    String _strUF = object.getString("uf");
                    String _strCidade = object.getString("localidade");

                    return _strUF + "-" + _strCidade;
            }


        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return "";
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                    return "";
                }
            }
        }
        return "";
    }
}
