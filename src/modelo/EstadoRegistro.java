package modelo;

public class EstadoRegistro {

    private String estReg;
    private String estRegNom;
    private String estRegEstReg;

    public EstadoRegistro() {
    }

    public EstadoRegistro(String estReg, String estRegNom, String estRegEstReg) {
        this.estReg = estReg;
        this.estRegNom = estRegNom;
        this.estRegEstReg = estRegEstReg;
    }

    public String getEstReg() {
        return estReg;
    }

    public void setEstReg(String estReg) {
        this.estReg = estReg;
    }

    public String getEstRegNom() {
        return estRegNom;
    }

    public void setEstRegNom(String estRegNom) {
        this.estRegNom = estRegNom;
    }

    public String getEstRegEstReg() {
        return estRegEstReg;
    }

    public void setEstRegEstReg(String estRegEstReg) {
        this.estRegEstReg = estRegEstReg;
    }

    @Override
    public String toString() {
        return estReg + " | " + estRegNom + " | " + estRegEstReg;
    }
}