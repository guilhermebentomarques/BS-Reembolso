package br.com.bornsolutions.bsreembolso.WebService;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import br.com.bornsolutions.bsreembolso.Activity.ListaDespesa;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA;

/**
 * Created by Guilherme on 06/10/2016.
 */
public class WS_Envio_Despesa_Reabrir extends AsyncTask<String, String, Boolean> {

    CAD_VIAGEM_DESPESA _gDespesa;
    Context context;
    ProgressDialog pdLoading;
    String prpUsuarioID, prpUsuarioLogado, prpTipoPesquisa;

    public WS_Envio_Despesa_Reabrir(Context context, CAD_VIAGEM_DESPESA _psDespesa, String _psUsuarioID, String _psUsuarioLogado, String _psTipoPesquisa) {
        this._gDespesa = _psDespesa;
        this.prpUsuarioID = _psUsuarioID;
        this.prpUsuarioLogado = _psUsuarioLogado;
        this.prpTipoPesquisa = _psTipoPesquisa;
        this.context = context;
    }

    //COMUNICAÇÃO COM O DATASET
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://localhost/GuilhermeConnection/BSReembolso.asmx";
    private String SOAP_ACTION = "http://tempuri.org/prcReabrirDespesa";
    private String METHOD_NAME = "prcReabrirDespesa";

    //ARRAY CAD_FILIAL
    String[] _strArrayCFL_ID;
    String[] _strArrayCFL_NOMEFANTASIA;

    @Override
    protected Boolean doInBackground(String... params) {

        CLG_WebService_Ksoap WebServiceMaster = new CLG_WebService_Ksoap();

        //CRIA REQUEST
        SoapObject request2 = WebServiceMaster.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME);

        //ADICIONA PARAMETRO STRING CONEXÃO
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVD_ID", String.valueOf(_gDespesa.getCVD_ID_BORN()));

        //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
        SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, URL, SOAP_ACTION);

        try {
            //RETORNA STRING NO FORMATO JSON
            SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();
            String _strResultado = result2.toString();

            //DOU SPLIT PARA PEGAR O ID E DAR UPDATE NA BASE
            String[] _sDados = _strResultado.split("_");

            if (!_sDados[0].trim().equals("-1")) {
                TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA = new TB_CAD_VIAGEM_DESPESA(context);
                TB_CAD_VIAGEM_DESPESA.prcAtualizar(_gDespesa);
            } else {
                //ATUALIZA O ITEM PARA NÃO ENVIADO - E NÃO PREENCHE O CAMPO CD_ID_BORN
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        pdLoading.dismiss();

        if (result) {

            Toast.makeText(context, "Dados Enviados com Sucesso!", Toast.LENGTH_LONG).show();

            //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
            Intent i = new Intent(context, ListaDespesa.class);

            Bundle params = new Bundle();
            params.putString("CU_LOGIN", prpUsuarioLogado);
            params.putString("CU_ID", prpUsuarioID);
            params.putString("TIPO_PESQUISA", prpTipoPesquisa);
            i.putExtras(params);

            context.startActivity(i);
        } else {
            Toast.makeText(context, "Falha ao enviar dados!", Toast.LENGTH_LONG).show();

            //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
            Intent i = new Intent(context, ListaDespesa.class);

            Bundle params = new Bundle();
            params.putString("CU_LOGIN", prpUsuarioLogado);
            params.putString("CU_ID", prpUsuarioID);
            params.putString("TIPO_PESQUISA", prpTipoPesquisa);
            i.putExtras(params);

            context.startActivity(i);
        }
    }

    @Override
    protected void onPreExecute() {
        pdLoading = ProgressDialog.show(context, "Enviando", "Por favor, aguarde..", false, true);
    }

    @Override
    protected void onProgressUpdate(String... values) {
    }
}