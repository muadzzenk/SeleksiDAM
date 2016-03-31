package entitas;

/**
 * Created by ZenK on 3/26/2016.
 */
public class list {
    private String datetime;
    private String lokasi;
    private String lokasi_tujuan;
    private double distance;

    public list(String datetime, String lokasi, String lokasi_tujuan,double distance) {
        this.datetime = datetime;
        this.lokasi = lokasi;
        this.lokasi_tujuan = lokasi_tujuan;
        this.distance = distance;
    }

    public list() {
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getLokasi_tujuan() {
        return lokasi_tujuan;
    }

    public void setLokasi_tujuan(String lokasi_tujuan) {
        this.lokasi_tujuan = lokasi_tujuan;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
