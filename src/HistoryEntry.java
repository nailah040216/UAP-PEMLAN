import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class HistoryEntry {
    private String tanggalWaktu;
    private String aksi;
    private String nis;
    private String nama;
    private String detail;

    public HistoryEntry(String aksi, String nis, String nama, String detail) {
        this.tanggalWaktu = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        this.aksi = aksi;
        this.nis = nis;
        this.nama = nama;
        this.detail = detail;
    }

    public String getTanggalWaktu() { return tanggalWaktu; }
    public void setTanggalWaktu(String tanggalWaktu) { this.tanggalWaktu = tanggalWaktu; }
    public String getAksi() { return aksi; }
    public String getNis() { return nis; }
    public String getNama() { return nama; }
    public String getDetail() { return detail; }
}