package br.com.bornsolutions.bsreembolso.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Calendar;

import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Fotos;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_GCM;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_Notification;
import br.com.bornsolutions.bsreembolso.CL_Globais.CLG_SQLite;
import br.com.bornsolutions.bsreembolso.CL_Negocios.CL_Preferences;
import br.com.bornsolutions.bsreembolso.R;
import br.com.bornsolutions.bsreembolso.Service.VerificaDespesas;
import br.com.bornsolutions.bsreembolso.Util.Constantes;
import br.com.bornsolutions.bsreembolso.WebService.WS_GCM;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener  {

    //OBJETO PARA SALVAR DADOS DA APLICAÇÃO, COMO COOKIES DA WEB OU SESSÕES
    private SharedPreferences preferences;
    CLG_Fotos CL_FOTOS = new CLG_Fotos();
    TextView USU_NOME;
    ProgressDialog progressBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //PERMISSÃO PARA SALVAR FOTOS
        CL_FOTOS.prcVerificaPermissaoDiretorio(this);

        CL_Preferences Preferences = new CL_Preferences();
        preferences = Preferences.PrcGet_SharedPreferences_Login(MainActivity.this, "CAD_USUARIO");
        prpUsuario_Logado = preferences.getString("CU_LOGIN", null);
        prpUsuario_ID = preferences.getString("CU_ID", null);

        USU_NOME = (TextView) findViewById(R.id.USU_NOME);

        if (prpUsuario_Logado != null)
        {
            USU_NOME.setText(prpUsuario_Logado);
        }
        else
        {
            //RECEBE O NOME ENVIADO POR PARAMETRO
            Bundle args = getIntent().getExtras();
            String _strNome = args.getString("CU_LOGIN");
            USU_NOME.setText(prpUsuario_Logado.toUpperCase());
        }


        //REGISTRA O USUARIO NA GCM, CASO NÃO TENHA DADOS NO PREFERENCES
        String regID = CLG_GCM.getRegistrationID(MainActivity.this);
        if (regID == null) {
            if (prcCheckPlaySeervices()) {
                try {
                    //NO WEB SERVICE JÁ VALIDA TUDO
                    WS_GCM Thread_RegistrarGCM = new WS_GCM(MainActivity.this, MainActivity.this, true, prpUsuario_Logado, prpUsuario_ID);
                    Thread_RegistrarGCM.execute();

                } catch (Exception ex) {
                    String _sErro = ex.getMessage();
                }
            }
        }

        try {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        }
        catch (Exception ex)
        {
            String _sErro = ex.getMessage();
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //CRIA SERVIÇO PARA VERIFICAR SE EXISTEM NOVAS DESPESAS CADASTRADAS.
        Intent i = new Intent(this, VerificaDespesas.class);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager am = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE); //CONSTANTE AO ALARM MANAGEMENT
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 60000 * 60, pi); //60 MINUTOS
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_novoreembolso) {
            Intent i = new Intent(this, ListaDespesa.class);

            Bundle params = new Bundle();
            params.putString("CU_LOGIN", prpUsuario_Logado);
            params.putString("CU_ID", prpUsuario_ID);
            params.putString("TIPO_PESQUISA", "CADASTRO");
            i.putExtras(params);

            startActivity(i);

        } else if (id == R.id.nav_consultar) {
            Intent i = new Intent(this, ConsultarDespesa.class);

            Bundle params = new Bundle();
            params.putString("CU_LOGIN", prpUsuario_Logado);
            params.putString("CU_ID", prpUsuario_ID);
            params.putString("TIPO_PESQUISA", "CONSULTA");
            i.putExtras(params);

            startActivity(i);

        } else if (id == R.id.nav_sincronizar) {
            Intent i = new Intent(this, SincronizarActivity.class);

            Bundle params = new Bundle();
            params.putString("CU_LOGIN", prpUsuario_Logado);
            params.putString("CU_ID", prpUsuario_ID);
            i.putExtras(params);

            startActivity(i);

        } else if (id == R.id.nav_configurar) {
            Intent i = new Intent(this, ConfigurarActivity.class);

            Bundle params = new Bundle();
            params.putString("CU_LOGIN", prpUsuario_Logado);
            params.putString("CU_ID", prpUsuario_ID);
            i.putExtras(params);

            startActivity(i);

        } else if (id == R.id.nav_bkpbd) {
            CLG_SQLite CL_Sqlite = new CLG_SQLite(this, Constantes.BD_NOME, Constantes.BD_VERSAO);
            Boolean _bRetorno = CL_Sqlite.BackupBD("//data//br.com.bornsolutions.bsreembolso//databases//" + Constantes.BD_NOME, "BSReembolso");

            if(_bRetorno) {
                Toast.makeText(this,"Backup realizado com sucesso!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Backup não realizado, tente novamente!", Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_sair) {
            CL_Preferences Preferences = new CL_Preferences();
            Preferences.PrcRemove_SharedPreferences(this, "CAD_USUARIO");

            try {
                //PARA DE REGISTRAR O CELULAR
                WS_GCM Thread_RegistrarGCM = new WS_GCM(MainActivity.this, MainActivity.this, false, prpUsuario_Logado, prpUsuario_ID);
                Thread_RegistrarGCM.execute();

            } catch (Exception ex) {
                String _sErro = ex.getMessage();
            }

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish(); //REMOVE ACTIVITY DA PILHA DO ANDROID.

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickNovoReembolso(View view) {
        //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
        Intent i = new Intent(this, ListaDespesa.class);

        Bundle params = new Bundle();
        params.putString("CU_LOGIN", prpUsuario_Logado);
        params.putString("CU_ID", prpUsuario_ID);
        params.putString("TIPO_PESQUISA", "CADASTRO");
        i.putExtras(params);

        startActivity(i);
    }

    public void prcConsultar(View view)
    {
        //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
        Intent i = new Intent(this, ConsultarDespesa.class);

        Bundle params = new Bundle();
        params.putString("CU_LOGIN", prpUsuario_Logado);
        params.putString("CU_ID", prpUsuario_ID);
        i.putExtras(params);

        startActivity(i);
    }

    public void prcSincronizar(View view) {
        //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
        Intent i = new Intent(this, SincronizarActivity.class);

        Bundle params = new Bundle();
        params.putString("CU_LOGIN", prpUsuario_Logado);
        params.putString("CU_ID", prpUsuario_ID);
        i.putExtras(params);

        startActivity(i);
    }

    public void prcConfigurar(View view) {
        //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
        Intent i = new Intent(this, ConfigurarActivity.class);

        Bundle params = new Bundle();
        params.putString("CU_LOGIN", prpUsuario_Logado);
        params.putString("CU_ID", prpUsuario_ID);
        i.putExtras(params);

        startActivity(i);
    }

    public void prcBackupBD(View view)
    {
        CLG_SQLite CL_Sqlite = new CLG_SQLite(this, Constantes.BD_NOME, Constantes.BD_VERSAO);
        Boolean _bRetorno = CL_Sqlite.BackupBD("//data//br.com.bornsolutions.bsreembolso//databases//" + Constantes.BD_NOME , "BSReembolso");

        if(_bRetorno) {
            Toast.makeText(this,"Backup realizado com sucesso!", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Backup não realizado, tente novamente!", Toast.LENGTH_SHORT).show();
        }
    }

    public void prcSobre(View view)
    {
        //NAVEGA PARA  PROXIMA TELA PASSANDO PARAMETROS
        Intent i = new Intent(this, SobreActivity.class);

        Bundle params = new Bundle();
        params.putString("CU_LOGIN", prpUsuario_Logado);
        params.putString("CU_ID", prpUsuario_ID);
        i.putExtras(params);

        startActivity(i);
    }

    private boolean prcCheckPlaySeervices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS)
        {
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                int PLAY_SERVICES_RESOLUTION_REQUEST= 9000;
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Toast.makeText(this, "Esse dispositivo não suporte google play services", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
