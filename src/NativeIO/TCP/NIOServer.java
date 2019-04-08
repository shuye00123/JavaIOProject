package NativeIO.TCP;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * New IO或者说Noblock IO
 * 1.缓冲区
 *   阻塞IO与nio的一个重要的区别是将数据直接写入和读取到Buffer中，为每个java基本类型提供一种Buffer
 * 2.通道
 *   Channel是全双工的，Stream不是
 * 3.Selector
 *  多路复用器Selector轮询就绪Channel
 */
public class NIOServer {

    public static void main(String[] arg) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            int keys = selector.select();
            if (keys > 0) {
                Set<SelectionKey> keySet = selector.selectedKeys();
                for (SelectionKey key : keySet) {
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel sc = server.accept();
                        if (sc == null) {
                            continue;
                        }
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                    }
                    if (key.isReadable()) {

                    }
                    if (key.isWritable()) {

                    }
                }
            } else {

            }

        }
    }
}
