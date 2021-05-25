package zhouww.nio.study;

import java.nio.ByteBuffer;

/**
 * buffer的学习
 */
public class BufferStudy {
    public static void main(String[] args) {
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        byteBuffer.put("zhang san nihao ni zai guanm ".getBytes());
        System.out.println(byteBuffer.hasRemaining());
        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.mark());
        byteBuffer.flip();
        System.out.println(byteBuffer.mark());
    }
}
