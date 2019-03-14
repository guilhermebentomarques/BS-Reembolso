package br.com.bornsolutions.bsreembolso.WebService;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.io.ByteArrayOutputStream;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_EMPRESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_PROPOSTA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_EMPRESA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_PROPOSTA;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 30/04/2016.
 */
public class WS_CAD_PROPOSTA extends AsyncTask<String, Integer, Boolean> {

    private Context _Ascontext;
    private Activity _mActivity;
    ProgressBar _progressBar;
    TextView _txtProgress;
    Integer _iTotalRegistros, _iRegistroCorrente;

    public WS_CAD_PROPOSTA(Activity _psActivity, Context context, ProgressBar progressBar, TextView txtProgress) {
        this._Ascontext = context;
        _progressBar = progressBar;
        _txtProgress = txtProgress;
        _mActivity = _psActivity;
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

        String _strSQL = "SELECT                                                                        " +
                         "   CP.CP_ID,              " +
                         "   CP.CPR_ID, " +
                         "   CP.CE_ID, " +
                         "   CP.CP_STATUS, " +
                         "   CP.CL_ID,  " +
                         "   CP.CP_NUMPROPOSTABORN, " +
                         "   CP_STATUS," +
                         "   CP_VALOR," +
                         "   (CL.CL_NOMEREDUZIDO + ' - ' + CP.CP_TITULO) CP_TITULO " +
                         "FROM " +
                         "   CAD_PROPOSTA CP " +
                         "   INNER JOIN CAD_CLIENTE CL ON(CL.CL_ID = CP.CL_ID) ";

        //ADICIONA PARAMETRO QUERY
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psSelect", _strSQL);

        //ADICIONA PARAMETRO QUERY
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psNomeTabela", "CAD_PROPOSTA");

        //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
        SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, _URL, _SOAP_ACTION);

        try {

            //RETORNA STRING NO FORMATO JSON
            SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();

            JSONArray _jsonArray = new JSONArray(result2.toString());

            _iTotalRegistros = _jsonArray.length();

            for(int n = 0; n < _jsonArray.length();n++) {

                JSONObject object = _jsonArray.getJSONObject(n);
                try
                {
                    _iRegistroCorrente = n + 1;

                    publishProgress(_iRegistroCorrente);

                    CAD_PROPOSTA Proposta = new CAD_PROPOSTA();

                    Proposta.setCP_ID(Integer.parseInt(object.getString("CP_ID")));
                    Proposta.setCE_ID(Integer.parseInt(object.getString("CE_ID")));
                    Proposta.setCPR_ID(Integer.parseInt(object.getString("CPR_ID")));
                    Proposta.setCL_ID(Integer.parseInt(object.getString("CL_ID")));
                    Proposta.setCP_NUMERO(object.getString("CP_NUMPROPOSTABORN"));
                    Proposta.setCP_STATUS(object.getString("CP_STATUS"));
                    Proposta.setCP_TITULO(object.getString("CP_TITULO"));

                    TB_CAD_PROPOSTA TB_CAD_PROPOSTA = new TB_CAD_PROPOSTA(_Ascontext);

                    //VERIFICA SE O CLIENTE JÁ EXISTE
                    if(!TB_CAD_PROPOSTA.prcVerificaExiste_CAD_PROPOSTA(object.getString("CP_ID")))
                    {
                        TB_CAD_PROPOSTA.prcIncluir(Proposta);
                    }

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
        if(result)
            _txtProgress.setText("Propostas Sincronizadas com sucesso!!");
        else
            _txtProgress.setText("Falha ao sincronizar propostas!!");
    }

    @Override
    protected void onPreExecute ()
    {
        _progressBar.setVisibility(View.VISIBLE);
        _progressBar.setProgress(0);
    }

    @Override
    protected void onProgressUpdate (Integer... values){

        //PROGRESS
        _progressBar.setProgress(values[0]);

        if(values[0] == 0)
            _progressBar.setMax(_iTotalRegistros);

        _txtProgress.setText("Sincronizando Propostas - " + _iRegistroCorrente + "/" + _iTotalRegistros);

    }
}
