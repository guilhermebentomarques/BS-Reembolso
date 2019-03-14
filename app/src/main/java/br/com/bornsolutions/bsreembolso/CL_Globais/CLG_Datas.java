package br.com.bornsolutions.bsreembolso.CL_Globais;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.Calendar;

import br.com.bornsolutions.bsreembolso.Fragment.DatePickerFragment;

/**
 * Created by guilherme on 19/04/2016.
 */
public class CLG_Datas  {

    public void CriaFragmentData(FragmentManager fragmentManager, DatePickerDialog.OnDateSetListener listener, String _psTitulo)
    {
        DatePickerFragment datePickerFragment = new DatePickerFragment();

        Calendar c = Calendar.getInstance();

        Bundle bundle = new Bundle(); //ARQUIVO DE PROPRIEDADES, CHAVES E VALORES
        bundle.putInt("dia", c.get(c.DAY_OF_MONTH));
        bundle.putInt("mes", c.get(c.MONTH));
        bundle.putInt("ano", c.get(c.YEAR));

        datePickerFragment.setArguments(bundle);
        datePickerFragment.setDateListener(listener);
        datePickerFragment.show(fragmentManager, _psTitulo);
    }
}
