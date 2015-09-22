package br.com.ficux.notasaesa;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import br.com.ficux.entidade.Aluno;
import br.com.ficux.entidade.Disciplina;

public class NotasActivity extends PrincipalActivity {
    private Aluno aluno;
    private LinearLayout listaNotas;
    private TextView tvAluno;
    private TextView tvCurso;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0024R.layout.notas);
        componentes();
        this.aluno = (Aluno) getIntent().getSerializableExtra("aluno");
        this.tvAluno.setText(this.aluno.getNome());
        this.tvCurso.setText(this.aluno.getCurso());
        populaNota();
    }

    private void populaNota() {
        int tamanho = this.aluno.quantidadeDisciplina();
        for (int i = 0; i < tamanho; i++) {
            Disciplina d = this.aluno.getDisciplina(i);
            LinearLayout lb = new LinearLayout(this);
            lb.setLayoutParams(new LayoutParams(-1, -2));
            lb.setWeightSum(1.0f);
            lb.setOrientation(0);
            TextView tvDisciplina = new TextView(this);
            LayoutParams tvDl = new LayoutParams(0, -2, 0.55f);
            tvDl.setMargins(1, 1, 1, 1);
            tvDisciplina.setLayoutParams(tvDl);
            tvDisciplina.setText(d.getDescricao());
            tvDisciplina.setTextSize(14.0f);
            tvDisciplina.setTextColor(-16777216);
            tvDisciplina.setBackgroundColor(-1);
            lb.addView(tvDisciplina);
            LayoutParams layoutnotas = new LayoutParams(0, -1, 0.15f);
            layoutnotas.setMargins(1, 1, 1, 1);
            TextView tvN1 = new TextView(this);
            tvN1.setLayoutParams(layoutnotas);
            tvN1.setText(d.getNota1());
            tvN1.setTextSize(12.0f);
            tvN1.setTextColor(-16777216);
            tvN1.setBackgroundColor(-1);
            tvN1.setGravity(17);
            lb.addView(tvN1);
            TextView tvN2 = new TextView(this);
            tvN2.setLayoutParams(layoutnotas);
            tvN2.setText(d.getNota2());
            tvN2.setTextSize(12.0f);
            tvN2.setTextColor(-16777216);
            tvN2.setBackgroundColor(-1);
            tvN2.setGravity(17);
            lb.addView(tvN2);
            TextView tvNsub = new TextView(this);
            tvNsub.setLayoutParams(layoutnotas);
            tvNsub.setText(d.getNotaSub());
            tvNsub.setTextSize(12.0f);
            tvNsub.setTextColor(-16777216);
            tvNsub.setBackgroundColor(-1);
            tvNsub.setGravity(17);
            lb.addView(tvNsub);
            this.listaNotas.addView(lb);
        }
    }

    private void componentes() {
        this.tvAluno = (TextView) findViewById(C0024R.id.tvAluno);
        this.tvCurso = (TextView) findViewById(C0024R.id.tvCurso);
        this.listaNotas = (LinearLayout) findViewById(C0024R.id.listaNotas);
    }
}
