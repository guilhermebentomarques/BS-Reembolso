package br.com.bornsolutions.bsreembolso.WebService;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_EMPRESA;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 14/08/2016.
 */
public class WS_CAD_VIAGEM_DESPESA_GCC extends AsyncTask<String, Integer, Boolean> {

    private Context _Ascontext;
    String prpUsuario_ID;

    public WS_CAD_VIAGEM_DESPESA_GCC(Context context, String _psCC_ID) {
        this._Ascontext = context;
        prpUsuario_ID = _psCC_ID;
    }

    //COMUNICAÇÃO COM O DATASET
    private final String NAMESPACE = "http://tempuri.org/";
    private final String _URL = "http://localhost/GuilhermeConnection/BSReembolso.asmx";
    private String _SOAP_ACTION = "http://tempuri.org/prcGetDados_JSON";
    private String METHOD_NAME = "prcGetDados_JSON";

    @Override
    protected Boolean doInBackground (String... params){

        CLG_WebService_Ksoap WebServiceMaster = new CLG_WebService_Ksoap();

        //CRIA REQUEST
        SoapObject request2 = WebServiceMaster.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME);

        //ADICIONA PARAMETRO STRING CONEXÃO
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psConexao", Constantes.DESKTOPBORN);

        String _strSQL = "SELECT * FROM CAD_VIAGEM_DESPESA WHERE CC_ID = " + prpUsuario_ID + "";

        //ADICIONA PARAMETRO QUERY
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psSelect", _strSQL);

        //ADICIONA PARAMETRO QUERY
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psNomeTabela", "CAD_EMPRESA");

        //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
        SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, _URL, _SOAP_ACTION);

        try {

            //RETORNA STRING NO FORMATO JSON
            SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();
            JSONArray _jsonArray = new JSONArray(result2.toString());

            for(int n = 0; n < _jsonArray.length();n++) {

                JSONObject object = _jsonArray.getJSONObject(n);
                try
                {
                    CAD_VIAGEM_DESPESA CAD_VIAGEM_DESPESA = new CAD_VIAGEM_DESPESA();

                    CAD_VIAGEM_DESPESA.setCU_ID(Integer.parseInt(object.getString("CC_ID")));
                    CAD_VIAGEM_DESPESA.setCL_ID(Integer.parseInt(object.getString("CL_ID")));
                    CAD_VIAGEM_DESPESA.setCVD_DESCRICAO(object.getString("CVD_DESCRICAO"));

                    TB_CAD_EMPRESA TB_CAD_EMPRESA = new TB_CAD_EMPRESA(_Ascontext);

                    /*//VERIFICA SE O CLIENTE JÁ EXISTE
                    if(!TB_CAD_VIAGEM_DESPESA.prcVerificaExiste(object.getString("CE_ID")))
                    {
                        TB_CAD_VIAGEM_DESPESA.prcIncluir(CAD_VIAGEM_DESPESA);
                    }*/

                } catch (Exception ex) {
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute (Boolean result)
    {
    }

    @Override
    protected void onPreExecute ()
    {
    }

    @Override
    protected void onProgressUpdate (Integer... values){

    }
}
