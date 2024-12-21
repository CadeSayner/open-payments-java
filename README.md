# Open Payments Java Client Library

## Overview
The Open Payments Java Client Library provides a simple and convenient way to interact with the [Interledger Open Payments API](https://openpayments.dev/). This library enables developers to create, manage, and interact with Open Payments resources, including grants, wallet addresses, incoming payments, outgoing payments, and quotes.

## Features
- Request and manage grants for authentication.
- Retrieve wallet address information.
- Create and manage incoming and outgoing payments.
- Generate quotes for payments.
- Easy-to-use API for integrating Open Payments into Java applications.

## Requirements
- Java 11 or higher
- Maven or Gradle for dependency management

## Usage

### Quick Start Example
Here's an example of how to use the library to create an incoming payment, generate a quote, and send an outgoing payment:

```java
package com.example;

import open_payments.api.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize the client with your server URL, private key, and client ID
        AuthenticatedClient client = new AuthenticatedClient(
            "https://ilp.interledger-test.dev/cadesayner",
            "private.key",
            "cc8de9e6-1160-4038-ab9d-38c984db39a0"
        );

        // Define wallet addresses
        final String receivingAddress = "https://ilp.interledger-test.dev/spca";
        final String sendingAddress = "https://ilp.interledger-test.dev/kaylasayner";

        WalletAddress receivingWalletAddress = client.getWalletAddress(receivingAddress);
        WalletAddress sendingWalletAddress = client.getWalletAddress(sendingAddress);

        // Request grants
        Grant incomingPaymentGrant = client.requestIncomingPaymentGrant(receivingWalletAddress);
        Grant quoteGrant = client.requestQuoteGrant(sendingWalletAddress);
        Grant outgoingPaymentGrant = client.requestOutgoingPaymentGrant(sendingWalletAddress);

        // Create an incoming payment
        IncomingPayment incomingPayment = client.createIncomingPayment(
            receivingWalletAddress,
            incomingPaymentGrant.access_token.value,
            200 // Amount in the smallest denomination (e.g., cents)
        );

        // Generate a quote for the payment
        Quote quote = client.createQuote(
            sendingWalletAddress,
            incomingPayment.id,
            quoteGrant
        );

        // Display the redirect URL for grant continuation
        System.out.println(outgoingPaymentGrant.interact.redirect);

        // Wait for user interaction (e.g., completing the grant flow in a browser)
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        // Continue the grant process
        Grant continuedGrant = client.continueGrant(outgoingPaymentGrant);

        // Create an outgoing payment
        OutgoingPayment outgoingPayment = client.createOutgoingPayment(
            sendingWalletAddress,
            quote,
            continuedGrant
        );

        if (!outgoingPayment.failed) {
            System.out.println("Payment successful");
        } else {
            System.out.println("Payment failed");
        }
    }
}
```

### Key Methods
#### Authentication
- `requestIncomingPaymentGrant(WalletAddress address)`: Requests a grant for incoming payments.
- `requestQuoteGrant(WalletAddress address)`: Requests a grant for generating quotes.
- `requestOutgoingPaymentGrant(WalletAddress address)`: Requests a grant for outgoing payments.
- `continueGrant(Grant grant)`: Continues a grant process after user interaction.

#### Payments
- `createIncomingPayment(WalletAddress address, String accessToken, int amount)`: Creates an incoming payment.
- `createOutgoingPayment(WalletAddress address, Quote quote, Grant grant)`: Creates an outgoing payment.

#### Quotes
- `createQuote(WalletAddress address, String incomingPaymentId, Grant grant)`: Generates a quote for a payment.

#### Wallet Addresses
- `getWalletAddress(String addressUri)`: Retrieves wallet address information.



