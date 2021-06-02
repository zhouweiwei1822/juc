package zhouww.java.network.nio;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * buffer的学习
 */
public class BufferStudy {
    public static void main(String[] args) throws UnsupportedEncodingException {
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        byteBuffer.put("zhang san nihao ni zai guanm ".getBytes());
        System.out.println(byteBuffer.hasRemaining());
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.mark());
        byteBuffer.flip();
        System.out.println(byteBuffer.mark());
        byteBuffer.get();
        System.out.println(byteBuffer.mark());
        byteBuffer.get();
        System.out.println(byteBuffer.mark());
        byteBuffer.get();
        System.out.println(byteBuffer.mark());
        byteBuffer.compact();
        System.out.println(byteBuffer.mark());
        byteBuffer.flip();
        System.out.println(byteBuffer.mark());
        byteBuffer.clear();
        System.out.println(byteBuffer.mark());


        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put((byte)1);
        buffer.put((byte)2);
        buffer.put((byte)3);
        buffer.put((byte)4);
        buffer.put((byte)5);
     //   buffer.rewind();
        buffer.flip();
       while (buffer.hasRemaining()){
           System.out.println(buffer.get());

       }

        ByteBuffer buffer2 = ByteBuffer.allocate(1024);
         buffer2.put("测试数据内推".getBytes("utf-8"));
        buffer2.flip();
       CharBuffer k= StandardCharsets.UTF_8.decode(buffer2);
       // System.out.println(k);
        System.out.println(buffer2);


    }
}
