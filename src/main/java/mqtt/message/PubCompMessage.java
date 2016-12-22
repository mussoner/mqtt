package mqtt.message;

import java.io.IOException;

public class PubCompMessage extends RetryableMessage {

    public PubCompMessage(int messageId) {
        super(Type.PUBCOMP);
        setMessageId(messageId);
    }

    public PubCompMessage(Header header) throws IOException {
        super(header);
    }

    @Override
    public void setDup(boolean dup) {
        throw new UnsupportedOperationException(
                "PubComp messages don't use the DUP flag.");
    }

    @Override
    public void setRetained(boolean retain) {
        throw new UnsupportedOperationException(
                "PubComp messages don't use the RETAIN flag.");
    }

    @Override
    public void setQos(QoS qos) {
        throw new UnsupportedOperationException(
                "PubComp messages don't use the QoS flags.");
    }

    @Override
    public String toString() {
        return "PubCompMessage{" + '}';
    }
}
