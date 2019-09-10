# KdvHesaplaPro
Uygulamaya Google Play üzerinden erişmek için [tıklayınız.](https://play.google.com/store/apps/details?id=com.Mtkn.umutt.kdvhesap)

KdvHesaplaPro kullanıcıların kdv hesabı yapmasını ve tüm bu işlemlerini kayıt altında tutmasına yarar.

%1, %8, %18 ve Diğer oranlarında hesap yapabilirler. Karşılığında mevcut tutar, tutarın kdv tutarı ve toplam tutar miktarı kullanıcıya bilgi olarak sunulur.
Tutar miktarı maksimum 9, kdv miktarı ise 5 basamaktan oluşabilir.

Kdv Dahil - Kdv Hariç butonları arasında geçişte eğer bir veri varsa hesap otomatik gerçekleştirilir. Aynı işlem ekranın döndürülmesi sonucuda hesaplanır.

### Başlangıç Sayfası

Kdv hesabının yapıldığı bölümdür. Otomatik olarak yapılmayan işlemler veritabanına eklenir.

![1main](https://user-images.githubusercontent.com/33953921/64454596-0cfcc300-d0f4-11e9-98ce-c6de53617a15.png)

### Kayıtlarım

Burada kullanılan metot ve sınıflar kısaca : ROOM,LiveData,ViewModel,RecyclerView ve Fragment'tır. 

Room ile yapılan tüm işlemler veritabanına eklenir.

RecyclerView ile bu veriler bir liste şeklinde gösterilir.

Livedata ise veritabanında yapılan değişikliklerin RecyclerView'da dinamik olarak değişiklik yapılmasına yarıyor.

![2kayitlarim](https://user-images.githubusercontent.com/33953921/64454597-0cfcc300-d0f4-11e9-85bd-5fac531dc920.png)

### Ayarlar

Ayarlar menüsü preference kullanılarak yapılmıştır. Layout fragment üzerine kurulur ve hiçbir şekilde activity kullanılmamıştır.

Ondalık sayısı ayarı, hesap yapılırken virgülden sonra kaç basamak geleceğini göstermektedir.

Kayıtlarım kısmı ile istersek kayıt tutulmasını pasifleştirebiliriz.

Ayrıca tüm kayıtları da yine ayarlar menüsünden silebiliriz.

![3ayarlar](https://user-images.githubusercontent.com/33953921/64454598-0cfcc300-d0f4-11e9-906b-f36cfe79f2bb.png)

### Hakkında

Programın kullanımı ve detayları hakkında bilgilendirme yapılan bölümdür. Ayrıca geri bildirimde yine buradan sağlanır. 

![4hakkinda](https://user-images.githubusercontent.com/33953921/64454599-0cfcc300-d0f4-11e9-8951-f9ae30e79891.png)
