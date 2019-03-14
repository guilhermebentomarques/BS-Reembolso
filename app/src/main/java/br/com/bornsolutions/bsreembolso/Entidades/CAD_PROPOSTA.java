package br.com.bornsolutions.bsreembolso.Entidades;

import java.io.Serializable;

/**
 * Created by guilherme on 29/04/2016.
 */
public class CAD_PROPOSTA implements Serializable {

    private int CP_ID;
    private int CPR_ID;
    private int CL_ID;
    private int CE_ID;
    private String CP_NUMERO;
    private String CP_STATUS;
    private String CP_TITULO;

    public String getCP_NUMERO() {
        return CP_NUMERO;
    }

    public void setCP_NUMERO(String CP_NUMERO) {
        this.CP_NUMERO = CP_NUMERO;
    }

    public String getCP_STATUS() {
        return CP_STATUS;
    }

    public void setCP_STATUS(String CP_STATUS) {
        this.CP_STATUS = CP_STATUS;
    }

    public String getCP_TITULO() {
        return CP_TITULO;
    }

    public void setCP_TITULO(String CP_TITULO) {
        this.CP_TITULO = CP_TITULO;
    }

    public int getCP_ID() {
        return CP_ID;
    }

    public void setCP_ID(int CP_ID) {
        this.CP_ID = CP_ID;
    }

    public int getCPR_ID() {
        return CPR_ID;
    }

    public void setCPR_ID(int CPR_ID) {
        this.CPR_ID = CPR_ID;
    }

    public int getCL_ID() {
        return CL_ID;
    }

    public void setCL_ID(int CL_ID) {
        this.CL_ID = CL_ID;
    }

    public int getCE_ID() {
        return CE_ID;
    }

    public void setCE_ID(int CE_ID) {
        this.CE_ID = CE_ID;
    }



}
