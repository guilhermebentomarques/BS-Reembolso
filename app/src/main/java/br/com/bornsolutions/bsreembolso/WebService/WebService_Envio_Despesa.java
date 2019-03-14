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

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA;

/**
 * Created by Guilherme on 18/08/2016.
 */
public class WebService_Envio_Despesa extends AsyncTask<String, Integer, Boolean> {

    private Context _Ascontext;
    private Activity _mActivity;
    ProgressBar _progressBar;
    TextView _txtProgress;
    Integer _iTotalRegistros, _iRegistroCorrente;
    List<CAD_VIAGEM_DESPESA> _gListaDespesa;
    String prpUsuarioLogado;

    public WebService_Envio_Despesa(List<CAD_VIAGEM_DESPESA> _psListaDespesas,  Activity _psActivity, Context context, ProgressBar progressBar, TextView txtProgress, String _psUsuario) {
        this._Ascontext = context;
        _progressBar = progressBar;
        _txtProgress = txtProgress;
        _mActivity = _psActivity;
        _gListaDespesa = _psListaDespesas;
        prpUsuarioLogado = _psUsuario;
    }

    //COMUNICAÇÃO COM O DATASET
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://localhost/GuilhermeConnection/BSReembolso.asmx";
    private String SOAP_ACTION = "http://tempuri.org/CadastrarDespesa";
    private String METHOD_NAME = "CadastrarDespesa";

    //ARRAY CAD_FILIAL
    String[] _strArrayCFL_ID;
    String[] _strArrayCFL_NOMEFANTASIA;

    @Override
    protected Boolean doInBackground (String...params){

        Boolean _bRetorno = true;

        _iRegistroCorrente = 0;
        _iTotalRegistros = _gListaDespesa.size();

        for(final CAD_VIAGEM_DESPESA Despesa : _gListaDespesa) {

            CLG_WebService_Ksoap WebServiceMaster = new CLG_WebService_Ksoap();

            //CRIA REQUEST
            SoapObject request2 = WebServiceMaster.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME);

            //ADICIONA PARAMETRO STRING CONEXÃO
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVD_ID", String.valueOf(Despesa.getCVD_ID_BORN()));
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCU_ID", String.valueOf(Despesa.getCU_ID()));
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCL_ID", String.valueOf(Despesa.getCL_ID()));
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVD_TIPO", String.valueOf(Despesa.getCVD_TIPO()));

            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            final String _strDataAdiantamento = dateFormat.format(Despesa.getCVD_ADIANTAMENTO_DATA());
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVD_ADIANTAMENTO_DATA", String.valueOf(_strDataAdiantamento));
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVD_ADIANTAMENTO_VALOR", String.valueOf(Despesa.getCVD_ADIANTAMENTO_VALOR()));

            final String _strDataViagemInicio = dateFormat.format(Despesa.getCVD_VIAGEM_DATA_INICIO());
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVD_VIAGEM_DATAINICIO", String.valueOf(_strDataViagemInicio));

            final String _strDataViagemFim = dateFormat.format(Despesa.getCVD_VIAGEM_DATA_FIM());
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVD_VIAGEM_DATAFIM", String.valueOf(_strDataViagemFim));


            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psCVD_DESCRICAO", String.valueOf(Despesa.getCVD_DESCRICAO()));

            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psUsuario", String.valueOf(prpUsuarioLogado));

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
                    TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA = new TB_CAD_VIAGEM_DESPESA(_Ascontext);
                    Despesa.setCVD_ID_BORN(Integer.parseInt(_sDados[0].trim()));
                    TB_CAD_VIAGEM_DESPESA.prcAtualizar(Despesa);
                    _bRetorno = true;
                } else {
                    _bRetorno = false;
                }
            } catch (Exception ex)
            {
                String _sErro = ex.getMessage();
                _bRetorno = false;
            }

        }

        return _bRetorno;
    }

    @Override
    protected void onPostExecute (Boolean result){
        if(result)
            _txtProgress.setText("Despesas Sincronizadas com sucesso!!");
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

        _txtProgress.setText("Sincronizando Despesa - " + _iRegistroCorrente + "/" + _iTotalRegistros);

    }

}
