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
import br.com.bornsolutions.bsreembolso.Entidades.CAD_CLIENTE;

/**
 * Created by guilherme on 21/04/2016.
 */
public class TB_CAD_CLIENTE extends ReembolsoDB {

    public TB_CAD_CLIENTE(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        prcCriaTabela(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void prcIncluir(CAD_CLIENTE Cliente)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getCLIENTEValues(Cliente);
        db.insert("CAD_CLIENTE", null, contentValues);
        db.close();
    }

    public void prcAtualizar(CAD_CLIENTE Cliente)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = getCLIENTEValues(Cliente);
        db.update("CAD_CLIENTE", contentValues, "CL_ID = ?", new String[]{String.valueOf(Cliente.getCL_ID())});
        db.close();
    }

    public void prcDeletar(CAD_CLIENTE Cliente)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("CAD_CLIENTE", "CL_ID = ?", new String[]{String.valueOf(Cliente.getCL_ID())});
        db.close();
    }

    @NonNull
    private ContentValues getCLIENTEValues(CAD_CLIENTE Cliente) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CL_ID", Cliente.getCL_ID());
        contentValues.put("CL_RAZAOSOCIAL", Cliente.getCL_RAZAOSOCIAL());
        contentValues.put("CL_NOMEFANTASIA", Cliente.getCL_NOMEFANTASIA());
        contentValues.put("CL_NOMEREDUZIDO", Cliente.getCL_NOMEREDUZIDO());
        contentValues.put("CL_CPFCNPJ", Cliente.getCL_CPFCNPJ()); //ENUM CHAMA O ORDINAL
        contentValues.put("CL_CIDADE", Cliente.getCL_CIDADE()); //PEGA A INFORMAÇÃO EM INTEIRO PARA TRANSCREVER EM DATE*/
        contentValues.put("CL_UF", Cliente.getCL_UF());
        return contentValues;
    }

    public List<CAD_CLIENTE> prcListaClientes(){
        List<CAD_CLIENTE> lista = new ArrayList<CAD_CLIENTE>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("CAD_CLIENTE", null, null, null, null, null, "CL_RAZAOSOCIAL");

        while(cursor.moveToNext()){
            CAD_CLIENTE cliente = new CAD_CLIENTE();
            prcPreencheClienteFromCursor(cursor, cliente);
            lista.add(cliente);
        }

        // closing connection
        cursor.close();
        db.close();

        return lista;
    }

    public Boolean prcVerificaClienteExiste(String _psCL_ID){
        // Select All Query
        String selectQuery = "SELECT * FROM CAD_CLIENTE WHERE CL_ID = " + _psCL_ID + " ORDER BY CL_RAZAOSOCIAL ";

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

    public CAD_CLIENTE prcRetornaCliente(int _piCP_ID){

        CAD_CLIENTE c = new CAD_CLIENTE();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("CAD_CLIENTE", null, "CL_ID = ?", new String[]{String.valueOf(_piCP_ID)}, null, null, "CL_RAZAOSOCIAL");

        if(cursor.moveToNext()){
            prcPreencheClienteFromCursor(cursor, c);
        }

        // closing connection
        cursor.close();
        db.close();

        return c;
    }

    public String prcGetCidadeCliente(String _psCL_ID){
        // Select All Query
        String selectQuery = "SELECT * FROM CAD_CLIENTE WHERE CL_ID = " + _psCL_ID + " ORDER BY CL_RAZAOSOCIAL ";

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

    public List< CLG_Spinner> getAllLabels(){
        List < CLG_Spinner > labels = new ArrayList< CLG_Spinner >();
        // Select All Query
        String selectQuery = "SELECT * FROM CAD_CLIENTE ORDER BY CL_RAZAOSOCIAL ";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if ( cursor.moveToFirst () ) {
            do {

                labels.add( new CLG_Spinner(cursor.getInt(cursor.getColumnIndex("CL_ID")), cursor.getString(cursor.getColumnIndex("CL_RAZAOSOCIAL"))));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning labels
        return labels;
    }

    public void loadSpinnerClientes(Context context ,Spinner spinner) {
        // Spinner Drop down elements
        List <CLG_Spinner> lables = getAllLabels();
        // Creating adapter for spinner
        ArrayAdapter<CLG_Spinner> dataAdapter = new ArrayAdapter<CLG_Spinner>(context, android.R.layout.simple_spinner_item, lables);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }

    private void prcPreencheClienteFromCursor(Cursor cursor, CAD_CLIENTE p) {
        p.setCL_ID(cursor.getInt(cursor.getColumnIndex("CL_ID")));
        p.setCL_RAZAOSOCIAL(cursor.getString(cursor.getColumnIndex("CL_RAZAOSOCIAL")));
        p.setCL_NOMEFANTASIA(cursor.getString(cursor.getColumnIndex("CL_NOMEFANTASIA")));
        p.setCL_NOMEREDUZIDO(cursor.getString(cursor.getColumnIndex("CL_NOMEREDUZIDO")));
        p.setCL_CPFCNPJ(cursor.getString(cursor.getColumnIndex("CL_CPFCNPJ")));
        p.setCL_CIDADE(cursor.getString(cursor.getColumnIndex("CL_CIDADE")));
        p.setCL_UF(cursor.getString(cursor.getColumnIndex("CL_UF")));

        /*//CPF CNPJ
        String _strCPF = cursor.getString(cursor.getColumnIndex("CP_CPF"));
        String _strCNPJ = cursor.getString(cursor.getColumnIndex("CP_CNPJ"));
        if(_strCPF != null && !"".equals(_strCPF)) {
            p.setTipoPessoa(TipoPessoa.FÍSICA);
            p.setCP_CPFCNPJ(_strCPF);
        }
        else {
            p.setTipoPessoa(TipoPessoa.JURÍDICA);
            p.setCP_CPFCNPJ(_strCNPJ);
        }

        int _iSexo = cursor.getInt(cursor.getColumnIndex("CP_SEXO"));
        p.setSexo(Sexo.getSexo(_iSexo));

        int _iProfissao = cursor.getInt(cursor.getColumnIndex("CP_PROFISSAO"));
        p.setProfissao(Profissao.getProfissao(_iProfissao));

        long _iDataNasc = cursor.getLong(cursor.getColumnIndex("CP_DATANASCIMENTO"));
        Date dtNasc = new Date();
        dtNasc.setTime(_iDataNasc);
        p.setCP_DATANASCIMENTO(dtNasc);*/
    }
}
