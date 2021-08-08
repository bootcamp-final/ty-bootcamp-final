# Kargo hesaplama servisi

Bu servisin amacı kullanıcıların sepetlerindeki ürünleri satın 
alma sırasında ödemeleri gereken kargo ücretini hesaplamaktır.

Kullanıcıların özelliklerine veya sepetlerindeki ürünlere göre kampanyalar (indirimli veya ücretsiz kargo fırsatı gibi) 
düzenlenebilir. Bu kampanyaları kural tabanlı bir sistem üzerinden konfigure etmekteyiz. 

Kampanya kuralları ```offering-rules.json``` dosyası üzerinden tanımlanmaktadır. 
Her bir kural için json objesinde ```rule```  ve ```cost``` değerleri içerir.

Örnek kural:

```json
{
  "rule": ["toplam tutar", "büyüktür", 75.0],
  "cost": 3.0
}
```

```rule``` değerimizde bir dizi içerisinde koşul içerir. 
İlgili koşul gerçekleşirse ```cost``` değeri kargo ücreti olarak bulunur. 
Örneğin yukarıdaki örnekte sepetteki ürünlerin toplam tutarı 75 TL'den fazla ise kargo ücretimiz 3 TL olarak belirlenecektir.

Kampanya kurallarımız birden fazla olabilir. 
Bu sebeple konfigurasyon dosyasında bir dizi içerinde kuralları tutmaktayız:

```json5
[
  { //Kural 1
    "rule": ...,
    "cost": ...
  },
  { //Kural 2
    "rule": ...,
    "cost": ...
  },
  //Kural 3,
  ...
]
```

Uygulamamız ```offering-rules.json``` dosyasındaki kural dizisini alır. 
Kullanıcının sepet bilgilerine göre uyan kampanya (kural) ne ise onun fiyatını döndürür. 
Birden fazla kampanya uyumlu olabilir. 
Bu durumda uyumlu kampanyalardan en düşük fiyatlı olanı geçerli olur. 
Bu sebeple konfigurasyon dosyamızda kuralları ücretlerine göre sıralamak zorunda değiliz.

Kural dizisinde girdi olarak verilen sepet bilgisi ile hiç bir kampanya uyumlu olmayabilir.
Bu nedenle kampanyasız müşteri için varsayılan kargo ücreti tanımı yapmamız gerekli.
Bu özel kurala ```Wildcard``` kural adını veriyoruz.
Wildcard kuralların  ```rule``` dizisi ```["*"]``` değerine sahip olmalıdır.

Örnek Wildcard kural:
```json
{
    "rule": ["*"],
    "cost": 9.9
  }
```

Kural dizimizde 1 tane wildcard kural bulunmalı ve en yüksek ```cost``` değerine sahip olan kural olmalıdır.

## Kural tanımlama
Obje içerisindeki ```rule``` değerine bir dizi veriyoruz. Dizimiz en fazla 3 uzunluğunda olabilir.

Bu dizinin:
* İlk elemanında sorgulayacağımız sepet bilgisini
* İkinci elemanında önerme bilgisini (örneğin nümerik bir özellik için büyüktür veya küçüktür)
* Üçüncü elemanında ise karşılaştırma için gerekli değer (opsiyonel)

İlk elemanda kullanabileceğimiz sepet bilgisi 3 farklı değer alabilir. 
Bunlar: "toplam tutar", "sepet ürünlerinden" ve "elit üyelik".

### 1. Toplam sepet tutarına göre kural belirtme
Bir sepetteki ürünlerin toplam fiyatını sorgulamak istediğimizde ```rule``` 
dizisinin ilk elemanını ```toplam tutar``` olarak belirtmemiz gerekir.

```rule``` dizisinin ikinci elemanı ```büyüktür``` veya ```küçüktür``` olabilir.

```rule``` dizisinin üçüncü elemanı nümerik bir değer olmalıdır.


#### Örnek
Aşağıda örnek tanımlamalar ve açıklamaları yer almaktadır (fazla yer tutmaması için cost değerlerini belirtmiyorum):
```json
"rule": ["toplam tutar", "küçüktür", 500.0]  => toplam tutar 500'den az ise cost değerini uygula
"rule": ["toplam tutar", "büyüktür", 100.0]  => toplam tutar 100'den fazla ise cost değerini uygula
"rule": ["toplam tutar", "büyüktür", 25.0]   => toplam tutar 25'den fazla ise cost değerini uygula
```


### 2. Sepet ürün kategorilerine göre kural belirtme
Bazı durumlarda kullanıcının sepetindeki ürünlerin kategorilerine göre kampanya uygulamak isteyebiliriz.
Bu durumda ```rule```dizisinin ilk elemanını ```sepet ürünlerinden``` olarak belirtebiliriz.

