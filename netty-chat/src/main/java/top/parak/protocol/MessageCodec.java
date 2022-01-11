package top.parak.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import top.parak.message.LoginRequestMessage;
import top.parak.message.Message;

import java.io.*;
import java.util.List;

/**
 * @author KHighness
 * @since 2021-10-03
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodec extends ByteToMessageCodec<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] bytes = bos.toByteArray();

        // 1. 4B魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1B版本
        out.writeByte(1);
        // 3. 1B序列化
        out.writeByte(0);
        // 4. 1B消息类型
        out.writeByte(msg.getMessageType());
        // 5. 4B消息ID
        out.writeInt(msg.getSequenceId());
        // 6. 1B填充
        out.writeByte(0XFF);
        // 7. 内容长度
        out.writeInt(bytes.length);
        // 8. 内容
        out.writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1. 魔数
        int magicNumber = in.readInt();
        // 2. 版本
        byte version = in.readByte();
        // 3. 序列化类型
        byte serializationType = in.readByte();
        // 4. 消息类型
        byte messageType = in.readByte();
        // 5. 消息ID
        int sequenceId = in.readInt();
        // 6. 填充
        byte padding = in.readByte();
        // 7. 内容长度
        int length = in.readInt();
        // 8. 内容
        byte[] bytes = new byte[length];
        in.readBytes(bytes, 0, length);

        if (serializationType == 0) { // JDK序列化
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Message message = (Message) ois.readObject();
            log.debug("message[magicNumber = {}, version = {}, serializationType = {}, messageType = {}, sequenceId = {}, padding = {}, length = {}, content={}]",
                    magicNumber, version, serializationType, messageType, sequenceId, padding, length, message);
            out.add(message);
        }
    }

    public static void main(String[] args) throws Exception {
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        EmbeddedChannel channel = new EmbeddedChannel(
                // 编解码器Handler：线程不安全
                new LengthFieldBasedFrameDecoder(1024, 12, 4, 0, 0),
                // 日志Handler：线程安全
                LOGGING_HANDLER,
                new MessageCodec()
        );
        LoginRequestMessage message = new LoginRequestMessage("KHighness", "KAG1823");
        channel.writeOutbound(message);
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        new MessageCodec().encode(null, message, buf);
        buf.writeByte(3);
        channel.writeInbound(buf);
    }
}
