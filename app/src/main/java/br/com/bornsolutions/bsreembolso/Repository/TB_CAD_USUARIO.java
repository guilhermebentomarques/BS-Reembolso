package br.com.bornsolutions.bsreembolso.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.bornsolutions.bsreembolso.Entidades.CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_USUARIO;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 21/04/2016.
 */
public class TB_CAD_USUARIO extends ReembolsoDB {

    public TB_CAD_USUARIO(Context context) {
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

    public void prcIncluir(CAD_USUARIO Usuario)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getCLIENTEValues(Usuario);

        db.insert("CAD_USUARIO", null, contentValues);

        // closing connection
        db.close();
    }

    public void prcAtualizar(CAD_USUARIO Usuario)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = getCLIENTEValues(Usuario);

        db.update("CAD_USUARIO", contentValues, "CU_ID = ?", new String[]{String.valueOf(Usuario.getCU_ID())});

        // closing connection
        db.close();
    }

    public void prcDeletar(CAD_USUARIO Usuario)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("CAD_USUARIO", "CU_ID = ? AND CU_LOGIN = ?", new String[]{Usuario.getCU_LOGIN(), Usuario.getCU_SENHA()});

        // closing connection
        db.close();
    }

    @NonNull
    private ContentValues getCLIENTEValues(CAD_USUARIO Usuario) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("CU_ID", String.valueOf(Usuario.getCU_ID()));
        contentValues.put("CU_LOGIN", Usuario.getCU_LOGIN());
        contentValues.put("CU_SENHA", Usuario.getCU_SENHA());



        return contentValues;
    }

    public List<CAD_USUARIO> prcLista(){
        List<CAD_USUARIO> lista = new ArrayList<CAD_USUARIO>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_USUARIO", null, null, null, null, null, "CU_LOGIN");

        while(cursor.moveToNext()){
            CAD_USUARIO Usuario = new CAD_USUARIO();
            prcPreenchePessoaFromCursor(cursor, Usuario);
            lista.add(Usuario);
        }

        // closing connection
        cursor.close();
        db.close();

        return lista;
    }

    private void prcPreenchePessoaFromCursor(Cursor cursor, CAD_USUARIO Usuario) {
        Usuario.setCU_ID(cursor.getInt(cursor.getColumnIndex("CU_ID")));
        Usuario.setCU_LOGIN(cursor.getString(cursor.getColumnIndex("CU_LOGIN")));
        Usuario.setCU_SENHA(cursor.getString(cursor.getColumnIndex("CU_SENHA")));
    }
}
