package modelo;

public class EstadoTrabajador {

    private String estTraCod;
    private String estTraNom;
    private String estTraEstReg;

    public EstadoTrabajador() {
    }

    public EstadoTrabajador(String estTraCod, String estTraNom, String estTraEstReg) {
        this.estTraCod = estTraCod;
        this.estTraNom = estTraNom;
        this.estTraEstReg = estTraEstReg;
    }

    public String getEstTraCod() {
        return estTraCod;
    }

    public void setEstTraCod(String estTraCod) {
        this.estTraCod = estTraCod;
    }

    public String getEstTraNom() {
        return estTraNom;
    }

    public void setEstTraNom(String estTraNom) {
        this.estTraNom = estTraNom;
    }

    public String getEstTraEstReg() {
        return estTraEstReg;
    }

    public void setEstTraEstReg(String estTraEstReg) {
        this.estTraEstReg = estTraEstReg;
    }

    @Override
    public String toString() {
        return estTraCod + " | " + estTraNom + " | " + estTraEstReg;
    }
}