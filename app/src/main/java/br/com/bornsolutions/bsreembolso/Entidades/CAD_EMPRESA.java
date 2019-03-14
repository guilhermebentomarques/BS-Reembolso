package br.com.bornsolutions.bsreembolso.Entidades;

import java.io.Serializable;

/**
 * Created by guilherme on 29/04/2016.
 */
public class CAD_EMPRESA implements Serializable {

    private int CE_ID;
    private String CE_CNPJ;
    private String CE_RAZAOSOCIAL;
    private String CE_NOMEREDUZIDO;

    public int getCE_ID() {
        return CE_ID;
    }

    public void setCE_ID(int CE_ID) {
        this.CE_ID = CE_ID;
    }

    public String getCE_CNPJ() {
        return CE_CNPJ;
    }

    public void setCE_CNPJ(String CE_CNPJ) {
        this.CE_CNPJ = CE_CNPJ;
    }

    public String getCE_RAZAOSOCIAL() {
        return CE_RAZAOSOCIAL;
    }

    public void setCE_RAZAOSOCIAL(String CE_RAZAOSOCIAL) {
        this.CE_RAZAOSOCIAL = CE_RAZAOSOCIAL;
    }

    public String getCE_NOMEREDUZIDO() {
        return CE_NOMEREDUZIDO;
    }

    public void setCE_NOMEREDUZIDO(String CE_NOMEREDUZIDO) {
        this.CE_NOMEREDUZIDO = CE_NOMEREDUZIDO;
    }

}
