package br.com.ficux.notasaesa;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;

public class CalculadoraActivity extends PrincipalActivity {
    private Button botaoCalcular;
    private EditText tvNota1;
    private TextView tvNota2;

    /* renamed from: br.com.ficux.notasaesa.CalculadoraActivity.1 */
    class C00201 implements OnClickListener {
        C00201() {
        }

        public void onClick(View v) {
            double nota1 = Double.parseDouble(CalculadoraActivity.this.tvNota1.getText().toString().replace(",", ".").replace(" ", "").trim());
            if (nota1 < 0.0d) {
                CalculadoraActivity.this.tvNota2.setText("1\u00aa nota inv\u00e1lida");
            } else if (nota1 > 10.0d) {
                CalculadoraActivity.this.tvNota2.setText("1\u00aa nota inv\u00e1lida");
                Toast.makeText(CalculadoraActivity.this, "Voc\u00ea tirou mais que 10 ? NERD EM !!", 1).show();
            } else {
                try {
                    CalculadoraActivity.this.tvNota2.setText(BigDecimal.valueOf(((46.0d - (nota1 * 4.0d)) * 10.0d) / 60.0d).setScale(1, 0).toString());
                } catch (Exception e) {
                    CalculadoraActivity.this.tvNota2.setText("0.0");
                }
            }
            ((InputMethodManager) CalculadoraActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(CalculadoraActivity.this.tvNota1.getWindowToken(), 0);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0024R.layout.calculadora);
        componentes();
        eventos();
    }

    private void componentes() {
        this.tvNota1 = (EditText) findViewById(C0024R.id.Nota1);
        this.tvNota2 = (TextView) findViewById(C0024R.id.Nota2);
        this.botaoCalcular = (Button) findViewById(C0024R.id.botaoCalcular);
    }

    public void menuCalculadora() {
        super.menuCalculadora();
        finish();
    }

    public void eventos() {
        if (this.botaoCalcular != null) {
            this.botaoCalcular.setOnClickListener(new C00201());
        }
    }
}
