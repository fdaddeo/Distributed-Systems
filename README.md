# Distributed Systems

This repo contains the assignments done for "Distributed Systems" course.

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

This second assignment can be found in the `it.unipr.assignment.two` package.

## Third Assignment - Java Messaging System

The goal is to realize a system implementing the mutual exclusion centralized algorithm, using the JMS API.
The system is made by a number of nodes, each of one can be active or inactive. One of these nodes has to act as coordinator while the others act as executors.
Before executing its job, each node can change its status (from active to inactive and vice versa). In particular, it becomes active with probability `H` and it becomes inactive with probability `K`.

The coordinator has two tasks: the first one is to assign the shared resources to an executor and the second one is to detect when the executor holding the resource becomes inactive.

The executors have two task: executing on the shared resource in a concurrent way and check if the coordinator is still alive. When an executor suspects for the coordinator inactivity, then it starts a new election process by the Bully algorithm. In particular, an executor randomly decides to ask for the resource:

- if it chooses to ask, then:
  - randomly chooses a timeout and completes the request if receives the permission before the timeout expires;
  - randomly chooses another timeout waits and then it restarts;
  - ask for a new coordinator if receives no permission before the first timeout;
- else:
  - randomly chooses a timeout and waits;
  - then it restarts.

This third assignment can be found in the `it.unipr.assignment.three` package.