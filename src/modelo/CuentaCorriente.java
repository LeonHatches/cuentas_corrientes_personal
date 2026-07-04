package modelo;

public class CuentaCorriente {

    private int ctaCorTraCod;     // PK, FK (N 6)
    private int ctaCorCtaCod;     // PK, FK (N 2)
    private int ctaCorSec;        // PK (N 4)
    private int ctaCorFec;        // N (8) - Formato AAAAMMDD
    private String ctaCorDocRfe;  // A (15)
    private double ctaCorMnt;     // N (10, 2)
    private String ctaCorEstReg;  // FK (A 1)

    public CuentaCorriente() {
    }

    public CuentaCorriente(int ctaCorTraCod, int ctaCorCtaCod, int ctaCorSec, int ctaCorFec, String ctaCorDocRfe, double ctaCorMnt, String ctaCorEstReg) {
        this.ctaCorTraCod = ctaCorTraCod;
        this.ctaCorCtaCod = ctaCorCtaCod;
        this.ctaCorSec = ctaCorSec;
        this.ctaCorFec = ctaCorFec;
        this.ctaCorDocRfe = ctaCorDocRfe;
        this.ctaCorMnt = ctaCorMnt;
        this.ctaCorEstReg = ctaCorEstReg;
    }

    public int getCtaCorTraCod() { return ctaCorTraCod; }
    public void setCtaCorTraCod(int ctaCorTraCod) { this.ctaCorTraCod = ctaCorTraCod; }

    public int getCtaCorCtaCod() { return ctaCorCtaCod; }
    public void setCtaCorCtaCod(int ctaCorCtaCod) { this.ctaCorCtaCod = ctaCorCtaCod; }

    public int getCtaCorSec() { return ctaCorSec; }
    public void setCtaCorSec(int ctaCorSec) { this.ctaCorSec = ctaCorSec; }

    public int getCtaCorFec() { return ctaCorFec; }
    public void setCtaCorFec(int ctaCorFec) { this.ctaCorFec = ctaCorFec; }

    public String getCtaCorDocRfe() { return ctaCorDocRfe; }
    public void setCtaCorDocRfe(String ctaCorDocRfe) { this.ctaCorDocRfe = ctaCorDocRfe; }

    public double getCtaCorMnt() { return ctaCorMnt; }
    public void setCtaCorMnt(double ctaCorMnt) { this.ctaCorMnt = ctaCorMnt; }

    public String getCtaCorEstReg() { return ctaCorEstReg; }
    public void setCtaCorEstReg(String ctaCorEstReg) { this.ctaCorEstReg = ctaCorEstReg; }
}