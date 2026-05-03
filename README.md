# Stock market

A simplified stock market simulation.

## Prerequisites

- Docker
- Linux/macOS/Windows

## How to run

1.  **Configure environment variables**:
    Copy the `.env.example` file to `.env`:
    ```bash
    cp .env.example .env
    ```
    (Optional) Edit `.env` to change credentials or the application port (`APP_PORT`).

2.  **Start the application**:
    You can start the application with a specific port directly from the command line:
    ```bash
    APP_PORT=9000 docker-compose up --build
    ```
    If you don't provide `APP_PORT`, it will default to `8080`.

The application will be available at `localhost:<PORT>` (e.g., `localhost:9000`).

### Swagger Documentation
Once the application is running, Swagger documentation is available at:
`http://localhost:<PORT>/swagger-ui/index.html`

## Endpoints

### Wallets

- `POST /wallets/{wallet_id}/stocks/{stock_name}`: Buy or sell a single stock.
  - Body: `{ "type": "buy" }` or `{ "type": "sell" }`
- `GET /wallets/{wallet_id}`: Returns the current state of a wallet.
- `GET /wallets/{wallet_id}/stocks/{stock_name}`: Returns the quantity of a specific stock in a wallet.

### Bank (Stocks)

- `GET /stocks`: Returns the current state of the bank.
- `POST /stocks`: Sets the state of the bank.
  - Body: `{ "stocks": [{ "name": "stock1", "quantity": 100 }] }`

### Audit Log

- `GET /log`: Returns the entire audit log of successful wallet operations.

### System

- `POST /chaos`: Shuts down the instance that serves this request.
