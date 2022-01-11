package top.parak.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import top.parak.config.ServerConfig;
import top.parak.message.Message;
import top.parak.serialize.SerializerFactory;

import java.util.List;

/**
 * @author KHighness
 * @since 2021-10-03
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        // 1. 4B魔数
        buf.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1B版本
        buf.writeByte(2);
        // 3. 1B序列化
        buf.writeByte(ServerConfig.getSerializationType());
        // 4. 1B消息类型
        buf.writeByte(msg.getMessageType());
        // 5. 4B消息ID
        buf.writeInt(msg.getSequenceId());
        // 6. 1B填充
        buf.writeByte(0XFF);
        // 序列化
        byte[] bytes = SerializerFactory.getInstance(ServerConfig.getSerializationType()).serialize(msg);
        // 7. 内容长度
        buf.writeInt(bytes.length);
        // 8. 内容
        buf.writeBytes(bytes);

        out.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        // 1. 魔数
        int magicNumber = msg.readInt();
        // 2. 版本
        byte version = msg.readByte();
        // 3. 序列化类型
        byte serializationType = msg.readByte();
        // 4. 消息类型
        byte messageType = msg.readByte();
        // 5. 消息ID
        int sequenceId = msg.readInt();
        // 6. 填充
        byte padding = msg.readByte();
        // 7. 内容长度
        int length = msg.readInt();
        // 8. 内容
        byte[] bytes = new byte[length];
        msg.readBytes(bytes, 0, length);
        // 反序列化
        Object message = SerializerFactory.getInstance(serializationType).deserialize(bytes, Message.getMessageClass(messageType));

        out.add(message);
    }
}
