package modelo;

public class TipoPrestamo {

    private int tipPreCod;
    private String tipPreNom;
    private String tipPreEstReg;

    public TipoPrestamo() {
    }

    public TipoPrestamo(int tipPreCod, String tipPreNom, String tipPreEstReg) {
        this.tipPreCod = tipPreCod;
        this.tipPreNom = tipPreNom;
        this.tipPreEstReg = tipPreEstReg;
    }

    public int getTipPreCod() {
        return tipPreCod;
    }

    public void setTipPreCod(int tipPreCod) {
        this.tipPreCod = tipPreCod;
    }

    public String getTipPreNom() {
        return tipPreNom;
    }

    public void setTipPreNom(String tipPreNom) {
        this.tipPreNom = tipPreNom;
    }

    public String getTipPreEstReg() {
        return tipPreEstReg;
    }

    public void setTipPreEstReg(String tipPreEstReg) {
        this.tipPreEstReg = tipPreEstReg;
    }

    @Override
    public String toString() {
        // Usamos String.format para que muestre "01", "02", etc., si es menor a 10
        return String.format("%02d", tipPreCod) + " | " + tipPreNom + " | " + tipPreEstReg;
    }
}