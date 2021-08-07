package main

import (
	"log"
	_ "log"
	"net/http"
	_ "net/http"
)

func main() {
	router := NewRouter()

	log.Fatal(http.ListenAndServe(":8123", router))
}
