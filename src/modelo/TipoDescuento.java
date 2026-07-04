package modelo;

public class TipoDescuento {

    private String tipDesCod;
    private String tipDesNom;
    private String tipDesEstReg;

    public TipoDescuento() {
    }

    public TipoDescuento(String tipDesCod, String tipDesNom, String tipDesEstReg) {
        this.tipDesCod = tipDesCod;
        this.tipDesNom = tipDesNom;
        this.tipDesEstReg = tipDesEstReg;
    }

    public String getTipDesCod() {
        return tipDesCod;
    }

    public void setTipDesCod(String tipDesCod) {
        this.tipDesCod = tipDesCod;
    }

    public String getTipDesNom() {
        return tipDesNom;
    }

    public void setTipDesNom(String tipDesNom) {
        this.tipDesNom = tipDesNom;
    }

    public String getTipDesEstReg() {
        return tipDesEstReg;
    }

    public void setTipDesEstReg(String tipDesEstReg) {
        this.tipDesEstReg = tipDesEstReg;
    }

    @Override
    public String toString() {
        return tipDesCod + " | " + tipDesNom + " | " + tipDesEstReg;
    }
}