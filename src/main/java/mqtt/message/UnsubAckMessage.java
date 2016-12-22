package mqtt.message;

import java.io.IOException;

public class UnsubAckMessage extends RetryableMessage {

    public UnsubAckMessage(Header header) throws IOException {
        super(header);
    }

    public UnsubAckMessage() {
        super(Type.UNSUBACK);
    }

    @Override
    public void setDup(boolean dup) {
        throw new UnsupportedOperationException(
                "UnsubAck messages don't use the DUP flag.");
    }

    @Override
    public void setRetained(boolean retain) {
        throw new UnsupportedOperationException(
                "UnsubAck messages don't use the RETAIN flag.");
    }

    @Override
    public void setQos(QoS qos) {
        throw new UnsupportedOperationException(
                "UnsubAck messages don't use the QoS flags.");
    }

    @Override
    public String toString() {
        return "UnsubAckMessage{" + '}';
    }
}
