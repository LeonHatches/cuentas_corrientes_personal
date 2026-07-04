package modelo;

public class Empresa {
    private int empCod;
    private String empRazSoc;
    private String empRuc;
    private String empEstReg;

    public Empresa() {
    }

    public Empresa(int empCod, String empRazSoc, String empRuc, String empEstReg) {
        this.empCod = empCod;
        this.empRazSoc = empRazSoc;
        this.empRuc = empRuc;
        this.empEstReg = empEstReg;
    }

    public int getEmpCod() { return empCod; }
    public void setEmpCod(int empCod) { this.empCod = empCod; }

    public String getEmpRazSoc() { return empRazSoc; }
    public void setEmpRazSoc(String empRazSoc) { this.empRazSoc = empRazSoc; }

    public String getEmpRuc() { return empRuc; }
    public void setEmpRuc(String empRuc) { this.empRuc = empRuc; }

    public String getEmpEstReg() { return empEstReg; }
    public void setEmpEstReg(String empEstReg) { this.empEstReg = empEstReg; }
}