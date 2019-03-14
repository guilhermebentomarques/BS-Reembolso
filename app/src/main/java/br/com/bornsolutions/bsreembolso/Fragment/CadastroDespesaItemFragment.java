package br.com.bornsolutions.bsreembolso.Fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import br.com.bornsolutions.bsreembolso.Activity.DespesaItensActivity;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Datas;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Fotos;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Moeda;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_String;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.Util.Constantes;
import br.com.bornsolutions.bsreembolso.WebService.WS_Envio_DespesaItem;

public class CadastroDespesaItemFragment extends Fragment {

    static final int RESULT_OK           = -1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    EditText cdiedtData, cdiedtValor, cdiedtKMValor, cdiedtKMDistancia, cdiedtDescricao;
    TextView txtValorKM, txtDistanciaKM;
    Button cdibtnGravar;
    ImageButton btnFoto, btnData;
    Spinner spnTipoDespesa;
    ImageView ivFotoDespesa;
    String _strNomeFoto, _gsTipoDespesa, prpUsuario_Logado, prpUsuario_ID, prpTipoPesquisa;
    File photoFile = null;
    TextView cditvTirarFoto;
    Integer _giCVD_ID;
    Boolean _gbEditando = false;
    Switch switchDespesaBorn;

    private String current = "";

    SharedPreferences preferences;

    private boolean zoomOut =  false;

    //CLASSES
    CL_CAD_VIAGEM_DESPESA_ITEM CL_CAD_VIAGEM_DESPESA_ITEM;
    CLG_String CL_STRING;
    CLG_Fotos CL_FOTOS;

