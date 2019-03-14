package br.com.bornsolutions.bsreembolso.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.WebService.WS_Deleta_DespesaItem;
import br.com.bornsolutions.bsreembolso.WebService.WS_Envio_DespesaItem;

/**
 * Created by guilherme on 01/05/2016.
 */
public class TB_CAD_VIAGEM_DESPESA_ITEM extends ReembolsoDB {

    public TB_CAD_VIAGEM_DESPESA_ITEM(Context context) {
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

    public void prcIncluir(Context context, CAD_VIAGEM_DESPESA_ITEM _psDespesaItem, String _psUsuarioID, String _psUsuarioLogado, String _psTipoPesquisa)
    {
        SQLiteDatabase db = getWritableDatabase(); //ON CREATE É CHAMADO AQUI
        ContentValues c = getValues_CAD_VIAGEM_DESPESA_ITEM(_psDespesaItem);

        db.insert("CAD_VIAGEM_DESPESA_ITEM", null, c);

        //GET MAX CVDI_ID
        Integer _iCVDI_ID = getMax_CVDI_ID(Integer.parseInt(c.get("CVD_ID").toString()));

        //ENVIA DESPESA PELO WEB SERVICE
        CAD_VIAGEM_DESPESA_ITEM DespesaItem = prcRetornaDespesaItem(_iCVDI_ID);

        TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA = new TB_CAD_VIAGEM_DESPESA(context);
        CAD_VIAGEM_DESPESA Despesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso(Integer.parseInt(c.get("CVD_ID").toString()));

        //ENVIA DESPESA POR WEB SERVICE
        try {
            //NO WEB SERVICE JÁ VALIDA TUDO
            WS_Envio_DespesaItem WS_Envio_DespesaItem = new WS_Envio_DespesaItem(context, DespesaItem, Despesa, _psUsuarioID, _psUsuarioLogado, _psTipoPesquisa);
            WS_Envio_DespesaItem.execute();

        } catch (Exception ex) {
            String _sErro = ex.getMessage();
        }
    }

    public void prcIncluir_WS(Context context, CAD_VIAGEM_DESPESA_ITEM DespesaItem)
    {
        SQLiteDatabase db = getWritableDatabase(); //ON CREATE É CHAMADO AQUI
        ContentValues c = getValues_CAD_VIAGEM_DESPESA_ITEM(DespesaItem);

        db.insert("CAD_VIAGEM_DESPESA_ITEM", null, c);
    }

    public void prcAtualizar(CAD_VIAGEM_DESPESA_ITEM Reembolso)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getValues_CAD_VIAGEM_DESPESA_ITEM(Reembolso);

        db.update("CAD_VIAGEM_DESPESA_ITEM", contentValues, "CVDI_ID = ?", new String[]{String.valueOf(Reembolso.getCVDI_ID())});

        // closing connection
        db.close();
    }

    public void prcDeletar(Context context, int _piCVDI_ID, CAD_VIAGEM_DESPESA_ITEM DespesaItem, String _psUsuarioID, String _psUsuarioLogado, String _psTipoPesquisa){
        SQLiteDatabase db = this.getReadableDatabase();

        Integer _iDelete = db.delete("CAD_VIAGEM_DESPESA_ITEM", "CVDI_ID = ?", new String[]{String.valueOf(_piCVDI_ID)});

        if(_iDelete == 1)
        {
            TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA = new TB_CAD_VIAGEM_DESPESA(context);
            CAD_VIAGEM_DESPESA Despesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso(Integer.parseInt(String.valueOf(DespesaItem.getCVD_ID())));

            //ENVIA DESPESA POR WEB SERVICE
            try {
                //NO WEB SERVICE JÁ VALIDA TUDO
                WS_Deleta_DespesaItem WS_Deleta_DespesaItem = new WS_Deleta_DespesaItem(context, DespesaItem, Despesa, _psUsuarioID, _psUsuarioLogado, _psTipoPesquisa);
                WS_Deleta_DespesaItem.execute();

            } catch (Exception ex) {
                String _sErro = ex.getMessage();
            }
        }

    }

    public void prcDeletar_Todos(int _piCVD_ID){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("CAD_VIAGEM_DESPESA_ITEM", "CVD_ID = ?", new String[]{String.valueOf(_piCVD_ID)});

        // closing connection
        db.close();
    }

    private ContentValues getValues_CAD_VIAGEM_DESPESA_ITEM(CAD_VIAGEM_DESPESA_ITEM Reembolso) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("CVD_ID", String.valueOf(Reembolso.getCVD_ID()));
        try {
            contentValues.put("CVDI_ID_BORN", String.valueOf(Reembolso.getCVDI_ID_BORN()));
        }catch (Exception ex)
        {

        }
        contentValues.put("CVDI_DATA", String.valueOf(Reembolso.getCVDI_DATA().getTime()));
        contentValues.put("CVDI_TIPO_DESPESA", Reembolso.getCVDI_TIPO_DESPESA());
        contentValues.put("CVDI_VALOR", Reembolso.getCVDI_VALOR());
        contentValues.put("CVDI_KM_VALOR", Reembolso.getCVDI_KM_VALOR());
        contentValues.put("CVDI_KM_DISTANCIA", Reembolso.getCVDI_KM_DISTANCIA());
        contentValues.put("CVDI_DESCRICAO", Reembolso.getCVDI_DESCRICAO());

        contentValues.put("CVDI_DESPESA_BORN", String.valueOf(Reembolso.getCVDI_DESPESA_BORN()));

        contentValues.put("CVDI_FOTO", Reembolso.getCVDI_FOTO());

