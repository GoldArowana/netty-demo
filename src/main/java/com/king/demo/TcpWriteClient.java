package com.king.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TcpWriteClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             OutputStream outputStream = socket.getOutputStream()) {

            outputStream.write(("hello\r\n" +
                    "i am a tcp client from localhost,\r\n" +
                    "testing...\r\n" +
                    "*******\r\n").getBytes());

            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
