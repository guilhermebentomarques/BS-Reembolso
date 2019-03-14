package br.com.bornsolutions.bsreembolso.Repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 16/04/2016.
 */
public class BaseRepository extends SQLiteOpenHelper {

    public BaseRepository(Context context) {
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //SÓ VAI SER CHAMADO UMA VEZ
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
