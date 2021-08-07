FROM golang:1.16-alpine AS builder

WORKDIR /app

COPY go.mod ./
COPY go.sum ./
RUN go mod download

COPY *.go ./
RUN go build -o email-service

FROM alpine

EXPOSE 8123

WORKDIR /src
COPY --from=builder /app/email-service ./

CMD ["./email-service"]
