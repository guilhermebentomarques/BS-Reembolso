package br.com.bornsolutions.bsreembolso.Entidades;

import java.io.Serializable;

/**
 * Created by guilherme on 21/04/2016.
 */
public class CAD_CLIENTE { //ENVIA PESSOA COMO BUNDLE, A CLASSE PRECISA SER SERIALIZABLE EM BYTES - AÍ NÃO DA ERRO

    private int CL_ID;
    private String CL_RAZAOSOCIAL;
    private String CL_NOMEREDUZIDO;
    private String CL_NOMEFANTASIA;
    private String CL_CPFCNPJ;
    private String CL_CIDADE;
    private String CL_UF;

    public int getCL_ID() {
        return CL_ID;
    }

    public void setCL_ID(int CL_ID) {
        this.CL_ID = CL_ID;
    }

    public String getCL_RAZAOSOCIAL() {
        return CL_RAZAOSOCIAL;
    }

    public void setCL_RAZAOSOCIAL(String CL_RAZAOSOCIAL) {
        this.CL_RAZAOSOCIAL = CL_RAZAOSOCIAL;
    }

    public String getCL_NOMEREDUZIDO() {
        return CL_NOMEREDUZIDO;
    }

    public void setCL_NOMEREDUZIDO(String CL_NOMEREDUZIDO) {
        this.CL_NOMEREDUZIDO = CL_NOMEREDUZIDO;
    }

    public String getCL_NOMEFANTASIA() {
        return CL_NOMEFANTASIA;
    }

    public void setCL_NOMEFANTASIA(String CL_NOMEFANTASIA) {
        this.CL_NOMEFANTASIA = CL_NOMEFANTASIA;
    }

    public String getCL_CPFCNPJ() {
        return CL_CPFCNPJ;
    }

    public void setCL_CPFCNPJ(String CL_CPFCNPJ) {
        this.CL_CPFCNPJ = CL_CPFCNPJ;
    }

    public String getCL_CIDADE() {
        return CL_CIDADE;
    }

    public void setCL_CIDADE(String CL_CIDADE) {
        this.CL_CIDADE = CL_CIDADE;
    }

    public String getCL_UF() {
        return CL_UF;
    }

    public void setCL_UF(String CL_UF) {
        this.CL_UF = CL_UF;
    }
}

