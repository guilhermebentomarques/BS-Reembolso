package br.com.bornsolutions.bsreembolso.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.bornsolutions.bsreembolso.Entidades.CAD_USUARIO;

/**
 * Created by guilherme on 17/08/2016.
 */
public class TB_TID_LAST extends ReembolsoDB {

    public TB_TID_LAST(Context context) {
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

    public int prcMaxNum(String _psNomeTabela)
    {
        int _iValor = 0;

        //ATUALIZO O CÓDIGO
        SQLiteDatabase db2 = this.getReadableDatabase();
        String selectQuery2 = "UPDATE TID_LAST SET IDL_CODIGO = IDL_CODIGO + 1 WHERE IDL_TABELA = '" + _psNomeTabela + "' ";
        db2.execSQL(selectQuery2);

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM TID_LAST WHERE IDL_TABELA = '" + _psNomeTabela + "' ";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToNext()){
            _iValor = cursor.getInt(cursor.getColumnIndex("IDL_CODIGO"));
        }

        // closing connection
        cursor.close();
        db.close();

        return _iValor;
    }
}
