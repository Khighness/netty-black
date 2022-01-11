package top.parak.netty.buf;

import io.netty.buffer.ByteBuf;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @author KHighness
 * @since 2021-10-01
 * @apiNote ByteBuf调试工具类
 */
public class ByteBufUtil {

    public static void log(ByteBuf buffer) {
        int len = buffer.readableBytes();
        int rows = len / 16 + (len % 15 == 0 ? 0 : 1) + 4;
        StringBuilder builder = new StringBuilder(rows * 80 * 2)
                .append("read index: ").append(buffer.readerIndex()).append(", ")
                .append("write index: ").append(buffer.writerIndex()).append(", ")
                .append("capacity: ").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(builder, buffer);
        System.out.println(builder.toString());
    }

}
