package br.com.ficux.entidade;

import java.io.Serializable;

public class Disciplina implements Serializable {
    private static final long serialVersionUID = -276882756387436012L;
    private String descricao;
    private String nota1;
    private String nota2;
    private String notaSub;
    private String tipo;

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNota1() {
        return this.nota1;
    }

    public void setNota1(String nota1) {
        this.nota1 = nota1;
    }

    public String getNota2() {
        return this.nota2;
    }

    public void setNota2(String nota2) {
        this.nota2 = nota2;
    }

    public String getNotaSub() {
        return this.notaSub;
    }

    public void setNotaSub(String notaSub) {
        this.notaSub = notaSub;
    }
}
