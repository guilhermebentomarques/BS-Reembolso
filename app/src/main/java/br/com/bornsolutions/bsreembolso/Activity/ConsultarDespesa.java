package br.com.bornsolutions.bsreembolso.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Datas;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

public class ConsultarDespesa extends BaseActivity {

    ImageView consulta_btnDataInicio, consulta_btnDataFim;
    Button consulta_btnPesquisar;
    EditText consulta_edtDataInicio, consulta_edtDataFim;
    CLG_Datas CL_Datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_despesa);

        CL_Datas = new CLG_Datas();

        //CRIA A ACTION BAR COM O BOTÃO VOLTA
        getSupportActionBar().setTitle("Consultar Despesas");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        consulta_btnDataInicio = (ImageView) findViewById(R.id.consulta_btnDataInicio);
        consulta_btnDataFim = (ImageView) findViewById(R.id.consulta_btnDataFim);
        consulta_btnPesquisar = (Button) findViewById(R.id.consulta_btnPesquisar);

        consulta_edtDataInicio = (EditText) findViewById(R.id.consulta_edtDataInico);
        consulta_edtDataFim = (EditText) findViewById(R.id.consulta_edtDataFim);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        consulta_edtDataInicio.setText(dateFormat.format(date));
        consulta_edtDataFim.setText(dateFormat.format(date));

        try {
            Constantes.DATA_INICIO = "";
            Constantes.DATA_FIM = "";
        }
        catch(Exception ex) {
        }

        consulta_btnPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //BUSCA SOMENTE AS DESPESAS FINALIZADAS
                //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
                Intent i = new Intent(ConsultarDespesa.this, ListaDespesa.class);

                Bundle params = new Bundle();
                params.putString("TIPO_PESQUISA", "CONSULTA");
                Constantes.DATA_INICIO = consulta_edtDataInicio.getText().toString();
                Constantes.DATA_FIM = consulta_edtDataFim.getText().toString();
                i.putExtras(params);
                startActivity(i);
                finish();
            }
        });

    }

    //EXIBE O COMPONENTE DE DATA PARA SELECIONAR O DIA CORRETO
    public void prcconsulta_edtDataInicio(View view){
        CL_Datas.CriaFragmentData(getSupportFragmentManager(), dateListenerInicio, "Data Início");
    }

    //EXIBE O COMPONENTE DE DATA PARA SELECIONAR O DIA CORRETO
    public void prcconsulta_edtDataFim(View view) {
        CL_Datas.CriaFragmentData(getSupportFragmentManager(), dateListenerFim, "Data Fim");
    }

    private DatePickerDialog.OnDateSetListener dateListenerInicio = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            consulta_edtDataInicio.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };

    private DatePickerDialog.OnDateSetListener dateListenerFim = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            consulta_edtDataFim.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
