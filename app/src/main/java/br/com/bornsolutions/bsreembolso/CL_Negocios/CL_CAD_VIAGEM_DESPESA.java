package br.com.bornsolutions.bsreembolso.CL_Negocios;

import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by guilherme on 07/09/2016.
 */
public class CL_CAD_VIAGEM_DESPESA extends  CL_Negocios_Master {

    public Boolean prcValidaGravar(Activity _psActivity, EditText _pedtDescricao) {
        Boolean _bRetorno = true;
        if (_pedtDescricao.getText().toString().equals("")) {
            Toast.makeText(_psActivity, "Campo Descrição Obrigatório!", Toast.LENGTH_LONG).show();
            //_pedtDescricao.setError("Campo Obrigatório!");
            //_pedtDescricao.requestFocus();
            _bRetorno = false;
        }

        return _bRetorno;
    }
}
