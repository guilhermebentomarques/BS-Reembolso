package br.com.bornsolutions.bsreembolso.Fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.bornsolutions.bsreembolso.Entidades.CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.Expandable.Child;
import br.com.bornsolutions.bsreembolso.Expandable.ExpandListAdapter;
import br.com.bornsolutions.bsreembolso.Expandable.Group;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_CLIENTE;
import br.com.bornsolutions.bsreembolso.Repository.TB_CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.Util.Constantes;

/**
 * Created by guilherme on 02/05/2016.
 */
public class FragmentDespesaItem_Valores extends Fragment {

    TextView txtReembolso,txtDataAdiantamento, txtAdiantamentoValor, txtTitulo, txtValores, txtProposta, txtValorTotal;
    private int _igPosicaoSelecionada;
    private SharedPreferences preferences;
    TB_CAD_VIAGEM_DESPESA_ITEM TB_CAD_VIAGEM_DESPESA_ITEM;
    CAD_VIAGEM_DESPESA Reembolso;
    Integer _giCVD_ID;

    private ExpandListAdapter ExpAdapter;
    private ArrayList<Group> ExpListItems;
    private ExpandableListView ExpandList;

    RelativeLayout llRodape;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragmentdespesaitem_valores, container, false);

        txtDataAdiantamento = (TextView) view.findViewById(R.id.txtData);
        txtAdiantamentoValor = (TextView) view.findViewById(R.id.txtAdiantamentoValor);
        txtTitulo = (TextView) view.findViewById(R.id.txtTitulo);
        txtProposta = (TextView) view.findViewById(R.id.txtProposta);

        txtValorTotal = (TextView) view.findViewById(R.id.txtValorTotal);

        llRodape = (RelativeLayout) view.findViewById(R.id.llRodape);

        Reembolso = (CAD_VIAGEM_DESPESA) getActivity().getIntent().getExtras().getSerializable("Reembolso"); //BUSCA A PESSOA PASSADA COMO PARAMETRO

        _giCVD_ID = Reembolso.getCVD_ID();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final String _strDataViagem = dateFormat.format(Reembolso.getCVD_VIAGEM_DATA_INICIO());

        TB_CAD_CLIENTE TB_CAD_CLIENTE = new TB_CAD_CLIENTE(getActivity());
        final CAD_CLIENTE Cliente = TB_CAD_CLIENTE.prcRetornaCliente(Reembolso.getCL_ID());

        txtDataAdiantamento.setText(_strDataViagem);
        String _strAdiantamentoValor = "R$" + String.format("%.2f", Reembolso.getCVD_ADIANTAMENTO_VALOR());
        txtAdiantamentoValor.setText(_strAdiantamentoValor);
        txtProposta.setText(Cliente.getCL_RAZAOSOCIAL());
        txtTitulo.setText(Reembolso.getCVD_DESCRICAO());

        ExpandList = (ExpandableListView) view.findViewById(R.id.exp_list);
        ExpListItems = prcPreencheDetalhes();
        ExpAdapter = new ExpandListAdapter(getActivity(), ExpListItems);
        ExpandList.setAdapter(ExpAdapter);


            //listViewCustomItem.setOnItemClickListener(clickListenerReembolso); //SETA O CLICK
            //listViewCustomItem.setOnCreateContextMenuListener(contextmenulistener); //CLICA E SEGURA
            //listViewCustomItem.setOnItemLongClickListener(longclicklistenerReembolso); //CLICK LONGO PEGA O ID CLICADO

        return view;
    }

    public ArrayList<Group> prcPreencheDetalhes() {

        String[] _strADespesas = Constantes.TIPO_DESPESAS;

        TB_CAD_VIAGEM_DESPESA_ITEM TB_CAD_VIAGEM_DESPESA_ITEM = new TB_CAD_VIAGEM_DESPESA_ITEM(getActivity());

        ArrayList<Group> list = new ArrayList<Group>();

        ArrayList<Child> ch_list;

        Double _dSomaTotal = 0.0;

        TB_CAD_VIAGEM_DESPESA_ITEM = new TB_CAD_VIAGEM_DESPESA_ITEM(getActivity());
        List<CAD_VIAGEM_DESPESA_ITEM> listareembolsoitem_detalhe = TB_CAD_VIAGEM_DESPESA_ITEM.prcLista(_giCVD_ID);

        for (int i = 0; i < _strADespesas.length; i++)
        {
            final String _strTipo = _strADespesas[i].toString();
            final Double _dSumTipoDespesa = TB_CAD_VIAGEM_DESPESA_ITEM.prcGetSumTipoDespesa(_giCVD_ID, _strTipo);

            String _strValor = "R$" + String.format("%.2f", _dSumTipoDespesa);

            Group g = new Group();
            g.setName(_strTipo);
            g.setValor(_strValor);

            ch_list = new ArrayList<Child>();

            for(CAD_VIAGEM_DESPESA_ITEM r : listareembolsoitem_detalhe){

                if(r.getCVDI_TIPO_DESPESA().toString().equals(_strTipo))
                {
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    final String _strDataViagem = dateFormat.format(r.getCVDI_DATA());
                    final String _sValor = String.format("%.2f", r.getCVDI_VALOR());

                    _dSomaTotal = _dSomaTotal +  r.getCVDI_VALOR();

                    Child ch = new Child();
                    ch.setData(_strDataViagem);
                    ch.setValor("R$" + _sValor);
                    ch_list.add(ch);

                }
            }

            g.setItems(ch_list);
            list.add(g);
        }

        Double _dValorTotal = Reembolso.getCVD_ADIANTAMENTO_VALOR() - _dSomaTotal;

        txtValorTotal.setText("R$" + String.format("%.2f", _dValorTotal));

        if(_dValorTotal > 0)
            llRodape.setBackgroundColor(getResources().getColor(R.color.verde));
        else
            llRodape.setBackgroundColor(getResources().getColor(R.color.vermelho));

        return list;
    }
}


