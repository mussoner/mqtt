package mqtt.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Abstract Message object in MQTT
 */
public abstract class Message {

    private static char nextId = 1;
    private Header header;

    public Message(Type type) {
        this.header = new Header(type, false, QoS.AT_MOST_ONCE, false);
    }

    public Message(Header header) {
        this.header = header;
    }

    final void read(InputStream in) throws IOException {
        readMessage(in, readMessageLength(in));
    }

    final void write(OutputStream out) throws IOException {
        out.write(this.header.encode());
        writeMessageLength(out);
        writeMessage(out);
    }

    private int readMessageLength(InputStream in) throws IOException {
        int msgLength = 0;
        int multiplier = 1;
        int digit;
        do {
            digit = in.read();
            msgLength += (digit & 0x7f) * multiplier;
            multiplier *= 128;
        } while ((digit & 0x80) > 0);
        return msgLength;
    }

    private void writeMessageLength(OutputStream out) throws IOException {
        int msgLength = messageLength();
        int val = msgLength;
        do {
            byte b = (byte) (val & 0x7F);
            val >>= 7;
            if (val > 0) {
                b |= 0x80;
            }
            out.write(b);
        } while (val > 0);
    }

    protected int messageLength() {
        return 0;
    }

    protected void writeMessage(OutputStream out) throws IOException {
    }

    protected void readMessage(InputStream in, int msgLength) throws IOException {

    }

    public final byte[] toBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            write(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    public void setRetained(boolean retain) {
        header.retain = retain;
    }

    public boolean isRetained() {
        return header.retain;
    }

    public void setQos(QoS qos) {
        header.qos = qos;
    }

    public QoS getQos() {
        return header.qos;
    }

    public void setDup(boolean dup) {
        header.dup = dup;
    }

    public boolean isDup() {
        return header.dup;
    }

    public Type getType() {
        return header.type;
    }

    public static char nextId() {
        return nextId++;
    }

    /**
     * Message Type enumeration
     */
    public enum Type {
        CONNECT(1),
        CONNACK(2),
        PUBLISH(3),
        PUBACK(4),
        PUBREC(5),
        PUBREL(6),
        PUBCOMP(7),
        SUBSCRIBE(8),
        SUBACK(9),
        UNSUBSCRIBE(10),
        UNSUBACK(11),
        PINGREQ(12),
        PINGRESP(13),
        DISCONNECT(14);

        final private int val;

        Type(int val) {
            this.val = val;
        }

        public int getValue() {
            return this.val;
        }

        static Type valueOf(int i) {
            for (Type t : Type.values()) {
                if (t.val == i) {
                    return t;
                }
            }
            return null;
        }
    }

    /**
     * Fixed Header inside the MQTT Message
     */
    public static class Header {

        private Type type;
        private boolean retain;
        private QoS qos = QoS.AT_MOST_ONCE;
        private boolean dup;

        public Header(Type type, boolean retain, QoS qos, boolean dup) {
            this.type = type;
            this.retain = retain;
            this.qos = qos;
            this.dup = dup;
        }

        public Header(byte flags) {
            this.retain = (flags & 1) > 0;
            this.qos = QoS.valueOf((flags & 0x6) >> 1);
            this.dup = (flags & 8) > 0;
            this.type = Type.valueOf((flags >> 4) & 0xF);
        }

        private byte encode() {
            byte b = 0;
            b = (byte) (this.type.getValue() << 4);
            b |= this.retain ? 1 : 0;
            b |= this.qos.getValue() << 1;
            b |= this.dup ? 8 : 0;
            return b;
        }

        public Type getType() {
            return this.type;
        }

        @Override
        public String toString() {
            return "Header{" + "type=" + type + ", retain=" + retain + ", qos=" + qos + ", dup=" + dup + '}';
        }

    }
}
