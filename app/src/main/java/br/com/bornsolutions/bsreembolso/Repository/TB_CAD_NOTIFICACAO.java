package br.com.bornsolutions.bsreembolso.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.bornsolutions.bsreembolso.Entidades.CAD_NOTIFICACAO;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_NOTIFICACAO_TEMPO;

/**
 * Created by guilherme on 18/08/2016.
 */
public class TB_CAD_NOTIFICACAO extends ReembolsoDB {

    public TB_CAD_NOTIFICACAO(Context context) {
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

    public void prcIncluir(CAD_NOTIFICACAO Notificacao) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = getValues(Notificacao);
        db.insert("CAD_NOTIFICACAO", null, contentValues);
    }

    public void prcAtualizar(CAD_NOTIFICACAO Notificacao) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getValues(Notificacao);

        db.update("CAD_NOTIFICACAO", contentValues, "CN_ID = ?", new String[]{String.valueOf(Notificacao.getCN_ID())});
    }

    public void prcDeletar(CAD_NOTIFICACAO Notificacao) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("CAD_NOTIFICACAO", "CN_ID = ?", new String[]{String.valueOf(Notificacao.getCN_ID())});
    }

    @NonNull
    private ContentValues getValues(CAD_NOTIFICACAO Notificacao) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("CN_ID", String.valueOf(Notificacao.getCN_ID()));
        contentValues.put("CU_ID", Notificacao.getCU_ID());
        contentValues.put("CN_DATA", Notificacao.getCN_DATA().getTime());
        contentValues.put("CN_NOTIFICACAO", Notificacao.getCN_NOTIFICACAO());

        return contentValues;
    }

    public List<CAD_NOTIFICACAO> prcLista() {
        List<CAD_NOTIFICACAO> lista = new ArrayList<CAD_NOTIFICACAO>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_NOTIFICACAO", null, null, null, null, null, "CU_ID");

        while (cursor.moveToNext()) {
            CAD_NOTIFICACAO Notificacao = new CAD_NOTIFICACAO();
            prcPreenchePessoaFromCursor(cursor, Notificacao);
            lista.add(Notificacao);
        }

        return lista;
    }

    private void prcPreenchePessoaFromCursor(Cursor cursor, CAD_NOTIFICACAO Notificacao) {

        Notificacao.setCN_ID(cursor.getInt(cursor.getColumnIndex("CN_ID")));
        Notificacao.setCU_ID(cursor.getInt(cursor.getColumnIndex("CU_ID")));

        long _iDataNasc = cursor.getLong(cursor.getColumnIndex("CN_DATA"));
        Date dtNasc = new Date();
        dtNasc.setTime(_iDataNasc);
        Notificacao.setCN_DATA(dtNasc);

        Notificacao.setCN_NOTIFICACAO(cursor.getString(cursor.getColumnIndex("CU_ID")));
    }
}
