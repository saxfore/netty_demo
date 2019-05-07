package example.nio;

import java.nio.ByteBuffer;

/**
 * 项目名称：netty_demo
 * 类 名 称：ByteBufferExample
 * 类 描 述：TODO
 * 创建时间：2019/5/5 8:17 PM
 * 创 建 人：john
 */
public class ByteBufferExample {

    public static void main(String[] args) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        for (int i=0; i<byteBuffer.capacity(); i++){
            byteBuffer.put((byte) i);
        }

        slice(byteBuffer);

        byteBuffer.clear();

        System.out.println("byteBuffer -------------");
        while (byteBuffer.hasRemaining()) {
            System.out.println(byteBuffer.get());
        }

        // 将读写bytebuffer转换为只读的byteBuffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();

    }

    /**
     * 重新生成一份从4到8的引用，当源数据改变或者本数据改变时，另一份数据也将同步改变
     * @param byteBuffer
     * @return
     */
    public static ByteBuffer slice(ByteBuffer byteBuffer) {

        byteBuffer.position(4);
        byteBuffer.limit(8);

        ByteBuffer sliceByteBuffer = byteBuffer.slice();

        for (int k = 0; k < sliceByteBuffer.capacity(); k++) {

            byte b = sliceByteBuffer.get(k);
            b *= 2;

            sliceByteBuffer.put(b);
        }

        sliceByteBuffer.flip();

        System.out.println("sliceByteBuffer -------------");
        while (sliceByteBuffer.hasRemaining()) {
            System.out.println(sliceByteBuffer.get());
        }

        return sliceByteBuffer;
    }
}
