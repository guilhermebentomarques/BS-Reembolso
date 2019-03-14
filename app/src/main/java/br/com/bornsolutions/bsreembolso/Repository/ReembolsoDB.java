package br.com.bornsolutions.bsreembolso.Repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Blob;
import java.util.Date;

import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 21/04/2016.
 */
public class ReembolsoDB extends SQLiteOpenHelper {

    private static final String TAG = "sql";
    private static Context _gcontext = null;

    public ReembolsoDB(Context context)
    {
        super(context, Constantes.BD_NOME, null, Constantes.BD_VERSAO); //INICIA O CONSTRUTOR DA CLASSE PAI, ANTES DE INICIAR ESTE CONSTRUTOR
        _gcontext = context;
        prcCriaTabela(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //SÓ VAI SER CHAMADO UMA VEZ

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    public void prcCriaTabela(SQLiteDatabase db)
    {
        //ELE É CASE SENSITIVE EM COLUNAS E TABELAS
        StringBuilder query = new StringBuilder();
        query.append(" CREATE TABLE IF NOT EXISTS CAD_USUARIO ( ");
        query.append(" CU_ID INTEGER PRIMARY KEY, ");
        query.append(" CU_LOGIN TEXT(15) NOT NULL, ");
        query.append(" CU_SENHA TEXT(15) NOT NULL) ");

        db.execSQL(query.toString());

        StringBuilder query2 = new StringBuilder();
        query2.append(" CREATE TABLE IF NOT EXISTS CAD_CLIENTE ( ");
        query2.append(" CL_ID INTEGER PRIMARY KEY, ");
        query2.append(" CL_RAZAOSOCIAL TEXT(65) NOT NULL, ");
        query2.append(" CL_NOMEFANTASIA TEXT(65) NOT NULL, ");
        query2.append(" CL_NOMEREDUZIDO TEXT(65) NOT NULL, ");
        query2.append(" CL_CPFCNPJ TEXT(18) NULL, ");
        query2.append(" CL_CIDADE TEXT(50) NOT NULL, ");
        query2.append(" CL_UF TEXT(20) NOT NULL) ");

        db.execSQL(query2.toString());

        StringBuilder query3 = new StringBuilder();
        query3.append(" CREATE TABLE IF NOT EXISTS CAD_VIAGEM_DESPESA ( ");
        query3.append(" CVD_ID INTEGER PRIMARY KEY, ");
        query3.append(" CVD_ID_BORN INTEGER NULL, ");
        query3.append(" CL_ID INTEGER NULL, ");
        query3.append(" CU_ID INTEGER NULL, ");
        query3.append(" CVD_ENVIADO_BORN TEXT(1) NULL, ");
        query3.append(" CVD_FINALIZADO TEXT(1) NULL, ");
        query3.append(" CVD_TIPO TEXT(1) NOT NULL, "); //LOCAL OU VIAGEM L OU V
        query3.append(" CVD_ADIANTAMENTO_DATA INTEGER NULL, ");
        query3.append(" CVD_ADIANTAMENTO_VALOR REAL NULL, ");
        query3.append(" CVD_VIAGEM_DATA_INICIO INTEGER NOT NULL, ");
        query3.append(" CVD_VIAGEM_DATA_FIM INTEGER NOT NULL, ");
        query3.append(" CVD_DESCRICAO TEXT(200) NOT NULL) ");

        db.execSQL(query3.toString());

        //ELE É CASE SENSITIVE EM COLUNAS E TABELAS
        StringBuilder query4 = new StringBuilder();
        query4.append(" CREATE TABLE IF NOT EXISTS TID_LAST ( ");
        query4.append(" IDL_TABELA TEXT(35) NOT NULL, ");
        query4.append(" IDL_CODIGO INTEGER NOT NULL) ");

        db.execSQL(query4.toString());

        db.execSQL("INSERT INTO TID_LAST VALUES ('CAD_VIAGEM_DESPESA',1)");
        db.execSQL("INSERT INTO TID_LAST VALUES ('CAD_VIAGEM_DESPESA_ITEM',1)");

        StringBuilder query6 = new StringBuilder();
        query6.append(" CREATE TABLE IF NOT EXISTS CAD_VIAGEM_DESPESA_ITEM ( ");
        query6.append(" CVDI_ID INTEGER PRIMARY KEY, ");
        query6.append(" CVD_ID INTEGER NOT NULL, ");
        query6.append(" CVDI_ID_BORN INTEGER NULL, ");
        query6.append(" CVDI_DATA INTEGER NOT NULL, ");
        query6.append(" CVDI_TIPO_DESPESA TEXT(25) NOT NULL, ");
        query6.append(" CVDI_VALOR REAL NOT NULL, ");
        query6.append(" CVDI_DESPESA_BORN TEXT(1) NOT NULL, ");
        query6.append(" CVDI_KM_VALOR REAL  NULL, ");
        query6.append(" CVDI_KM_DISTANCIA REAL  NULL, ");
        query6.append(" CVDI_DESCRICAO TEXT(250)  NULL, ");
        query6.append(" CVDI_FOTO BLOB NULL) ");

        db.execSQL(query6.toString());


        //ELE É CASE SENSITIVE EM COLUNAS E TABELAS
        StringBuilder query5 = new StringBuilder();
        query5.append(" CREATE TABLE IF NOT EXISTS CAD_NOTIFICACAO_TEMPO ( ");
        query5.append(" CU_ID INTEGER NOT NULL, ");
        query5.append(" CNT_TEMPO INTEGER NOT NULL) ");
        db.execSQL(query5.toString());

        //ELE É CASE SENSITIVE EM COLUNAS E TABELAS
        StringBuilder query7 = new StringBuilder();
        query7.append(" CREATE TABLE IF NOT EXISTS CAD_NOTIFICACAO ( ");
        query7.append(" CN_ID INTEGER PRIMARY KEY, ");
        query7.append(" CU_ID INTEGER NOT NULL, ");
        query7.append(" CN_DATA INTEGER NOT NULL, ");
        query7.append(" CN_DESCRICAO TEXT(100) NOT NULL) ");
        db.execSQL(query7.toString());
    }

}
