package br.com.bornsolutions.bsreembolso.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import br.com.bornsolutions.bsreembolso.Activity.MainActivity;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_USUARIO;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_USUARIO;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 13/04/2016.
 */
public class WebService_Login extends AsyncTask<String, Integer, Boolean> {

    private Context _Ascontext;
    private Activity mActivity;
    private String _strLogin,_strSenha, _strUsuNum;
    ProgressDialog _progressDialog;
    EditText edtSenha, edtLogin;

    public WebService_Login(Activity _psActivity,Context context, EditText _pedtLogin, EditText _pedtSenha) {
        this._Ascontext = context;
        _strLogin = _pedtLogin.getText().toString();
        _strSenha = _pedtSenha.getText().toString();
        edtSenha = _pedtSenha;
        edtLogin = _pedtLogin;
        mActivity = _psActivity;
    }

    //COMUNICAÇÃO COM O DATASET
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://localhost/GuilhermeConnection/BSReembolso.asmx";
    private String SOAP_ACTION = "http://tempuri.org/prcGetDados_JSON";
    private String METHOD_NAME = "prcGetDados_JSON";

    @Override
    protected Boolean doInBackground (String... params){

        CLG_WebService_Ksoap WebServiceMaster = new CLG_WebService_Ksoap();

        //CRIA REQUEST
        SoapObject request2 = WebServiceMaster.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME);

        //ADICIONA PARAMETRO STRING CONEXÃO
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psConexao", Constantes.SEGURANCA);

        String _strSQL = "SELECT USU_LOGIN, USU_SENHA, USU_NUM FROM CAD_USUARIO WHERE USU_LOGIN = '" + _strLogin + "' AND USU_SENHA = '" + _strSenha + "' ";

        //ADICIONA PARAMETRO QUERY
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psSelect", _strSQL);

        //ADICIONA PARAMETRO QUERY
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psNomeTabela", "CAD_USUARIO");

        //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
        SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, URL, SOAP_ACTION);

        try {

            //RETORNA STRING NO FORMATO JSON
            SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();

            JSONArray _jsonArray = new JSONArray(result2.toString());

            if(_jsonArray.length() > 0)
            {
                JSONObject object = _jsonArray.getJSONObject(0);
                try {
                    String USU_SENHA = object.getString("USU_SENHA");
                    String USU_LOGIN = object.getString("USU_LOGIN");
                    _strUsuNum = object.getString("USU_NUM");
                } catch (Exception ex) {
                    return false;
                }
            }
            else {
                return false;
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
        _progressDialog.dismiss();

        if(!result) {
            edtSenha.setError("Login ou Senha Inválida!");
            edtLogin.setError("Login ou Senha Inválida!");
        }
        else
        {
            CL_Preferences Preferences = new CL_Preferences();
            Preferences.prcSet_SharedPreferences_Login(mActivity,_strLogin,_strSenha,_strUsuNum);

            Intent i = new Intent(_Ascontext, MainActivity.class);
            _Ascontext.startActivity(i);
            mActivity.finish(); //REMOVE ACTIVITY DA PILHA DO ANDROID.
        }
    }

    @Override
    protected void onPreExecute () {
        _progressDialog = ProgressDialog.show(_Ascontext, "Login", "Por favor, aguarde..", false, true);
    }

    @Override
    protected void onProgressUpdate (Integer... values){

    }


}
