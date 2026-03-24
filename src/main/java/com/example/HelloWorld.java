package com.example;

import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import java.io.BufferedWriter;

/**
 * A simple HTTP function that responds with "Hello, World from Cloud Run!"
 */
public class HelloWorld implements HttpFunction {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws Exception {
        BufferedWriter writer = response.getWriter();
        writer.write("Hello, World from Cloud Run!");
    }
}