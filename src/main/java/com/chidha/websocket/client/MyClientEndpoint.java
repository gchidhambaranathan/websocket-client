package com.chidha.websocket.client;


import javax.websocket.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@ClientEndpoint
public class MyClientEndpoint {

    private CountDownLatch countDownLatch;
    private String clientName = null;
    public MyClientEndpoint(CountDownLatch countDownLatch, String clientName){
        super();
        this.countDownLatch = countDownLatch;
        this.clientName = clientName;
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Connected to endpoint: " + session.getBasicRemote());
        try {
            while (true) {
                String name = this.clientName+" Duke";
                System.out.println("Sending message to endpoint: " + name);
                session.getBasicRemote().sendText(name);

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException ex) {
            Logger.getLogger(MyClientEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public void processMessage(String message) {
        System.out.println("Received message in client: " + message);
        this.countDownLatch.countDown();
    }

    @OnError
    public void processError(Throwable t) {
        t.printStackTrace();
    }
}
