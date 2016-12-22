package mqtt.message;

import java.io.IOException;

public class PubAckMessage extends RetryableMessage {

    public PubAckMessage(int messageId) {
        super(Type.PUBACK);
        setMessageId(messageId);
    }

    public PubAckMessage(Header header) throws IOException {
        super(header);
    }

    @Override
    public void setDup(boolean dup) {
        throw new UnsupportedOperationException(
                "PubAck messages don't use the DUP flag.");
    }

    @Override
    public void setRetained(boolean retain) {
        throw new UnsupportedOperationException(
                "PubAck messages don't use the RETAIN flag.");
    }

    @Override
    public void setQos(QoS qos) {
        throw new UnsupportedOperationException(
                "PubAck messages don't use the QoS flags.");
    }

    @Override
    public String toString() {
        return "PubAckMessage{" + '}';
    }
    
}
