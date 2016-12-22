package mqtt.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnAckMessage extends Message {

    private ConnectionStatus status;

    public ConnAckMessage() {
        super(Type.CONNACK);
    }

    public ConnAckMessage(Header header) {
        super(header);
    }

    public ConnAckMessage(ConnectionStatus status) {
        super(Type.CONNACK);
        if (status == null) {
            throw new IllegalArgumentException("The status of ConnAskMessage can't be null");
        }
        this.status = status;
    }

    @Override
    protected int messageLength() {
        return 2;
    }

    @Override
    protected void readMessage(InputStream in, int msgLength) throws IOException {
        if (msgLength != messageLength()) {
            throw new IllegalStateException(
                    "Message Length must be 2 for CONNACK. Current value: "
                    + msgLength);
        }

        in.read();//ignore first byte
        this.status = ConnectionStatus.valueOf(in.read());
    }

    @Override
    protected void writeMessage(OutputStream out) throws IOException {
        out.write(0x00);
        out.write(this.status.getValue());
    }

    public ConnectionStatus getStatus() {
        return this.status;
    }

    @Override
    public void setDup(boolean dup) {
        throw new UnsupportedOperationException(
                "CONNACK messages don't use the DUP flag.");
    }

    @Override
    public void setRetained(boolean retain) {
        throw new UnsupportedOperationException(
                "CONNACK messages don't use the RETAIN flag.");
    }

    @Override
    public void setQos(QoS qos) {
        throw new UnsupportedOperationException(
                "CONNACK messages don't use the QoS flags.");
    }

    public enum ConnectionStatus {
        ACCEPTED(0),
        UNACCEPTABLE_PROTOCOL_VERSION(1),
        IDENTIFIER_REJECTED(2),
        SERVER_UNAVAILABLE(3),
        BAD_USERNAME_OR_PASSWORD(4),
        NOT_AUTHORIZED(5);

        private final int val;

        ConnectionStatus(int val) {
            this.val = val;
        }

        public int getValue() {
            return this.val;
        }

        static ConnectionStatus valueOf(int val) {
            for (ConnectionStatus cs : values()) {
                if (cs.val == val) {
                    return cs;
                }
            }
            throw new IllegalArgumentException("Unkwnown ConnectionStatus detected : " + val);
        }
    }

    @Override
    public String toString() {
        return "ConnAckMessage{" + "status=" + status + '}';
    }

}
