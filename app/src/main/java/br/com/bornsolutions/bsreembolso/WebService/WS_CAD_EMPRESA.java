package br.com.bornsolutions.bsreembolso.WebService;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_EMPRESA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_EMPRESA;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 29/04/2016.
 */
public class WS_CAD_EMPRESA extends AsyncTask<String, Integer, Boolean> {

    private Context _Ascontext;
    private Activity _mActivity;
    ProgressBar _progressBar;
    TextView _txtProgress;
    Integer _iTotalRegistros, _iRegistroCorrente;

    public WS_CAD_EMPRESA(Activity _psActivity, Context context, ProgressBar progressBar, TextView txtProgress) {
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

        String _strSQL = "SELECT * FROM CAD_EMPRESA ";

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

            _iTotalRegistros = _jsonArray.length();

            for(int n = 0; n < _jsonArray.length();n++) {

                JSONObject object = _jsonArray.getJSONObject(n);
                try
                {
                    _iRegistroCorrente = n + 1;

                    publishProgress(_iRegistroCorrente);

                    CAD_EMPRESA Empresa = new CAD_EMPRESA();

                    Empresa.setCE_ID(Integer.parseInt(object.getString("CE_ID")));
                    Empresa.setCE_CNPJ(object.getString("CE_CNPJ"));
                    Empresa.setCE_RAZAOSOCIAL(object.getString("CE_RAZAOSOCIAL"));
                    Empresa.setCE_NOMEREDUZIDO(object.getString("CE_NOMEREDUZIDO"));

                    TB_CAD_EMPRESA TB_CAD_EMPRESA = new TB_CAD_EMPRESA(_Ascontext);

                    //VERIFICA SE O CLIENTE JÁ EXISTE
                    if(!TB_CAD_EMPRESA.prcVerificaExiste_CAD_EMPRESA(object.getString("CE_ID")))
                    {
                        TB_CAD_EMPRESA.prcIncluir(Empresa);
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
        _txtProgress.setText("Empresas Sincronizadas com sucesso!!");
        _progressBar.setProgress(_iTotalRegistros);
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

        if(values[0] == 1)
            _progressBar.setMax(_iTotalRegistros);

        _txtProgress.setText("Sincronizando Empresas - " + _iRegistroCorrente + "/" + _iTotalRegistros);

    }
}
