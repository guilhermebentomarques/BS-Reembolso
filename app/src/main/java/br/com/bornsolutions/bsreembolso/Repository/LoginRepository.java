package br.com.bornsolutions.bsreembolso.Repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 16/04/2016.
 */
public class LoginRepository extends BaseRepository {

    public LoginRepository(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //SÓ VAI SER CHAMADO UMA VEZ
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}
