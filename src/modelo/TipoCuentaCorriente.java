package modelo;

public class TipoCuentaCorriente {

    private int tipCtaCod;
    private String tipCtaNom;
    private String tipCtaEstReg;

    public TipoCuentaCorriente() {
    }

    public TipoCuentaCorriente(int tipCtaCod, String tipCtaNom, String tipCtaEstReg) {
        this.tipCtaCod = tipCtaCod;
        this.tipCtaNom = tipCtaNom;
        this.tipCtaEstReg = tipCtaEstReg;
    }

    public int getTipCtaCod() {
        return tipCtaCod;
    }

    public void setTipCtaCod(int tipCtaCod) {
        this.tipCtaCod = tipCtaCod;
    }

    public String getTipCtaNom() {
        return tipCtaNom;
    }

    public void setTipCtaNom(String tipCtaNom) {
        this.tipCtaNom = tipCtaNom;
    }

    public String getTipCtaEstReg() {
        return tipCtaEstReg;
    }

    public void setTipCtaEstReg(String tipCtaEstReg) {
        this.tipCtaEstReg = tipCtaEstReg;
    }

    @Override
    public String toString() {
        return String.format("%02d", tipCtaCod) + " | " + tipCtaNom + " | " + tipCtaEstReg;
    }
}