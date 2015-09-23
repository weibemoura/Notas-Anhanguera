package br.com.rodrigo.calculadoradenotas;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class ConfigurationActivity extends AppCompatActivity {

    private EditText ra, password, unit;
    private Button save;
    private Handler handlerMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);

        this.ra = (EditText) findViewById(R.id.configuration_txt_ra);
        this.password = (EditText) findViewById(R.id.configuration_txt_password);
        this.unit = (EditText) findViewById(R.id.configuration_txt_unit);

        this.handlerMsg = createHandlerMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        HomeActivity.MAIN.isUserAuthorized();
    }

    private Handler createHandlerMessage() {
        return new Handler(){
            @Override
            public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            switch (msg.what) {
//                case MSG_FIELDS_EMPTY:
//                    Toast.makeText(LoginActivity.this, "Campos obrigatórios.", Toast.LENGTH_LONG).show();
//                    break;
//                case MSG_USER_LOGIN_OK:
//                    Toast.makeText(LoginActivity.this, "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();
//                    try {
//                        Thread.sleep(500);
//
//                        Intent data = new Intent();
//                        data.putExtra("login", true);
//
//                        setResult(Activity.RESULT_OK, data);
//                        finish();
//                    } catch (Exception ex) {
//                        Log.e(getClass().getName(), ex.getMessage());
//                    }
//                    break;
//                default:
//                    Toast.makeText(LoginActivity.this, "Usuário não autorizado.", Toast.LENGTH_LONG).show();
//            }
            }
        };
    }
}
