package NativeIO.TCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BIOServer {
    private ServerSocket serverSocket;
    private Executor executor = Executors.newFixedThreadPool(6);

    static class SocketRunnable implements Runnable {
        private Socket socket;

        public SocketRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                //  InputStream的read方法will blocks until input data is available
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = in.readLine();
                System.out.println("Client send " + message);
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println(Thread.currentThread().getName() + message);
                printWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public BIOServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("server bind " + port);
    }

    public void startServer() throws IOException {
        while (true) {
            System.out.println("Wait client...");
            Socket socket = serverSocket.accept();
            executor.execute(new SocketRunnable(socket));
        }
    }

    public static void main(String[] arg){
        try {
            BIOServer server = new BIOServer(6666);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
