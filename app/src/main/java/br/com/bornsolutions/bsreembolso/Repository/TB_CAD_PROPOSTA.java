package br.com.bornsolutions.bsreembolso.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Spinner;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_EMPRESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_PROPOSTA;

/**
 * Created by guilherme on 30/04/2016.
 */
public class TB_CAD_PROPOSTA extends ReembolsoDB {

    public TB_CAD_PROPOSTA(Context context) {
        super(context); //INICIA O CONSTRUTOR DA CLASSE PAI, ANTES DE INICIAR ESTE CONSTRUTOR
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

    public void prcIncluir(CAD_PROPOSTA Proposta)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getValues_CAD_PROPOSTA(Proposta);

        db.insert("CAD_PROPOSTA", null, contentValues);
    }

    public void prcAtualizar(CAD_PROPOSTA Proposta)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getValues_CAD_PROPOSTA(Proposta);

        db.update("CAD_PROPOSTA", contentValues, "CP_ID = ?", new String[]{String.valueOf(Proposta.getCP_ID())});
    }

    public void prcDeletar(CAD_PROPOSTA Proposta)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("CAD_PROPOSTA", "CP_ID = ?", new String[]{String.valueOf(Proposta.getCE_ID())});
    }

    @NonNull
    private ContentValues getValues_CAD_PROPOSTA(CAD_PROPOSTA Proposta) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("CP_ID", String.valueOf(Proposta.getCP_ID()));
        contentValues.put("CPR_ID", Proposta.getCPR_ID());
        contentValues.put("CE_ID", Proposta.getCE_ID());
        contentValues.put("CL_ID", Proposta.getCL_ID());
        contentValues.put("CP_NUMERO", Proposta.getCP_NUMERO());
        contentValues.put("CP_STATUS", Proposta.getCP_STATUS());
        contentValues.put("CP_TITULO", Proposta.getCP_TITULO());

        return contentValues;
    }

    public List<CAD_PROPOSTA> prcLista(){
        List<CAD_PROPOSTA> lista = new ArrayList<CAD_PROPOSTA>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_PROPOSTA", null, null, null, null, null, "CL_NOMEREDUZIDO");

        while(cursor.moveToNext()){
            CAD_PROPOSTA Proposta = new CAD_PROPOSTA();
            prcPreencheFromCursor_CAD_PROPOSTA(cursor, Proposta);
            lista.add(Proposta);
        }

        return lista;
    }

    private void prcPreencheFromCursor_CAD_PROPOSTA(Cursor cursor, CAD_PROPOSTA Proposta) {
        Proposta.setCE_ID(cursor.getInt(cursor.getColumnIndex("CP_ID")));
        Proposta.setCPR_ID(cursor.getInt(cursor.getColumnIndex("CPR_ID")));
        Proposta.setCL_ID(cursor.getInt(cursor.getColumnIndex("CL_ID")));
        Proposta.setCP_NUMERO(cursor.getString(cursor.getColumnIndex("CP_NUMERO")));
        Proposta.setCP_STATUS(cursor.getString(cursor.getColumnIndex("CP_STATUS")));
        Proposta.setCP_TITULO(cursor.getString(cursor.getColumnIndex("CP_STATUS")));

    }

    public Boolean prcVerificaExiste_CAD_PROPOSTA(String _psCP_ID){
        // Select All Query
        String selectQuery = "SELECT * FROM CAD_PROPOSTA WHERE CP_ID = " + _psCP_ID + " ORDER BY CP_ID ";

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

    public List<CLG_Spinner> getAllLabels(){
        List < CLG_Spinner > labels = new ArrayList< CLG_Spinner >();
        // Select All Query
        String selectQuery = "SELECT " +
                             "  * " +
                             "FROM " +
                             "  CAD_PROPOSTA CP " +
                             "  INNER JOIN CAD_CLIENTE CL ON (CL.CL_ID = CP.CL_ID) " +
                             "ORDER BY " +
                             "  CP_TITULO ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if ( cursor.moveToFirst () ) {
            do {
                //String _strTexto = cursor.getString(cursor.getColumnIndex("CL_NOMEREDUZIDO")) + " - " +  cursor.getString(cursor.getColumnIndex("CP_NUMERO")) + "|" + cursor.getString(cursor.getColumnIndex("CP_TITULO"));

                labels.add( new CLG_Spinner(cursor.getInt(cursor.getColumnIndex("CP_ID")), cursor.getString(cursor.getColumnIndex("CP_TITULO"))));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning labels
        return labels;
    }

    public void loadSpinner(Context context ,Spinner spinner) {
        // Spinner Drop down elements
        List <CLG_Spinner> lables = getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<CLG_Spinner> dataAdapter = new ArrayAdapter<CLG_Spinner>(context, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    public String prcGetCidade(String _psCP_ID){
        // Select All Query
        String selectQuery = "SELECT * FROM CAD_PROPOSTA CP " +
                             " INNER JOIN CAD_CLIENTE CL ON (CL.CL_ID = CP.CL_ID) " +
                             "WHERE CP_ID = " + _psCP_ID + " ORDER BY CP_ID ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String _strCidade = "";

        // looping through all rows and adding to list
        if ( cursor.moveToFirst () ) {
            do {
                _strCidade = cursor.getString(cursor.getColumnIndex("CL_UF")) + " - " + cursor.getString(cursor.getColumnIndex("CL_CIDADE"));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning labels
        return _strCidade;
    }

    public String prcRetornaNomeProposta(String _psCP_ID){
        List < CLG_Spinner > labels = new ArrayList< CLG_Spinner >();
        // Select All Query
        String selectQuery = "SELECT " +
                "  * " +
                "FROM " +
                "  CAD_PROPOSTA CP " +
                "  INNER JOIN CAD_CLIENTE CL ON (CL.CL_ID = CP.CL_ID) " +
                "WHERE CP.CP_ID = " + _psCP_ID + " " +
                "ORDER BY " +
                "  CL_NOMEREDUZIDO ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String _strTexto = "";

        // looping through all rows and adding to list
        if ( cursor.moveToFirst () ) {
            do {
                _strTexto = cursor.getString(cursor.getColumnIndex("CP_NUMERO")) + "|" + cursor.getString(cursor.getColumnIndex("CP_TITULO"));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning labels
        return _strTexto;
    }

}
