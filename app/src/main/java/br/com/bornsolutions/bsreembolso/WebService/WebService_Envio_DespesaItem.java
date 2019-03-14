package br.com.bornsolutions.bsreembolso.WebService;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Fotos;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA_ITEM;

/**
 * Created by Guilherme on 18/08/2016.
 */

//(string _psCVD_ID, string _psCVDI_TIPO_DESPESA, string _psCVDI_VALOR, string _psCVDI_DATA, string _psCVDI_DESCRICAO, string _sDadosFoto)

public class WebService_Envio_DespesaItem extends AsyncTask<String, Integer, Boolean> {

    private Context _Ascontext;
    private Activity _mActivity;
    ProgressBar _progressBar;
    TextView _txtProgress;
    Integer _iTotalRegistros, _iRegistroCorrente;
    List<CAD_VIAGEM_DESPESA_ITEM> _gListaDespesaItem;
    String prpUsuarioLogado;

    public WebService_Envio_DespesaItem(List<CAD_VIAGEM_DESPESA_ITEM> _psListaDespesasItem,  Activity _psActivity, Context context, ProgressBar progressBar, TextView txtProgress, String _psUsuario) {
        this._Ascontext = context;
        _progressBar = progressBar;
        _txtProgress = txtProgress;
        _mActivity = _psActivity;
        _gListaDespesaItem = _psListaDespesasItem;
        prpUsuarioLogado = _psUsuario;
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

        _iRegistroCorrente = 0;
        _iTotalRegistros = _gListaDespesaItem.size();

        for(final CAD_VIAGEM_DESPESA_ITEM DespesaItem : _gListaDespesaItem) {

            TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA = new TB_CAD_VIAGEM_DESPESA(_Ascontext);
            CAD_VIAGEM_DESPESA Despesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso(DespesaItem.getCVD_ID());


            CLG_WebService_Ksoap WebServiceMaster = new CLG_WebService_Ksoap();

            //CRIA REQUEST
            SoapObject request2 = WebServiceMaster.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME);

            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_ID", String.valueOf(DespesaItem.getCVDI_ID_BORN()));

            //ADICIONA PARAMETRO STRING CONEXÃO
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVD_ID", String.valueOf(Despesa.getCVD_ID_BORN())); //BUSCA ID DA BORN PARA NÃO DAR ERRO DE FK

            String _sTipoDespesa = "";
            if(String.valueOf(DespesaItem.getCVDI_TIPO_DESPESA()).equals("Celular"))
                _sTipoDespesa = "CL";
            else if(String.valueOf(DespesaItem.getCVDI_TIPO_DESPESA()).equals("Estacionamento"))
                _sTipoDespesa = "ET";
            else if(String.valueOf(DespesaItem.getCVDI_TIPO_DESPESA()).equals("Hotel"))
                _sTipoDespesa = "HT";
            else if(String.valueOf(DespesaItem.getCVDI_TIPO_DESPESA()).equals("Km Rodado"))
                _sTipoDespesa = "KM";
            else if(String.valueOf(DespesaItem.getCVDI_TIPO_DESPESA()).equals("Alimentação"))
                _sTipoDespesa = "AL";
            else if(String.valueOf(DespesaItem.getCVDI_TIPO_DESPESA()).equals("Taxi"))
                _sTipoDespesa = "TX";
            else if(String.valueOf(DespesaItem.getCVDI_TIPO_DESPESA()).equals("Pedágio"))
                _sTipoDespesa = "PD";
            else if(String.valueOf(DespesaItem.getCVDI_TIPO_DESPESA()).equals("Outros"))
                _sTipoDespesa = "OU";



            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_TIPO_DESPESA", _sTipoDespesa);

            DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
            final String _strData = dateFormat.format(DespesaItem.getCVDI_DATA());
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_DATA", String.valueOf(_strData));
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_VALOR", String.valueOf(DespesaItem.getCVDI_VALOR()));

            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_DESCRICAO", String.valueOf(DespesaItem.getCVDI_DESCRICAO()));

            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_DESPESA_BORN", String.valueOf(DespesaItem.getCVDI_DESPESA_BORN()));

            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_KM_VALOR", String.valueOf(DespesaItem.getCVDI_KM_VALOR()));
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_KM_DISTANCIA", String.valueOf(DespesaItem.getCVDI_KM_DISTANCIA()));
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psUsuario", String.valueOf(prpUsuarioLogado));

            try {
                CLG_Fotos CL_FOTOS = new CLG_Fotos();
                String _sFoto = CL_FOTOS.prcAlteraFotoParaBase64(CL_FOTOS.prcSQLite_RetornaFoto(DespesaItem.getCVDI_FOTO()));
                WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVDI_FOTO", _sFoto);
            }
            catch (Exception ex)
            { }

            //UPDATE NA BASE DE DADOS OFICIAL, COM OS NOVOS VALORES
            //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
            SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, URL, SOAP_ACTION);

            try {

                //RETORNA STRING NO FORMATO JSON
                SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();
                String _strResultado = result2.toString();

                _iRegistroCorrente = _iRegistroCorrente + 1;
                publishProgress(_iRegistroCorrente);

                //DOU SPLIT PARA PEGAR O ID E DAR UPDATE NA BASE
                String[] _sDados = _strResultado.split("_");

                if (!_sDados[0].trim().equals("-1")) {
                    //JÁ DOU UPDATE NO BANCO DE DADOS COM O CÓDIGO INTERNO DO SISTEMA
                    TB_CAD_VIAGEM_DESPESA_ITEM TB_CAD_VIAGEM_DESPESA_ITEM = new TB_CAD_VIAGEM_DESPESA_ITEM(_Ascontext);
                    DespesaItem.setCVDI_ID_BORN(Integer.parseInt(_sDados[0].trim()));
                    TB_CAD_VIAGEM_DESPESA_ITEM.prcAtualizar(DespesaItem);
                } else {
                    //ATUALIZA O ITEM PARA NÃO ENVIADO - E NÃO PREENCHE O CAMPO CVD_ID_BORN
                    _bRetorno = false;
                }

            }catch (Exception e) {
                _bRetorno = false;
                e.printStackTrace();
            }

        }

        return _bRetorno;
    }

    @Override
    protected void onPostExecute (Boolean result){
        if(result)
            _txtProgress.setText("Despesas Itens Sincronizadas com sucesso!!");
        else
            _txtProgress.setText("Falha ao sincronizar!!");
    }

    @Override
    protected void onPreExecute () {
        _progressBar.setVisibility(View.VISIBLE);
        _progressBar.setProgress(0);
    }

    @Override
    protected void onProgressUpdate (Integer...values){

        //PROGRESS
        _progressBar.setProgress(values[0]);

        if(values[0] == 1)
            _progressBar.setMax(_iTotalRegistros);

        _txtProgress.setText("Sincronizando Despesa Itens - " + _iRegistroCorrente + "/" + _iTotalRegistros);

    }

}
