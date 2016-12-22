package mqtt.message;

import java.io.IOException;

public class PingRespMessage extends Message {

    public PingRespMessage(Header header) throws IOException {
        super(header);
    }

    public PingRespMessage() {
        super(Type.PINGRESP);
    }

    @Override
    public String toString() {
        return "PingRespMessage{" + '}';
    }
}
