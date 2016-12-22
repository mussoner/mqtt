package mqtt.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class UnsubscribeMessage extends RetryableMessage {

    private List<String> topics = new ArrayList<String>();

    public UnsubscribeMessage(String topic) {
        super(Type.UNSUBSCRIBE);
        setQos(QoS.AT_LEAST_ONCE);
        topics.add(topic);
    }

    public void addTopic(String topic) {
        topics.add(topic);
    }

    public UnsubscribeMessage(Header header) throws IOException {
        super(header);
    }

    @Override
    protected int messageLength() {
        int length = 2; // message id length
        for (String topic : topics) {
            length += FormatUtil.toMQttString(topic).length;
        }
        return length;
    }

    @Override
    protected void writeMessage(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        super.writeMessage(dos);
        for (String topic : topics) {
            dos.writeUTF(topic);
        }
        dos.flush();
    }

    @Override
    protected void readMessage(InputStream in, int msgLength) throws IOException {
        super.readMessage(in, msgLength);
        DataInputStream dis = new DataInputStream(in);
        int pos = 2;
        while (pos < msgLength) {
            topics.add(dis.readUTF());
            pos += FormatUtil.toMQttString(topics.get(topics.size() - 1)).length;
        }
    }

    @Override
    public void setQos(QoS qos) {
        if (qos != QoS.AT_LEAST_ONCE) {
            throw new IllegalArgumentException(
                    "SUBSCRIBE is always using QoS-level AT LEAST ONCE. Requested level: "
                    + qos);
        }
        super.setQos(qos);
    }

    @Override
    public void setDup(boolean dup) {
        if (dup == true) {
            throw new IllegalArgumentException(
                    "SUBSCRIBE can't set the DUP flag.");
        }
        super.setDup(dup);
    }

    @Override
    public void setRetained(boolean retain) {
        throw new UnsupportedOperationException(
                "SUBSCRIBE messages don't use the RETAIN flag");
    }

    public List<String> getTopics() {
        return topics;
    }

}
