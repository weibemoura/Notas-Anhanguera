package br.com.ficux.notasaesa;

import android.os.Bundle;

public class SobreActivity extends PrincipalActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0024R.layout.sobre);
    }

    public void menuSobre() {
        super.menuSobre();
        finish();
    }
}
