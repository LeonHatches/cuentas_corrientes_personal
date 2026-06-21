package modelo;

public class Prestamo {

    private int preTraCod;      // PK, FK (N 6)
    private int preTipCod;      // PK, FK (N 2)
    private int preFecPre;      // PK (N 8) - Formato AAAAMMDD
    private double preMnt;      // N (10, 2)
    private int preCanCuo;      // N (2)
    private double preMntCuo;   // N (10, 2)
    private String preEstReg;   // FK (A 1)

    public Prestamo() {
    }

    public Prestamo(int preTraCod, int preTipCod, int preFecPre, double preMnt, int preCanCuo, double preMntCuo, String preEstReg) {
        this.preTraCod = preTraCod;
        this.preTipCod = preTipCod;
        this.preFecPre = preFecPre;
        this.preMnt = preMnt;
        this.preCanCuo = preCanCuo;
        this.preMntCuo = preMntCuo;
        this.preEstReg = preEstReg;
    }

    public int getPreTraCod() { return preTraCod; }
    public void setPreTraCod(int preTraCod) { this.preTraCod = preTraCod; }

    public int getPreTipCod() { return preTipCod; }
    public void setPreTipCod(int preTipCod) { this.preTipCod = preTipCod; }

    public int getPreFecPre() { return preFecPre; }
    public void setPreFecPre(int preFecPre) { this.preFecPre = preFecPre; }

    public double getPreMnt() { return preMnt; }
    public void setPreMnt(double preMnt) { this.preMnt = preMnt; }

    public int getPreCanCuo() { return preCanCuo; }
    public void setPreCanCuo(int preCanCuo) { this.preCanCuo = preCanCuo; }

    public double getPreMntCuo() { return preMntCuo; }
    public void setPreMntCuo(double preMntCuo) { this.preMntCuo = preMntCuo; }

    public String getPreEstReg() { return preEstReg; }
    public void setPreEstReg(String preEstReg) { this.preEstReg = preEstReg; }
}