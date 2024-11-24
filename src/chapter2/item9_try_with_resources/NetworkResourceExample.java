package chapter2.item9_try_with_resources;

import java.io.*;
import java.net.*;

/**
 * Demonstrates try-with-resources usage with network operations.
 * Shows proper resource management for sockets and network streams.
 */
public class NetworkResourceExample {
    
    /**
     * Downloads content from a URL using try-with-resources
     */
    public static String downloadContent(String urlString) throws IOException {
        try (InputStream in = new URL(urlString).openStream();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(in))) {
            
            StringBuilder content = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            
            return content.toString();
        }
    }
    
    /**
     * Implements a simple echo server using try-with-resources
     */
    public static void runEchoServer(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Echo server listening on port " + port);
            
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(
                             new InputStreamReader(clientSocket.getInputStream()))) {
                    
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        out.println("Echo: " + inputLine);
                        if (inputLine.equals("exit")) {
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Implements a simple HTTP client using try-with-resources
     */
    public static void sendHttpRequest(String host, int port, String path) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {
            
            // Send HTTP request
            out.println("GET " + path + " HTTP/1.1");
            out.println("Host: " + host);
            out.println("Connection: close");
            out.println();
            
            // Read response
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
    
    /**
     * Copies data between streams using try-with-resources
     */
    public static void copyStream(InputStream src, OutputStream dst) throws IOException {
        try (InputStream input = new BufferedInputStream(src);
             OutputStream output = new BufferedOutputStream(dst)) {
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }
    
    public static void main(String[] args) {
        // Example usage of downloadContent
        try {
            String content = downloadContent("http://example.com");
            System.out.println("Downloaded content length: " + content.length());
        } catch (IOException e) {
            System.err.println("Error downloading content: " + e.getMessage());
        }
        
        // Example usage of HTTP client
        try {
            sendHttpRequest("example.com", 80, "/");
        } catch (IOException e) {
            System.err.println("Error sending HTTP request: " + e.getMessage());
        }
        
        // Start echo server (commented out as it runs indefinitely)
        /*
        try {
            runEchoServer(8080);
        } catch (IOException e) {
            System.err.println("Error running echo server: " + e.getMessage());
        }
        */
    }
}
