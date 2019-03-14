package br.com.bornsolutions.bsreembolso.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.bornsolutions.bsreembolso.Activity.MainActivity;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_USUARIO;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_USUARIO;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 21/04/2016.
 */
public class WS_CAD_CLIENTE extends AsyncTask<String, Integer, Boolean> {

        private Context _Ascontext;
        private Activity _mActivity;
        ProgressBar _progressBar;
        TextView _txtProgress;
        Integer _iTotalRegistros, _iRegistroCorrente;

        public WS_CAD_CLIENTE(Activity _psActivity, Context context, ProgressBar progressBar, TextView txtProgress) {
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

                String _strSQL = "SELECT CL_ID, CL_CPFCNPJ, CL_RAZAOSOCIAL, CL_NOMEFANTASIA, CL_NOMEREDUZIDO, CL_CIDADE, CL_ESTADO FROM CAD_CLIENTE ";

                //ADICIONA PARAMETRO QUERY
                WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psSelect", _strSQL);

                //ADICIONA PARAMETRO QUERY
                WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psNomeTabela", "CAD_USUARIO");

                //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
                SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, _URL, _SOAP_ACTION);

                try {

                        //RETORNA STRING NO FORMATO JSON
                        SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();

                        JSONArray _jsonArray = new JSONArray(result2.toString());

                        _iTotalRegistros = _jsonArray.length();

                        for(int n = 0; n < _jsonArray.length();n++) {

                                String _strUF = "";
                                String _strCidade = "";

                                JSONObject object = _jsonArray.getJSONObject(n);
                                try
                                {
                                        _iRegistroCorrente = n + 1;

                                        publishProgress(_iRegistroCorrente);

                                        CAD_CLIENTE Cliente = new CAD_CLIENTE();

                                        Cliente.setCL_ID(Integer.parseInt(object.getString("CL_ID")));
                                        Cliente.setCL_CPFCNPJ(object.getString("CL_CPFCNPJ"));
                                        Cliente.setCL_RAZAOSOCIAL(object.getString("CL_RAZAOSOCIAL"));
                                        Cliente.setCL_NOMEFANTASIA(object.getString("CL_NOMEFANTASIA"));
                                        Cliente.setCL_NOMEREDUZIDO(object.getString("CL_NOMEREDUZIDO"));
                                        Cliente.setCL_CIDADE(object.getString("CL_CIDADE"));
                                        Cliente.setCL_UF(object.getString("CL_ESTADO"));


                                        /*WebService_Cep WS_CEP = new WebService_Cep();
                                        String _strCidadeCep = WS_CEP.prcRetornaArrayCEP(object.getString("CL_CEP").replace("-",""));

                                        if(_strCidadeCep.equals(""))
                                        {
                                                _strCidade = "";
                                                _strUF = "";
                                        }
                                        else{

                                                String[] parts_cidade = _strCidadeCep.split("-");
                                                _strUF = parts_cidade[0]; // DIA
                                                _strCidade = parts_cidade[1]; // MES
                                                Cliente.setCL_CIDADE(_strCidade);
                                                Cliente.setCL_UF(_strUF);
                                        }*/

                                        TB_CAD_CLIENTE TB_CAD_CLIENTE = new TB_CAD_CLIENTE(_Ascontext);

                                        //VERIFICA SE O CLIENTE JÁ EXISTE
                                        if(!TB_CAD_CLIENTE.prcVerificaClienteExiste(object.getString("CL_ID")))
                                        {
                                            TB_CAD_CLIENTE.prcIncluir(Cliente);
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
            _txtProgress.setText("Clientes Sincronizados com sucesso!!");
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

                _txtProgress.setText("Sincronizando Clientes - " + _iRegistroCorrente + "/" + _iTotalRegistros);

        }


}