```rule``` dizisinin ikinci elemanı ```en az birini içeren```, 
```en az birini içermeyen```, ```hepsini içeren``` veya 
```hiçbirini içermeyen``` olabilir.

```rule``` dizisinin üçüncü elemanı bir başka dizi almaktadir. 
Bu dizide koşulumuzun bir parçası olan kategorileri belirtmekteyiz.

#### Örnek
Aşağıda örnek açıklama ve bunlara uygun tanımlamalar yer almaktadır. 

* Sepet ürünlerindeki kategorilerden en az biri
   "giyim" veya "kozmetik" ise cost değerini uygula:
```json
"rule": ["sepet ürünlerinden", "en az birini içeren", ["giyim", "kozmetik"]]
```


* Sepet ürünlerindeki kategorilerden en az biri
   "giyim" veya "kozmetik" değil ise cost değerini uygula:
```json
"rule": ["sepet ürünlerinden", "en az birini içermeyen", ["giyim", "kozmetik"]]
```


* Sepet ürünlerindeki kategorilerden hepsi
"giyim" veya "kozmetik" ise cost değerini uygula:
```json
"rule": ["sepet ürünlerinden", "hepsini içeren", ["giyim", "kozmetik"]]
```


* Sepet ürünlerindeki kategorilerden hiçbiri
   "giyim" veya "kozmetik" değil ise cost değerini uygula:
```json
"rule": ["sepet ürünlerinden", "hiçbirini içermeyen", ["giyim", "kozmetik"]]
```

### 3. Elit üyeliğe göre kural tanımlama

Kullanıcılar elit üye olabilirler. Bu üyeliğe göre kampanya uygulamak istediğimizde 
```rule``` dizisinin ilk elemanını ```elit üyelik```  olarak belirtiyoruz.

```rule``` dizisinin ikinci elemanı ```var``` veya ```yok``` olabilir. 

Üçüncü bir eleman belirtmemize gerek yok.

#### Örnek
* Elit üyelik olduğu durumda cost değerini uygula:
```json
"rule": ["elit üyelik", "var"]
```

* Elit üyelik olmadığı durumda cost değerini uygula:
```json
"rule": ["elit üyelik", "yok"]
```

## Bringing all together
Şimdi tüm öğrendiklerimizi toparlamamıza katkı sağlayacak bir örnek inceleyelim:

```json
[
  {
    "rule": ["toplam tutar", "büyüktür", 100.0],
    "cost": 0
  },
  {
    "rule": ["sepet ürünlerinden", "en az birini içeren", ["giyim", "yaşam", "kozmetik"]],
    "cost": 0
  },
  {
    "rule": ["elit üyelik", "var"],
    "cost": 3.9
  },
  {
    "rule": ["toplam tutar", "küçüktür", 50.0],
    "cost": 7.49
  },
  {
    "rule": ["sepet ürünlerinden", "hiçbirini içermeyen", ["elektronik", "araç aksesuar"]],
    "cost": 8.9
  },
  {
    "rule": ["*"],
    "cost": 9.9
  }
]
```

Yukarıdaki konfigürasyon dosyasına göre:
* Sepetteki ürünlerin toplam tutarı 100 Tl'den yüksek ise kargo ücretsiz olacaktır.
* Sepetteki ürünlerden en az biri "giyim", "yaşam" veya "kozmetik" kategorisinde ise kargo ücretsiz olacaktır
* Yukarıdaki koşullar sağlanamıyorsa ancak kullanıcının elit üyeliği varsa kargo ücreti olarak 3.9 Tl ödeyecektir
* Yukarıdaki koşullar sağlanamıyorsa ancak sepetteki ürünlerin toplam tutarı 50 Tl den az ise kargo ücreti 7.49 olacaktır
* Yukarıdaki koşullar sağlanamıyorsa ancak sepetteki ürünlerin hiçbiri "elektronik" veya "araç aksesuar" kategorisinden değil ise 8.9 kargo ücreti uygulanacaktır.
* Yukarıdaki koşullar sağlanamıyorsa varsayılan kargo ücreti olan 9.9 uygulanacaktır.

## İyileştirmek için neler yapılabilirdi?
Kurallar ilgili konfigurasyon dosyası kullanılarak kod üzerinde değişiklik gerektirmeden değiştirilebilir.
Ancak ilgili dosya proje içerisinde olduğu için her konfigurasyon değişikliği sırasında yeni deployment yapılması gerekecektir.
Bu sebeple konfigurasyon dosyasını uzaktan okuyacak şekilde (örn. google sheets sayfasından) düzenlenebilir.
