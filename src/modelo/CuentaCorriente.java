package modelo;

public class CuentaCorriente {

    private int ctaCod;         // PK (N 8)
    private int ctaTraCod;      // FK (N 6)
    private int ctaTipCtaCod;   // FK (N 2)
    private int ctaFecApe;      // N (8)
    private double ctaSalIni;   // N (10, 2)
    private double ctaSalAct;   // N (10, 2)
    private String ctaEstReg;   // FK (A 1)

    public CuentaCorriente() {}

    public CuentaCorriente(int ctaCod, int ctaTraCod, int ctaTipCtaCod, int ctaFecApe, double ctaSalIni, double ctaSalAct, String ctaEstReg) {
        this.ctaCod = ctaCod;
        this.ctaTraCod = ctaTraCod;
        this.ctaTipCtaCod = ctaTipCtaCod;
        this.ctaFecApe = ctaFecApe;
        this.ctaSalIni = ctaSalIni;
        this.ctaSalAct = ctaSalAct;
        this.ctaEstReg = ctaEstReg;
    }

    public int getCtaCod() { return ctaCod; }
    public void setCtaCod(int ctaCod) { this.ctaCod = ctaCod; }

    public int getCtaTraCod() { return ctaTraCod; }
    public void setCtaTraCod(int ctaTraCod) { this.ctaTraCod = ctaTraCod; }

    public int getCtaTipCtaCod() { return ctaTipCtaCod; }
    public void setCtaTipCtaCod(int ctaTipCtaCod) { this.ctaTipCtaCod = ctaTipCtaCod; }

    public int getCtaFecApe() { return ctaFecApe; }
    public void setCtaFecApe(int ctaFecApe) { this.ctaFecApe = ctaFecApe; }

    public double getCtaSalIni() { return ctaSalIni; }
    public void setCtaSalIni(double ctaSalIni) { this.ctaSalIni = ctaSalIni; }

    public double getCtaSalAct() { return ctaSalAct; }
    public void setCtaSalAct(double ctaSalAct) { this.ctaSalAct = ctaSalAct; }

    public String getCtaEstReg() { return ctaEstReg; }
    public void setCtaEstReg(String ctaEstReg) { this.ctaEstReg = ctaEstReg; }
}