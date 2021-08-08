## Ty Bootcamp Final Ödevi - Burak Dursunlar

Projenin ayağa kaldırılması:

```bash
docker-compose up --build
```

Proje için 5 mikroservis implement ettim.

#### 1. Shopping Cart Service

Kullanıcılarının sepetlerinin yönetildiği servis. 
Product servis üzerinden gelen fiyat ve stok değişimlerini kafka üzerinden dinler ve gerektiğinde Email servise istek gönderir.

Teknolojiler: Kotlin, Spring WebFlux, Reactive MongoDB, Kafka, Mockito

#### 2. Shipping Calculation Service
Kargo kampanlayalarının tanımlandığı ve kargo ücretlerinin hesaplandığı servistir.

Kampanyalar dekleratif olarak tanımlanabilir. Detaylı bilgiye [shipping-calculation-service uzantısındaki readme üzerinden](https://github.com/bootcamp-final/ty-bootcamp-final/blob/master/shipping-calculation-service/README.md)
erişebilirsiniz.

Teknolojiler: Node.js, Express, Jest

#### 3. Product Service
Satılacak ürünlerin yönetildiği servistir. Fiyat veya stok değişikliği durumunda kafka'ya event gönderir.

Teknolojiler: Java, Spring Boot, MongoDB


#### 4. Email Service
Shopping cart service'den gelen stok fiyat değişikliklerini kullanıcılara mail olarak gönderir. 
Herhangi bir veritabanına bağımlı değildir.

Teknolojiler: Go, Mux, Mailgun

#### 5. User Service

Kullanıcıların yönetildiği servistir. Veritabanı olarak Postgres kullanıldı.

Teknolojiler: Java, Spring Boot, Postgres
