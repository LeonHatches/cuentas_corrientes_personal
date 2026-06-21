package modelo;

import java.math.BigDecimal;

public class Descuento {
    private int desConEmpCod;
    private int desConOrgCod;
    private String desConTipDesCod;
    private int desConSec;
    private int desSec;
    private int desCtaCod;
    private int desFec;
    private BigDecimal desMonTot;
    private int desNumCuo;
    private int desCuoDes;
    private BigDecimal desMonCuo;
    private BigDecimal desMonCuoAcu;
    private String desEstReg;

    public Descuento() {
    }

    public Descuento(int desConEmpCod, int desConOrgCod, String desConTipDesCod, int desConSec,
                     int desSec, int desCtaCod, int desFec, BigDecimal desMonTot, int desNumCuo,
                     int desCuoDes, BigDecimal desMonCuo, BigDecimal desMonCuoAcu, String desEstReg) {
        this.desConEmpCod = desConEmpCod;
        this.desConOrgCod = desConOrgCod;
        this.desConTipDesCod = desConTipDesCod;
        this.desConSec = desConSec;
        this.desSec = desSec;
        this.desCtaCod = desCtaCod;
        this.desFec = desFec;
        this.desMonTot = desMonTot;
        this.desNumCuo = desNumCuo;
        this.desCuoDes = desCuoDes;
        this.desMonCuo = desMonCuo;
        this.desMonCuoAcu = desMonCuoAcu;
        this.desEstReg = desEstReg;
    }

    public int getDesConEmpCod() { return desConEmpCod; }
    public void setDesConEmpCod(int desConEmpCod) { this.desConEmpCod = desConEmpCod; }

    public int getDesConOrgCod() { return desConOrgCod; }
    public void setDesConOrgCod(int desConOrgCod) { this.desConOrgCod = desConOrgCod; }

    public String getDesConTipDesCod() { return desConTipDesCod; }
    public void setDesConTipDesCod(String desConTipDesCod) { this.desConTipDesCod = desConTipDesCod; }

    public int getDesConSec() { return desConSec; }
    public void setDesConSec(int desConSec) { this.desConSec = desConSec; }

    public int getDesSec() { return desSec; }
    public void setDesSec(int desSec) { this.desSec = desSec; }

    public int getDesCtaCod() { return desCtaCod; }
    public void setDesCtaCod(int desCtaCod) { this.desCtaCod = desCtaCod; }

    public int getDesFec() { return desFec; }
    public void setDesFec(int desFec) { this.desFec = desFec; }

    public BigDecimal getDesMonTot() { return desMonTot; }
    public void setDesMonTot(BigDecimal desMonTot) { this.desMonTot = desMonTot; }

    public int getDesNumCuo() { return desNumCuo; }
    public void setDesNumCuo(int desNumCuo) { this.desNumCuo = desNumCuo; }

    public int getDesCuoDes() { return desCuoDes; }
    public void setDesCuoDes(int desCuoDes) { this.desCuoDes = desCuoDes; }

    public BigDecimal getDesMonCuo() { return desMonCuo; }
    public void setDesMonCuo(BigDecimal desMonCuo) { this.desMonCuo = desMonCuo; }

    public BigDecimal getDesMonCuoAcu() { return desMonCuoAcu; }
    public void setDesMonCuoAcu(BigDecimal desMonCuoAcu) { this.desMonCuoAcu = desMonCuoAcu; }

    public String getDesEstReg() { return desEstReg; }
    public void setDesEstReg(String desEstReg) { this.desEstReg = desEstReg; }
}
