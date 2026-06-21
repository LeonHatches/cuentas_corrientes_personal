package modelo;

public class Convenio {
    private int conEmpCod;
    private int conOrgCod;
    private String conTipDesCod;
    private int conSec;
    private String conDes;
    private int conActCod;
    private String conCon;
    private String conEstReg;

    public Convenio() {
    }

    public Convenio(int conEmpCod, int conOrgCod, String conTipDesCod, int conSec,
                    String conDes, int conActCod, String conCon, String conEstReg) {
        this.conEmpCod = conEmpCod;
        this.conOrgCod = conOrgCod;
        this.conTipDesCod = conTipDesCod;
        this.conSec = conSec;
        this.conDes = conDes;
        this.conActCod = conActCod;
        this.conCon = conCon;
        this.conEstReg = conEstReg;
    }

    public int getConEmpCod() { return conEmpCod; }
    public void setConEmpCod(int conEmpCod) { this.conEmpCod = conEmpCod; }

    public int getConOrgCod() { return conOrgCod; }
    public void setConOrgCod(int conOrgCod) { this.conOrgCod = conOrgCod; }

    public String getConTipDesCod() { return conTipDesCod; }
    public void setConTipDesCod(String conTipDesCod) { this.conTipDesCod = conTipDesCod; }

    public int getConSec() { return conSec; }
    public void setConSec(int conSec) { this.conSec = conSec; }

    public String getConDes() { return conDes; }
    public void setConDes(String conDes) { this.conDes = conDes; }

    public int getConActCod() { return conActCod; }
    public void setConActCod(int conActCod) { this.conActCod = conActCod; }

    public String getConCon() { return conCon; }
    public void setConCon(String conCon) { this.conCon = conCon; }

    public String getConEstReg() { return conEstReg; }
    public void setConEstReg(String conEstReg) { this.conEstReg = conEstReg; }
}
