package example.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 项目名称：netty_demo
 * 类 名 称：DirectorByteBufferExample
 * 类 描 述：TODO
 * 创建时间：2019/5/7 2:36 PM
 * 创 建 人：john
 */
public class DirectorByteBufferExample {

    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        SocketAddress socketAddress = new InetSocketAddress(8899);
        serverSocketChannel.bind(socketAddress);


        long messageMaxLength = 2+3+4;

        ByteBuffer[] byteBuffers = new ByteBuffer[3];

        byteBuffers[0] = ByteBuffer.allocate(2);
        byteBuffers[1] = ByteBuffer.allocate(3);
        byteBuffers[2] = ByteBuffer.allocate(4);

        SocketChannel socketChannel = serverSocketChannel.accept();// 阻塞检测输入信号

        while (true) {

            long hasReadLength = 0;

            while (hasReadLength < messageMaxLength) {
                long length = socketChannel.read(byteBuffers);// 将输入信号读取到bytebuffers数组中

                hasReadLength += length;
                System.out.println("hasReadLength = [" + hasReadLength + "]");

                Arrays.asList(byteBuffers).stream().map(buffer-> "position: "+buffer.position()+", limit: "+buffer.limit()+", capacity: "+buffer.capacity()).forEach(System.out::println);
            }


            Arrays.asList(byteBuffers).stream().forEach(byteBuffer -> byteBuffer.flip());// 将position归位到0，limit指向可写的最大位置

            long hasWrittenLength = 0;
            while (hasWrittenLength < messageMaxLength) {
                long length = socketChannel.write(byteBuffers);// 将bytebuffers内容写入到socketchannel通道中，实现在终端回显的效果
                hasWrittenLength += length;
            }

            System.out.println("hasWrittenLength = [" + hasWrittenLength + "]");

            Arrays.asList(byteBuffers).stream().forEach(byteBuffer -> byteBuffer.clear());
        }

    }
}
