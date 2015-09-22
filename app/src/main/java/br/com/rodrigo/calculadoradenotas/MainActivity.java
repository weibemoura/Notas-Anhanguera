package br.com.rodrigo.calculadoradenotas;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        AsyncTask<Object, Object, Object> task = new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
//                    URL url = new URL("http://micromed.ind.br");
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("GET");
//                    conn.setDoInput(true);
//                    conn.connect();
//
//                    conn.getResponseCode();
//                    InputStream stream = conn.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
//                    String line = null;
//                    while ((line = reader.readLine()) != null) {
//                        Log.e("INTERNET", line);
//                    }
//                    stream.close();

                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpGet httpget = new HttpGet("http://micromed.ind.br");
                    CloseableHttpResponse response = httpclient.execute(httpget);
                    HttpEntity entity = response.getEntity();

                    InputStream stream = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        Log.e("INTERNET", line);
                    }
                    stream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }
        };
        task.execute(new Object[]{null});

        // Calcula Media
        Button btnCalc = (Button) findViewById(R.id.btnCalc);
        btnCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText N1B = (EditText) findViewById(R.id.N1B);
                EditText N2B = (EditText) findViewById(R.id.N2B);
                EditText MF = (EditText) findViewById(R.id.MF);
                if (MF.getText().toString().length() <= 0) {                                        // Verifica se a média foi preenchida
                    MF.setError(getString(R.string.lbIMF));                                         // Mostra a menssagem
                    MF.requestFocus();
                } else {
                    if (N1B.getText().toString().length() <= 0)                                     // Verifica se o usuario preencheu o campo
                    {
                        N1B.setError(getString(R.string.lbIN1B));                                   // Mostra a menssagem
                        N1B.requestFocus();
                    } else {
                        double N1 = Double.parseDouble(N1B.getText().toString());
                        if (N1 <= 10)                                                               // Verifica se a nota digitada é <=10
                        {
                            if (N2B.getText().toString().length() <= 0) {                           // verifica se campo esta vazio
                                N2B.setError(getString(R.string.lbIN2B));                           // Mostra a menssagem
                                N2B.requestFocus();
                            } else {
                                double N2 = Double.parseDouble(N2B.getText().toString());
                                if (N2 <= 10)                                                       // Verifica se a nota digitada é <=10
                                {
                                    double M = Double.parseDouble(MF.getText().toString());
                                    double resut = (float) ((N1 * 0.4) + (N2 * 0.6));
                                    // Mostrar caixa de dialogo
                                    AlertDialog.Builder dialogo = new
                                            AlertDialog.Builder(MainActivity.this);
                                    dialogo.setTitle(getString(R.string.txtResultado));
                                    dialogo.setMessage(getString(R.string.lbMedia) + getString(R.string.lb2p) + String.format("%.2f",resut));
                                    dialogo.setNeutralButton(getString(R.string.txtOK), null);      //null indica que nenhuma acao sera executada quando o botao for clicado (simplesmente a caixa sera fechada e nada mais).
                                    dialogo.show();

                                    if (resut < M)                                                  // verifica se passou
                                        Toast.makeText(getBaseContext(), getString(R.string.lbRpro), Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(getBaseContext(), getString(R.string.lbApro), Toast.LENGTH_LONG).show();
                                } else
                                    N2B.setError(getString(R.string.txtValorIncorreto));            // Mostra a menssagem de erro se o valor for > 10
                            }
                        } else
                            N1B.setError(getString(R.string.txtValorIncorreto));                    // Mostra a menssagem de erro se o valor for > 10
                    }
                }
            }
        });
        // Calcula Nota necessaria
        Button btnNN = (Button) findViewById(R.id.btnNN);
        btnNN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText N1B = (EditText) findViewById(R.id.N1B);
                EditText MF = (EditText) findViewById(R.id.MF);
                if (MF.getText().toString().length() <= 0) {
                    MF.setError(getString(R.string.lbIMF));                                         // Mostra a menssagem
                    MF.requestFocus();
                } else {
                    if (N1B.getText().toString().length() <= 0) {
                        N1B.setError(getString(R.string.lbIN1B));                                   // Mostra a menssagem
                        N1B.requestFocus();
                    } else {
                        double N1 = Double.parseDouble(N1B.getText().toString());
                        double M = Double.parseDouble(MF.getText().toString());
                        if(N1 <= 10)                                                                // Verifica se a nota digitada é <=10
                        {
                            double resut = ((M - (N1 * 0.4)) / 0.6);
                            //Toast.makeText(getBaseContext(), getString(R.string.lbNN)+ getString(R.string.lb2p) + String.valueOf(resut), Toast.LENGTH_LONG).show();
                            // Mostrar caixa de dialogo
                            AlertDialog.Builder dialogo = new
                                    AlertDialog.Builder(MainActivity.this);
                            dialogo.setTitle(getString(R.string.lbNN));
                            dialogo.setMessage(String.valueOf(String.format("%.2f", resut)));
                            dialogo.setNeutralButton(getString(R.string.txtOK), null);              //null indica que nenhuma acao sera executada quando o botao for clicado (simplesmente a caixa sera fechada e nada mais).
                            dialogo.show();
                        }else
                            N1B.setError(getString(R.string.txtValorIncorreto));                    // Mostra a menssagem de erro se o valor for > 10
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
