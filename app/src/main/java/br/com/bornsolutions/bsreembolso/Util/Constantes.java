package br.com.bornsolutions.bsreembolso.Util;

/**
 * Created by Guilherme on 28/02/2016.
 */
public class Constantes {

    //BANCO DE DADOS
    public static final String BD_NOME = "bsreembolso_v2.sqlite";
    public static final int BD_VERSAO = 1;
    public static final String PROJECT_NUMBER = "439693514382"; //CRIADO NO GCM ID = gcmbsreembolso

    public static String DATA_INICIO = "";
    public static String DATA_FIM = "";

    public static final String[] TIPO_DESPESAS = new String[]{"Alimentação","Celular","Estacionamento","Hotel","Km Rodado","Pedágio","Taxi","Outros"};

    //STRING DE CONEXÃO BORNHALLMANN
    public static final String SEGURANCA = "Data Source=BH-BDSERVER2\\BHBDSERVER2014;Initial Catalog=Seguranca;Persist Security Info=True;User ID=svt; Password=svt";
    public static final String BHPAT = "Data Source=BH-BDSERVER2\\BHBDSERVER2014;Initial Catalog=BensPatrimoniais;Persist Security Info=True;User ID=bhpat; Password=bhpat";
    public static final String DESKTOPBORN = "Data Source=BH-BDSERVER2\\BHBDSERVER2014;Initial Catalog=DesktopBorn;Persist Security Info=True;User ID=gcc; Password=gcc";
    public static final String SVT = "Data Source=BH-WEBSERVER\\BH_WEBSERVER;Initial Catalog=SVT_20;Persist Security Info=True;User ID=svt; Password=svt";
    public static final String CAT = "Data Source=BH-WEBSERVER\\BH_WEBSERVER;Initial Catalog=ConciliadorAutomaticoTransacoes;Persist Security Info=True;User ID=cat; Password=cat";
}
