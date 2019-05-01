package example.nio;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * 项目名称：netty_demo
 * 类 名 称：NioExample
 * 类 描 述：NIO初探
 * 创建时间：2019/5/1 2:00 PM
 * 创 建 人：john
 */
public class NioExample {

    public static void main(String[] args){

        IntBuffer buffer = IntBuffer.allocate(10); // 初始化buffer

        System.out.println("capacity = [" + buffer.capacity() + "]");
        System.out.println("limit = [" + buffer.limit() + "]");
        System.out.println("position = [" + buffer.position() + "]");

        for (int i=0; i<10; i++) {// 生成10个数字20以内的随机数放到buffer中
            int random = new SecureRandom().nextInt(20);
            buffer.put(random);

            if (i == 3) {

                System.out.println("capacity = [" + buffer.capacity() + "]");
                System.out.println("limit = [" + buffer.limit() + "]");
                System.out.println("position = [" + buffer.position() + "]");
            }
        }

        buffer.flip();// 将buffer的写操作反转为读操作，至关重要


        System.out.println("capacity = [" + buffer.capacity() + "]");
        System.out.println("limit = [" + buffer.limit() + "]");
        System.out.println("position = [" + buffer.position() + "]");

        int k = 0;
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());// 从buffer中读取内容

            k++;

            if (k == 3) {

                System.out.println("capacity = [" + buffer.capacity() + "]");
                System.out.println("limit = [" + buffer.limit() + "]");
                System.out.println("position = [" + buffer.position() + "]");
            }


        }
    }
}
