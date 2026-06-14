package modelo;

public class TipoMovimiento {

    private String tipMovCod;
    private String tipMovNom;
    private String tipMovEstReg;

    public TipoMovimiento() {
    }

    public TipoMovimiento(String tipMovCod, String tipMovNom, String tipMovEstReg) {
        this.tipMovCod = tipMovCod;
        this.tipMovNom = tipMovNom;
        this.tipMovEstReg = tipMovEstReg;
    }

    public String getTipMovCod() {
        return tipMovCod;
    }

    public void setTipMovCod(String tipMovCod) {
        this.tipMovCod = tipMovCod;
    }

    public String getTipMovNom() {
        return tipMovNom;
    }

    public void setTipMovNom(String tipMovNom) {
        this.tipMovNom = tipMovNom;
    }

    public String getTipMovEstReg() {
        return tipMovEstReg;
    }

    public void setTipMovEstReg(String tipMovEstReg) {
        this.tipMovEstReg = tipMovEstReg;
    }

    @Override
    public String toString() {
        return tipMovCod + " | " + tipMovNom + " | " + tipMovEstReg;
    }
}