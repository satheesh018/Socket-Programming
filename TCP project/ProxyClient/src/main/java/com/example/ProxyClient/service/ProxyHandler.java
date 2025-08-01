package com.example.ProxyClient.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ProxyHandler extends Thread {
    private final Socket clientSocket;
    private final RequestQueue requestQueue;
    private final Logger logger = LoggerFactory.getLogger(ProxyHandler.class);

    public ProxyHandler(Socket clientSocket, RequestQueue requestQueue) {
        this.clientSocket = clientSocket;
        this.requestQueue = requestQueue;
    }

    @Override
    public void run() {
        try {
            // Read client request
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                requestBuilder.append(line).append("\r\n");
            }
            // Add request to queue
            String request = requestBuilder.toString();
            logger.info("Receiving Request from the client {}",request);
            String response = requestQueue.processRequest(request);

            // Send response back to client
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(response);
            logger.info("Received response from the server {}",response);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}