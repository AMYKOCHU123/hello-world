package com.example;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.example.model.MailRequest;
import com.example.service.MailService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedWriter;

public class MailFunction implements HttpFunction {
    private final MailService mailService = new MailService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        String path = request.getPath();   // capture request path

        // Landing page for GET /
        if ("GET".equalsIgnoreCase(request.getMethod()) && ("/".equals(path) || "".equals(path))) {
            writer.write("{\"message\":\"Hello, Cloud Function! This is the hello-world landing page.\"}");
            return;
        }

        // Email endpoint: POST /mail/send
        if ("POST".equalsIgnoreCase(request.getMethod()) && "/mail/send".equals(path)) {
            if (!request.getReader().ready()) {
                response.setStatusCode(400);
                writer.write("{\"error\":\"Request body is empty\",\"status\":400}");
                return;
            }

            MailRequest mailRequest = mapper.readValue(request.getReader(), MailRequest.class);
            int statusCode = mailService.sendMail(mailRequest);

            if (statusCode == 202) {
                writer.write("{\"message\":\"Email accepted by SendGrid\",\"status\":202}");
            } else {
                writer.write("{\"message\":\"Failed to send email\",\"status\":" + statusCode + "}");
            }
            return;
        }

        // Fallback for unsupported routes
        response.setStatusCode(404);
        writer.write("{\"error\":\"Endpoint not found\",\"status\":404}");
    }
}