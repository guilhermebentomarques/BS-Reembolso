package br.com.bornsolutions.bsreembolso.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by GuilhermeBento on 03/03/2016.
 */
public class DatePickerFragment extends android.support.v4.app.DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    private int dia, mes, ano;

    public DatePickerFragment() {};

    public void setDateListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        dia = args.getInt("dia");
        mes = args.getInt("mes");
        ano = args.getInt("ano");
    }

    //ON CREATE
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), listener, ano, mes, dia);
    }
}
