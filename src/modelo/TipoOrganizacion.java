package modelo;

public class TipoOrganizacion {

    private int tipOrgCod;
    private String tipOrgNom;
    private String tipOrgEstReg;

    public TipoOrganizacion() {
    }

    public TipoOrganizacion(int tipOrgCod, String tipOrgNom, String tipOrgEstReg) {
        this.tipOrgCod = tipOrgCod;
        this.tipOrgNom = tipOrgNom;
        this.tipOrgEstReg = tipOrgEstReg;
    }

    public int getTipOrgCod() {
        return tipOrgCod;
    }

    public void setTipOrgCod(int tipOrgCod) {
        this.tipOrgCod = tipOrgCod;
    }

    public String getTipOrgNom() {
        return tipOrgNom;
    }

    public void setTipOrgNom(String tipOrgNom) {
        this.tipOrgNom = tipOrgNom;
    }

    public String getTipOrgEstReg() {
        return tipOrgEstReg;
    }

    public void setTipOrgEstReg(String tipOrgEstReg) {
        this.tipOrgEstReg = tipOrgEstReg;
    }

    @Override
    public String toString() {
        return String.format("%02d", tipOrgCod) + " | " + tipOrgNom + " | " + tipOrgEstReg;
    }
}