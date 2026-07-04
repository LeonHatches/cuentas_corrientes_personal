package modelo;

public class CentroCosto {
    private String cenCosCod;
    private String cenCosNom;
    private String cenCosEstReg;

    public CentroCosto() {
    }

    public CentroCosto(String cenCosCod, String cenCosNom, String cenCosEstReg) {
        this.cenCosCod = cenCosCod;
        this.cenCosNom = cenCosNom;
        this.cenCosEstReg = cenCosEstReg;
    }

    public String getCenCosCod() { return cenCosCod; }
    public void setCenCosCod(String cenCosCod) { this.cenCosCod = cenCosCod; }

    public String getCenCosNom() { return cenCosNom; }
    public void setCenCosNom(String cenCosNom) { this.cenCosNom = cenCosNom; }

    public String getCenCosEstReg() { return cenCosEstReg; }
    public void setCenCosEstReg(String cenCosEstReg) { this.cenCosEstReg = cenCosEstReg; }
}