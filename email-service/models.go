package main

type ProductPriceChangeDto struct {
	ProductName		string	`json:"productName"`
	UserFullName	string	`json:"userFullName"`
	UserEmail 		string	`json:"userEmail"`
	PreviousPrice 	float32 `json:"previousPrice"`
	CurrentPrice 	float32 `json:"currentPrice"`
}

type StockChangeDto struct {
	ProductName		string	`json:"productName"`
	UserFullName	string	`json:"userFullName"`
	UserEmail 		string	`json:"userEmail"`
	StockCount		int		`json:"stockCount"`
}

type Mail struct {
	Recipient	string	`json:"recipient"`
	Subject		string	`json:"subject"`
	Text		string	`json:"text"`
}
