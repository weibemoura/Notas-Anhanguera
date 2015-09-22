package br.com.ficux.exception;

public class LoginException extends Exception {
    private static final long serialVersionUID = -4719246508605053344L;

    public String toString() {
        return "LoginException[RA ou Senha invalidos]";
    }
}
