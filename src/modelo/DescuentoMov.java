package modelo;

import java.math.BigDecimal;

public class DescuentoMov {
    private int desMovConEmpCod;
    private int desMovConOrgCod;
    private String desMovConTipDesCod;
    private int desMovConSec;
    private int desMovDesSec;
    private int desMovPlaAnio;
    private int desMovPlaMes;
    private int desMovPlaNum;
    private String desMovTipMovCod;
    private BigDecimal desMovMon;
    private String desMovEstReg;

    public DescuentoMov() {
    }

    public DescuentoMov(int desMovConEmpCod, int desMovConOrgCod, String desMovConTipDesCod,
                        int desMovConSec, int desMovDesSec, int desMovPlaAnio,
                        int desMovPlaMes, int desMovPlaNum, String desMovTipMovCod,
                        BigDecimal desMovMon, String desMovEstReg) {
        this.desMovConEmpCod = desMovConEmpCod;
        this.desMovConOrgCod = desMovConOrgCod;
        this.desMovConTipDesCod = desMovConTipDesCod;
        this.desMovConSec = desMovConSec;
        this.desMovDesSec = desMovDesSec;
        this.desMovPlaAnio = desMovPlaAnio;
        this.desMovPlaMes = desMovPlaMes;
        this.desMovPlaNum = desMovPlaNum;
        this.desMovTipMovCod = desMovTipMovCod;
        this.desMovMon = desMovMon;
        this.desMovEstReg = desMovEstReg;
    }

    public int getDesMovConEmpCod() { return desMovConEmpCod; }
    public void setDesMovConEmpCod(int desMovConEmpCod) { this.desMovConEmpCod = desMovConEmpCod; }

    public int getDesMovConOrgCod() { return desMovConOrgCod; }
    public void setDesMovConOrgCod(int desMovConOrgCod) { this.desMovConOrgCod = desMovConOrgCod; }

    public String getDesMovConTipDesCod() { return desMovConTipDesCod; }
    public void setDesMovConTipDesCod(String desMovConTipDesCod) { this.desMovConTipDesCod = desMovConTipDesCod; }

    public int getDesMovConSec() { return desMovConSec; }
    public void setDesMovConSec(int desMovConSec) { this.desMovConSec = desMovConSec; }

    public int getDesMovDesSec() { return desMovDesSec; }
    public void setDesMovDesSec(int desMovDesSec) { this.desMovDesSec = desMovDesSec; }

    public int getDesMovPlaAnio() { return desMovPlaAnio; }
    public void setDesMovPlaAnio(int desMovPlaAnio) { this.desMovPlaAnio = desMovPlaAnio; }

    public int getDesMovPlaMes() { return desMovPlaMes; }
    public void setDesMovPlaMes(int desMovPlaMes) { this.desMovPlaMes = desMovPlaMes; }

    public int getDesMovPlaNum() { return desMovPlaNum; }
    public void setDesMovPlaNum(int desMovPlaNum) { this.desMovPlaNum = desMovPlaNum; }

    public String getDesMovTipMovCod() { return desMovTipMovCod; }
    public void setDesMovTipMovCod(String desMovTipMovCod) { this.desMovTipMovCod = desMovTipMovCod; }

    public BigDecimal getDesMovMon() { return desMovMon; }
    public void setDesMovMon(BigDecimal desMovMon) { this.desMovMon = desMovMon; }

    public String getDesMovEstReg() { return desMovEstReg; }
    public void setDesMovEstReg(String desMovEstReg) { this.desMovEstReg = desMovEstReg; }
}
