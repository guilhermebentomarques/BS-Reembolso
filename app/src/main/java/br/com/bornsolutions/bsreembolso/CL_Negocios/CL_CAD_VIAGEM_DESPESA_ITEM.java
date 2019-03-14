package br.com.bornsolutions.bsreembolso.CL_Negocios;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by guilherme on 31/08/2016.
 */
public class CL_CAD_VIAGEM_DESPESA_ITEM extends  CL_Negocios_Master {

    public Boolean prcValidaGravar(Activity _psActivity, EditText _pedtData, EditText _pedtValor, EditText _pedtDescricao)
    {
        Boolean _bRetorno = true;
        if (_pedtData.getText().toString().equals("")) {
            Toast.makeText(_psActivity, "Campo Data Obrigatório!", Toast.LENGTH_LONG).show();
            //_pedtData.setError("Campo Obrigatório!");
            //_pedtData.requestFocus();
            _bRetorno = false;
        } else if (_pedtValor.getText().toString().equals("")) {
            Toast.makeText(_psActivity, "Campo Valor Obrigatório!", Toast.LENGTH_LONG).show();
            //_pedtValor.setError("Campo Obrigatório!");
            //_pedtValor.requestFocus();
            _bRetorno = false;
        }else if (_pedtValor.getText().toString().equals("R$0,00")) {
            Toast.makeText(_psActivity, "Campo Valor deve ser maior que R$0,00!", Toast.LENGTH_LONG).show();
            //_pedtValor.setError("Valor Inválido!");
            //_pedtValor.requestFocus();
            _bRetorno = false;
        }
        else if (_pedtDescricao.getText().toString().equals("")) {
            Toast.makeText(_psActivity, "Campo Descrição Obrigatório!", Toast.LENGTH_LONG).show();
            //_pedtDescricao.setError("Campo Obrigatório!");
            //_pedtDescricao.requestFocus();
            _bRetorno = false;
        }

        return _bRetorno;
    }
}
