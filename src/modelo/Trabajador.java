package modelo;

public class Trabajador {
    private int traCod;
    private String traNom;
    private int traFecIng;
    private Integer traFecCes;
    private Integer traFecUltSalVac;
    private int traEmpCod;
    private String traEstCod;
    private String traCenCosCod;
    private int traTipCod;
    private String traEstReg;

    public Trabajador() {}

    public Trabajador(int traCod, String traNom, int traFecIng, Integer traFecCes, Integer traFecUltSalVac,
                      int traEmpCod, String traEstCod, String traCenCosCod, int traTipCod, String traEstReg) {
        this.traCod = traCod;
        this.traNom = traNom;
        this.traFecIng = traFecIng;
        this.traFecCes = traFecCes;
        this.traFecUltSalVac = traFecUltSalVac;
        this.traEmpCod = traEmpCod;
        this.traEstCod = traEstCod;
        this.traCenCosCod = traCenCosCod;
        this.traTipCod = traTipCod;
        this.traEstReg = traEstReg;
    }

    public int getTraCod() { return traCod; }
    public void setTraCod(int traCod) { this.traCod = traCod; }
    public String getTraNom() { return traNom; }
    public void setTraNom(String traNom) { this.traNom = traNom; }
    public int getTraFecIng() { return traFecIng; }
    public void setTraFecIng(int traFecIng) { this.traFecIng = traFecIng; }
    public Integer getTraFecCes() { return traFecCes; }
    public void setTraFecCes(Integer traFecCes) { this.traFecCes = traFecCes; }
    public Integer getTraFecUltSalVac() { return traFecUltSalVac; }
    public void setTraFecUltSalVac(Integer traFecUltSalVac) { this.traFecUltSalVac = traFecUltSalVac; }
    public int getTraEmpCod() { return traEmpCod; }
    public void setTraEmpCod(int traEmpCod) { this.traEmpCod = traEmpCod; }
    public String getTraEstCod() { return traEstCod; }
    public void setTraEstCod(String traEstCod) { this.traEstCod = traEstCod; }
    public String getTraCenCosCod() { return traCenCosCod; }
    public void setTraCenCosCod(String traCenCosCod) { this.traCenCosCod = traCenCosCod; }
    public int getTraTipCod() { return traTipCod; }
    public void setTraTipCod(int traTipCod) { this.traTipCod = traTipCod; }
    public String getTraEstReg() { return traEstReg; }
    public void setTraEstReg(String traEstReg) { this.traEstReg = traEstReg; }
}