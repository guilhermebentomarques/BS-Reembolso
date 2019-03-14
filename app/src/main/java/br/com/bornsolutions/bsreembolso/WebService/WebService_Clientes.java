package br.com.bornsolutions.bsreembolso.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 19/04/2016.
 */
public class WebService_Clientes extends AsyncTask<String, Integer, Boolean> {

    private Context _Ascontext;
    private Activity mActivity;
    ProgressDialog _progressDialog;
    Spinner _spinner;
    EditText edtSenha, edtLogin;
    String [] _sArrayClientes;

    public WebService_Clientes(Activity _psActivity, Context context, Spinner _psspinner) {
        this._Ascontext = context;
        _spinner = _psspinner;
        mActivity = _psActivity;
    }

    //COMUNICAÇÃO COM O DATASET
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://localhost/GuilhermeConnection/Selecao.asmx";
    private String SOAP_ACTION = "http://tempuri.org/prcGetDados_JSON1";
    private String METHOD_NAME = "prcGetDados_JSON1";

    @Override
    protected Boolean doInBackground(String... params) {

        CLG_WebService_Ksoap WebServiceMaster = new CLG_WebService_Ksoap();

        //CRIA REQUEST
        SoapObject request2 = WebServiceMaster.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME);

        //ADICIONA PARAMETRO STRING CONEXÃO
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psConexao", Constantes.DESKTOPBORN);

        String _strSQL = "SELECT CL_ID, CL_RAZAOSOCIAL, CL_NOMEREDUZIDO FROM CAD_CLIENTE ORDER BY CL_NOMEREDUZIDO ";

        //ADICIONA PARAMETRO QUERY
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psSelect", _strSQL);

        //ADICIONA PARAMETRO QUERY
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psNomeTabela", "CAD_CLIENTE");

        //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
        SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, URL, SOAP_ACTION);

        try {

            //RETORNA STRING NO FORMATO JSON
            SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();

            JSONArray _jsonArray = new JSONArray(result2.toString());

                _sArrayClientes = new String[_jsonArray.length()];

                for(int n = 0;n < _jsonArray.length();n++) {

                        JSONObject object = _jsonArray.getJSONObject(n);
                        try {
                            String CL_ID = object.getString("CL_ID");
                            String CL_RAZAOSOCIAL = object.getString("CL_RAZAOSOCIAL");
                            String CL_NOMEREDUZIDO = object.getString("CL_NOMEREDUZIDO");
                            _sArrayClientes[n] = CL_NOMEREDUZIDO;
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
    protected void onPostExecute(Boolean result) {
        _progressDialog.dismiss();

        //CRIA OS TIPOS DE DESPESAS
        //String[] _strADespesas = new String[]{"Estacionamento","Km Rodado","Refeição","Outros"};
        ArrayAdapter<String> ArrayTipoDespesa = new ArrayAdapter<String>(mActivity,android.R.layout.simple_spinner_item, _sArrayClientes);
        ArrayTipoDespesa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinner.setAdapter(ArrayTipoDespesa);

    }

    @Override
    protected void onPreExecute() {
        _progressDialog = ProgressDialog.show(_Ascontext, "Clientes", "Buscando Clientes, aguarde..", false, true);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

    }
}
