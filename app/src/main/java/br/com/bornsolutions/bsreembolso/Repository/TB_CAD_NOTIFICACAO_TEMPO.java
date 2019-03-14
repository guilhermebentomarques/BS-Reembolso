package br.com.bornsolutions.bsreembolso.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.bornsolutions.bsreembolso.Entidades.CAD_EMPRESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_NOTIFICACAO_TEMPO;

/**
 * Created by guilherme on 18/08/2016.
 */
public class TB_CAD_NOTIFICACAO_TEMPO extends  ReembolsoDB {

    public TB_CAD_NOTIFICACAO_TEMPO(Context context) {
        super(context); //INICIA O CONSTRUTOR DA CLASSE PAI, ANTES DE INICIAR ESTE CONSTRUTOR
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        prcCriaTabela(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void prcIncluir(CAD_NOTIFICACAO_TEMPO NotTempo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = getValues(NotTempo);
        db.insert("CAD_NOTIFICACAO_TEMPO", null, contentValues);
    }

    public void prcAtualizar(CAD_NOTIFICACAO_TEMPO NotTempo) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getValues(NotTempo);

        db.update("CAD_NOTIFICACAO_TEMPO", contentValues, "CU_ID = ?", new String[]{String.valueOf(NotTempo.getCU_ID())});
    }

    public void prcDeletar(CAD_NOTIFICACAO_TEMPO NotTempo) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("CAD_NOTIFICACAO_TEMPO", "CU_ID = ?", new String[]{String.valueOf(NotTempo.getCU_ID())});
    }

    @NonNull
    private ContentValues getValues(CAD_NOTIFICACAO_TEMPO NotTempo) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("CU_ID", String.valueOf(NotTempo.getCU_ID()));
        contentValues.put("CNT_TEMPO", NotTempo.getCNT_TEMPO());

        return contentValues;
    }

    public List<CAD_NOTIFICACAO_TEMPO> prcLista() {
        List<CAD_NOTIFICACAO_TEMPO> lista = new ArrayList<CAD_NOTIFICACAO_TEMPO>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_NOTIFICACAO_TEMPO", null, null, null, null, null, "CU_ID");

        while (cursor.moveToNext()) {
            CAD_NOTIFICACAO_TEMPO NotTempo = new CAD_NOTIFICACAO_TEMPO();
            prcPreenchePessoaFromCursor(cursor, NotTempo);
            lista.add(NotTempo);
        }

        return lista;
    }

    private void prcPreenchePessoaFromCursor(Cursor cursor, CAD_NOTIFICACAO_TEMPO NotTempo) {
        NotTempo.setCU_ID(cursor.getInt(cursor.getColumnIndex("CU_ID")));
        NotTempo.setCNT_TEMPO(cursor.getInt(cursor.getColumnIndex("CNT_TEMPO")));
    }

    public CAD_NOTIFICACAO_TEMPO prcRetornaTempoUsuario(String _psCU_ID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_NOTIFICACAO_TEMPO", null, "CU_ID = " + _psCU_ID, null, null, null, "CU_ID");

        CAD_NOTIFICACAO_TEMPO NotTempo = new CAD_NOTIFICACAO_TEMPO();

        while (cursor.moveToNext()) {
            prcPreenchePessoaFromCursor(cursor, NotTempo);
        }

        return NotTempo;
    }

    public Boolean prcVerificaExiste(String _psCU_ID)
    {
        // Select All Query
        String selectQuery = "SELECT * FROM CAD_NOTIFICACAO_TEMPO WHERE CU_ID = " + _psCU_ID + " ";

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

        // returning labels
        return _bExiste;
    }
}
