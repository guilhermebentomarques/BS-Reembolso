package br.com.bornsolutions.bsreembolso.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.bornsolutions.bsreembolso.Activity.MainActivity;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Conexao;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Notification;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_WebService_Ksoap;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 14/08/2016.
 */
public class VerificaDespesas extends IntentService {

    //CLASSE SERVIÇO É A CLASSE QUE CHAMA O SERVIÇO EM BACKGROUND QUE VERIFICA SE EXISTEM DESPESAS ATIVAS

    private SharedPreferences preferences;

    String prpUsuario_Logado ,prpUsuario_ID;

    //COMUNICAÇÃO COM O DATASET
    private final String NAMESPACE = "http://tempuri.org/";
    private final String _URL = "http://localhost/GuilhermeConnection/BSReembolso.asmx";
    private String _SOAP_ACTION = "http://tempuri.org/prcGetDados_JSON";
    private String METHOD_NAME = "prcGetDados_JSON";
    Integer _iCountDespesas = 0;
    Integer _iCountDespesasItem = 0;

    public VerificaDespesas(){
    super("BSReembolso");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        CLG_Conexao CLG_Conexao = new CLG_Conexao(this);

        //VERIFICA SE USUÁRIO ESTÁ CONECTADO A INTERNET
        if(CLG_Conexao.verificaConexao()) {

            try {
                CL_Preferences Preferences = new CL_Preferences();
                preferences = getSharedPreferences("CAD_USUARIO", Context.MODE_PRIVATE);
                prpUsuario_Logado = preferences.getString("CU_LOGIN", null);
                prpUsuario_ID = preferences.getString("CU_ID", null);
            }
            catch (Exception ex){
                return;
            }

            CLG_WebService_Ksoap WebServiceMaster = new CLG_WebService_Ksoap();

            //CRIA REQUEST
            SoapObject request2 = WebServiceMaster.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME);

            //ADICIONA PARAMETRO STRING CONEXÃO
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psConexao", Constantes.DESKTOPBORN);

            String _strSQL = "SELECT " +
                    "   CVD_ID, " +
                    "           CC_ID, " +
                    "           CL_ID, " +
                    "           CVD_TIPO, " +
                    "           CONVERT(VARCHAR(10),CVD_VIAGEM_DATAINICIO,103) CVD_VIAGEM_DATAINICIO, " +
                    "           CONVERT(VARCHAR(10),CVD_VIAGEM_DATAFIM,103) CVD_VIAGEM_DATAFIM, " +
                    "           CONVERT(VARCHAR(10),CVD_ADIANTAMENTO_DATA,103) CVD_ADIANTAMENTO_DATA, " +
                    "           CVD_ADIANTAMENTO_VALOR, " +
                    "           CVD_DESCRICAO, " +
                    "           CVD_FINALIZADO, " +
                    "           CVD_ENVIADO_APP " +
                    "   FROM " +
                    "           CAD_VIAGEM_DESPESA " +
                    "   WHERE " +
                    "           CC_ID = " + prpUsuario_ID + "  " +
                    "           AND CVD_FINALIZADO = '0' ";

            //ADICIONA PARAMETRO QUERY
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psSelect", _strSQL);

            //ADICIONA PARAMETRO QUERY
            WebServiceMaster.Master_Web_03_prcAdicionaParametroWebService(request2, "_psNomeTabela", "CAD_VIAGEM_DESPESA");

            //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
            SoapSerializationEnvelope envelope2 = WebServiceMaster.Master_Web_04_prcCriaEnvelope(request2, _URL, _SOAP_ACTION);

            try {

                //RETORNA STRING NO FORMATO JSON
                SoapPrimitive result2 = (SoapPrimitive) envelope2.getResponse();
                JSONArray _jsonArray = new JSONArray(result2.toString());

                for (int n = 0; n < _jsonArray.length(); n++) {

                    JSONObject object = _jsonArray.getJSONObject(n);
                    try {
                            TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA = new TB_CAD_VIAGEM_DESPESA(this);

                            //VERIFICA SE A DESPESA JÁ EXISTE
                            if (!TB_CAD_VIAGEM_DESPESA.prcVerificaExiste(object.getString("CVD_ID"), object.getString("CC_ID"))) {
                                _iCountDespesas++;

                                CAD_VIAGEM_DESPESA CAD_VIAGEM_DESPESA = new CAD_VIAGEM_DESPESA();

                                CAD_VIAGEM_DESPESA.setCU_ID(Integer.parseInt(object.getString("CC_ID")));
                                CAD_VIAGEM_DESPESA.setCVD_ID_BORN(Integer.parseInt(object.getString("CVD_ID")));
                                CAD_VIAGEM_DESPESA.setCL_ID(Integer.parseInt(object.getString("CL_ID")));
                                CAD_VIAGEM_DESPESA.setCVD_DESCRICAO(object.getString("CVD_DESCRICAO"));

                                CAD_VIAGEM_DESPESA.setCVD_ADIANTAMENTO_VALOR(Double.parseDouble(object.getString("CVD_ADIANTAMENTO_VALOR")));

                                //ADIANTAMENTO DATA
                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                try {
                                    Date nasc = dateFormat.parse(object.getString("CVD_ADIANTAMENTO_DATA").toString());
                                    CAD_VIAGEM_DESPESA.setCVD_ADIANTAMENTO_DATA(nasc);
                                } catch (ParseException error) {
                                    error.printStackTrace();
                                }

                                //DATA PARTIDA - VIAGEM
                                try {
                                    Date nasc = dateFormat.parse(object.getString("CVD_VIAGEM_DATAINICIO").toString());
                                    CAD_VIAGEM_DESPESA.setCVD_VIAGEM_DATA_INICIO(nasc);
                                } catch (ParseException error) {
                                    error.printStackTrace();
                                }

                                //DATA VOLTA - VIAGEM
                                try {
                                    Date nasc = dateFormat.parse(object.getString("CVD_VIAGEM_DATAFIM").toString());
                                    CAD_VIAGEM_DESPESA.setCVD_VIAGEM_DATA_FIM(nasc);
                                } catch (ParseException error) {
                                    error.printStackTrace();
                                }

                                CAD_VIAGEM_DESPESA.setCVD_DESCRICAO(object.getString("CVD_DESCRICAO"));

                                CAD_VIAGEM_DESPESA.setCVD_TIPO(object.getString("CVD_TIPO"));

                                String _sFin = object.getString("CVD_FINALIZADO");

                                String _sFinalziado = "";
                                if (object.getString("CVD_FINALIZADO").equals("false"))
                                    _sFinalziado = "N";
                                else
                                    _sFinalziado = "S";

                                CAD_VIAGEM_DESPESA.setCVD_FINALIZADO(_sFinalziado);

                                //INCLUI DESPESA
                                TB_CAD_VIAGEM_DESPESA.prcIncluir(this, CAD_VIAGEM_DESPESA, prpUsuario_ID, prpUsuario_Logado, "CADASTRO");
                            }
                            else
                            {
                                //SE A DESPESA EXISTE, VERIFICA SE ELA ESTÁ FECHADA NO APP - CASO ESTEJA, ABRE A MESMA NOVAMENTE.
                                String _sCVD_ID = object.getString("CVD_ID");

                                CAD_VIAGEM_DESPESA ViagemDespesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso_ID_BORN(Integer.parseInt(_sCVD_ID));
                                ViagemDespesa.setCVD_FINALIZADO("N");
                                TB_CAD_VIAGEM_DESPESA.prcAtualizar(ViagemDespesa);

                            }

                            //INCLUI SUB ITENS
                            CLG_WebService_Ksoap WebServiceMaster_CDI = new CLG_WebService_Ksoap();

                            //CRIA REQUEST
                            SoapObject request2_CDI = WebServiceMaster_CDI.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME);

                            //ADICIONA PARAMETRO STRING CONEXÃO
                            WebServiceMaster_CDI.Master_Web_03_prcAdicionaParametroWebService(request2_CDI, "_psConexao", Constantes.DESKTOPBORN);

                            String _strSQL_CDI = "SELECT " +
                                    "       CVDI_ID, " +
                                    "       CDI.CVD_ID, " +
                                    "       CVDI_TIPO_DESPESA, " +
                                    "       CONVERT(VARCHAR(10),CVDI_DATA,103) CVDI_DATA, " +
                                    "       CVDI_VALOR, " +
                                    "       CVDI_KM_VALOR, " +
                                    "       CVDI_KM_DISTANCIA, " +
                                    "       CVDI_DESPESA_BORN, " +
                                    "       CVDI_DESCRICAO " +
                                    "   FROM " +
                                    "   CAD_VIAGEM_DESPESA CD " +
                                    "   INNER JOIN CAD_VIAGEM_DESPESA_ITEM CDI ON (CD.CVD_ID = CDI.CVD_ID) " +
                                    "   WHERE " +
                                    "       CC_ID =  " + prpUsuario_ID + " " +
                                    "       AND CVD_FINALIZADO = '0' " +
                                    "       AND CDI.CVD_ID = " + object.getString("CVD_ID") + "  ";

                                //ADICIONA PARAMETRO QUERY
                            WebServiceMaster_CDI.Master_Web_03_prcAdicionaParametroWebService(request2_CDI, "_psSelect", _strSQL_CDI);

                                //ADICIONA PARAMETRO QUERY
                            WebServiceMaster_CDI.Master_Web_03_prcAdicionaParametroWebService(request2_CDI, "_psNomeTabela", "CAD_VIAGEM_DESPESA_ITEM");

                            //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
                            SoapSerializationEnvelope envelope2_CDI = WebServiceMaster_CDI.Master_Web_04_prcCriaEnvelope(request2_CDI, _URL, _SOAP_ACTION);

                            try {

                                //RETORNA STRING NO FORMATO JSON
                                SoapPrimitive result2_CDI = (SoapPrimitive) envelope2_CDI.getResponse();
                                JSONArray _jsonArray_CDI = new JSONArray(result2_CDI.toString());

                                for (int n_CDI = 0; n_CDI < _jsonArray_CDI.length(); n_CDI++) {

                                    JSONObject object_CDI = _jsonArray_CDI.getJSONObject(n_CDI);
                                    try {
                                        TB_CAD_VIAGEM_DESPESA_ITEM TB_CAD_VIAGEM_DESPESA_ITEM = new TB_CAD_VIAGEM_DESPESA_ITEM(this);

                                        //VERIFICA SE A DESPESA JÁ EXISTE
                                        if (!TB_CAD_VIAGEM_DESPESA_ITEM.prcVerificaExiste(object_CDI.getString("CVDI_ID"))) {
                                            _iCountDespesasItem++;

                                            CAD_VIAGEM_DESPESA_ITEM CAD_VIAGEM_DESPESA_Item = new CAD_VIAGEM_DESPESA_ITEM();

                                            CAD_VIAGEM_DESPESA Despesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso_ID_BORN(Integer.parseInt(object.getString("CVD_ID")));

                                            CAD_VIAGEM_DESPESA_Item.setCVD_ID(Despesa.getCVD_ID());
                                            CAD_VIAGEM_DESPESA_Item.setCVDI_ID_BORN(Integer.parseInt(object_CDI.getString("CVDI_ID")));
                                            CAD_VIAGEM_DESPESA_Item.setCVDI_DESCRICAO(object_CDI.getString("CVDI_DESCRICAO"));
                                            CAD_VIAGEM_DESPESA_Item.setCVDI_VALOR(Double.parseDouble(object_CDI.getString("CVDI_VALOR")));

                                            String _sTipoDespesa = "";
                                            if(object_CDI.getString("CVDI_TIPO_DESPESA").equals("CL"))
                                                _sTipoDespesa = "Celular";
                                            else if(object_CDI.getString("CVDI_TIPO_DESPESA").equals("ET"))
                                                _sTipoDespesa = "Estacionamento";
                                            else if(object_CDI.getString("CVDI_TIPO_DESPESA").equals("HT"))
                                                _sTipoDespesa = "Hotel";
                                            else if(object_CDI.getString("CVDI_TIPO_DESPESA").equals("KM"))
                                                _sTipoDespesa = "Km Rodado";
                                            else if(object_CDI.getString("CVDI_TIPO_DESPESA").equals("AL"))
                                                _sTipoDespesa = "Alimentação";
                                            else if(object_CDI.getString("CVDI_TIPO_DESPESA").equals("TX"))
                                                _sTipoDespesa = "Taxi";
                                            else if(object_CDI.getString("CVDI_TIPO_DESPESA").equals("PD"))
                                                _sTipoDespesa = "Pedágio";
                                            else if(object_CDI.getString("CVDI_TIPO_DESPESA").equals("OU"))
                                                _sTipoDespesa = "Outros";

                                            //CELULAR CL
                                            //ESTACIONAMENTO ET
                                            //HOTEL HT
                                            //KM RODADO KM
                                            //ALIMENTAÇÃO AL
                                            //TAXI TX
                                            //PEDÁGIO PD
                                            //OUTROS OU
                                            //public static final String[] TIPO_DESPESAS = new String[]{"Celular","Estacionamento","Hotel","Km Rodado","Alimentação","Taxi","Pedágio","Outros"};

                                            CAD_VIAGEM_DESPESA_Item.setCVDI_TIPO_DESPESA(_sTipoDespesa);

                                            String _sDespesaBorn = "";
                                            if (object_CDI.getString("CVDI_DESPESA_BORN").equals("false"))
                                                _sDespesaBorn = "N";
                                            else
                                                _sDespesaBorn = "S";

                                            CAD_VIAGEM_DESPESA_Item.setCVDI_DESPESA_BORN(_sDespesaBorn);

                                            try {
                                                CAD_VIAGEM_DESPESA_Item.setCVDI_KM_VALOR(Double.parseDouble(object_CDI.getString("CVDI_KM_VALOR")));
                                            }
                                            catch (Exception ex)
                                            {}

                                            try {
                                                CAD_VIAGEM_DESPESA_Item.setCVDI_KM_DISTANCIA(Double.parseDouble(object_CDI.getString("CVDI_KM_DISTANCIA")));
                                            }
                                            catch (Exception ex)
                                            {}

                                            //ADIANTAMENTO DATA
                                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                            try {
                                                Date nasc = dateFormat.parse(object_CDI.getString("CVDI_DATA").toString());
                                                CAD_VIAGEM_DESPESA_Item.setCVDI_DATA(nasc);
                                            } catch (ParseException error) {
                                                error.printStackTrace();
                                            }

                                            //INCLUI FOTO
                                            //INCLUI SUB ITENS
                                            CLG_WebService_Ksoap WebServiceMaster_CVDI_FOTO = new CLG_WebService_Ksoap();

                                            String _SOAP_ACTION_FOTO = "http://tempuri.org/prcGetFotoDespesa";
                                            String METHOD_NAME_FOTO = "prcGetFotoDespesa";

                                            //CRIA REQUEST
                                            SoapObject request2_CVDI_FOTO = WebServiceMaster_CVDI_FOTO.Master_Web_01_prcCriaRequest(NAMESPACE, METHOD_NAME_FOTO);

                                            //ADICIONA PARAMETRO QUERY
                                            WebServiceMaster_CVDI_FOTO.Master_Web_03_prcAdicionaParametroWebService(request2_CVDI_FOTO, "_psCVDI_ID", object_CDI.getString("CVDI_ID"));

                                            //CRIA ENVELOPE E ALIMENTA COM DADOS DO WEBSERVICE
                                            SoapSerializationEnvelope envelope2_CVDI_FOTO = WebServiceMaster_CVDI_FOTO.Master_Web_04_prcCriaEnvelope(request2_CVDI_FOTO, _URL, _SOAP_ACTION_FOTO);

                                            try {

                                                SoapPrimitive result2_CVDI_FOTO = null;
                                                try {
                                                    //RETORNA STRING NO FORMATO JSON
                                                    result2_CVDI_FOTO = (SoapPrimitive) envelope2_CVDI_FOTO.getResponse();
                                                }
                                                catch (Exception ex)
                                                {
                                                    //QUANDO NÃO TEM FOTO - DA ERRO DE NULL EXCEPTION
                                                    //INCLUI DESPESA SEM FOTO
                                                    TB_CAD_VIAGEM_DESPESA_ITEM.prcIncluir_WS(this, CAD_VIAGEM_DESPESA_Item);
                                                }

                                                String _psImg = result2_CVDI_FOTO.toString();

                                                    try {

                                                        //FOTO
                                                        byte[] imgBytes = Base64.decode(_psImg, Base64.DEFAULT);
                                                        Bitmap bmp = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
                                                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                                                        byte[] bArray = bos.toByteArray();

                                                        CAD_VIAGEM_DESPESA_Item.setCVDI_FOTO(bArray);

                                                        //INCLUI DESPESA - COM FOTO
                                                        TB_CAD_VIAGEM_DESPESA_ITEM.prcIncluir_WS(this, CAD_VIAGEM_DESPESA_Item);

                                                        //CARREGO A FOTO POIS A FOTO É MUITO PESADA PARA O JSON, E DEVE SER CARREGADA INDIVIDUALMENTE

                                                    } catch (Exception ex) {
                                                        String _strErro = ex.getMessage();
                                                    }
                                            }
                                            catch (Exception ex) {
                                                String _strErro = ex.getMessage();
                                            }
                                        }
                                    } catch (Exception ex) {
                                        String _strErro = ex.getMessage();
                                    }
                                }
                            }
                            catch (Exception ex)
                            {
                                String _strErro = ex.getMessage();
                            }

                    } catch (Exception ex) {
                        String _strErro = ex.getMessage();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            if (_iCountDespesas > 0 ) {
                //CRIA INTENT QUE SERÁ CHAMADA AO CLICAR NA NOTIFICAÇÃO
                //NESTE VAMOS ABRIR A MAINACTIVITY NOVAMENTE
                Intent Intentmsg = new Intent(this, MainActivity.class);
                //DISPARA A NOTIFICAÇÃO
                CLG_Notification.create(this, 1, Intentmsg, "BS Reembolso", " " + prpUsuario_Logado + ", Você tem nova(s) despesa(s)!");
            }

            if (_iCountDespesasItem > 0 ) {
                //CRIA INTENT QUE SERÁ CHAMADA AO CLICAR NA NOTIFICAÇÃO
                //NESTE VAMOS ABRIR A MAINACTIVITY NOVAMENTE
                Intent Intentmsg = new Intent(this, MainActivity.class);
                //DISPARA A NOTIFICAÇÃO
                CLG_Notification.create(this, 1, Intentmsg, "BS Reembolso", " " + prpUsuario_Logado + ", Você tem novo(s) itens(s)!");
            }
        }

    }
}
