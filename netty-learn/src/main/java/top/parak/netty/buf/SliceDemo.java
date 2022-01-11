package top.parak.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

/**
 * @author KHighness
 * @since 2021-10-01
 * @apiNote Slice零拷贝
 */
@Slf4j
public class SliceDemo {
    public static void main(String[] args) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer(10);
        buf.writeBytes(new byte[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        ByteBufUtil.log(buf);

        ByteBuf buf1 = buf.slice(0, 5);
        ByteBufUtil.log(buf1);
        ByteBuf buf2 = buf.slice(5, 5);
        ByteBufUtil.log(buf2);

        buf1.setByte(0, 'k');
        ByteBufUtil.log(buf1);
        ByteBufUtil.log(buf);
    }
}
