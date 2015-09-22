package br.com.ficux.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Aluno implements Serializable {
    private static final long serialVersionUID = -5863081405894761950L;
    private String curso;
    private List<Disciplina> disciplinas;
    private String nome;
    private String ra;

    public Aluno() {
        this.disciplinas = new ArrayList();
    }

    public int quantidadeDisciplina() {
        return this.disciplinas.size();
    }

    public void adicionarDisciplina(Disciplina d) {
        this.disciplinas.add(d);
    }

    public Disciplina getDisciplina(int key) {
        return (Disciplina) this.disciplinas.get(key);
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRa() {
        return this.ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getCurso() {
        return this.curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public List<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }

    public void setDisciplinas(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }
}
