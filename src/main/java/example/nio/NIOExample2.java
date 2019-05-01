package example.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * 项目名称：netty_demo
 * 类 名 称：NIOExample2
 * 类 描 述：TODO
 * 创建时间：2019/5/1 6:28 PM
 * 创 建 人：john
 */
public class NIOExample2 {

    public static void main(String[] args) throws IOException {

        FileInputStream fileInputStream = new FileInputStream("src/main/java/example/nio/NIOExample2.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("src/main/java/example/nio/NIOExample2_output.txt");

        FileChannel inputChannel = fileInputStream.getChannel();
        FileChannel outputChannel = fileOutputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(100);

        while(true){
            buffer.clear(); // 底层代码并没有清空原有的数据，仅仅是对position、limit进行了重新定位, 注释掉此行程序将陷入死循环

            int read = inputChannel.read(buffer);

            System.out.println("read = "+ read);

            if (-1 != read) {

                buffer.flip();
                outputChannel.write(buffer);

                continue;
            }

            System.out.println("run end ...");
            break;
        }
    }

}
