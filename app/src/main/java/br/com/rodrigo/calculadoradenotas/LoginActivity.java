package br.com.rodrigo.calculadoradenotas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.commons.lang3.StringUtils;

public class LoginActivity extends AppCompatActivity {

    private EditText usuario, senha;
    private Button autenticar;
    private Handler handlerMsg;

    private static final int MSG_FIELDS_EMPTY = 0;
    private static final int MSG_USER_LOGIN_OK = 1;
    private static final int MSG_USER_LOGIN_FAIL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.usuario = (EditText) findViewById(R.id.txtUsuario);
        this.senha = (EditText) findViewById(R.id.txtSenha);
        this.autenticar = (Button) findViewById(R.id.btnAutenticar);

        this.handlerMsg = createHandlerMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (autenticar != null) {
            this.autenticar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtils.isEmpty(usuario.getText()) || StringUtils.isEmpty(senha.getText())) {
                        handlerMsg.sendEmptyMessage(MSG_FIELDS_EMPTY);
                    } else if(StringUtils.equals(usuario.getText(), "admin") && StringUtils.equals(senha.getText(), "admin")) {
                        handlerMsg.sendEmptyMessage(MSG_USER_LOGIN_OK);
                    } else {
                        handlerMsg.sendEmptyMessage(MSG_USER_LOGIN_FAIL);
                    }
                }
            });
        }
    }

    private Handler createHandlerMessage() {
        return new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_FIELDS_EMPTY:
                        Toast.makeText(getApplicationContext(), "Campos obrigatórios.", Toast.LENGTH_LONG).show();
                        break;
                    case MSG_USER_LOGIN_OK:
                        Toast.makeText(getApplicationContext(), "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(500);

                            Intent data = new Intent();
                            data.putExtra("login", true);

                            setResult(Activity.RESULT_OK, data);
                            finish();
                        } catch (Exception ex) {
                            Log.e(getClass().getName(), ex.getMessage());
                        }
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Usuário não autorizado.", Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}