    CAD_VIAGEM_DESPESA_ITEM ReembolsoItem;
    CAD_VIAGEM_DESPESA Reembolso;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_cadastro_despesa_item, container, false);

        CL_CAD_VIAGEM_DESPESA_ITEM = new CL_CAD_VIAGEM_DESPESA_ITEM();
        CL_FOTOS = new CLG_Fotos();
        CL_STRING = new CLG_String();

        prpTipoPesquisa = getActivity().getIntent().getExtras().getSerializable("TIPO_PESQUISA").toString();
        ReembolsoItem = (CAD_VIAGEM_DESPESA_ITEM) getActivity().getIntent().getExtras().getSerializable("ReembolsoItem"); //BUSCA A PESSOA PASSADA COMO PARAMETRO

        //CRIA A ACTION BAR COM O BOTÃO VOLTAR
        if(ReembolsoItem == null)
            _gbEditando = false;
        else {
            _gbEditando = true;
        }

        Reembolso = (CAD_VIAGEM_DESPESA) getActivity().getIntent().getExtras().getSerializable("Reembolso"); //BUSCA A PESSOA PASSADA COMO PARAMETRO
        if(Reembolso != null)
            _giCVD_ID = Reembolso.getCVD_ID();

        //FIND VIEW EM TODOS COMPONENTES DA TELA
        prcIniciaComponentesTela(view);

        //EDITAR
        if(_gbEditando)
        {
            cdibtnGravar.setText("Atualizar");

            //PREENCHE DATA
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            cdiedtData.setText(dateFormat.format(ReembolsoItem.getCVDI_DATA()));

            //PREENCHE KM RODADO
            cdiedtKMValor.setText(String.format("%.2f", ReembolsoItem.getCVDI_KM_VALOR()));
            //PREENCHE DISTANCIA
            cdiedtKMDistancia.setText(String.format("%.2f", ReembolsoItem.getCVDI_KM_DISTANCIA()));

            int _iPos = CL_STRING.indexOf(spnTipoDespesa.getAdapter(), ReembolsoItem.getCVDI_TIPO_DESPESA());
            spnTipoDespesa.setSelection(_iPos);

            if(ReembolsoItem.getCVDI_DESPESA_BORN().equals("S"))
                switchDespesaBorn.setChecked(true);
            else
                switchDespesaBorn.setChecked(false);

            //PREENCHE VALOR
            cdiedtValor.setText(String.format("%.2f", ReembolsoItem.getCVDI_VALOR()));

            //PREENCHE DESCRICAO
            cdiedtDescricao.setText(ReembolsoItem.getCVDI_DESCRICAO());

            //TRATA CASO NÃO EXISTA FOTO - POR EXEMPLO EM KM RODADO
            try {
                ivFotoDespesa.setImageBitmap(CLG_Fotos.prcSQLite_RetornaFoto(ReembolsoItem.getCVDI_FOTO()));
            }
            catch (Exception ex)
            {}

        }

        //BUSCA O USUARIO LOGADO E
        //RECEBE O NOME ENVIADO POR PARAMETRO
        CL_Preferences Preferences = new CL_Preferences();
        preferences = Preferences.PrcGet_SharedPreferences_Login(getActivity(), "CAD_USUARIO");
        prpUsuario_Logado = preferences.getString("CU_LOGIN", null);
        prpUsuario_ID = preferences.getString("CU_ID", null);

        if(Reembolso.getCVD_TIPO().equals("V"))
            txtValorKM.setText("Valor KM");
        else
            txtValorKM.setText("Combustível");

        //NÃO EXIBE O BOTÃO ATUALIZAR, SE JÁ FOR UM REGISTRO FINALIZADO
        if(prpTipoPesquisa.equals("CONSULTA")) {
            cdibtnGravar.setVisibility(View.INVISIBLE);
            btnFoto.setVisibility(View.INVISIBLE);
            spnTipoDespesa.setEnabled(false);
            cdiedtDescricao.setEnabled(false);
            cdiedtValor.setEnabled(false);
            switchDespesaBorn.setEnabled(false);
            cditvTirarFoto.setVisibility(View.INVISIBLE);
            btnData.setVisibility(View.INVISIBLE);
        }

        //ON ITEM SELECTED SPINNER
        spnTipoDespesa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Object item = parent.getItemAtPosition(pos);
                _gsTipoDespesa = spnTipoDespesa.getSelectedItem().toString();

                if(_gsTipoDespesa == "Km Rodado") {
                    prcMostraDialogKMRodado(Reembolso.getCVD_TIPO());
                    cdiedtKMValor.setVisibility(View.VISIBLE);
                    cdiedtKMDistancia.setVisibility(View.VISIBLE);
                    txtDistanciaKM.setVisibility(View.VISIBLE);
                    txtValorKM.setVisibility(View.VISIBLE);
                }
                else
                {
                    txtDistanciaKM.setVisibility(View.INVISIBLE);
                    txtValorKM.setVisibility(View.INVISIBLE);
                    cdiedtKMValor.setVisibility(View.INVISIBLE);
                    cdiedtKMValor.setText("R$0,00");
                    cdiedtKMDistancia.setVisibility(View.INVISIBLE);
                    cdiedtKMDistancia.setText("0,00");
                }
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VALIDAÇÃO ANTES DE ABRIR A CAMERA
                if (CL_CAD_VIAGEM_DESPESA_ITEM.prcValidaGravar(getActivity(), cdiedtData, cdiedtValor, cdiedtDescricao)) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //CHAMA A CAMERA

                    //CONFIRMA SE A CAMERA ESTÁ ABERTA
                    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                        try {
                            //BUSCA DATA
                            String[] parts_data = cdiedtData.getText().toString().split("/");
                            String _strDia = parts_data[0]; // DIA
                            String _strMes = parts_data[1]; // MES
                            String _strAno = parts_data[2]; // ANO

                            //BUSCA VALOR
                            String parts_valor = cdiedtValor.getText().toString();

                            //CRIA ARQUIVO DA FOTO COM O NOME DO EDTTEXT, E JA CRIA O DIRETÓRIO CASO O MESMO NÃO EXISTA.
                            _strNomeFoto = prpUsuario_ID + "_" + Reembolso.getCVD_ID() + "_" + _strDia + _strMes + _strAno + System.currentTimeMillis();
                            photoFile = CL_FOTOS.prcCriaArquivoImagem("BSReembolso", _strNomeFoto);
                        } catch (IOException ex) {
                        }

                        //COMEÇA A EXIBIR A CAMERA, COM A FOTO EM ALTA RESOLUÇÃO.
                        if (photoFile != null) {
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                        }
                    }
                }
            }
        });

        cdibtnGravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CL_CAD_VIAGEM_DESPESA_ITEM.prcValidaGravar(getActivity(), cdiedtData, cdiedtValor, cdiedtDescricao)) {

                    CAD_VIAGEM_DESPESA_ITEM r = prcMontarReembolsoItem(_gbEditando);

                    TB_CAD_VIAGEM_DESPESA_ITEM TB_CAD_VIAGEM_DESPESA_ITEM = new TB_CAD_VIAGEM_DESPESA_ITEM(getActivity());

                    if (_gbEditando) {

                        TB_CAD_VIAGEM_DESPESA_ITEM.prcAtualizar(r);

                        TB_CAD_VIAGEM_DESPESA TB_CAD_VIAGEM_DESPESA = new TB_CAD_VIAGEM_DESPESA(getActivity());
                        CAD_VIAGEM_DESPESA Despesa = TB_CAD_VIAGEM_DESPESA.prcRetornaReembolso(Integer.parseInt(r.getCVD_ID().toString()));
                        //ENVIA A DESPESA QUE JÁ EXISTE PELO WEB SERVICE
                        //ENVIA DESPESA POR WEB SERVICE
                        try {
                            //NO WEB SERVICE JÁ VALIDA TUDO
                            WS_Envio_DespesaItem WS_Envio_DespesaItem = new WS_Envio_DespesaItem(getActivity(), r, Despesa, prpUsuario_ID, prpUsuario_Logado, prpTipoPesquisa);
                            WS_Envio_DespesaItem.execute();

                        } catch (Exception ex) {
                            String _sErro = ex.getMessage();
                        }
                    } else {
                        TB_CAD_VIAGEM_DESPESA_ITEM.prcIncluir(getContext(), r, prpUsuario_ID, prpUsuario_Logado, prpTipoPesquisa);
                    }
                }
            }
        });

        ivFotoDespesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //.setCurrentItem(1);
            }
        });

        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CLG_Datas CL_Datas = new CLG_Datas();
                CL_Datas.CriaFragmentData(getActivity().getSupportFragmentManager(), dateListener, "Data Despesa");
            }
        });

        return view;
    }

    private void prcIniciaComponentesTela(View view) {
        //ABRE CAMERA
        btnFoto = (ImageButton) view.findViewById(R.id.btnFotoFrag);
        btnData = (ImageButton) view.findViewById(R.id.btnData);

        //DESCRICAO
        cdiedtDescricao = (EditText) view.findViewById(R.id.cdiedtDescricao);

        switchDespesaBorn = (Switch) view.findViewById(R.id.switchDespesaBorn);

        //KM
        txtDistanciaKM = (TextView) view.findViewById(R.id.txtDistanciaKM);
        txtValorKM = (TextView) view.findViewById(R.id.txtValorKM);


        //DATA
        cdiedtData = (EditText) view.findViewById(R.id.cdiedtData);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        cdiedtData.setText(dateFormat.format(date));

        //VALOR
        cdiedtValor = (EditText) view.findViewById(R.id.cdiedtValor);
        cdiedtValor.setText("R$0,00");

        //KM DISTANCIA E VALOR
        cdiedtKMDistancia = (EditText) view.findViewById(R.id.cdiedtKMDistancia);
        cdiedtKMValor = (EditText) view.findViewById(R.id.cdiedtKMValor);
        cdiedtKMValor.setText("R$0,00");
        cdiedtKMDistancia.setText("0.0");


        /*if(_gbEditando) {
            cdiedtKMValor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prcMostraDialogKMRodado(Reembolso.getCVD_TIPO());
                }
            });

            cdiedtKMDistancia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prcMostraDialogKMRodado(Reembolso.getCVD_TIPO());
                }
            });
        }*/

        //TIPO DESPESA E FOTO
        spnTipoDespesa = (Spinner) view.findViewById(R.id.spnTipoDespesa);
        ivFotoDespesa = (ImageView) view.findViewById(R.id.ivFotoDespesa);

        //COLOCA O CAMPO MOEDA PARA PREENCHER NO FORMATO CORRETO
        cdiedtValor.addTextChangedListener(new CLG_Moeda(cdiedtValor));
        cdiedtKMValor.addTextChangedListener(new CLG_Moeda(cdiedtKMValor));

        //CRIA OS TIPOS DE DESPESAS
        String[] _strADespesas = Constantes.TIPO_DESPESAS;
        ArrayAdapter<String> ArrayTipoDespesa = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, _strADespesas);
        ArrayTipoDespesa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnTipoDespesa.setAdapter(ArrayTipoDespesa);

        cdibtnGravar = (Button) view.findViewById(R.id.cdibtnGravar);

        cditvTirarFoto = (TextView) view.findViewById(R.id.cditvTirarFoto);
    }

    public void prcMostraDialogKMRodado(String _psTipoDespesa) {

        Locale locale = getContext().getResources().getConfiguration().locale;
        Currency currency= Currency.getInstance(locale);
        final String symbol = currency.getSymbol();

        if(!prpTipoPesquisa.equals("CONSULTA")) {
            if (_psTipoDespesa.equals("V")) { //VIAGEM - CALCULA KM RODADO VEZES O CENTAVO ACORDADO COM O CLIENTE
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                //CRIA DIALOGO BASEADO NO LAYOUT DO XML KM RODADO
                final View dialogView = inflater.inflate(R.layout.dialog_km_rodado, null);
                dialogBuilder.setView(dialogView);

                TextView txt = (TextView) dialogView.findViewById(R.id.txtTipoDoKM);
                txt.setText("Valor KM");

                //BUSCA OS ITENS EDIT TEXT
                final EditText _edtKMRodadoValor = (EditText) dialogView.findViewById(R.id.edtKmRodadoValor);
                final EditText _edtKMRodadoQtdade = (EditText) dialogView.findViewById(R.id.edtKmRodadoQtdade);
                final Switch swtCalculaCombustivel = (Switch) dialogView.findViewById(R.id.swtCalculaCombustivel);

                //SETA O CAMPO COM MASCARA DE MOEDA
                _edtKMRodadoValor.addTextChangedListener(new CLG_Moeda(_edtKMRodadoValor));

                if(_gbEditando)
                {
                    //String _sValorKM = ReembolsoItem.getCVDI_KM_VALOR().toString().replace(".",",");
                    String _sValorKM = String.format("%.2f", ReembolsoItem.getCVDI_KM_VALOR());
                    _edtKMRodadoValor.setText(String.format("%.2f", ReembolsoItem.getCVDI_KM_VALOR()));

                    String _sKmRodadoQtdade = ReembolsoItem.getCVDI_KM_DISTANCIA().toString();
                    _edtKMRodadoQtdade.setText(_sKmRodadoQtdade);

                }

                dialogBuilder.setTitle("Cálculo - KM Rodado");
                dialogBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //String _strValorKM = _edtKMRodadoValor.getText().toString().replace("R$", "");
                        String _strValorKM = _edtKMRodadoValor.getText().toString().replace(symbol.toString(), "");
                        String _strQtdadeKM = _edtKMRodadoQtdade.getText().toString();

                        //CALCULA O VALOR DO KM RODADO E JOGA NO CAMPO VALOR TOTAL
                        double _dValor = Double.parseDouble(_strValorKM.replace(",", "."));
                        double _dDistanciaKmRodado = Double.parseDouble(_strQtdadeKM);

                        double _dValorTotal = 0;
                        if(!swtCalculaCombustivel.isChecked())
                            _dValorTotal = (_dValor * _dDistanciaKmRodado);
                        else
                            _dValorTotal = ((_dValor * _dDistanciaKmRodado) / 10);

                        String _sValorFinal = String.format("%.2f", _dValorTotal); //FORMATA VALOR FINAL
                        String _sValorKM = String.format("%.2f", _dValor); //FORMATA VALOR FINAL
                        String _sDistanciaKMRodado = String.format("%.2f", _dDistanciaKmRodado); //FORMATA VALOR FINAL

                        cdiedtValor.setText(_sValorFinal);
                        cdiedtKMValor.setText(_sValorKM);
                        cdiedtKMDistancia.setText(_sDistanciaKMRodado);

                    }
                });

                dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });

                //ABRE O DIALOG
                AlertDialog b = dialogBuilder.create();
                b.show();
            } else if (_psTipoDespesa.equals("L")) //LOCAL - CALCULA KM PELO PREÇO DA GASOLINA
            {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();

                //CRIA DIALOGO BASEADO NO LAYOUT DO XML KM RODADO
                final View dialogView = inflater.inflate(R.layout.dialog_km_rodado, null);
                dialogBuilder.setView(dialogView);

                TextView txt = (TextView) dialogView.findViewById(R.id.txtTipoDoKM);
                txt.setText("Valor Combustível");

                //BUSCA OS ITENS EDIT TEXT
                final EditText _edtKMRodadoValor = (EditText) dialogView.findViewById(R.id.edtKmRodadoValor);
                final EditText _edtKMRodadoQtdade = (EditText) dialogView.findViewById(R.id.edtKmRodadoQtdade);

                //DESPESA LOCAL NÃO MOSTRA CALCULAR COMBUSTÍVEL - POIS JA É PADRÃO
                final Switch swtCalculaCombustivel = (Switch) dialogView.findViewById(R.id.swtCalculaCombustivel);
                swtCalculaCombustivel.setVisibility(View.INVISIBLE);

                //SETA O CAMPO COM MASCARA DE MOEDA
                _edtKMRodadoValor.addTextChangedListener(new CLG_Moeda(_edtKMRodadoValor));

                if(_gbEditando)
                {
                    String _sValorKM = String.format("%.2f", ReembolsoItem.getCVDI_KM_VALOR());
                    _edtKMRodadoValor.setText(String.format("%.2f", ReembolsoItem.getCVDI_KM_VALOR()));
                    _edtKMRodadoQtdade.setText(String.format("%.2f", ReembolsoItem.getCVDI_KM_DISTANCIA()));
                }

                dialogBuilder.setTitle("Cálculo - KM Rodado");
                dialogBuilder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //String _strValorKM = _edtKMRodadoValor.getText().toString().replace("R$", "");
                        String _strValorKM = _edtKMRodadoValor.getText().toString().replace(symbol.toString(), "");
                        String _strQtdadeKM = _edtKMRodadoQtdade.getText().toString();

                        //CALCULA O VALOR DO KM RODADO E JOGA NO CAMPO VALOR TOTAL
                        double _dValor = Double.parseDouble(_strValorKM.replace(",", "."));
                        double _dDistanciaKmRodado = Double.parseDouble(_strQtdadeKM);

                        double _dValorTotal = ((_dValor * _dDistanciaKmRodado) / 10);

                        String _sValorFinal = String.format("%.2f", _dValorTotal); //FORMATA VALOR FINAL
                        String _sValorKM = String.format("%.2f", _dValor); //FORMATA VALOR FINAL
                        String _sDistanciaKMRodado = String.format("%.2f", _dDistanciaKmRodado); //FORMATA VALOR FINAL

                        cdiedtValor.setText(_sValorFinal);
                        cdiedtKMValor.setText(_sValorKM);
                        cdiedtKMDistancia.setText(_sDistanciaKMRodado);

                    }
                });

                dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });

                if(!prpTipoPesquisa.equals("CONSULTA")) {
                    //ABRE O DIALOG
                    AlertDialog b = dialogBuilder.create();
                    b.show();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
                Intent i = new Intent(getActivity(), DespesaItensActivity.class);
                i.putExtra("Reembolso",Reembolso); //ENVIA O REEMBOLSO COMO BUNDLE, A CLASSE PRECISA SER SERIALIZABLE EM BYTES - AÍ NÃO DA ERRO
                Bundle params = new Bundle();
                params.putString("CU_LOGIN", prpUsuario_Logado);
                params.putString("CU_ID", prpUsuario_ID);
                i.putExtras(params);
                startActivity(i);
                getActivity().finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear +1;
            cdiedtData.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
        }
    };

    public void btnAbreCamera(View view)
    {
        if(CL_CAD_VIAGEM_DESPESA_ITEM.prcValidaGravar(getActivity(), cdiedtData,cdiedtValor,cdiedtDescricao)) {

            //CHAMA A CAMERA
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            //CONFIRMA SE A CAMERA ESTÁ ABERTA
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                try {
                    //BUSCA DATA
                    String[] parts_data = cdiedtData.getText().toString().split("/");
                    String _strDia = parts_data[0]; // DIA
                    String _strMes = parts_data[1]; // MES
                    String _strAno = parts_data[2]; // ANO

                    //BUSCA VALOR
                    String parts_valor = cdiedtValor.getText().toString();

                    //CRIA ARQUIVO DA FOTO COM O NOME DO EDTTEXT, E JA CRIA O DIRETÓRIO CASO O MESMO NÃO EXISTA.
                    _strNomeFoto = prpUsuario_ID + "_" + Reembolso.getCVD_ID() + "_" + _strDia + _strMes + _strAno + System.currentTimeMillis();
                    photoFile = CL_FOTOS.prcCriaArquivoImagem("BSReembolso", _strNomeFoto);
                } catch (IOException ex) {
                }

                //COMEÇA A EXIBIR A CAMERA, COM A FOTO EM ALTA RESOLUÇÃO.
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            try {
                //ADICIONA A FOTO AO COMPONENTE IMAGEVIEW
                CL_FOTOS.prcAdicionaFoto_ImageView(ivFotoDespesa, photoFile.getAbsolutePath());
            }
            catch (Exception ex)
            {

            }
            //SE O ARQUIVO FOI CRIADO COM SUCESSO - EXIBE O MESMO NA GALERIA.
            if (photoFile != null)
            {
                //REDIMENSIONA PARA RESOLUÇÃO PEQUENA
                try {
                    CL_FOTOS.prcRedimensionaFoto_Porcentagem(photoFile, 20, 80); //NÃO ALTERAR ESSE CÓDIGO, POIS A FOTO MAIOR DA ERRO DE MEMÓRIA
                } catch (IOException e) {
                    e.printStackTrace();
                }

                CL_FOTOS.prcExibeFotoNaGaleria(getActivity(), photoFile);
            }
        }
    }

    private CAD_VIAGEM_DESPESA_ITEM prcMontarReembolsoItem(Boolean _pbEditando){

        Locale locale = this.getResources().getConfiguration().locale;
        Currency currency= Currency.getInstance(locale);
        final String symbol = currency.getSymbol();


        CAD_VIAGEM_DESPESA_ITEM reembolso = new CAD_VIAGEM_DESPESA_ITEM();
        reembolso.setCVD_ID(_giCVD_ID);
        try {
            reembolso.setCVDI_ID_BORN(ReembolsoItem.getCVDI_ID_BORN());
        }
        catch (Exception ex)
        {

        }

        if(switchDespesaBorn.isChecked())
            reembolso.setCVDI_DESPESA_BORN("S");
        else
            reembolso.setCVDI_DESPESA_BORN("N");

        //ADIANTAMENTO
        DateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date nasc = dateFormat.parse(cdiedtData.getText().toString());
            reembolso.setCVDI_DATA(nasc);
        }
        catch (ParseException error){
            error.printStackTrace();
        }

        reembolso.setCVDI_TIPO_DESPESA(_gsTipoDespesa);

        CLG_String CL_String = new CLG_String();


        //VALOR
        String _sValor = cdiedtValor.getText().toString().replace(symbol.toString(), "");
        _sValor = CL_String.prcTransformaValoremDouble(_sValor);
        reembolso.setCVDI_VALOR(Double.parseDouble(_sValor));

        //KM VALOR OU COMBUSTIVEL VALOR
        String  _sValorKM = cdiedtKMValor.getText().toString().replace(symbol.toString(), "");
        _sValorKM = CL_String.prcTransformaValoremDouble(_sValorKM);
        reembolso.setCVDI_KM_VALOR(Double.parseDouble(_sValorKM));

        //KM DISTANCIA
        String _sValorKMDist = cdiedtKMDistancia.getText().toString().replace(symbol.toString(), "");
        _sValorKMDist = CL_String.prcTransformaValoremDouble(_sValorKMDist);
        reembolso.setCVDI_KM_DISTANCIA(Double.parseDouble(_sValorKMDist));

        reembolso.setCVDI_DESCRICAO(cdiedtDescricao.getText().toString());

        if (_pbEditando) {
            reembolso.setCVDI_FOTO(ReembolsoItem.getCVDI_FOTO());
            reembolso.setCVD_ID(ReembolsoItem.getCVD_ID());
            reembolso.setCVDI_ID(ReembolsoItem.getCVDI_ID());
        }
        else {
            try {
                reembolso.setCVDI_FOTO(CLG_Fotos.prcSQLite_SalvaFoto(photoFile.getAbsolutePath()));
            }
            catch (Exception ex)
            {

            }
        }

        return reembolso;

    }
}
