package modelo;

public class PrestamoMov {

    private int preMovTraCod;
    private int preMovTipCod;
    private int preMovPreSec;
    private int preMovPlaAnio;
    private int preMovPlaMes;
    private int preMovPlaNum;
    private String preMovTipMovCod;
    private double preMovMonDes;
    private String preMovEstReg;

    public PrestamoMov() {}

    public PrestamoMov(int preMovTraCod, int preMovTipCod, int preMovPreSec, int preMovPlaAnio, int preMovPlaMes, int preMovPlaNum, String preMovTipMovCod, double preMovMonDes, String preMovEstReg) {
        this.preMovTraCod = preMovTraCod;
        this.preMovTipCod = preMovTipCod;
        this.preMovPreSec = preMovPreSec;
        this.preMovPlaAnio = preMovPlaAnio;
        this.preMovPlaMes = preMovPlaMes;
        this.preMovPlaNum = preMovPlaNum;
        this.preMovTipMovCod = preMovTipMovCod;
        this.preMovMonDes = preMovMonDes;
        this.preMovEstReg = preMovEstReg;
    }

    public int getPreMovTraCod() { return preMovTraCod; }
    public void setPreMovTraCod(int preMovTraCod) { this.preMovTraCod = preMovTraCod; }

    public int getPreMovTipCod() { return preMovTipCod; }
    public void setPreMovTipCod(int preMovTipCod) { this.preMovTipCod = preMovTipCod; }

    public int getPreMovPreSec() { return preMovPreSec; }
    public void setPreMovPreSec(int preMovPreSec) { this.preMovPreSec = preMovPreSec; }

    public int getPreMovPlaAnio() { return preMovPlaAnio; }
    public void setPreMovPlaAnio(int preMovPlaAnio) { this.preMovPlaAnio = preMovPlaAnio; }

    public int getPreMovPlaMes() { return preMovPlaMes; }
    public void setPreMovPlaMes(int preMovPlaMes) { this.preMovPlaMes = preMovPlaMes; }

    public int getPreMovPlaNum() { return preMovPlaNum; }
    public void setPreMovPlaNum(int preMovPlaNum) { this.preMovPlaNum = preMovPlaNum; }

    public String getPreMovTipMovCod() { return preMovTipMovCod; }
    public void setPreMovTipMovCod(String preMovTipMovCod) { this.preMovTipMovCod = preMovTipMovCod; }

    public double getPreMovMonDes() { return preMovMonDes; }
    public void setPreMovMonDes(double preMovMonDes) { this.preMovMonDes = preMovMonDes; }

    public String getPreMovEstReg() { return preMovEstReg; }
    public void setPreMovEstReg(String preMovEstReg) { this.preMovEstReg = preMovEstReg; }
}