package com.example.proxy_server.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    private final Socket shipConnection;
    private final Logger logger = LoggerFactory.getLogger(ConnectionHandler.class);


    public ConnectionHandler(Socket shipConnection) {
        this.shipConnection = shipConnection;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(shipConnection.getInputStream()));
            PrintWriter out = new PrintWriter(shipConnection.getOutputStream(), true);

            while (true) {
                // Read request from ship proxy
                StringBuilder requestBuilder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null && !line.isEmpty()) {
                    requestBuilder.append(line).append("\r\n");
                }

                String request = requestBuilder.toString();

                logger.info("Request received in the server {}",line);
                // Process the request
                String response = RequestProcessor.process(request);
                logger.info("Passing response to the client from the server:{}",response);
                // Send response back to ship proxy
                out.println(response);
                out.println("END_OF_RESPONSE");
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}