package modelo;

public class TipoTrabajador {

    private int tipTraCod;
    private String tipTraNom;
    private String tipTraEstReg;

    public TipoTrabajador() {
    }

    public TipoTrabajador(int tipTraCod, String tipTraNom, String tipTraEstReg) {
        this.tipTraCod = tipTraCod;
        this.tipTraNom = tipTraNom;
        this.tipTraEstReg = tipTraEstReg;
    }

    public int getTipTraCod() {
        return tipTraCod;
    }

    public void setTipTraCod(int tipTraCod) {
        this.tipTraCod = tipTraCod;
    }

    public String getTipTraNom() {
        return tipTraNom;
    }

    public void setTipTraNom(String tipTraNom) {
        this.tipTraNom = tipTraNom;
    }

    public String getTipTraEstReg() {
        return tipTraEstReg;
    }

    public void setTipTraEstReg(String tipTraEstReg) {
        this.tipTraEstReg = tipTraEstReg;
    }

    @Override
    public String toString() {
        return tipTraCod + " | " + tipTraNom + " | " + tipTraEstReg;
    }
}