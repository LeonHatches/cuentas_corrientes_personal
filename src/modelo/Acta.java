package modelo;

public class Acta {

    private int actTraCod;        // PK, FK (N 6)
    private String actCenCosCod;  // PK, FK (A 4)
    private int actFec;           // PK (N 8) - Formato AAAAMMDD
    private String actDes;        // A (200)
    private String actEstReg;     // FK (A 1)

    public Acta() {
    }

    public Acta(int actTraCod, String actCenCosCod, int actFec, String actDes, String actEstReg) {
        this.actTraCod = actTraCod;
        this.actCenCosCod = actCenCosCod;
        this.actFec = actFec;
        this.actDes = actDes;
        this.actEstReg = actEstReg;
    }

    public int getActTraCod() { return actTraCod; }
    public void setActTraCod(int actTraCod) { this.actTraCod = actTraCod; }

    public String getActCenCosCod() { return actCenCosCod; }
    public void setActCenCosCod(String actCenCosCod) { this.actCenCosCod = actCenCosCod; }

    public int getActFec() { return actFec; }
    public void setActFec(int actFec) { this.actFec = actFec; }

    public String getActDes() { return actDes; }
    public void setActDes(String actDes) { this.actDes = actDes; }

    public String getActEstReg() { return actEstReg; }
    public void setActEstReg(String actEstReg) { this.actEstReg = actEstReg; }
}