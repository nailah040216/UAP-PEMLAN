class Siswa {
    private String nis;
    private String nama;
    private String kelas;
    private String jurusan;

    public Siswa(String nis, String nama, String kelas, String jurusan) {
        this.nis = nis;
        this.nama = nama;
        this.kelas = kelas;
        this.jurusan = jurusan;
    }

    public String getNis() { return nis; }
    public void setNis(String nis) { this.nis = nis; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }
    public String getJurusan() { return jurusan; }
    public void setJurusan(String jurusan) { this.jurusan = jurusan; }
}