package modelo;

public class Acta {

    private int actCod;       // PK (N 6)
    private String actRef;    // A (20)
    private int actFec;       // N (8) - Formato AAAAMMDD
    private String actEstReg; // FK (A 1)

    public Acta() {}

    public Acta(int actCod, String actRef, int actFec, String actEstReg) {
        this.actCod = actCod;
        this.actRef = actRef;
        this.actFec = actFec;
        this.actEstReg = actEstReg;
    }

    public int getActCod() { return actCod; }
    public void setActCod(int actCod) { this.actCod = actCod; }

    public String getActRef() { return actRef; }
    public void setActRef(String actRef) { this.actRef = actRef; }

    public int getActFec() { return actFec; }
    public void setActFec(int actFec) { this.actFec = actFec; }

    public String getActEstReg() { return actEstReg; }
    public void setActEstReg(String actEstReg) { this.actEstReg = actEstReg; }
}