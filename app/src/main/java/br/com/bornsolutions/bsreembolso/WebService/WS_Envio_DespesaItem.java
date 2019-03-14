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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import br.com.bornsolutions.bsreembolso.Activity.DespesaItensActivity;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Fotos;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA_ITEM;

/**
 * Created by Guilherme on 18/08/2016.
 */

//(string _psCVD_ID, string _psCVDI_TIPO_DESPESA, string _psCVDI_VALOR, string _psCVDI_DATA, string _psCVDI_DESCRICAO, string _sDadosFoto)

public class WS_Envio_DespesaItem extends AsyncTask<String, String, Boolean> {

    CAD_VIAGEM_DESPESA_ITEM _gDespesaItem;
    CAD_VIAGEM_DESPESA _gDespesa;
    Context context;
    ProgressDialog pdLoading;
    String prpUsuarioID, prpUsuarioLogado, prpTipoPesquisa;

    public WS_Envio_DespesaItem(Context context, CAD_VIAGEM_DESPESA_ITEM _psDespesaItem, CAD_VIAGEM_DESPESA _psDespesa, String _psUsuarioID, String _psUsuarioLogado, String _psTipoPesquisa) {
        this._gDespesaItem = _psDespesaItem;
        this._gDespesa = _psDespesa;
        this.prpUsuarioID = _psUsuarioID;
        this.prpUsuarioLogado = _psUsuarioLogado;
        this.prpTipoPesquisa = _psTipoPesquisa;
        this.context = context;
    }

    //COMUNICAÇÃO COM O DATASET
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://localhost/GuilhermeConnection/BSReembolso.asmx";
    private String SOAP_ACTION = "http://tempuri.org/CadastrarDespesaItem";
    private String METHOD_NAME = "CadastrarDespesaItem";

    //ARRAY CAD_FILIAL
    String[] _strArrayCFL_ID;
    String[] _strArrayCFL_NOMEFANTASIA;

    @Override
    protected Boolean doInBackground (String...params){

        Boolean _bRetorno = true;

        CLG_WebService_Ksoap WebServiceMaster = new CLG_WebService_Ksoap();

        //CRIA REQUEST
        SoapObject request2 = WebServiceMaster.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME);

        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_ID", String.valueOf(_gDespesaItem.getCVDI_ID_BORN()));

        //ADICIONA PARAMETRO STRING CONEXÃO
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVD_ID", String.valueOf(_gDespesa.getCVD_ID_BORN())); //BUSCA ID DA BORN PARA NÃO DAR ERRO DE FK

        //CELULAR CL
        //ESTACIONAMENTO ET
        //HOTEL HT
        //KM RODADO KM
        //ALIMENTAÇÃO AL
        //TAXI TX
        //PEDÁGIO PD
        //OUTROS OU
        //public static final String[] TIPO_DESPESAS = new String[]{"Celular","Estacionamento","Hotel","Km Rodado","Alimentação","Taxi","Pedágio","Outros"};


        String _sTipoDespesa = "";
        if(String.valueOf(_gDespesaItem.getCVDI_TIPO_DESPESA()).equals("Celular"))
            _sTipoDespesa = "CL";
        else if(String.valueOf(_gDespesaItem.getCVDI_TIPO_DESPESA()).equals("Estacionamento"))
            _sTipoDespesa = "ET";
        else if(String.valueOf(_gDespesaItem.getCVDI_TIPO_DESPESA()).equals("Hotel"))
            _sTipoDespesa = "HT";
        else if(String.valueOf(_gDespesaItem.getCVDI_TIPO_DESPESA()).equals("Km Rodado"))
            _sTipoDespesa = "KM";
        else if(String.valueOf(_gDespesaItem.getCVDI_TIPO_DESPESA()).equals("Alimentação"))
            _sTipoDespesa = "AL";
        else if(String.valueOf(_gDespesaItem.getCVDI_TIPO_DESPESA()).equals("Taxi"))
            _sTipoDespesa = "TX";
        else if(String.valueOf(_gDespesaItem.getCVDI_TIPO_DESPESA()).equals("Pedágio"))
            _sTipoDespesa = "PD";
        else if(String.valueOf(_gDespesaItem.getCVDI_TIPO_DESPESA()).equals("Outros"))
            _sTipoDespesa = "OU";

        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_TIPO_DESPESA", _sTipoDespesa);

        DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
        final String _strData = dateFormat.format(_gDespesaItem.getCVDI_DATA());
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_DATA", String.valueOf(_strData));
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_VALOR", String.valueOf(_gDespesaItem.getCVDI_VALOR()));

        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_DESCRICAO", String.valueOf(_gDespesaItem.getCVDI_DESCRICAO()));

        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_DESPESA_BORN", String.valueOf(_gDespesaItem.getCVDI_DESPESA_BORN()));

        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_KM_VALOR", String.valueOf(_gDespesaItem.getCVDI_KM_VALOR()));
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_KM_DISTANCIA", String.valueOf(_gDespesaItem.getCVDI_KM_DISTANCIA()));
        WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psUsuario", String.valueOf(prpUsuarioLogado));

        try {
            CLG_Fotos CL_FOTOS = new CLG_Fotos();
            String _sFoto = CL_FOTOS.prcAlteraFotoParaBase64(CL_FOTOS.prcSQLite_RetornaFoto(_gDespesaItem.getCVDI_FOTO()));
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_FOTO", _sFoto);
        }
        catch (Exception ex)
        { }

        //SE TEM ID BORN = 0 DA UPDATE, SE NÃO TEM ID BORN DA UM INSERT
        if(String.valueOf(_gDespesaItem.getCVDI_ID_BORN()) != "0")
        {
            //UPDATE NA BASE DE DADOS OFICIAL, COM OS NOVOS VALORES
            //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
            SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, URL, SOAP_ACTION);

            try {
                //RETORNA STRING NO FORMATO JSON
                SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();
                String _strResultado = result2.toString();

                //DOU SPLIT PARA PEGAR O ID E DAR UPDATE NA BASE
                String[] _sDados = _strResultado.split("_");

                if (!_sDados[0].trim().equals("-1")) {
                    return true;
                }
                else
                {
                    return false;
                }
            }catch (Exception e) {
                _bRetorno = false;
                e.printStackTrace();
            }
        }
        else
        {
            //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
            SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, URL, SOAP_ACTION);

            try {
                    //RETORNA STRING NO FORMATO JSON
                    SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();
                    String _strResultado = result2.toString();

                    //DOU SPLIT PARA PEGAR O ID E DAR UPDATE NA BASE
                    String[] _sDados = _strResultado.split("_");

                    if (!_sDados[0].trim().equals("-1")) {
                        //JÁ DOU UPDATE NO BANCO DE DADOS COM O CÓDIGO INTERNO DO SISTEMA
                        TB_CAD_VIAGEM_DESPESA_ITEM TB_CAD_VIAGEM_DESPESA_ITEM = new TB_CAD_VIAGEM_DESPESA_ITEM(context);
                        _gDespesaItem.setCVDI_ID_BORN(Integer.parseInt(_sDados[0].trim()));
                        TB_CAD_VIAGEM_DESPESA_ITEM.prcAtualizar(_gDespesaItem);
                    } else {
                        //ATUALIZA O ITEM PARA NÃO ENVIADO - E NÃO PREENCHE O CAMPO CVD_ID_BORN
                        _bRetorno = false;
                    }

            } catch (Exception e) {
                _bRetorno = false;
                e.printStackTrace();
            }
        }

        return _bRetorno;
    }

    @Override
    protected void onPostExecute (Boolean result){
        //pdLoading = ProgressDialog.show(context, "Enviado", "Dados enviados com sucesso..", false, true);
        pdLoading.dismiss();

        if(result) {

            Toast.makeText(context, "Dados Enviados com Sucesso!", Toast.LENGTH_LONG).show();

            //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
            Intent i = new Intent(context, DespesaItensActivity.class);

            i.putExtra("Reembolso", _gDespesa);

            Bundle params = new Bundle();
            params.putString("CU_LOGIN", prpUsuarioLogado);
            params.putString("CU_ID", prpUsuarioID);
            params.putString("TIPO_PESQUISA", prpTipoPesquisa);
            i.putExtras(params);

            context.startActivity(i);
        }
        else
        {
            Toast.makeText(context, "Falha ao enviar dados!", Toast.LENGTH_LONG).show();

            //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
            Intent i = new Intent(context, DespesaItensActivity.class);

            i.putExtra("Reembolso", _gDespesa);

            Bundle params = new Bundle();
            params.putString("CU_LOGIN", prpUsuarioLogado);
            params.putString("CU_ID", prpUsuarioID);
            params.putString("TIPO_PESQUISA", prpTipoPesquisa);
            i.putExtras(params);

            context.startActivity(i);
        }

    }

    @Override
    protected void onPreExecute () {
        pdLoading = ProgressDialog.show(context, "Enviando", "Por favor, aguarde..", false, true);
    }

    @Override
    protected void onProgressUpdate (String...values){


    }

}
