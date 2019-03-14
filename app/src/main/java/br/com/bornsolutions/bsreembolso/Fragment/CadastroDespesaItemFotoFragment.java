package br.com.bornsolutions.bsreembolso.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Fotos;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA;
import br.com.bornsolutions.bsreembolso.Entidades.CAD_VIAGEM_DESPESA_ITEM;
import br.com.bornsolutions.bsreembolso.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CadastroDespesaItemFotoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CadastroDespesaItemFotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CadastroDespesaItemFotoFragment extends Fragment {

    EditText edtData, edtValor, edtKMValor, edtKMDistancia, edtDescricao;
    Button btnGravar;
    Spinner spnTipoDespesa;
    ImageView ivFotoDespesa;
    String _strNomeFoto;
    CLG_Fotos CL_FOTOS = new CLG_Fotos();
    File photoFile = null;
    CAD_VIAGEM_DESPESA Reembolso;
    String _gsTipoDespesa;
    Integer _giCVD_ID;

    String prpUsuario_Logado, prpUsuario_ID;

    SharedPreferences preferences;

    //EDITAR
    CAD_VIAGEM_DESPESA_ITEM ReembolsoItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cadastro_despesa_item_foto, container, false);

        ReembolsoItem = (CAD_VIAGEM_DESPESA_ITEM) getActivity().getIntent().getExtras().getSerializable("ReembolsoItem"); //BUSCA A PESSOA PASSADA COMO PARAMETRO

        Reembolso = (CAD_VIAGEM_DESPESA) getActivity().getIntent().getExtras().getSerializable("Reembolso"); //BUSCA A PESSOA PASSADA COMO PARAMETRO
        if(Reembolso != null)
            _giCVD_ID = Reembolso.getCVD_ID();

        if(ReembolsoItem != null) {
            ImageView ivFoto = (ImageView) view.findViewById(R.id.ivFotoDespesaFrag);

            try {
                CLG_Fotos CL_FOTOS = new CLG_Fotos();
                ivFoto.setImageBitmap(CL_FOTOS.prcSQLite_RetornaFoto(ReembolsoItem.getCVDI_FOTO()));
            }
            catch (Exception ex)
            {

            }
        }

        return view;

    }
}
