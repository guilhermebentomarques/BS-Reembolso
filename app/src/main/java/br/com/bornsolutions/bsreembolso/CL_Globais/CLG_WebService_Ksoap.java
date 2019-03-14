package br.com.bornsolutions.bsreembolso.CL_Globais;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by GuilhermeBento on 07/03/2016.
 */
public class CLG_WebService_Ksoap {

    //CRIA O REQUEST
    public SoapObject Master_Web_01_prcCriaRequest(String _pNAMESPACE, String _pMETHOD_NAME)
    {
        SoapObject request = new SoapObject(_pNAMESPACE, _pMETHOD_NAME);
        return request;
    }

    //STRING DE CONEXAO COM SQL SERVER
    public String Master_Web_02_funGetConexao_BensPatrimoniais(String _psConexao)
    {
        String _strConexao = _psConexao;
        return _strConexao;
    }

    //ADICIONA PARAMETROS AO CHAMAR O WEBSERVICE
    public void Master_Web_03_prcAdicionaParametroWebService(SoapObject _prequest, String _pstrParametro, String _pstrValor) {

        PropertyInfo _piParametro = new PropertyInfo();
        _piParametro.setName(_pstrParametro);
        _piParametro.setValue(_pstrValor);
        _prequest.addProperty(_piParametro);
    }

    //CRIA UM ENVELOPE COM AS INFORMAÇÕES REQUISITADAS NO WEBSERVICE
    //URL = CAMINHO DO WEBSERVICE
    //SOAP_ACTION = TEMPURI/METODO
    public SoapSerializationEnvelope Master_Web_04_prcCriaEnvelope(SoapObject request, String URL, String SOAP_ACTION)
    {
        //CRIA ENVELOPE
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        //COMUNICA COM O WEBSERVICE
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
        } catch (Exception Ex) {
            Ex.printStackTrace();
        }

        return envelope;
    }

}
