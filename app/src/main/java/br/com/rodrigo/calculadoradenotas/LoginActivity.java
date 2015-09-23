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

    private EditText username, password;
    private Button authentication;
    private Handler handlerMsg;

    private static final int MSG_FIELDS_EMPTY = 0;
    private static final int MSG_USER_LOGIN_OK = 1;
    private static final int MSG_USER_LOGIN_FAIL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.username = (EditText) findViewById(R.id.login_txt_username);
        this.password = (EditText) findViewById(R.id.login_txt_password);
        this.authentication = (Button) findViewById(R.id.login_btn_authentication);

        this.handlerMsg = createHandlerMessage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (authentication != null) {
            this.authentication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtils.isEmpty(username.getText()) || StringUtils.isEmpty(password.getText())) {
                        handlerMsg.sendEmptyMessage(MSG_FIELDS_EMPTY);
                    } else if(StringUtils.equals(username.getText(), "admin") && StringUtils.equals(password.getText(), "admin")) {
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
                        Toast.makeText(LoginActivity.this, "Required fields.", Toast.LENGTH_LONG).show();
                        break;
                    case MSG_USER_LOGIN_OK:
                        Toast.makeText(LoginActivity.this, "Logged successfully.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LoginActivity.this, "Unauthorized user.", Toast.LENGTH_LONG).show();
                }
            }
        };
    }
}
