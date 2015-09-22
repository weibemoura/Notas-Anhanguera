package br.com.ficux.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Unidade implements Serializable {
    private static final long serialVersionUID = -1596157570344772839L;
    private String codigo;
    private String nome;

    public Unidade(String codigo, String nome) {
        this.codigo = codigo;
        this.nome = nome;
    }

    public String getCodigo() {
        return this.codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public static List<Unidade> listar() {
        List<Unidade> list = new ArrayList();
        list.add(new Unidade("", "Escolha a localidade"));
        list.add(new Unidade("FLA", "An\u00e1polis (GO)"));
        list.add(new Unidade("FFA", "Bauru - Unidade Norte (SP)"));
        list.add(new Unidade("FFB", "Bauru - Unidade Sede (SP)"));
        list.add(new Unidade("FBH", "Belo Horizonte (MG) - Fac. de Neg\u00f3cios de BH"));
        list.add(new Unidade("FAB", "Belo Horizonte (MG) - Unidade 1"));
        list.add(new Unidade("FBO", "Belo Horizonte (MG) - Unidade 2"));
        list.add(new Unidade("FCB", "Bras\u00edlia (DF) Fac. de Ci\u00eancias e Tecnologia - FACITEB"));
        list.add(new Unidade("FAC", "Campinas (SP) - Unidade 1"));
        list.add(new Unidade("FA2", "Campinas (SP) - Unidade 2"));
        list.add(new Unidade("FA3", "Campinas (SP) - Unidade 3"));
        list.add(new Unidade("FC4", "Campinas (SP) - Unidade 4"));
        list.add(new Unidade("UDA", "Campo Grande (MS) - Agr\u00e1rias"));
        list.add(new Unidade("UDP", "Campo Grande (MS) - Matriz"));
        list.add(new Unidade("FCG", "Campo Grande (MS) - Unidade 1"));
        list.add(new Unidade("FCI", "Campo Grande (MS) - Unidade 2"));
        list.add(new Unidade("FKA", "Caxias do Sul (RS)"));
        list.add(new Unidade("CF2", "Col\u00e9gio: Campinas (SP) - Unidade 2"));
        list.add(new Unidade("CFJ", "Col\u00e9gio: Jundia\u00ed (SP)"));
        list.add(new Unidade("COS", "Col\u00e9gio: Osasco (SP)"));
        list.add(new Unidade("CFI", "Col\u00e9gio: Piracicaba (SP)"));
        list.add(new Unidade("CRP", "Col\u00e9gio: Ribeir\u00e3o Preto (SP)"));
        list.add(new Unidade("CFO", "Col\u00e9gio: Sorocaba (SP)"));
        list.add(new Unidade("CFT", "Col\u00e9gio: Taubat\u00e9 (SP) - Unidade 1"));
        list.add(new Unidade("FCA", "Cuiab\u00e1 (MT)"));
        list.add(new Unidade("FDO", "Dourados (MS)"));
        list.add(new Unidade("FSB", "Faculdade Anchieta (S\u00e3o Bernardo)"));
        list.add(new Unidade("ISC", "Faculdade Anhanguera de Joinville"));
        list.add(new Unidade("FS2", "Faculdade de Tec. Anchieta (S\u00e3o Bernardo)"));
        list.add(new Unidade("FGO", "Goi\u00e2nia (GO)"));
        list.add(new Unidade("FTG", "Guarulhos (SP) - Facs. Integradas Torricelli"));
        list.add(new Unidade("FAI", "Indaiatuba (SP)"));
        list.add(new Unidade("FIS", "Itapecerica da Serra (SP)"));
        list.add(new Unidade("FIJ", "Jacare\u00ed (SP)"));
        list.add(new Unidade("FMA", "Jacare\u00ed (SP) - FMA"));
        list.add(new Unidade("FIT", "Jaragu\u00e1 do Sul (SC)"));
        list.add(new Unidade("FTJ", "Jaragu\u00e1 do Sul (SC) - FATEJ/FATEJA"));
        list.add(new Unidade("FED", "Joinville (SC) - Unidade 1"));
        list.add(new Unidade("FSC", "Joinville (SC) - Unidade 2"));
        list.add(new Unidade("FPJ", "Jundia\u00ed (SP)"));
        list.add(new Unidade("FLR", "Jundia\u00ed (SP) - Luiz Rosa"));
        list.add(new Unidade("FAL", "Leme (SP)"));
        list.add(new Unidade("FA5", "Limeira (SP)"));
        list.add(new Unidade("FPM", "Mat\u00e3o (SP)"));
        list.add(new Unidade("FMT", "Mato Grosso (MT) Fac. de Mato Grosso - FAMAT"));
        list.add(new Unidade("CPL", "NIter\u00f3i (RJ) - Centro Univ. Pl\u00ednio Leite - UNIPLI"));
        list.add(new Unidade("FIZ", "Osasco (SP)"));
        list.add(new Unidade("FPL", "Passo Fundo (RS)"));
        list.add(new Unidade("FPE", "Pelotas (RS)"));
        list.add(new Unidade("FPD", "Pindamonhangaba/SP"));
        list.add(new Unidade("FPI", "Piracicaba (SP)"));
        list.add(new Unidade("FAP", "Pirassununga (SP)"));
        list.add(new Unidade("FPA", "Porto Alegre (RS)"));
        list.add(new Unidade("FRP", "Ribeir\u00e3o Preto (SP)"));
        list.add(new Unidade("FRC", "Rio Claro (SP)"));
        list.add(new Unidade("FRG", "Rio Grande (RS)"));
        list.add(new Unidade("FRV", "Rio Verde de Mato Grosso (MS)"));
        list.add(new Unidade("FRF", "Rio Verde de Mato Grosso (MS) - FIRVE"));
        list.add(new Unidade("FMG", "Rondon\u00f3polis (MT)"));
        list.add(new Unidade("FA4", "Santa B\u00e1rbara d'Oeste (SP)"));
        list.add(new Unidade("FEC", "Santo Andr\u00e9 - UniABC (SP)"));
        list.add(new Unidade("FSA", "Santo Andr\u00e9 (SP)"));
        list.add(new Unidade("FEB", "S\u00e3o Caetano do Sul (SP) - Unidade 1"));
        list.add(new Unidade("FEA", "S\u00e3o Caetano do Sul (SP) - Unidade 2"));
        list.add(new Unidade("FSJ", "S\u00e3o Jos\u00e9 dos Campos (SP)"));
        list.add(new Unidade("CIA", "S\u00e3o Paulo (SP) - Brigadeiro"));
        list.add(new Unidade("CIL", "S\u00e3o Paulo (SP) - Campo Limpo"));
        list.add(new Unidade("UM2", "S\u00e3o Paulo (SP) - Campus MR - Unibero"));
        list.add(new Unidade("UV2", "S\u00e3o Paulo (SP) - Campus VM - Unibero"));
        list.add(new Unidade("UN2", "S\u00e3o Paulo (SP) - Matriz - Unibero"));
        list.add(new Unidade("CIP", "S\u00e3o Paulo (SP) - Pirituba"));
        list.add(new Unidade("UT2", "S\u00e3o Paulo/ (SP) - Campus TP - Unibero"));
        list.add(new Unidade("FSE", "Sert\u00e3ozinho (SP)"));
        list.add(new Unidade("FSO", "Sorocaba (SP)"));
        list.add(new Unidade("FSI", "Sorocaba (SP) - IMAPES"));
        list.add(new Unidade("FSR", "Sorocaba(SP) - Faculdade UIRAPURU"));
        list.add(new Unidade("FSU", "Sumar\u00e9 (SP)"));
        list.add(new Unidade("FTS", "Tabo\u00e3o da Serra (SP)"));
        list.add(new Unidade("FJK", "Taguatinga (DF) - Bras\u00edlia"));
        list.add(new Unidade("FNT", "Taguatinga (DF) - FACNET"));
        list.add(new Unidade("FST", "Taguatinga (DF) - Santa Terezinha"));
        list.add(new Unidade("FAT", "Taubat\u00e9 (SP) - Unidade 1"));
        list.add(new Unidade("FPT", "Taubat\u00e9 (SP) - Unidade 2"));
        list.add(new Unidade("UES", "Uniban - S\u00e3o Jos\u00e9"));
        list.add(new Unidade("USE", "Uniban - S\u00e3o Jos\u00e9 Matriz UNESEG"));
        list.add(new Unidade("UAB", "Uniban - Unidade ABC"));
        list.add(new Unidade("UCL", "Uniban - Unidade Campo Limpo"));
        list.add(new Unidade("UMC", "Uniban - Unidade Maria C\u00e2ndida"));
        list.add(new Unidade("UMR", "Uniban - Unidade Marte"));
        list.add(new Unidade("UNB", "Uniban - Unidade Morumbi"));
        list.add(new Unidade("UOS", "Uniban - Unidade Osasco"));
        list.add(new Unidade("UTP", "Uniban - Unidade Tatuap\u00e9"));
        list.add(new Unidade("UVM", "Uniban - Unidade Vila Mariana"));
        list.add(new Unidade("UFM", "Unipan - Unidade Avenida"));
        list.add(new Unidade("UFF", "Unipan - Unidade Lago"));
        list.add(new Unidade("FAV", "Valinhos (SP)"));
        list.add(new Unidade("FVP", "Valpara\u00edso de Goi\u00e1s (GO)"));
        return list;
    }

    public String toString() {
        return getNome();
    }
}
