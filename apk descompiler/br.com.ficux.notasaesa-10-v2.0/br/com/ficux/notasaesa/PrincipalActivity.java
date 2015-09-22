package br.com.ficux.notasaesa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import br.com.ficux.servicos.CarregarAluno;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class PrincipalActivity extends SherlockActivity {
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(C0024R.menu.menu_principal, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case C0024R.id.menu_notas:
                menuNotas();
                break;
            case C0024R.id.menu_calculadora:
                menuCalculadora();
                break;
            case C0024R.id.menu_sobre:
                menuSobre();
                break;
            case C0024R.id.menu_sair:
                menuSair();
                break;
        }
        return true;
    }

    public void menuNotas() {
        try {
            SharedPreferences p = getSharedPreferences("aluno", 0);
            String unidade = p.getString("unidade", "");
            String ra = p.getString("ra", "");
            String senha = p.getString("senha", "");
            if (unidade == null || ra == null || senha == null) {
                menuSair();
            } else if (unidade.trim() == "" || ra.trim() == "" || senha.trim() == "") {
                menuSair();
            } else {
                new CarregarAluno(this).carregarAluno(unidade.toString(), ra.toString(), senha.toString());
            }
        } catch (Exception e) {
            menuSair();
        }
    }

    public void menuCalculadora() {
        startActivity(new Intent(this, CalculadoraActivity.class));
    }

    public void menuSobre() {
        startActivity(new Intent(this, SobreActivity.class));
    }

    public void menuSair() {
        Editor e = getSharedPreferences("aluno", 0).edit();
        e.putString("unidade", "");
        e.putString("ra", "");
        e.putString("senha", "");
        e.commit();
        Intent i = new Intent(getApplicationContext(), NotasAnhangueraActivity.class);
        i.addFlags(872448000);
        startActivity(i);
        finish();
    }
}
