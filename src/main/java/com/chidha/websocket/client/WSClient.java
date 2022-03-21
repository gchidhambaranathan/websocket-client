package com.chidha.websocket.client;


import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javax.websocket.*;

@ClientEndpoint
public class WSClient  {
    //final static CountDownLatch messageLatch = new CountDownLatch(1);

    public static void main(String[] args) {
       /* try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String uri = "ws://192.168.0.101:9000/";
            System.out.println("Connecting to " + uri);
            container.connectToServer(MyClientEndpoint.class, URI.create(uri));
            messageLatch.await(300, TimeUnit.SECONDS);
        } catch (DeploymentException | InterruptedException | IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(WSClient.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        WSClient wsClient = new WSClient();
        wsClient.startClients();

    }

    private void startClients() {
        for(int i = 0; i < 5; i++){
            MyClientThread  clientThread = new MyClientThread();
            new Thread(clientThread, "ClientWS "+ i).start();
        }
    }


    private class MyClientThread implements Runnable {

        @Override
        public void run() {
            try {
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                String uri = "ws://192.168.0.101:9000/";
                System.out.println("Connecting to " + uri);
                CountDownLatch messageLatch = new CountDownLatch(1);
                MyClientEndpoint clientEndpoint = new MyClientEndpoint(messageLatch, Thread.currentThread().getName());
                container.connectToServer(clientEndpoint, URI.create(uri));
                messageLatch.await(300, TimeUnit.SECONDS);
            } catch (DeploymentException | InterruptedException | IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(WSClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}