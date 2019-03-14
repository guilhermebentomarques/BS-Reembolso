package br.com.bornsolutions.bsreembolso.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.bornsolutions.bsreembolso.Entidades.CAD_EMPRESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_USUARIO;

/**
 * Created by guilherme on 30/04/2016.
 */
public class TB_CAD_EMPRESA extends  ReembolsoDB {

    public TB_CAD_EMPRESA(Context context) {
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

    public void prcIncluir(CAD_EMPRESA Empresa)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getValues_CAD_EMPRESA(Empresa);

        db.insert("CAD_EMPRESA", null, contentValues);

        // closing connection
        db.close();
    }

    public void prcAtualizar(CAD_EMPRESA Empresa)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getValues_CAD_EMPRESA(Empresa);

        db.update("CAD_EMPRESA", contentValues, "CE_ID = ?", new String[]{String.valueOf(Empresa.getCE_ID())});

        // closing connection
        db.close();
    }

    public void prcDeletar(CAD_EMPRESA Empresa)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("CAD_EMPRESA", "CE_ID = ?", new String[]{String.valueOf(Empresa.getCE_ID())});

        // closing connection
        db.close();
    }

    @NonNull
    private ContentValues getValues_CAD_EMPRESA(CAD_EMPRESA Empresa) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("CE_ID", String.valueOf(Empresa.getCE_ID()));
        contentValues.put("CE_CNPJ", Empresa.getCE_CNPJ());
        contentValues.put("CE_RAZAOSOCIAL", Empresa.getCE_RAZAOSOCIAL());
        contentValues.put("CE_NOMEREDUZIDO", Empresa.getCE_NOMEREDUZIDO());


        return contentValues;
    }

    public List<CAD_EMPRESA> prcLista(){
        List<CAD_EMPRESA> lista = new ArrayList<CAD_EMPRESA>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_EMPRESA", null, null, null, null, null, "CE_RAZAOSOCIAL");

        while(cursor.moveToNext()){
            CAD_EMPRESA Empresa = new CAD_EMPRESA();
            prcPreenchePessoaFromCursor(cursor, Empresa);
            lista.add(Empresa);
        }

        // closing connection
        cursor.close();
        db.close();

        return lista;
    }

    private void prcPreenchePessoaFromCursor(Cursor cursor, CAD_EMPRESA Empresa) {
        Empresa.setCE_ID(cursor.getInt(cursor.getColumnIndex("CE_ID")));
        Empresa.setCE_CNPJ(cursor.getString(cursor.getColumnIndex("CE_CNPJ")));
        Empresa.setCE_RAZAOSOCIAL(cursor.getString(cursor.getColumnIndex("CE_RAZAOSOCIAL")));
        Empresa.setCE_NOMEREDUZIDO(cursor.getString(cursor.getColumnIndex("CE_NOMEREDUZIDO")));
    }

    public Boolean prcVerificaExiste_CAD_EMPRESA(String _psCE_ID){
        // Select All Query
        String selectQuery = "SELECT * FROM CAD_EMPRESA WHERE CE_ID = " + _psCE_ID + " ORDER BY CE_RAZAOSOCIAL ";

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
