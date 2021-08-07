package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
)

func respondWithError(w http.ResponseWriter, code int, message string) {
	respondWithJSON(w, code, map[string]string{"error": message})
}

func respondWithJSON(w http.ResponseWriter, code int, payload interface{}) {
	response, _ := json.Marshal(payload)

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(code)
	w.Write(response)
}

func PriceChanged(w http.ResponseWriter, r *http.Request) {
	var p ProductPriceChangeDto

	err := json.NewDecoder(r.Body).Decode(&p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	var mail = generateMailPriceChanged(p)

	msg, err := SendMail(mail)
	if err != nil {
		log.Fatal(err)
	}
	log.Println(msg)

	respondWithJSON(w, 200, mail)
}

func StockChanged(w http.ResponseWriter, r *http.Request) {
	var p StockChangeDto

	err := json.NewDecoder(r.Body).Decode(&p)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	mail := generateMailStockChanged(p)

	msg, err := SendMail(mail)
	if err != nil {
		respondWithError(w, http.StatusBadRequest, err.Error())
		log.Fatal(err)
	}
	log.Println(msg)

	respondWithJSON(w, 200, mail)
}

func generateMailPriceChanged(p ProductPriceChangeDto) Mail {
	return Mail{
		Recipient: p.UserEmail,
		Subject:   "Sepete eklediğin ürünün fiyatı düştü",
		Text:      fmt.Sprintf("Değerli %s,\n\n" +
			"Sepete eklemiş olduğun \"%s\" adlı ürününün fiyatı %.2f'den %.2f'ye düştü",
			p.UserFullName, p.ProductName, p.PreviousPrice, p.CurrentPrice),
	}
}

func generateMailStockChanged(p StockChangeDto) Mail {
	if p.StockCount <= 0 {
		return Mail{
			Recipient: p.UserEmail,
			Subject:   "Sepete eklediğin ürün tükendi!",
			Text:      fmt.Sprintf("Değerli %s,\n\n" +
				"Sepete eklemiş olduğun \"%s\" adlı ürün maalesef stoklarda kalmadı.",
				p.UserFullName, p.ProductName),
		}
	}
	return Mail{
		Recipient: p.UserEmail,
		Subject:   "Sepete eklediğin ürün tükenmek üzere!",
		Text:      fmt.Sprintf("Değerli %s,\n\n" +
			"Sepete eklemiş olduğun \"%s\" adlı üründen stoklarda sadece %d adet kaldı.\n\n" +
			"Ürünü satın almak için acele et.",
			p.UserFullName, p.ProductName, p.StockCount),
	}
}