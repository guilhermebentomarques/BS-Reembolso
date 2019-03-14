package br.com.bornsolutions.bsreembolso.Entidades;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by guilherme on 26/04/2016.
 */
public class CAD_VIAGEM_DESPESA implements Serializable{ //SERIALIZABLE PARA PASSAR COMO PARAMETRO

    private int CVD_ID;
    private int CVD_ID_BORN;
    private int CL_ID;
    private int CU_ID;
    private Date CVD_ADIANTAMENTO_DATA;
    private Double CVD_ADIANTAMENTO_VALOR;
    private Date CVD_VIAGEM_DATA_INICIO;
    private Date CVD_VIAGEM_DATA_FIM;
    private String CVD_DESCRICAO;
    private String CVD_TIPO; //V OU F
    private String CVD_FINALIZADO;

    public int getCVD_ID() {
        return CVD_ID;
    }

    public void setCVD_ID(int CVD_ID) {
        this.CVD_ID = CVD_ID;
    }

    public int getCVD_ID_BORN() {
        return CVD_ID_BORN;
    }

    public void setCVD_ID_BORN(int CVD_ID_BORN) {
        this.CVD_ID_BORN = CVD_ID_BORN;
    }

    public int getCL_ID() {
        return CL_ID;
    }

    public void setCL_ID(int CP_ID) {
        this.CL_ID = CP_ID;
    }

    public int getCU_ID() {
        return CU_ID;
    }

    public void setCU_ID(int CU_ID) {
        this.CU_ID = CU_ID;
    }

    public String getCVD_TIPO() {
        return CVD_TIPO;
    }

    public void setCVD_TIPO(String CVD_TIPO) {
        this.CVD_TIPO = CVD_TIPO;
    }

    public Date getCVD_ADIANTAMENTO_DATA() {
        return CVD_ADIANTAMENTO_DATA;
    }

    public void setCVD_ADIANTAMENTO_DATA(Date CVD_ADIANTAMENTO_DATA) {
        this.CVD_ADIANTAMENTO_DATA = CVD_ADIANTAMENTO_DATA;
    }

    public Double getCVD_ADIANTAMENTO_VALOR() {
        return CVD_ADIANTAMENTO_VALOR;
    }

    public void setCVD_ADIANTAMENTO_VALOR(Double CVD_ADIANTAMENTO_VALOR) {
        this.CVD_ADIANTAMENTO_VALOR = CVD_ADIANTAMENTO_VALOR;
    }

    public Date getCVD_VIAGEM_DATA_INICIO() {
        return CVD_VIAGEM_DATA_INICIO;
    }

    public void setCVD_VIAGEM_DATA_INICIO(Date CVD_VIAGEM_DATA_INICIO) {
        this.CVD_VIAGEM_DATA_INICIO = CVD_VIAGEM_DATA_INICIO;
    }

    public Date getCVD_VIAGEM_DATA_FIM() {
        return CVD_VIAGEM_DATA_FIM;
    }

    public void setCVD_VIAGEM_DATA_FIM(Date CVD_VIAGEM_DATA_FIM) {
        this.CVD_VIAGEM_DATA_FIM = CVD_VIAGEM_DATA_FIM;
    }

    public String getCVD_DESCRICAO() {
        return CVD_DESCRICAO;
    }

    public void setCVD_DESCRICAO(String CVD_DESCRICAO) {
        this.CVD_DESCRICAO = CVD_DESCRICAO;
    }

    public String getCVD_FINALIZADO() {
        return CVD_FINALIZADO;
    }

    public void setCVD_FINALIZADO(String CVD_FINALIZADO) {
        this.CVD_FINALIZADO = CVD_FINALIZADO;
    }
}
