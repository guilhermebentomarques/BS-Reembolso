package br.com.bornsolutions.bsreembolso.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Conexao;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.WebService.WS_Deleta_Despesa;
import br.com.bornsolutions.bsreembolso.WebService.WS_Deleta_DespesaItem;
import br.com.bornsolutions.bsreembolso.WebService.WS_Envio_Despesa;

/**
 * Created by guilherme on 26/04/2016.
 */
public class TB_CAD_VIAGEM_DESPESA extends ReembolsoDB {

    public TB_CAD_VIAGEM_DESPESA(Context context) {
        super(context); //INICIA O CONSTRUTOR DA CLASSE PAI, ANTES DE INICIAR ESTE CONSTRUTOR

        prcCriaTabela(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        prcCriaTabela(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void prcIncluir(Context context, CAD_VIAGEM_DESPESA Reembolso, String _psUsuarioID, String _psUsuarioLogado, String _psTipoPesquisa)
    {
        SQLiteDatabase db = getWritableDatabase(); //ON CREATE É CHAMADO AQUI
        ContentValues r = getREEMBOLSO_Values(Reembolso);

        TB_TID_LAST TID_LAST = new TB_TID_LAST(context);

        Integer _iCVD_ID = TID_LAST.prcMaxNum("CAD_VIAGEM_DESPESA");

        String _psFinalizado = "";
        if(r.get("CVD_FINALIZADO").toString().equals("null"))
            _psFinalizado = "N";
        else
            _psFinalizado = r.get("CVD_FINALIZADO").toString();



        StringBuilder query = new StringBuilder();
        query.append(" INSERT INTO CAD_VIAGEM_DESPESA( " +
                " CVD_ID, " +
                " CVD_ID_BORN, " +
                " CL_ID, " +
                " CU_ID, " +
                " CVD_ENVIADO_BORN, " +
                " CVD_FINALIZADO, " +
                " CVD_TIPO, " +
                " CVD_ADIANTAMENTO_DATA, " +
                " CVD_ADIANTAMENTO_VALOR, " +
                " CVD_VIAGEM_DATA_INICIO, " +
                " CVD_VIAGEM_DATA_FIM, " +
                " CVD_DESCRICAO" + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");

        db.execSQL(query.toString(), new String[]{
                _iCVD_ID.toString(),
                r.get("CVD_ID_BORN").toString(),
                r.get("CL_ID").toString(),
                r.get("CU_ID").toString(),
                "N",
                _psFinalizado,
                r.get("CVD_TIPO").toString(),
                r.get("CVD_ADIANTAMENTO_DATA").toString(),
                r.get("CVD_ADIANTAMENTO_VALOR").toString(),
                r.get("CVD_VIAGEM_DATA_INICIO").toString(),
                r.get("CVD_VIAGEM_DATA_FIM").toString(),
                r.get("CVD_DESCRICAO").toString()});


        //ENVIA DESPESA PELO WEB SERVICE
        CAD_VIAGEM_DESPESA Despesa = prcRetornaReembolso(_iCVD_ID);

        //ENVIA DESPESA POR WEB SERVICE
        try {
            //NO WEB SERVICE JÁ VALIDA TUDO
            WS_Envio_Despesa WebService_EnvioDespesa = new WS_Envio_Despesa(context, Despesa, _psUsuarioID, _psUsuarioLogado, _psTipoPesquisa);
            WebService_EnvioDespesa.execute();

        } catch (Exception ex) {
            String _sErro = ex.getMessage();
        }
    }

    public Integer prcAtualizar(CAD_VIAGEM_DESPESA Despesa)
    {
        Integer _iRetorno = 0;
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getREEMBOLSO_Values(Despesa);

        _iRetorno = db.update("CAD_VIAGEM_DESPESA", contentValues, "CVD_ID = ?", new String[]{String.valueOf(Despesa.getCVD_ID())});

        // closing connection
        db.close();

        return _iRetorno;
    }

    public Integer prcDeletar(Context context, int _piCVD_ID, CAD_VIAGEM_DESPESA _psDespesa, String _psUsuarioID, String _psUsuarioLogado, String _psTipoPesquisa)
    {
        Integer _iDelete = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        _iDelete = db.delete("CAD_VIAGEM_DESPESA", "CVD_ID = ?", new String[]{String.valueOf(_piCVD_ID)});

        if(_iDelete == 1)
        {
            //ENVIA DESPESA POR WEB SERVICE
            try {
                //NO WEB SERVICE JÁ VALIDA TUDO
                WS_Deleta_Despesa WS_Deleta_Despesa = new WS_Deleta_Despesa(context, _psDespesa, _psUsuarioID, _psUsuarioLogado, _psTipoPesquisa);
                WS_Deleta_Despesa.execute();

            } catch (Exception ex) {
                String _sErro = ex.getMessage();
            }
        }


        return _iDelete;
    }

    @NonNull
    private ContentValues getREEMBOLSO_Values(CAD_VIAGEM_DESPESA Reembolso) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("CVD_ID", String.valueOf(Reembolso.getCVD_ID()));

        try {
            contentValues.put("CVD_ID_BORN", String.valueOf(Reembolso.getCVD_ID_BORN()));
        }
        catch (Exception ex)
        {

        }

        contentValues.put("CL_ID", String.valueOf(Reembolso.getCL_ID()));
        contentValues.put("CU_ID", String.valueOf(Reembolso.getCU_ID()));
        contentValues.put("CVD_ADIANTAMENTO_DATA", Reembolso.getCVD_ADIANTAMENTO_DATA().getTime());
        contentValues.put("CVD_ADIANTAMENTO_VALOR", Reembolso.getCVD_ADIANTAMENTO_VALOR());
        contentValues.put("CVD_VIAGEM_DATA_INICIO", Reembolso.getCVD_VIAGEM_DATA_INICIO().getTime());
        contentValues.put("CVD_VIAGEM_DATA_FIM", Reembolso.getCVD_VIAGEM_DATA_FIM().getTime());
        contentValues.put("CVD_DESCRICAO", Reembolso.getCVD_DESCRICAO());
        contentValues.put("CVD_TIPO", Reembolso.getCVD_TIPO());
        contentValues.put("CVD_FINALIZADO", String.valueOf(Reembolso.getCVD_FINALIZADO()));

        return contentValues;
    }

    public List<CAD_VIAGEM_DESPESA> prcLista(String _psCC_ID, String _psTipoPesquisa, String _psDataInicio, String _psDataFim){
        List<CAD_VIAGEM_DESPESA> lista = new ArrayList<CAD_VIAGEM_DESPESA>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        String _sTipoPesquisa = "";
        if(_psTipoPesquisa.equals("CONSULTA")) {
            _sTipoPesquisa = "AND CVD_FINALIZADO = 'S' ";

            Date _dtInicio = null;
            Date _dtFim = null;
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                _dtInicio = dateFormat.parse(_psDataInicio);
                _dtFim = dateFormat.parse(_psDataFim);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                String[] _sDataInicio = _psDataInicio.split("/");
                String[] _sDataFim = _psDataFim.split("/");

                String _strDiaInicio = _sDataInicio[0];

                if (Integer.parseInt(_strDiaInicio) < 10)
                    _strDiaInicio = "0" + Integer.parseInt(_strDiaInicio);

                String _strMesInicio = _sDataInicio[1];

                if (Integer.parseInt(_strMesInicio) < 10)
                    _strMesInicio = "0" + Integer.parseInt(_strMesInicio);

                String _strAnoInicio = _sDataInicio[2];

                String _strDiaFim = _sDataFim[0];

                if (Integer.parseInt(_strDiaFim) < 10)
                    _strDiaFim = "0" + Integer.parseInt(_strDiaFim);

                String _strMesFim = _sDataFim[1];

                if (Integer.parseInt(_strMesFim) < 10)
                    _strMesFim = "0" + Integer.parseInt(_strMesFim);

                String _strAnoFim = _sDataFim[2];

                String _strDataInicio = _strAnoInicio + "-" + _strMesInicio + "-" + _strDiaInicio;
                String _strDataFim = _strAnoFim + "-" + _strMesFim + "-" + _strDiaFim;

                //QUERY
                String selectQuery = "SELECT " +
                        "  *  " +
                        "FROM " +
                        "  CAD_VIAGEM_DESPESA " +
                        "WHERE " +
                        "  CU_ID = " + _psCC_ID + " " +
                        "  " + _sTipoPesquisa + " " +
                        "   AND strftime('%Y-%m-%d', CVD_VIAGEM_DATA_INICIO / 1000, 'unixepoch')  BETWEEN '" + _strDataInicio + "' AND '" + _strDataFim + "' " +
                        "ORDER BY " +
                        "  CVD_ID ";
                cursor = db.rawQuery(selectQuery, null);
            }
            catch (Exception ex)
            {

            }
        }
        else {
            _sTipoPesquisa = "AND CVD_FINALIZADO = 'N' ";
            cursor = db.query("CAD_VIAGEM_DESPESA", null, "CU_ID = " + _psCC_ID + " " + _sTipoPesquisa + " ", null, null, null, "CVD_VIAGEM_DATA_INICIO");
        }

        while(cursor.moveToNext()){
            CAD_VIAGEM_DESPESA Reembolso = new CAD_VIAGEM_DESPESA();
            prcPreencheFromCursor(cursor, Reembolso);
            lista.add(Reembolso);
        }

        // closing connection
        cursor.close();
        db.close();

        return lista;
    }

    public List<CAD_VIAGEM_DESPESA> prcLista_Despesa_Pendente(String _psCC_ID){
        List<CAD_VIAGEM_DESPESA> lista = new ArrayList<CAD_VIAGEM_DESPESA>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_VIAGEM_DESPESA", null, "CU_ID = " + _psCC_ID + " AND CVD_ID_BORN = '0' ", null, null, null, "CVD_VIAGEM_DATA_INICIO");

        while(cursor.moveToNext()){
            CAD_VIAGEM_DESPESA Reembolso = new CAD_VIAGEM_DESPESA();
            prcPreencheFromCursor(cursor, Reembolso);
            lista.add(Reembolso);
        }

        // closing connection
        cursor.close();
        db.close();

        return lista;
    }

    public CAD_VIAGEM_DESPESA prcRetornaReembolso(int _piCVD_ID){

        CAD_VIAGEM_DESPESA r = new CAD_VIAGEM_DESPESA();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_VIAGEM_DESPESA", null, "CVD_ID = ?", new String[]{String.valueOf(_piCVD_ID)}, null, null, "CVD_ID");

        if(cursor.moveToNext()){
            prcPreencheFromCursor(cursor, r);
        }

        // closing connection
        cursor.close();
        db.close();

        return r;
    }

    public CAD_VIAGEM_DESPESA prcRetornaReembolso_ID_BORN(int _piCVD_ID_BORN){

        CAD_VIAGEM_DESPESA r = new CAD_VIAGEM_DESPESA();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_VIAGEM_DESPESA", null, "CVD_ID_BORN = ?", new String[]{String.valueOf(_piCVD_ID_BORN)}, null, null, "CVD_ID");

        if(cursor.moveToNext()){
            prcPreencheFromCursor(cursor, r);
        }

        // closing connection
        cursor.close();
        db.close();

        return r;
    }

    private void prcPreencheFromCursor(Cursor cursor, CAD_VIAGEM_DESPESA Reembolso) {
        Reembolso.setCVD_ID(cursor.getInt(cursor.getColumnIndex("CVD_ID")));

        try {
            Reembolso.setCVD_ID_BORN(cursor.getInt(cursor.getColumnIndex("CVD_ID_BORN")));
        }catch (Exception ex){

        }

        Reembolso.setCL_ID(cursor.getInt(cursor.getColumnIndex("CL_ID")));
        Reembolso.setCU_ID(cursor.getInt(cursor.getColumnIndex("CU_ID")));

        long _iDataNasc = cursor.getLong(cursor.getColumnIndex("CVD_ADIANTAMENTO_DATA"));
        Date dtNasc = new Date();
        dtNasc.setTime(_iDataNasc);
        Reembolso.setCVD_ADIANTAMENTO_DATA(dtNasc);

        Reembolso.setCVD_ADIANTAMENTO_VALOR(cursor.getDouble(cursor.getColumnIndex("CVD_ADIANTAMENTO_VALOR")));

        long _iDataViagemInicio = cursor.getLong(cursor.getColumnIndex("CVD_VIAGEM_DATA_INICIO"));
        Date _dtViagemInicio = new Date();
        _dtViagemInicio.setTime(_iDataViagemInicio);
        Reembolso.setCVD_VIAGEM_DATA_INICIO(_dtViagemInicio);

        long _iDataViagemFim = cursor.getLong(cursor.getColumnIndex("CVD_VIAGEM_DATA_FIM"));
        Date _dtViagemFim = new Date();
        _dtViagemFim.setTime(_iDataViagemFim);
        Reembolso.setCVD_VIAGEM_DATA_FIM(_dtViagemFim);

        Reembolso.setCVD_DESCRICAO(cursor.getString(cursor.getColumnIndex("CVD_DESCRICAO")));

        Reembolso.setCVD_TIPO(cursor.getString(cursor.getColumnIndex("CVD_TIPO")));
        Reembolso.setCVD_FINALIZADO(cursor.getString(cursor.getColumnIndex("CVD_FINALIZADO")));
    }

    public Boolean prcVerificaExiste(String _psCVD_ID, String _psCU_ID)
    {
        // Select All Query
        String selectQuery = "SELECT CVD_ID FROM CAD_VIAGEM_DESPESA WHERE CVD_ID_BORN = " + _psCVD_ID + " AND CU_ID = " + _psCU_ID +" ORDER BY CVD_ID ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String _strCidade = "";
        Boolean _bExiste = false;

        // looping through all rows and adding to list
        if ( cursor.moveToFirst () ) {
            do {
                _bExiste = true;
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning labels
        return _bExiste;
    }
}
