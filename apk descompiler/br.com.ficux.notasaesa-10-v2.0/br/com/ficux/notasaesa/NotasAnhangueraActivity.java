package br.com.ficux.notasaesa;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import br.com.ficux.adapters.TextViewAdapter;
import br.com.ficux.entidade.Unidade;
import br.com.ficux.servicos.CarregarAluno;
import java.util.List;

public class NotasAnhangueraActivity extends Activity {
    private Button bEntrar;
    private CarregarAluno ca;
    private EditText etRa;
    private EditText etSenha;
    private Spinner sUnidade;

    /* renamed from: br.com.ficux.notasaesa.NotasAnhangueraActivity.1 */
    class C00211 extends TextViewAdapter<Unidade> {
        C00211(Context $anonymous0, int $anonymous1, List $anonymous2) {
            super($anonymous0, $anonymous1, $anonymous2);
        }

        public String getText(Unidade item) {
            return item.getNome();
        }
    }

    /* renamed from: br.com.ficux.notasaesa.NotasAnhangueraActivity.2 */
    class C00222 implements OnItemSelectedListener {
        C00222() {
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
            NotasAnhangueraActivity.this.sUnidade.setSelection(pos);
            NotasAnhangueraActivity.this.sUnidade.clearFocus();
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            NotasAnhangueraActivity.this.sUnidade.clearFocus();
        }
    }

    /* renamed from: br.com.ficux.notasaesa.NotasAnhangueraActivity.3 */
    class C00233 implements OnClickListener {
        C00233() {
        }

        public void onClick(View v) {
            EditText ra = NotasAnhangueraActivity.this.getEtRa();
            EditText senha = NotasAnhangueraActivity.this.getEtSenha();
            Spinner spinnerUnidade = NotasAnhangueraActivity.this.getsUnidade();
            ((InputMethodManager) NotasAnhangueraActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(ra.getWindowToken(), 0);
            Unidade unidade = (Unidade) spinnerUnidade.getSelectedItem();
            if (NotasAnhangueraActivity.this.validar(unidade.getCodigo(), ra.getText().toString(), senha.getText().toString())) {
                NotasAnhangueraActivity.this.ca = new CarregarAluno(NotasAnhangueraActivity.this);
                NotasAnhangueraActivity.this.ca.carregarAluno(unidade.getCodigo(), ra.getText().toString(), senha.getText().toString());
            }
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        if (temInternet()) {
            setContentView(C0024R.layout.main);
            componentes();
            verificaLogin();
            return;
        }
        setContentView(C0024R.layout.seminternet);
    }

    protected void onStop() {
        dimissJanelas();
        finish();
        super.onStop();
    }

    protected void onPause() {
        dimissJanelas();
        super.onPause();
    }

    private void dimissJanelas() {
        if (this.ca != null) {
            this.ca.dimissJanelas();
            this.ca = null;
        }
    }

    public void escondeOsCampos() {
        this.sUnidade.setVisibility(8);
        this.etRa.setVisibility(8);
        this.etSenha.setVisibility(8);
        this.bEntrar.setVisibility(8);
    }

    public void exibeOsCampos() {
        this.sUnidade.setVisibility(0);
        this.etRa.setVisibility(0);
        this.etSenha.setVisibility(0);
        this.bEntrar.setVisibility(0);
    }

    private void verificaLogin() {
        SharedPreferences p = getSharedPreferences("aluno", 0);
        String unidade = p.getString("unidade", "");
        String ra = p.getString("ra", "");
        String senha = p.getString("senha", "");
        if (unidade != null && ra != null && senha != null && unidade.trim() != "" && ra.trim() != "" && senha.trim() != "") {
            this.ca = new CarregarAluno(this);
            this.ca.carregarAluno(unidade.toString(), ra.toString(), senha.toString());
            escondeOsCampos();
        }
    }

    private void componentes() {
        this.sUnidade = (Spinner) findViewById(C0024R.id.unidade);
        this.etRa = (EditText) findViewById(C0024R.id.RA);
        this.etSenha = (EditText) findViewById(C0024R.id.Senha);
        this.bEntrar = (Button) findViewById(C0024R.id.botaoEntrar);
        TextViewAdapter<Unidade> adapter = new C00211(this, 17367048, Unidade.listar());
        adapter.setDropDownViewResource(17367049);
        this.sUnidade.setAdapter(adapter);
        this.sUnidade.setOnItemSelectedListener(new C00222());
        this.bEntrar.setOnClickListener(new C00233());
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.etRa.getWindowToken(), 0);
    }

    protected boolean validar(String unidade, String ra, String senha) {
        if (unidade.trim() == "") {
            Toast.makeText(this, "Escolha a unidade !", 1).show();
            return false;
        } else if (ra.trim() == "") {
            Toast.makeText(this, "Preencha o RA !", 1).show();
            return false;
        } else if (ra.trim() != "") {
            return true;
        } else {
            Toast.makeText(this, "Preencha sua senha !", 1).show();
            return false;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return super.onKeyDown(keyCode, event);
        }
        finish();
        return true;
    }

    public Spinner getsUnidade() {
        return this.sUnidade;
    }

    public EditText getEtRa() {
        return this.etRa;
    }

    public EditText getEtSenha() {
        return this.etSenha;
    }

    public Button getbEntrar() {
        return this.bEntrar;
    }

    public boolean temInternet() {
        try {
            NetworkInfo ni = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
            if (!ni.equals(null) && ni.isConnected() && ni.isAvailable()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