        return contentValues;
    }

    public List<CAD_VIAGEM_DESPESA_ITEM> prcLista(Integer _piCVD_ID){
        List<CAD_VIAGEM_DESPESA_ITEM> lista = new ArrayList<CAD_VIAGEM_DESPESA_ITEM>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_VIAGEM_DESPESA_ITEM", null, "CVD_ID = ?", new String[]{String.valueOf(_piCVD_ID)}, null, null, "CVDI_DATA");

        while(cursor.moveToNext()){
            CAD_VIAGEM_DESPESA_ITEM Reembolso = new CAD_VIAGEM_DESPESA_ITEM();
            prcPreencheFromCursor(cursor, Reembolso);
            lista.add(Reembolso);
        }

        // closing connection
        cursor.close();
        db.close();

        return lista;
    }

    public List<CAD_VIAGEM_DESPESA_ITEM> prcLista_DespesaItem_Pendente(String _psCU_ID){
        List<CAD_VIAGEM_DESPESA_ITEM> lista = new ArrayList<CAD_VIAGEM_DESPESA_ITEM>();
        SQLiteDatabase db = this.getReadableDatabase();

        String _strSQL =    "SELECT " +
                            "   CU_ID, " +
                            "   CVDI.*   " +
                            "FROM " +
                            "   CAD_VIAGEM_DESPESA CD " +
                            "   INNER JOIN CAD_VIAGEM_DESPESA_ITEM CVDI ON (CD.CVD_ID = CVDI.CVD_ID) " +
                            "WHERE " +
                            "   CU_ID = '" + _psCU_ID + "' " +
                            "   AND CVDI_ID_BORN = 'null' ";


        Cursor cursor = db.rawQuery(_strSQL, null);

        while(cursor.moveToNext()){
            CAD_VIAGEM_DESPESA_ITEM Reembolso = new CAD_VIAGEM_DESPESA_ITEM();
            prcPreencheFromCursor(cursor, Reembolso);
            lista.add(Reembolso);
        }

        // closing connection
        cursor.close();
        db.close();

        return lista;
    }

    public Integer getMax_CVDI_ID(Integer _psCVD_ID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT max(CVDI_ID) AS TOTAL FROM CAD_VIAGEM_DESPESA_ITEM WHERE CVD_ID = " + _psCVD_ID + " ", null);

        Integer total = 0;
        while(c.moveToNext()){

            total = c.getInt(c.getColumnIndex("TOTAL"));

        }

        // closing connection
        c.close();
        db.close();

        return total;
    }

    public double prcGetSumTipoDespesa(Integer _psCVD_ID, String _psTipo)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT sum(CVDI_VALOR) AS TOTAL FROM CAD_VIAGEM_DESPESA_ITEM WHERE CVD_ID = " + _psCVD_ID + " AND CVDI_TIPO_DESPESA = '" + _psTipo + "' ", null);

        double total = 0;
        while(c.moveToNext()){

            total = c.getDouble(c.getColumnIndex("TOTAL"));

        }

        // closing connection
        c.close();
        db.close();

        return total;
    }

    public CAD_VIAGEM_DESPESA_ITEM prcRetornaDespesaItem(int _piCVDI_ID){

        CAD_VIAGEM_DESPESA_ITEM r = new CAD_VIAGEM_DESPESA_ITEM();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("CAD_VIAGEM_DESPESA_ITEM", null, "CVDI_ID = ?", new String[]{String.valueOf(_piCVDI_ID)}, null, null, "CVDI_ID");

        if(cursor.moveToNext()){
            prcPreencheFromCursor(cursor, r);
        }

        // closing connection
        cursor.close();
        db.close();

        return r;
    }

    private void prcPreencheFromCursor(Cursor cursor, CAD_VIAGEM_DESPESA_ITEM Reembolso) {
        Reembolso.setCVDI_ID(cursor.getInt(cursor.getColumnIndex("CVDI_ID")));
        try {
            Reembolso.setCVDI_ID_BORN(cursor.getInt(cursor.getColumnIndex("CVDI_ID_BORN")));
        }
        catch (Exception ex){}
        Reembolso.setCVD_ID(cursor.getInt(cursor.getColumnIndex("CVD_ID")));

        long _iDataNasc = cursor.getLong(cursor.getColumnIndex("CVDI_DATA"));
        Date dtNasc = new Date();
        dtNasc.setTime(_iDataNasc);
        Reembolso.setCVDI_DATA(dtNasc);

        Reembolso.setCVDI_TIPO_DESPESA(cursor.getString(cursor.getColumnIndex("CVDI_TIPO_DESPESA")));

        Reembolso.setCVDI_DESPESA_BORN(cursor.getString(cursor.getColumnIndex("CVDI_DESPESA_BORN")));

        Reembolso.setCVDI_VALOR(cursor.getDouble(cursor.getColumnIndex("CVDI_VALOR")));

        Reembolso.setCVDI_KM_VALOR(cursor.getDouble(cursor.getColumnIndex("CVDI_KM_VALOR")));
        Reembolso.setCVDI_KM_DISTANCIA(cursor.getDouble(cursor.getColumnIndex("CVDI_KM_DISTANCIA")));
        Reembolso.setCVDI_DESCRICAO(cursor.getString(cursor.getColumnIndex("CVDI_DESCRICAO")));

        try {
            Reembolso.setCVDI_FOTO(cursor.getBlob(cursor.getColumnIndex("CVDI_FOTO")));
        }catch (Exception ex)
        {

        }
    }

    public Boolean prcVerificaExiste(String _psCVDI_ID)
    {
        // Select All Query
        String selectQuery = "SELECT CVDI_ID FROM CAD_VIAGEM_DESPESA_ITEM WHERE CVDI_ID_BORN = " + _psCVDI_ID + " ORDER BY CVDI_ID ";

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
