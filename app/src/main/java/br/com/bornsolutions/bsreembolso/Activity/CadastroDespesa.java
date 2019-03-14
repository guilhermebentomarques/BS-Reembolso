package br.com.bornsolutions.bsreembolso.Activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Datas;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Moeda;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Spinner;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_String;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.WebService.WS_Envio_Despesa;

public class CadastroDespesa extends BaseActivity {

    EditText edtDataInicio, edtDataFim, edtDataAdiantamento, edtValorAdiantamento, edtDescricao;
    Spinner spinnerclientes;
    TextView txtTipoDespesa;
    Button btnDataInicio, btnDataFim, btnDataAdiantamento, CVD_btnGravar;

    CLG_Datas CL_Datas = new CLG_Datas();

    private SharedPreferences preferences;

    Integer _giCL_ID;
    String _gsCL_RAZAOSOCIAL, _sEditando;
    String _gsCVD_TIPO;
    ImageView imgTipoDespesa;
    Boolean _gEditando = false;
    CAD_VIAGEM_DESPESA Despesa;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_despesa);

        getSupportActionBar().setTitle("Cadastrar Despesa");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        prcIniciaComponentesTela();

        Despesa = (CAD_VIAGEM_DESPESA) this.getIntent().getExtras().getSerializable("Despesa");
        _gsCVD_TIPO = this.getIntent().getExtras().getSerializable("CVD_TIPO").toString();
        prpTipoPesquisa = this.getIntent().getExtras().getSerializable("TIPO_PESQUISA").toString();
        prpUsuario_Logado = this.getIntent().getExtras().getSerializable("CU_LOGIN").toString();
        prpUsuario_ID = this.getIntent().getExtras().getSerializable("CU_ID").toString();

        //COLOCA A MASCARA DE VALOR NO CAMPO
        edtValorAdiantamento.addTextChangedListener(new CLG_Moeda(edtValorAdiantamento));

        final TB_CAD_CLIENTE TB_CAD_CLIENTE = new TB_CAD_CLIENTE(this);
        TB_CAD_CLIENTE.loadSpinnerClientes(this, spinnerclientes);


        //VERIFICA SE ESTÁ EM EDIÇÃO OU NÃO
        if(Despesa != null) {
            _gEditando = true;
            CVD_btnGravar.setText("Atualizar");

            //PREENCHE DATA
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            edtDataAdiantamento.setText(dateFormat.format(Despesa.getCVD_ADIANTAMENTO_DATA()));
            edtDataInicio.setText(dateFormat.format(Despesa.getCVD_VIAGEM_DATA_INICIO()));
            edtDataFim.setText(dateFormat.format(Despesa.getCVD_VIAGEM_DATA_FIM()));

            //PREENCHE KM RODADO
            edtValorAdiantamento.setText(String.format("%.2f", Despesa.getCVD_ADIANTAMENTO_VALOR()));

            CAD_CLIENTE Cliente = TB_CAD_CLIENTE.prcRetornaCliente(Despesa.getCL_ID());

            CLG_String CL_STRING = new CLG_String();
            int _iPos = CL_STRING.indexOf(spinnerclientes.getAdapter(), Cliente.getCL_RAZAOSOCIAL());
            spinnerclientes.setSelection(_iPos);

            //PREENCHE DESCRICAO
            edtDescricao.setText(Despesa.getCVD_DESCRICAO());

        }
        else {
            _gEditando = false;
            CVD_btnGravar.setText("Cadastrar");
        }

        //VERIFICA SE É UMA DESPESA DE VIAGEM OU UMA DESPESA LOCAL
        if(_gsCVD_TIPO.equals("V")) {
            imgTipoDespesa.setImageResource(R.drawable.btn_aviao_azul);
            txtTipoDespesa.setText("DESPESA VIAGEM");
        }
        else if(_gsCVD_TIPO.equals("L")) {
            imgTipoDespesa.setImageResource(R.drawable.btn_dinheiro_azul);
            txtTipoDespesa.setText("DESPESA LOCAL");
        }

        //ON ITEM SELECTED SPINNER
        spinnerclientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                _giCL_ID = ((CLG_Spinner) spinnerclientes.getSelectedItem()).getId();
                _gsCL_RAZAOSOCIAL = ((CLG_Spinner) spinnerclientes.getSelectedItem()).getValue();

                String _strCidadeUF = TB_CAD_CLIENTE.prcGetCidadeCliente(String.valueOf(_giCL_ID));
                Toast.makeText(CadastroDespesa.this, _strCidadeUF, Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void prcIniciaComponentesTela()
    {
        edtDataInicio = (EditText) findViewById(R.id.edtDataInicio);
        edtDataFim = (EditText) findViewById(R.id.edtDataFim);
        edtDataAdiantamento = (EditText) findViewById(R.id.edtDataAdiantamento);
        edtValorAdiantamento = (EditText) findViewById(R.id.edtValorAdiantamento);
        edtValorAdiantamento.setText("R$0,00");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        edtDataAdiantamento.setText(dateFormat.format(date));
        edtDataInicio.setText(dateFormat.format(date));
        edtDataFim.setText(dateFormat.format(date));

        edtDescricao = (EditText) findViewById(R.id.edtDescricao);

        spinnerclientes = (Spinner) findViewById(R.id.spnListaClientes);

        CVD_btnGravar = (Button) findViewById(R.id.cd_btnGravar);
        imgTipoDespesa = (ImageView) findViewById(R.id.imgTipoDespesa);
        txtTipoDespesa = (TextView) findViewById(R.id.txtTipoDespesa);
    }

    public void prcGravarReembolso(View view)
    {
        CL_CAD_VIAGEM_DESPESA CL_CAD_VIAGEM_DESPESA = new CL_CAD_VIAGEM_DESPESA();

        CAD_VIAGEM_DESPESA r = prcMontarReembolso();
        if(CL_CAD_VIAGEM_DESPESA.prcValidaGravar(this,edtDescricao))
        {
            TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA = new TB_CAD_VIAGEM_DESPESA(this);
            if(_gEditando)
            {
                TB_CAD_VIAGEM_DESPESA.prcAtualizar(r);

                //ENVIA DESPESA POR WEB SERVICE
                try {
                    //NO WEB SERVICE JÁ VALIDA TUDO
                    WS_Envio_Despesa WebService_EnvioDespesa = new WS_Envio_Despesa(this, r, prpUsuario_ID, prpUsuario_Logado, prpTipoPesquisa);
                    WebService_EnvioDespesa.execute();

                } catch (Exception ex) {
                    String _sErro = ex.getMessage();
                }
            }
            else
            {
                TB_CAD_VIAGEM_DESPESA.prcIncluir(this, r, prpUsuario_ID, prpUsuario_Logado, prpTipoPesquisa);
            }
        }
    }

    private CAD_VIAGEM_DESPESA prcMontarReembolso(){

        Locale locale = this.getResources().getConfiguration().locale;
        Currency currency= Currency.getInstance(locale);
        final String symbol = currency.getSymbol();

        CAD_VIAGEM_DESPESA reembolso = new CAD_VIAGEM_DESPESA();
        reembolso.setCL_ID(_giCL_ID);
        reembolso.setCU_ID(Integer.parseInt(prpUsuario_ID));

        if(_gEditando) {
            reembolso.setCVD_FINALIZADO(Despesa.getCVD_FINALIZADO());
            reembolso.setCVD_ID(Despesa.getCVD_ID());
        }

        try {
            reembolso.setCVD_ID_BORN(Despesa.getCVD_ID_BORN());
        }
        catch (Exception ex)
        {

        }

        //ADIANTAMENTO
        DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date nasc = dateFormat.parse(edtDataAdiantamento.getText().toString());
            reembolso.setCVD_ADIANTAMENTO_DATA(nasc);
        }
        catch (ParseException error){
            error.printStackTrace();
        }

        try {
            String _sValor = edtValorAdiantamento.getText().toString().replace(symbol.toString(), "");
            CLG_String CL_String = new CLG_String();
            _sValor = CL_String.prcTransformaValoremDouble(_sValor);
            reembolso.setCVD_ADIANTAMENTO_VALOR(Double.parseDouble(_sValor));
        }catch (Exception ex)
        {
            String _sErro = ex.getMessage();
        }


        //VIAGEM
        try {
            Date nasc = dateFormat.parse(edtDataInicio.getText().toString());
            reembolso.setCVD_VIAGEM_DATA_INICIO(nasc);
        }
        catch (ParseException error){
            error.printStackTrace();
        }
        try {
            Date nasc = dateFormat.parse(edtDataFim.getText().toString());
            reembolso.setCVD_VIAGEM_DATA_FIM(nasc);
        }
        catch (ParseException error){
            error.printStackTrace();
        }

        reembolso.setCVD_DESCRICAO(edtDescricao.getText().toString());

        reembolso.setCVD_TIPO(_gsCVD_TIPO);

        return reembolso;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //EXIBE O COMPONENTE DE DATA PARA SELECIONAR O DIA CORRETO
    public void setDataInicio(View view){
        CL_Datas.CriaFragmentData(getSupportFragmentManager(), dateListenerInicio, "Data Início");
    }

    //EXIBE O COMPONENTE DE DATA PARA SELECIONAR O DIA CORRETO
    public void setDataFim(View view){
        CL_Datas.CriaFragmentData(getSupportFragmentManager(), dateListenerFim, "Data Fim");
    }

    //EXIBE O COMPONENTE DE DATA PARA SELECIONAR O DIA CORRETO
    public void setDataAdiantamento(View view){
        CL_Datas.CriaFragmentData(getSupportFragmentManager(), dateListenerAdiantamento, "Data Adiantamento");
    }

    private DatePickerDialog.OnDateSetListener dateListenerInicio = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            edtDataInicio.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };

    private DatePickerDialog.OnDateSetListener dateListenerFim = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            edtDataFim.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };

    private DatePickerDialog.OnDateSetListener dateListenerAdiantamento = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            edtDataAdiantamento.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };
}

