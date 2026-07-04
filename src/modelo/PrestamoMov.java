package modelo;

public class PrestamoMov {

    private int preMovTraCod;   // PK, FK (N 6)
    private int preMovTipCod;   // PK, FK (N 2)
    private int preMovFecPre;   // PK, FK (N 8)
    private int preMovNumCuo;   // PK (N 2)
    private int preMovFecPag;   // N (8) - Formato AAAAMMDD
    private double preMovMntCuo; // N (10, 2)
    private String preMovEstReg; // FK (A 1)

    public PrestamoMov() {
    }

    public PrestamoMov(int preMovTraCod, int preMovTipCod, int preMovFecPre, int preMovNumCuo, int preMovFecPag, double preMovMntCuo, String preMovEstReg) {
        this.preMovTraCod = preMovTraCod;
        this.preMovTipCod = preMovTipCod;
        this.preMovFecPre = preMovFecPre;
        this.preMovNumCuo = preMovNumCuo;
        this.preMovFecPag = preMovFecPag;
        this.preMovMntCuo = preMovMntCuo;
        this.preMovEstReg = preMovEstReg;
    }

    public int getPreMovTraCod() { return preMovTraCod; }
    public void setPreMovTraCod(int preMovTraCod) { this.preMovTraCod = preMovTraCod; }

    public int getPreMovTipCod() { return preMovTipCod; }
    public void setPreMovTipCod(int preMovTipCod) { this.preMovTipCod = preMovTipCod; }

    public int getPreMovFecPre() { return preMovFecPre; }
    public void setPreMovFecPre(int preMovFecPre) { this.preMovFecPre = preMovFecPre; }

    public int getPreMovNumCuo() { return preMovNumCuo; }
    public void setPreMovNumCuo(int preMovNumCuo) { this.preMovNumCuo = preMovNumCuo; }

    public int getPreMovFecPag() { return preMovFecPag; }
    public void setPreMovFecPag(int preMovFecPag) { this.preMovFecPag = preMovFecPag; }

    public double getPreMovMntCuo() { return preMovMntCuo; }
    public void setPreMovMntCuo(double preMovMntCuo) { this.preMovMntCuo = preMovMntCuo; }

    public String getPreMovEstReg() { return preMovEstReg; }
    public void setPreMovEstReg(String preMovEstReg) { this.preMovEstReg = preMovEstReg; }
}