package com.example.deconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ngrok.Session;
import java.net.URL;


@SpringBootApplication
public class DeConnectApplication {

    private static Session ngrokSession;

    public static void main(String[] args) throws Exception{
        SpringApplication.run(DeConnectApplication.class, args);

        ngrokSession = Session.withAuthtoken("36t7oj1KL88lxbMpBC8tzVqIdAq_4sU7khx7sRyJGjr7D61ib").connect();

        var forwarder = ngrokSession
                .httpEndpoint()
                .domain("pelitic-unpresumptuous-francina.ngrok-free.dev")
                .forward(new URL("http://localhost:8080"));

        System.out.println("Available at: " + forwarder.getUrl());
    }
}