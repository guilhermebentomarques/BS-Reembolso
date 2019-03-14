package br.com.bornsolutions.bsreembolso.CL_Globais;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by GuilhermeBento on 28/03/2016.
 */
public class CLG_SQLite extends SQLiteOpenHelper {

    public CLG_SQLite(Context context, String _psNomeBD, Integer _piVersaoBD ) {
        super(context, _psNomeBD, null, _piVersaoBD);
    }

    public void prcDeleteRegistro_ChaveSimples(String _psNomeTabela, String _psColunaChave, String _psValor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(_psNomeTabela, " " + _psColunaChave + " = ? ", new String[]{_psValor});
    }

    public void prcDeleteRegistro_ChaveComposta(String _psNomeTabela, String _psColunaChave1, String _psValor1, String _psColunaChave2,String _psValor2)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(_psNomeTabela, " " + _psColunaChave1 + " = ? AND " + _psColunaChave2 + " = ? ", new String[]{_psValor1,_psValor2});
    }

    public void prcCriaTabela(SQLiteDatabase db, StringBuilder _psCreateTable)
    {
        db.execSQL(_psCreateTable.toString());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //AO CRIAR O BD

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //AO ATUALIZAR O BD
        onCreate(db);

    }

    public Boolean BackupBD(String _psCaminho, String _psPasta)
    {
        File dbFile = new File(Environment.getDataDirectory() + _psCaminho);

        File exportDir = new File(Environment.getExternalStorageDirectory(), _psPasta);
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, dbFile.getName());

        try {
            file.createNewFile();
            this.copyFile(dbFile, file);
            return true;
        } catch (IOException e) {
            Log.e("mypck", e.getMessage(), e);
            return false;
        }
    }

    void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }
}
