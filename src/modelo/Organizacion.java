package modelo;

public class Organizacion {
    private int orgCod;
    private String orgNom;
    private String orgRuc;
    private int tipOrgCod;
    private String estReg;

    public Organizacion() {
    }

    public Organizacion(int orgCod, String orgNom, String orgRuc, int tipOrgCod, String estReg) {
        this.orgCod = orgCod;
        this.orgNom = orgNom;
        this.orgRuc = orgRuc;
        this.tipOrgCod = tipOrgCod;
        this.estReg = estReg;
    }

    public int getOrgCod() { return orgCod; }
    public void setOrgCod(int orgCod) { this.orgCod = orgCod; }

    public String getOrgNom() { return orgNom; }
    public void setOrgNom(String orgNom) { this.orgNom = orgNom; }

    public String getOrgRuc() { return orgRuc; }
    public void setOrgRuc(String orgRuc) { this.orgRuc = orgRuc; }

    public int getTipOrgCod() { return tipOrgCod; }
    public void setTipOrgCod(int tipOrgCod) { this.tipOrgCod = tipOrgCod; }

    public String getEstReg() { return estReg; }
    public void setEstReg(String estReg) { this.estReg = estReg; }
}
