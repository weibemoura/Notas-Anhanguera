package br.com.ficux.servicos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import br.com.ficux.entidade.Aluno;
import br.com.ficux.notasaesa.NotasAnhangueraActivity;

public class CarregarAluno {
    private static final int HANDLER_LOGIN_ERROR = -1;
    private static final int HANDLER_MESSAGE_COMPLETED = 1;
    private static final int HANDLER_MESSAGE_ERROR = 0;
    private static final int HANDLER_MESSAGE_ERROR_CUSTOM = -2;
    private Aluno aluno;
    private Activity ctx;
    public String erro;
    private AlertDialog erroDialog;
    private AlertDialog erroLoginDialog;
    private Handler mHandler;
    private ProgressDialog progresso;

    /* renamed from: br.com.ficux.servicos.CarregarAluno.1 */
    class C00251 extends Handler {
        C00251() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CarregarAluno.HANDLER_MESSAGE_ERROR_CUSTOM /*-2*/:
                    if (CarregarAluno.this.ctx instanceof NotasAnhangueraActivity) {
                        ((NotasAnhangueraActivity) CarregarAluno.this.ctx).exibeOsCampos();
                    }
                    CarregarAluno.this.progresso.dismiss();
                    if (CarregarAluno.this.erroDialog != null) {
                        CarregarAluno.this.erroDialog.setMessage(CarregarAluno.this.erro);
                        CarregarAluno.this.erroDialog.show();
                    }
                case CarregarAluno.HANDLER_LOGIN_ERROR /*-1*/:
                    if (CarregarAluno.this.ctx instanceof NotasAnhangueraActivity) {
                        ((NotasAnhangueraActivity) CarregarAluno.this.ctx).exibeOsCampos();
                    }
                    CarregarAluno.this.progresso.dismiss();
                    if (CarregarAluno.this.erroLoginDialog != null) {
                        CarregarAluno.this.erroLoginDialog.show();
                    }
                case CarregarAluno.HANDLER_MESSAGE_ERROR /*0*/:
                    if (CarregarAluno.this.ctx instanceof NotasAnhangueraActivity) {
                        ((NotasAnhangueraActivity) CarregarAluno.this.ctx).exibeOsCampos();
                    }
                    CarregarAluno.this.progresso.dismiss();
                    if (CarregarAluno.this.erroDialog != null) {
                        CarregarAluno.this.erroDialog.show();
                    }
                default:
            }
        }
    }

    /* renamed from: br.com.ficux.servicos.CarregarAluno.2 */
    class C00262 implements OnClickListener {
        C00262() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
        }
    }

    /* renamed from: br.com.ficux.servicos.CarregarAluno.3 */
    class C00273 implements OnClickListener {
        C00273() {
        }

        public void onClick(DialogInterface dialog, int id) {
            dialog.dismiss();
        }
    }

    /* renamed from: br.com.ficux.servicos.CarregarAluno.4 */
    class C00284 implements Runnable {
        private final /* synthetic */ String val$ra;
        private final /* synthetic */ String val$senha;
        private final /* synthetic */ String val$unidade;

        C00284(String str, String str2, String str3) {
            this.val$unidade = str;
            this.val$ra = str2;
            this.val$senha = str3;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            r7 = this;
            r6 = 1;
            r2 = br.com.ficux.servicos.CarregarAluno.this;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r3 = r7.val$unidade;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r4 = r7.val$ra;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r5 = r7.val$senha;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r3 = br.com.ficux.servicos.ServicoProvedor.obterAluno(r3, r4, r5);	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2.aluno = r3;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = r7.val$unidade;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r3 = r7.val$ra;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r4 = r7.val$senha;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r7.salvarAluno(r2, r3, r4);	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = br.com.ficux.servicos.CarregarAluno.this;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = r2.ctx;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            if (r2 == 0) goto L_0x005f;
        L_0x0021:
            r1 = new android.content.Intent;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = br.com.ficux.servicos.CarregarAluno.this;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = r2.ctx;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r3 = br.com.ficux.notasaesa.NotasActivity.class;
            r1.<init>(r2, r3);	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = "aluno";
            r3 = br.com.ficux.servicos.CarregarAluno.this;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r3 = r3.aluno;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r1.putExtra(r2, r3);	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = br.com.ficux.servicos.CarregarAluno.this;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = r2.ctx;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2.startActivity(r1);	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = br.com.ficux.servicos.CarregarAluno.this;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = r2.ctx;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = r2 instanceof br.com.ficux.notasaesa.NotasAnhangueraActivity;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            if (r2 != 0) goto L_0x0056;
        L_0x004c:
            r2 = br.com.ficux.servicos.CarregarAluno.this;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = r2.ctx;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = r2 instanceof br.com.ficux.notasaesa.NotasActivity;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            if (r2 == 0) goto L_0x005f;
        L_0x0056:
            r2 = br.com.ficux.servicos.CarregarAluno.this;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = r2.ctx;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2.finish();	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
        L_0x005f:
            r2 = br.com.ficux.servicos.CarregarAluno.this;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2 = r2.progresso;	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r2.dismiss();	 Catch:{ LoginException -> 0x006c, ClientProtocolException -> 0x0075, IOException -> 0x007e, Exception -> 0x0087 }
            r7.sendMessage(r6);
        L_0x006b:
            return;
        L_0x006c:
            r0 = move-exception;
            r2 = -1;
            r7.sendMessage(r2);	 Catch:{ all -> 0x0090 }
            r7.sendMessage(r6);
            goto L_0x006b;
        L_0x0075:
            r0 = move-exception;
            r2 = 0;
            r7.sendMessage(r2);	 Catch:{ all -> 0x0090 }
            r7.sendMessage(r6);
            goto L_0x006b;
        L_0x007e:
            r0 = move-exception;
            r2 = 0;
            r7.sendMessage(r2);	 Catch:{ all -> 0x0090 }
            r7.sendMessage(r6);
            goto L_0x006b;
        L_0x0087:
            r0 = move-exception;
            r2 = 0;
            r7.sendMessage(r2);	 Catch:{ all -> 0x0090 }
            r7.sendMessage(r6);
            goto L_0x006b;
        L_0x0090:
            r2 = move-exception;
            r7.sendMessage(r6);
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: br.com.ficux.servicos.CarregarAluno.4.run():void");
        }

        private void salvarAluno(String unidade, String ra, String senha) {
            Editor e = CarregarAluno.this.ctx.getSharedPreferences("aluno", CarregarAluno.HANDLER_MESSAGE_ERROR).edit();
            e.putString("unidade", unidade);
            e.putString("ra", ra);
            e.putString("senha", senha);
            e.commit();
        }

        private void sendMessage(int what) {
            Message msg = Message.obtain();
            msg.what = what;
            CarregarAluno.this.mHandler.sendMessage(msg);
        }
    }

    public CarregarAluno(Activity ctx) {
        this.mHandler = new C00251();
        this.ctx = ctx;
        Builder alertDialogBuilder = new Builder(ctx);
        alertDialogBuilder.setTitle("Erro");
        alertDialogBuilder.setMessage("Aconteceu algum erro ao buscar os dados no site da anhanguera! Tente novamente.").setCancelable(false).setNeutralButton("Ok", new C00262());
        this.erroDialog = alertDialogBuilder.create();
        Builder loginBuilder = new Builder(ctx);
        loginBuilder.setTitle("Login errado.");
        loginBuilder.setMessage("Seu RA, Senha ou Unidade est\u00e1 incorreto.").setCancelable(false).setNeutralButton("Ok", new C00273());
        this.erroLoginDialog = loginBuilder.create();
    }

    public void carregarAluno(String unidade, String ra, String senha) {
        this.progresso = ProgressDialog.show(this.ctx, "Carregando", "Buscando Informa\u00e7\u00f5es ...");
        new Thread(new C00284(unidade, ra, senha)).start();
    }

    public void dimissJanelas() {
        this.erroDialog.dismiss();
        this.erroLoginDialog.dismiss();
        this.progresso.dismiss();
        this.erroLoginDialog = null;
        this.erroDialog = null;
    }
}
