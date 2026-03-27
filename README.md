# 🔥 Fire Monitoring System

Aplikasi monitoring kebakaran berbasis Android yang terintegrasi dengan **Firebase Realtime Database**. Aplikasi ini dirancang untuk memberikan peringatan dini jika terdeteksi suhu ekstrem atau indikasi kebakaran melalui sensor yang terhubung ke Firebase.

## 📥 Download Aplikasi
Anda dapat mengunduh file instalasi terbaru langsung dari halaman **Releases**:
- **[👉 Klik di sini untuk mengunduh APK (Lihat bagian Assets)](https://github.com/ArifKurniawan/FireMonitoringApp/releases)**
*(Silakan cari file bernama `app-debug.apk` atau `FireMonitoring.apk` di bagian Assets paling bawah halaman tersebut)*

## 🚀 Fitur Utama

- **Real-time Monitoring**: Memantau suhu secara langsung dari database Firebase.
- **Grafik Real-time**: Visualisasi data suhu menggunakan `MPAndroidChart` untuk melihat tren perubahan suhu.
- **Sistem Peringatan (Alert)**: Notifikasi otomatis dan perubahan UI (warna background) saat status berubah menjadi **"KEBAKARAN"**.
- **Background Service**: Aplikasi tetap berjalan di latar belakang untuk memastikan notifikasi tetap masuk meskipun aplikasi sedang ditutup.
- **Riwayat Kebakaran**: Menyimpan dan menampilkan daftar kejadian kebakaran yang pernah terdeteksi.
- **Material Design**: Tampilan antarmuka yang modern dan responsif menggunakan Material Components.

## 🛠️ Tech Stack

- **Bahasa**: [Kotlin](https://kotlinlang.org/)
- **Database**: [Firebase Realtime Database](https://firebase.google.com/products/realtime-database)
- **UI Framework**: XML Layouts & Material Design
- **Library Grafik**: [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)

## 📋 Prasyarat & Instalasi

Untuk menjalankan project ini secara lokal, pastikan Anda memiliki:
1. **Android Studio** versi terbaru (Koala atau lebih baru).
2. **File `google-services.json`**: Karena alasan keamanan, file ini tidak disertakan dalam repositori. Anda perlu membuat project di Firebase Console dan mengunduh filenya ke folder `app/`.

### Langkah Instalasi:
1. Clone repositori ini:
   ```bash
   git clone https://github.com/ArifKurniawan/FireMonitoringApp.git
   ```
2. Buka project di Android Studio.
3. Masukkan file `google-services.json` ke folder `app/`.
4. Lakukan **Sync Project with Gradle Files**.
5. Jalankan aplikasi di Emulator atau Perangkat Fisik.

## 📁 Struktur Data Firebase
Project ini mengharapkan struktur data berikut di Firebase:
```json
{
  "monitoring": {
    "suhu": 30.5,
    "status": "AMAN"
  },
  "history": {
    "unique_id_1": {
      "suhu": "45.0",
      "waktu": "2023-10-27 10:00:00"
    }
  }
}
```

## ✒️ Author
**Arif Kurniawan**
- Mahasiswa Teknologi Informasi
- Universitas Muhammadiyah Sumatera Utara
- Project Tugas Akhir / Skripsi

---
*Dibuat dengan ❤️ untuk keselamatan bersama.*
