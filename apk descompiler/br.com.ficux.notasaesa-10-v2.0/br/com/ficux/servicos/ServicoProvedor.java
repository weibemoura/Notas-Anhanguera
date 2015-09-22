package br.com.ficux.servicos;

import br.com.ficux.entidade.Aluno;
import br.com.ficux.entidade.Disciplina;
import br.com.ficux.exception.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class ServicoProvedor {
    public static String buscar(String unidade, String RA, String senha) throws ClientProtocolException, IOException, LoginException {
        HttpClient httpclient = new DefaultHttpClient();
        ClientConnectionManager ccm = httpclient.getConnectionManager();
        ccm.closeExpiredConnections();
        HttpContext localContext = new BasicHttpContext();
        localContext.setAttribute("http.cookie-store", new BasicCookieStore());
        HttpPost httppost = new HttpPost("http://ww4.unianhanguera.edu.br/servicos/arearestritaaluno/loginaluno/login.php");
        httppost.getParams().setParameter("http.protocol.expect-continue", Boolean.FALSE);
        List<NameValuePair> nameValuePairs = new ArrayList(2);
        nameValuePairs.add(new BasicNameValuePair("RA", RA));
        nameValuePairs.add(new BasicNameValuePair("Senha", senha));
        nameValuePairs.add(new BasicNameValuePair("CodigoUnidade", unidade));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpEntity entity = httpclient.execute(httppost, localContext).getEntity();
        String retorno = EntityUtils.toString(entity, "ISO-8859-1");
        entity.consumeContent();
        if (retorno.indexOf("rio ou senha inv") >= 0) {
            throw new LoginException();
        }
        HttpPost httppost2 = new HttpPost("http://ww4.unianhanguera.edu.br/servicos/arearestritaaluno/meucurso/notasfrequencia.php");
        httppost2.getParams().setParameter("http.protocol.expect-continue", Boolean.FALSE);
        nameValuePairs.add(new BasicNameValuePair("Opcao", "5"));
        httppost2.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        HttpResponse response = httpclient.execute(httppost2, localContext);
        response.setHeader("Content-Type", "text/html; charset=iso-8859-1");
        HttpEntity entity2 = response.getEntity();
        retorno = EntityUtils.toString(entity2, "ISO-8859-1");
        entity2.consumeContent();
        retorno = retorno.substring(retorno.indexOf("<label class=\"username\">"));
        retorno = retorno.substring(0, retorno.indexOf("<form class=\"inputAesa\">"));
        httppost.abort();
        httppost2.abort();
        ccm.closeExpiredConnections();
        ccm.shutdown();
        return retorno;
    }

    public static Aluno obterAluno(String unidade, String ra, String senha) throws ClientProtocolException, IOException, LoginException {
        String retorno = buscar(unidade, ra, senha);
        Aluno a = new Aluno();
        String aluno = "";
        String curso = "";
        a.setNome(retorno.substring(retorno.indexOf(">Ol\u00e1") + 4, retorno.indexOf("</label>")).trim());
        retorno = retorno.substring(retorno.indexOf("option"));
        String str = retorno;
        retorno = str.substring(retorno.indexOf(">") + 1);
        a.setCurso(retorno.substring(retorno.indexOf("-") + 1, retorno.indexOf("</option>")).trim());
        a.setRa(ra);
        retorno = retorno.substring(retorno.indexOf("<table"));
        str = retorno;
        retorno = str.substring(retorno.indexOf("<table") + 6);
        str = retorno;
        retorno = str.substring(retorno.indexOf("</tr>") + 5);
        str = retorno;
        retorno = str.substring(retorno.indexOf("</tr>") + 5);
        String parteAntes = "";
        String parteDepois = "";
        String notaEAD = "";
        boolean limpar = true;
        while (limpar) {
            if (retorno.indexOf("<table") >= 0) {
                int inicioTable = retorno.indexOf("<table");
                int fimTable = retorno.indexOf("</table>");
                parteAntes = retorno.substring(0, inicioTable);
                notaEAD = retorno.substring(inicioTable + 7, fimTable + 8);
                parteDepois = retorno.substring(fimTable + 8);
                notaEAD = notaEAD.substring(notaEAD.indexOf("</tr>") + 5);
                notaEAD = notaEAD.substring(notaEAD.indexOf("<td") + 3);
                notaEAD = notaEAD.substring(notaEAD.indexOf("<td") + 3);
                notaEAD = notaEAD.substring(notaEAD.indexOf("<td") + 3);
                int inicio = notaEAD.indexOf(">");
                int i = inicio + 1;
                notaEAD = notaEAD.substring(r22, notaEAD.indexOf("<"));
                retorno = new StringBuilder(String.valueOf(parteAntes)).append(notaEAD).append("&nbsp;").append(parteDepois).toString();
            } else {
                limpar = false;
            }
        }
        String disciplina = "";
        String nota1 = "";
        String nota2 = "";
        String notasub = "";
        String tipo = "";
        boolean temMaisNota = true;
        while (temMaisNota) {
            retorno = retorno.substring(retorno.indexOf("<tr>") + 4);
            retorno = retorno.substring(retorno.indexOf("<td") + 3);
            retorno = retorno.substring(retorno.indexOf(">") + 1);
            disciplina = retorno.substring(0, retorno.indexOf("&nbsp;"));
            retorno = retorno.substring(retorno.indexOf("<td") + 3);
            retorno = retorno.substring(retorno.indexOf(">") + 1);
            tipo = retorno.substring(0, retorno.indexOf("&nbsp;"));
            retorno = retorno.substring(retorno.indexOf("<td") + 3);
            retorno = retorno.substring(retorno.indexOf(">") + 1);
            nota1 = retorno.substring(0, retorno.indexOf("&nbsp;"));
            retorno = retorno.substring(retorno.indexOf("<td") + 3);
            retorno = retorno.substring(retorno.indexOf(">") + 1);
            nota2 = retorno.substring(0, retorno.indexOf("&nbsp;"));
            retorno = retorno.substring(retorno.indexOf("<td") + 3);
            retorno = retorno.substring(retorno.indexOf(">") + 1);
            notasub = retorno.substring(0, retorno.indexOf("&nbsp;"));
            retorno = retorno.substring(retorno.indexOf("</tr>") + 5);
            if (retorno.indexOf("<tr>") >= 0) {
                temMaisNota = true;
            } else {
                temMaisNota = false;
            }
            Disciplina d = new Disciplina();
            d.setDescricao(disciplina);
            d.setTipo(tipo);
            d.setNota1(nota1);
            d.setNota2(nota2);
            d.setNotaSub(notasub);
            a.adicionarDisciplina(d);
        }
        return a;
    }

    public static DefaultHttpClient getThreadSafeClient() {
        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        return new DefaultHttpClient(new ThreadSafeClientConnManager(params, mgr.getSchemeRegistry()), params);
    }
}
