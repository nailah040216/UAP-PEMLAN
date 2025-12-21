# 📚 Sistem Penghitungan Jumlah Anggota OSIS

Aplikasi desktop berbasis Java Swing untuk mengelola data anggota OSIS dengan 4 halaman utama yang lengkap dan menarik.

## 📖 Daftar Isi

- [Fitur Utama](#-fitur-utama)
- [Tampilan Aplikasi](#-tampilan-aplikasi)
- [Teknologi](#️-teknologi)
- [Persyaratan Sistem](#-persyaratan-sistem)
- [Cara Instalasi](#-cara-instalasi)
- [Cara Menjalankan](#-cara-menjalankan)
- [Panduan Penggunaan](#-panduan-penggunaan)
- [Struktur File](#-struktur-file)
- [Konfigurasi](#-konfigurasi)
- [Troubleshooting](#-troubleshooting)
- [Kontribusi](#-kontribusi)
- [Lisensi](#-lisensi)
- [Pembuat](#-pembuat)

## ✨ Fitur Utama

### 🏠 1. Halaman Dashboard (Beranda)
- Menampilkan statistik jumlah siswa secara real-time
- Rincian jumlah siswa per jurusan (IPA, IPS, Bahasa)
- Tampilan kartu statistik yang visual dan informatif
- Navigasi cepat ke halaman lain

### 📋 2. Halaman List Data (Daftar Data)
- Tabel lengkap data siswa dengan tampilan rapi
- Fitur pencarian data secara real-time
- Fitur pengurutan (sorting) berdasarkan kolom
- Klik baris untuk mengedit data
- Mudah diekspor ke format lain

### ➕ 3. Halaman Form Input (Input Data)
- Form untuk menambah data siswa baru
- Mengedit data siswa yang sudah ada
- Menghapus data siswa
- Validasi input otomatis
- Tombol reset untuk mengosongkan form

### 📊 4. Halaman History/Laporan (Riwayat)
- Mencatat semua transaksi (tambah, ubah, hapus)
- Timestamp (waktu dan tanggal) setiap aktivitas
- Detail lengkap perubahan data
- Opsi menghapus seluruh riwayat

## 🖼️ Tampilan Aplikasi

Aplikasi ini memiliki desain modern dengan:
- **Antarmuka Modern**: Interface yang bersih dan mudah digunakan
- **Skema Warna**: Ungu gradasi dengan aksen biru
- **Layout Responsif**: Tata letak yang rapi dan proporsional
- **Ikon & Emoji**: Visual yang menarik dan mudah dipahami

## 🛠️ Teknologi

| Komponen | Teknologi |
|----------|-----------|
| Bahasa Pemrograman | Java |
| Framework GUI | Java Swing |
| Penyimpanan Data | File Handling (.txt) |
| Versi JDK | 8 atau lebih tinggi |

## 📋 Persyaratan Sistem

### Perangkat Lunak
- **Java Development Kit (JDK)** versi 8 atau lebih baru
- **Sistem Operasi**: Windows, macOS, atau Linux

### Perangkat Keras
- **RAM**: Minimal 512 MB
- **Penyimpanan**: Minimal 50 MB
- **Resolusi Layar**: Minimal 1024x768

## 🚀 Cara Menjalankan

### Metode 1: Menggunakan Script

#### Windows
Double-click file `run.bat` atau jalankan di Command Prompt:
```cmd
run.bat
```

### Metode 2: Compile dan Run Manual

```bash
# 1. Compile source code
javac SistemPenghitunganAnggotaOSIS.java

# 2. Jalankan aplikasi
java SistemPenghitunganAnggotaOSIS
```

### Metode 3: Menggunakan IDE

#### IntelliJ IDEA
1. Buka IDE
2. Import project atau buka file SistemPenghitunganAnggotaOSIS.java
3. Klik tombol Run atau tekan F5

## 📚 Panduan Penggunaan

### 🏠 Menggunakan Dashboard

1. **Membuka Dashboard**
    - Setelah aplikasi dibuka, Dashboard akan muncul otomatis
    - Atau klik tombol "🏠 Dashboard" di menu navigasi

2. **Melihat Data**
    - Total Siswa: Jumlah keseluruhan siswa
    - Jurusan IPA: Jumlah siswa jurusan IPA
    - Jurusan IPS: Jumlah siswa jurusan IPS
    - Jurusan Bahasa: Jumlah siswa jurusan Bahasa

3. **Memperbarui Data**
    - Data otomatis diperbarui setelah tambah/edit/hapus data
    - Atau klik menu Dashboard untuk refresh manual

### 📋 Menggunakan List Data

1. **Melihat Semua Data**
    - Klik tombol "📋 List Data"
    - Semua data siswa ditampilkan dalam tabel

2. **Mencari Data**
    - Ketik kata kunci di kolom pencarian
    - Hasil pencarian muncul otomatis
    - Pencarian bekerja di semua kolom (NIS, Nama, Kelas, Jurusan)

3. **Mengurutkan Data**
    - Klik header kolom untuk mengurutkan
    - Klik lagi untuk membalik urutan
    - Contoh: Klik "Nama" untuk urut A-Z, klik lagi untuk Z-A

4. **Mengedit Data dari Tabel**
    - Klik baris data yang ingin diedit
    - Aplikasi otomatis berpindah ke Form Input
    - Data sudah terisi di form, tinggal edit

### ➕ Menggunakan Form Input

1. **Menambah Data Baru**
   ```
   Langkah:
   1. Klik tombol "➕ Input Data"
   2. Isi semua field yang diperlukan:
      - NIS: Masukkan nomor induk siswa
      - Nama Siswa: Masukkan nama lengkap
      - Kelas: Masukkan kelas (contoh: XII IPA 1)
      - Jurusan: Pilih dari dropdown (IPA/IPS/Bahasa)
   3. Klik tombol "Tambah" (hijau)
   4. Data tersimpan otomatis
   5. Form akan dikosongkan, siap untuk input berikutnya
   ```

2. **Mengubah/Update Data**
   ```
   Langkah:
   1. Dari List Data, klik baris yang ingin diubah
   2. Aplikasi pindah ke Form Input dengan data terisi
   3. Ubah field yang diperlukan
   4. Klik tombol "Update" (orange)
   5. Data berhasil diperbarui
   ```

3. **Menghapus Data**
   ```
   Langkah:
   1. Dari List Data, klik baris yang ingin dihapus
   2. Aplikasi pindah ke Form Input dengan data terisi
   3. Klik tombol "Hapus" (merah)
   4. Konfirmasi penghapusan
   5. Data terhapus dari sistem
   ```

4. **Mengosongkan Form**
   ```
   - Klik tombol "Reset" (biru) kapan saja
   - Semua field akan dikosongkan
   - Siap untuk input data baru
   ```

### 📊 Menggunakan History

1. **Melihat Riwayat Transaksi**
    - Klik tombol "📊 History"
    - Tabel riwayat menampilkan semua aktivitas:
        - Tanggal dan Waktu
        - Jenis Aksi (TAMBAH/UPDATE/HAPUS)
        - NIS dan Nama Siswa
        - Detail perubahan

2. **Menghapus Semua Riwayat**
    - Klik tombol "🗑️ Hapus Semua History"
    - Konfirmasi penghapusan
    - Semua riwayat akan terhapus permanen

## 💾 Format Data

### File: data_siswa.txt
Format: `NIS|Nama|Kelas|Jurusan`

Contoh:
```
012|Nailah Dardjatur Rofi'ah|12 IPA 1|IPA
089|Siti Aminah|11 IPS 2|IPS
019|Budi Santoso|10 Bahasa 1|Bahasa
```

### File: history_transaksi.txt
Format: `Tanggal|Aksi|NIS|Nama|Detail`

Contoh:
```
21/12/2025 10:30:45|TAMBAH|012|Nailah Dardjatur Rofi'ah|Kelas: 12 IPA 1, Jurusan: IPA
21/12/2025 10:35:12|UPDATE|089|Siti Aminah | Data lama: 11 IPA 1 → Baru: 11 IPA 2
```

### ❌ Data Tidak Tersimpan

**Penyebab**: Aplikasi ditutup secara paksa (force close)

**Solusi**:
- Tutup aplikasi dengan benar menggunakan tombol X atau Alt+F4
- Jangan kill process dari Task Manager
- Data disimpan otomatis saat aplikasi ditutup dengan benar

### ❌ File Tidak Ditemukan

**Penyebab**: File data belum dibuat

**Solusi**:
- File `data_siswa.txt` dan `history_transaksi.txt` dibuat otomatis
- Pastikan aplikasi memiliki izin menulis di folder
- Jika perlu, buat file manual dengan format yang benar

### ❌ Aplikasi Tidak Muncul

**Penyebab**: Error saat compile atau Java GUI tidak didukung

**Solusi**:
1. Cek pesan error di terminal
2. Pastikan menggunakan JDK, bukan JRE

## 👨‍💻 Identitas

**Nama**: Nailah Dardjatur Rofi'ah  
**NIM**: 202410370110384

**Nama**: Jenita Oktaviana R  
**NIM**: 202410370110338

**Kelas**: 3H-Informatika   
**Universitas**: Universitas Muhammadiyah Malang

**Mata Kuliah**: Pemrograman Lanjut

<div align="center">

⭐ **Semoga Kami Mendapatkan Nilai yang Terbaik dan Semoga Bermanfaat** ⭐


**Dibuat dengan ❤️ menggunakan Java**

**TERIMAKASIH**

</div>
