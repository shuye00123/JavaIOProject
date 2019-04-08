package NativeIO.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BIOClient {
    private Socket socket;
    public BIOClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }
    public void send() throws IOException {
        System.out.println("Connection Success...");
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println("Hello world");
        printWriter.flush();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = in.readLine();
        System.out.println("Receive " + message);
    }
    static class SendRunnable implements Runnable {
        private BIOClient bioClient;

        public SendRunnable(BIOClient bioClient) {
            this.bioClient = bioClient;
        }

        @Override
        public void run() {
            try {
                bioClient.send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] arg) throws IOException, InterruptedException {
        Thread thread1 = new Thread(new SendRunnable(new BIOClient("127.0.0.1", 6666)));
        Thread thread2 = new Thread(new SendRunnable(new BIOClient("127.0.0.1", 6666)));
        Thread thread3 = new Thread(new SendRunnable(new BIOClient("127.0.0.1", 6666)));
        Thread thread4 = new Thread(new SendRunnable(new BIOClient("127.0.0.1", 6666)));
        thread1.run();
        thread2.run();
        thread3.run();
        thread4.run();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
    }

}
