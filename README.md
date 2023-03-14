# Distributed Systems

WORK IN PROGRESS. TODO.

## First Assignment - RMI and RPC

The goal is to realize a client-server application, developed with RMI, where client can buy products from server.
The server generates random prices for the products periodically and comunicate this value to the clients. Then, each client generates a random offer for a product.
If the offer is greater, or equal, than the price, the client sends a purchase request to the server.

Once received the purchase offer, the server sends a confirmation to the client if the offer is greater, or equal, than the actual price; otherwise it will send a deny message.

Each client ends its processing after at least 10 purchases.
The server stops as there are no more client to serve.

This first assignment can be found in the `it.unipr.assignment.one` package.

## Second Assignment - Sockets

The goal is to realize a client-server application, developed with sockets, where client can buy products from server.
The server generates random prices for the products periodically and comunicate this value to the clients. Then, each client generates a random offer for a product.
If the offer is greater, or equal, than the price, the client sends a purchase request to the server.

Once received the purchase offer, the server sends a confirmation to the client if the offer is greater, or equal, than the actual price; otherwise it will send a deny message.

Each client ends its processing after at least 10 purchases.
The server stops as there are no more client to serve.

This first assignment can be found in the `it.unipr.assignment.two` package.