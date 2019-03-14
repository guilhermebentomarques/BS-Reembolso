package br.com.bornsolutions.bsreembolso.WebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import br.com.bornsolutions.bsreembolso.Activity.LoginActivity;
import br.com.bornsolutions.bsreembolso.Activity.MainActivity;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_GCM;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 29/08/2016.
 */
public class WS_GCM extends AsyncTask<String, String, Boolean> {

    private Context _Ascontext;
    private Activity mActivity;
    ProgressDialog _progressDialog;
    String _gsUsuario, _gsUsuarioID;
    Boolean _bRegistrar;

    //COMUNICAÇÃO COM O DATASET
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://localhost/GuilhermeConnection/BSReembolso.asmx";
    private String SOAP_ACTION = "http://tempuri.org/prcInsereDispositivo";
    private String METHOD_NAME = "prcInsereDispositivo";
    private String SOAP_ACTION_DEL = "http://tempuri.org/prcRemoveDispositivo";
    private String METHOD_NAME_DEL = "prcRemoveDispositivo";

    public WS_GCM(Activity _psActivity, Context context, Boolean _pbRegistrar, String _psUsuario, String _psUsuarioID) {
        this._Ascontext = context;
        this._gsUsuarioID = _psUsuarioID;
        this.mActivity = _psActivity;
        this._bRegistrar = _pbRegistrar;
        this._gsUsuario = _psUsuario;
    }

    @Override
    protected Boolean doInBackground(String... params) {

        String regID = CLG_GCM.getRegistrationID(_Ascontext);

        try {
            if(_bRegistrar) {
                if (regID == null) {
                    //FAZ REGISTRO E PEGA ID REGISTRATION
                    regID = CLG_GCM.register(_Ascontext, Constantes.PROJECT_NUMBER);

                    //ENVIA PARA O BANCO
                    CLG_WebService_Ksoap WebServiceMaster = new CLG_WebService_Ksoap();
                    //CRIA REQUEST
                    SoapObject request2 = WebServiceMaster.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME);
                    WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCC_ID", _gsUsuarioID);
                    WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCCC_CELULAR_CODIGO", String.valueOf(regID));
                    WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCCC_APP_NOME", "BS REEMBOLSO");
                    WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCCC_CELULAR_NOME", "TESTE");
                    WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCCC_ATIVO", "1");
                    WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psUsuario", _gsUsuario);

                    SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, URL, SOAP_ACTION);

                    try {
                        //RETORNA STRING NO FORMATO JSON
                        SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();
                        String _strResultado = result2.toString();
                    }
                    catch (Exception ex)
                    {
                        String _sErro = ex.getMessage();
                    }

                    Log.d("GCM", "Registrado com Sucesso " + regID);
                    publishProgress(regID.toString());
                }
                else {
                    Log.d("GCM", "Registro existente" + regID);
                    publishProgress(regID.toString());
                }
            }
            else {
                CLG_GCM.unregister(_Ascontext, Constantes.PROJECT_NUMBER);

                //ENVIA PARA O BANCO
                CLG_WebService_Ksoap WebServiceMaster = new CLG_WebService_Ksoap();
                //CRIA REQUEST
                SoapObject request2 = WebServiceMaster.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME_DEL);
                WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCC_ID", _gsUsuarioID);
                WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCCC_CELULAR_CODIGO", String.valueOf(regID));
                WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCCC_APP_NOME", "BS REEMBOLSO");
                WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCCC_CELULAR_NOME", "TESTE");
                WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCCC_ATIVO", "1");
                WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psUsuario", _gsUsuario);

                SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, URL, SOAP_ACTION_DEL);

                try {
                    //RETORNA STRING NO FORMATO JSON
                    SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();
                    String _strResultado = result2.toString();
                }
                catch (Exception ex)
                {

                }

                Log.d("GCM", "Registro cancelado");
            }

        } catch (Exception ex) {

        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        //_progressDialog.dismiss();
    }

    @Override
    protected void onPreExecute() {
        //_progressDialog = ProgressDialog.show(_Ascontext, "GCM Registro", "Por favor, aguarde..", false, true);
    }

    @Override
    protected void onProgressUpdate(String... values)
    {
        //this._gsUsuario

        //ENVIA E-MAIL COM O GCM
        String _sValor = values[0].toString();
    }
}
