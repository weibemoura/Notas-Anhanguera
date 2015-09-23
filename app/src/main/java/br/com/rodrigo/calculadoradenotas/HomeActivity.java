package br.com.rodrigo.calculadoradenotas;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import br.com.rodrigo.calculadoradenotas.util.DialogUtil;

public class HomeActivity extends AppCompatActivity {

    private boolean isLogin = false;
    private int backPressed = 0;
    public static HomeActivity MAIN;

    public static final int ACTIVITY_LOGIN = 0x001;
    public static final int ACTIVITY_CONFIGURATION = 0x002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MAIN = HomeActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isUserAuthorized();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ++backPressed;
        if (backPressed == 2) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_note:
                break;
            case R.id.menu_calculator:
                break;
            case R.id.menu_configuration:
                menuConfiguration();
                break;
            case R.id.menu_about:
                menuAbout();
                break;
            case R.id.menu_logout:
                menuLogout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_LOGIN && resultCode == Activity.RESULT_OK) {
            this.isLogin = data.getBooleanExtra("login", false);
        }
    }

    public void isUserAuthorized() {
        if (!isLogin) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivityForResult(login, ACTIVITY_LOGIN);
        }
    }

    /**
     * eventos do menu
     */
    private void menuConfiguration() {
        Intent login = new Intent(this, ConfigurationActivity.class);
        startActivityForResult(login, ACTIVITY_CONFIGURATION);
    }
    private void menuAbout() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception ex) {}

        final StringBuilder msg = new StringBuilder();
        msg.append("Calculator Notes").append("\n");
        msg.append("Version: ").append(pInfo.versionName);

        DialogUtil.createDialogOK(HomeActivity.this, "Calculator", msg.toString()).show();
    }

    private void menuLogout() {
        this.isLogin = false;

        Intent login = new Intent(this, LoginActivity.class);
        startActivityForResult(login, ACTIVITY_LOGIN);
    }
}
