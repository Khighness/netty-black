package top.parak.buffer;

import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * @author KHighness
 * @since 2021-09-28
 * @apiNote ByteBuffer的API
 */
@Slf4j
public class ByteBufferStructure {
    /**
     * ByteBuffer
     *
     * <ul> 主要属性
     * <li> capacity:   缓冲区最大容量
     * <li> position:  表示当前指针的位置
     * <li> limit:     表示当前数组最大的使用量
     * <li> mark:      用于记录当前position的前一个位置或者默认-1
     * </ul>
     *
     * <ul> 主要方法
     * <li> mark():     设置mark=position，做个标记，用于回退
     * <li> reset():    设置position=mark，做个标记，用于回退
     * <li> clear():    设置position=0，limit=capacity，mark=-1，清除所有内容
     * <li> flip():     设置limit=position，position=0，mark=-1，用于读写转换
     * <li> rewind():   设置position=0, mark=-1, limit不变，重置指针
     * <li> compact():  将[position, limit]之间的内容移到[0, limit-position]之间的区域，
     *                  设置position=limit-position，limit=capacity，压缩数据并切换写模式
     * <li> remaining():返回(limit - position)
     * <li> hasRemaining():返回 position < limit
     * </ul>
     * @see <a href="https://my.oschina.net/u/4403946/blog/3404963">OSChina</a>
     */
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        log.debug(">>> 初始化 <<<");
        log.debug("position: {}", buffer.position());
        log.debug("limit: {}", buffer.limit());
        log.debug("capacity: {}", buffer.capacity());
        ByteBufferUtil.debugAll(buffer);

        log.debug(">>> 添加abc<<<");
        buffer.put(new byte[] {'a', 'b', 'c'});
        log.debug("position: {}", buffer.position());
        ByteBufferUtil.debugAll(buffer);

        log.debug(">>> 重置 <<<");
        buffer.rewind();
        log.debug("position: {}", buffer.position());
        ByteBufferUtil.debugAll(buffer);

        log.debug(">>> 标记 <<<");
        buffer.mark();
        log.debug("read: {}", Character.toChars(buffer.get()));
        log.debug("read: {}", Character.toChars(buffer.get()));
        buffer.reset();
        log.debug("read: {}", Character.toChars(buffer.get()));
        log.debug("read: {}", Character.toChars(buffer.get()));
        log.debug("position: {}", buffer.position());
        ByteBufferUtil.debugAll(buffer);

        log.debug(">>> 压缩 <<<");
        buffer.compact();
        log.debug("position: {}", buffer.position());
        ByteBufferUtil.debugAll(buffer);

        log.debug(">>> 添加 <<<");
        buffer.put(new byte[] {'d'});
        log.debug("position: {}", buffer.position());
        ByteBufferUtil.debugAll(buffer);
    }
}
