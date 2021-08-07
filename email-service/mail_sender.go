package main

import (
	"context"
	"fmt"
	"github.com/mailgun/mailgun-go/v3"
	"time"
)

const (
	apiKey = "78105ba9fe8bbd85c0052c3f7e2d6797-64574a68-a7b52521"
	domain = "sandboxe7380c60c4a64794b60852a0ed7b9730.mailgun.org"
)

func SendMail(mail Mail) (string, error){
	mg := mailgun.NewMailgun(domain, apiKey)
	m := mg.NewMessage(
		fmt.Sprintf("Mail Service <mailgun@%s>", domain),
		mail.Subject,
		mail.Text,
		mail.Recipient,
	)
	ctx, cancel := context.WithTimeout(context.Background(), time.Second*30)
	defer cancel()

	mes, _, err := mg.Send(ctx, m)

	return mes, err
}
