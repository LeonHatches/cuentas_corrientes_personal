package modelo;

public class Prestamo {

    private int preTraCod;
    private int preTipCod;
    private int preSec;
    private int preCtaCod;
    private int preFec;
    private double preMon;
    private int preCuo;
    private double preMonCuo;
    private double preMonAcuDes;
    private String preEstReg;

    public Prestamo() {}

    public Prestamo(int preTraCod, int preTipCod, int preSec, int preCtaCod, int preFec, double preMon, int preCuo, double preMonCuo, double preMonAcuDes, String preEstReg) {
        this.preTraCod = preTraCod;
        this.preTipCod = preTipCod;
        this.preSec = preSec;
        this.preCtaCod = preCtaCod;
        this.preFec = preFec;
        this.preMon = preMon;
        this.preCuo = preCuo;
        this.preMonCuo = preMonCuo;
        this.preMonAcuDes = preMonAcuDes;
        this.preEstReg = preEstReg;
    }

    public int getPreTraCod() { return preTraCod; }
    public void setPreTraCod(int preTraCod) { this.preTraCod = preTraCod; }

    public int getPreTipCod() { return preTipCod; }
    public void setPreTipCod(int preTipCod) { this.preTipCod = preTipCod; }

    public int getPreSec() { return preSec; }
    public void setPreSec(int preSec) { this.preSec = preSec; }

    public int getPreCtaCod() { return preCtaCod; }
    public void setPreCtaCod(int preCtaCod) { this.preCtaCod = preCtaCod; }

    public int getPreFec() { return preFec; }
    public void setPreFec(int preFec) { this.preFec = preFec; }

    public double getPreMon() { return preMon; }
    public void setPreMon(double preMon) { this.preMon = preMon; }

    public int getPreCuo() { return preCuo; }
    public void setPreCuo(int preCuo) { this.preCuo = preCuo; }

    public double getPreMonCuo() { return preMonCuo; }
    public void setPreMonCuo(double preMonCuo) { this.preMonCuo = preMonCuo; }

    public double getPreMonAcuDes() { return preMonAcuDes; }
    public void setPreMonAcuDes(double preMonAcuDes) { this.preMonAcuDes = preMonAcuDes; }

    public String getPreEstReg() { return preEstReg; }
    public void setPreEstReg(String preEstReg) { this.preEstReg = preEstReg; }
}