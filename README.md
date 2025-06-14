#  Barkod Okuyucu - Bitirme Projesi

** Ad:** Kadir Belen

** Okul:** İskenderun Teknik Üniversitesi

** Bölüm:** Bilgisayar Mühendisliği

** Öğrenci No:** 202523064

---

##  Proje Özeti

Bu proje, Android tabanlı bir mobil uygulama ile barkod okutularak ürün bilgilerini **MySQL veritabanından çekme ve güncelleme** işlemlerini kapsar.
Uygulama, Android cihaz üzerinden barkod tarar, PHP ile yazılmış API üzerinden veri alışverişi sağlar.

---

## ⚙️ Kullanılan Teknolojiler

| Katman         | Teknoloji                                 |
| -------------- | ----------------------------------------- |
| Mobil Uygulama | Android Studio + Kotlin (Jetpack Compose) |
| Barkod Tarama  | ZXing (QR/Barcode scanner)                |
| HTTP İstek     | Volley                                    |
| Backend        | PHP (XAMPP)                               |
| Veritabanı     | MySQL                                     |
| Sunucu         | Apache (XAMPP)                            |

---

##  Proje Yapısı

```
barkod-okuyucu/
├── APK/                          # Uygulama APK dosyası
│   └── BarkodOkuyucu.apk
├── backend/                      # PHP + SQL dosyaları
│   ├── get_product.php
│   ├── update_product.php
│   └── urunler.sql
├── android/                      # Android uygulama kaynak kodları
│   └── MainActivity.kt

```

---

##  Kurulum ve Çalıştırma

###  Android Uygulama

1. Android Studio ile `android/` klasörünü aç
2. `MainActivity.kt` içindeki IP adresini kendi lokal IP adresinle değiştir:

   ```kotlin
   val url = "http://192.168.X.X/barkod_app/get_product.php?barcode=$barcode"
   ```

   ** Android Emülatör kullanıyorsan IP yerine şunu yaz:**

   ```kotlin
   val url = "http://10.0.2.2/barkod_app/get_product.php?barcode=$barcode"
   ```
3. Bilgisayar ve telefon/emülatör aynı Wi-Fi ağına bağlı olmalı
4. Uygulamayı cihaza/emülatöre yükleyip test edebilirsin

###  Backend (PHP + MySQL)

1. XAMPP kurulu olmalı → Apache ve MySQL çalışıyor olmalı
2. `backend/` klasörünü `C:/xampp/htdocs/barkod_app/` içine at
3. `http://localhost/phpmyadmin` adresinden yeni bir veritabanı oluştur: `urunler`
4. `urunler.sql` dosyasını içe aktar (Import)
5. Tarayıcıdan test için:

   ```
   http://localhost/barkod_app/get_product.php?barcode=1234567890123
   ```

###  Telefonda Çalıştırmak

1. Telefonu bilgisayarla aynı ağa bağla
2. `MainActivity.kt` içindeki IP'yi `192.168.x.x` olacak şekilde güncelle
3. Uygulamayı telefona yükle ve çalıştır
4. PHP dosyaları bilgisayarın `htdocs/barkod_app` klasöründe olmalı
5. Telefon, bilgisayara tarayıcıdan şu adrese ulaşabilmeli:

   ```
   http://192.168.x.x/barkod_app/get_product.php?barcode=1234567890123
   ```

---

##  Kullanım Senaryosu

1. Uygulama açılır
2. Kullanıcı barkodu taratır veya manuel girer
3. Ürün bilgileri çekilir
4. Kullanıcı, adı/fiyatı/stok/kategori gibi bilgileri günceller
5. Sunucuya POST edilir, güncelleme yapılır

---

##  Teslim İçeriği

* `APK/BarkodOkuyucu.apk` → Android uygulaması (çalışan sürüm)
* `backend/` → PHP backend + SQL dosyası
* `android/` → Kotlin kaynak kodları
* `README.pdf` → Açıklayıcı dökümantasyon

---

## 📌 Geliştirilebilir Alanlar

* Firebase entegrasyonu
* Admin paneli (web arayüzü)
* Ürün fotoğrafı ekleme
* Gelişmiş stok yönetimi

---

## 📄 Lisans

Bu proje eğitim amaçlı geliştirilmiştir. Açık kaynaklıdır, dilediğiniz gibi kullanabilirsiniz.
