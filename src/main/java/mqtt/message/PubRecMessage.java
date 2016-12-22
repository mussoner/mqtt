package mqtt.message;

import java.io.IOException;

public class PubRecMessage extends RetryableMessage {

    public PubRecMessage(int messageId) {
        super(Type.PUBREC);
        setMessageId(messageId);
    }

    public PubRecMessage(Header header) throws IOException {
        super(header);
    }

    @Override
    public void setDup(boolean dup) {
        throw new UnsupportedOperationException(
                "PubRec messages don't use the DUP flag.");
    }

    @Override
    public void setRetained(boolean retain) {
        throw new UnsupportedOperationException(
                "PubRec messages don't use the RETAIN flag.");
    }

    @Override
    public void setQos(QoS qos) {
        throw new UnsupportedOperationException(
                "PubRec messages don't use the QoS flags.");
    }
}
