package mqtt.message;

import java.io.IOException;

public class PubRelMessage extends RetryableMessage {

    public PubRelMessage(int messageId) {
        super(Type.PUBREL);
        setMessageId(messageId);
    }

    public PubRelMessage(Header header) throws IOException {
        super(header);
    }

    @Override
    public void setRetained(boolean retain) {
        throw new UnsupportedOperationException(
                "PubRel messages don't use the RETAIN flag.");
    }
}
