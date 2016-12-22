package mqtt.message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectMessage extends Message {

    private static int CONNECT_HEADER_SIZE = 12;

    private String protocolId = "MQIsdp";
    private byte protocolVersion = 3;
    private String clientId;
    private int keepAlive;
    private String username;
    private String password;
    private boolean cleanSession;
    private String willTopic;
    private String will;
    private QoS willQoS;
    private boolean retainWill;
    private boolean hasUsername;
    private boolean hasPassword;
    private boolean hasWill;

    public ConnectMessage() {
        super(Type.CONNECT);
    }

    public ConnectMessage(Header header) {
        super(header);
    }

    public ConnectMessage(String clientId, boolean cleanSesssion, int keepAlive) {
        super(Type.CONNECT);
        if (clientId == null || clientId.length() > 23) {
            throw new IllegalArgumentException("Client id cannot be null and must be at most 23 chars long : " + clientId);
        }
        this.clientId = clientId;
        this.cleanSession = cleanSesssion;
        this.keepAlive = keepAlive;
    }

    @Override
    protected int messageLength() {
        return FormatUtil.toMQttString(this.clientId).length
                + FormatUtil.toMQttString(this.willTopic).length
                + FormatUtil.toMQttString(this.will).length
                + FormatUtil.toMQttString(this.username).length
                + FormatUtil.toMQttString(password).length + CONNECT_HEADER_SIZE;
    }

    @Override
    protected void readMessage(InputStream in, int msgLength) throws IOException {
        DataInputStream dis = new DataInputStream(in);
        this.protocolId = dis.readUTF();
        this.protocolVersion = dis.readByte();
        byte cFlags = dis.readByte();
        this.hasUsername = (cFlags & 0x80) > 0;
        this.hasPassword = (cFlags & 0x40) > 0;
        this.retainWill = (cFlags & 0x20) > 0;
        this.willQoS = QoS.valueOf(cFlags >> 3 & 0x03);
        this.hasWill = (cFlags & 0x04) > 0;
        this.cleanSession = (cFlags & 0x20) > 0;
        this.keepAlive = dis.read() * 256 + dis.read();
        this.clientId = dis.readUTF();
        if (this.hasWill) {
            this.willTopic = dis.readUTF();
            this.will = dis.readUTF();
        }
        if (this.hasUsername) {
            try {
                this.username = dis.readUTF();
            } catch (EOFException e) {
                // ignore
            }
        }
        if (this.hasPassword) {
            try {
                this.password = dis.readUTF();
            } catch (EOFException e) {
                // ignore
            }
        }
    }

    @Override
    protected void writeMessage(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        dos.writeUTF(this.protocolId);
        dos.write(this.protocolVersion);
        int flags = this.cleanSession ? 2 : 0;
        flags |= this.hasWill ? 0x04 : 0;
        flags |= this.willQoS == null ? 0 : this.willQoS.getValue() << 3;
        flags |= this.retainWill ? 0x20 : 0;
        flags |= this.hasPassword ? 0x40 : 0;
        flags |= this.hasUsername ? 0x80 : 0;
        dos.write((byte) flags);
        dos.writeChar(this.keepAlive);

        dos.writeUTF(this.clientId);
        if (this.hasWill) {
            dos.writeUTF(this.willTopic);
            dos.writeUTF(this.will);
        }
        if (this.hasUsername) {
            dos.writeUTF(this.username);
        }
        if (this.hasPassword) {
            dos.writeUTF(this.password);
        }
        dos.flush();
    }

    public void setCredentials(String username, String password) {
        if ((username == null || username.isEmpty())
                && (password != null && !password.isEmpty())) {
            throw new IllegalArgumentException(
                    "It is not valid to supply a password without supplying a username.");
        }

        this.username = username;
        this.password = password;
        this.hasUsername = this.username != null;
        this.hasPassword = this.password != null;
    }

    public void setWill(String willTopic, String will) {
        setWill(willTopic, will, QoS.AT_MOST_ONCE, false);
    }

    public void setWill(String willTopic, String will, QoS willQoS,
            boolean retainWill) {
        if ((willTopic == null ^ will == null)
                || (will == null ^ willQoS == null)) {
            throw new IllegalArgumentException(
                    "Can't set willTopic, will or willQoS value independently");
        }

        this.willTopic = willTopic;
        this.will = will;
        this.willQoS = willQoS;
        this.retainWill = retainWill;
        this.hasWill = willTopic != null;
    }

    @Override
    public void setDup(boolean dup) {
        throw new UnsupportedOperationException(
                "CONNECT messages don't use the DUP flag.");
    }

    @Override
    public void setRetained(boolean retain) {
        throw new UnsupportedOperationException(
                "CONNECT messages don't use the RETAIN flag.");
    }

    @Override
    public void setQos(QoS qos) {
        throw new UnsupportedOperationException(
                "CONNECT messages don't use the QoS flags.");
    }

    public String getProtocolId() {
        return protocolId;
    }

    public byte getProtocolVersion() {
        return protocolVersion;
    }

    public String getClientId() {
        return clientId;
    }

    public int getKeepAlive() {
        return keepAlive;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public String getWillTopic() {
        return willTopic;
    }

    public String getWill() {
        return will;
    }

    public QoS getWillQoS() {
        return willQoS;
    }

    public boolean isWillRetained() {
        return retainWill;
    }

    public boolean hasUsername() {
        return hasUsername;
    }

    public boolean hasPassword() {
        return hasPassword;
    }

    public boolean hasWill() {
        return hasWill;
    }

    @Override
    public String toString() {
        return "ConnectMessage{" + "protocolId=" + protocolId + ", protocolVersion=" + protocolVersion + ", clientId=" + clientId + ", keepAlive=" + keepAlive + ", username=" + username + ", password=" + password + ", cleanSession=" + cleanSession + ", willTopic=" + willTopic + ", will=" + will + ", willQoS=" + willQoS + ", retainWill=" + retainWill + ", hasUsername=" + hasUsername + ", hasPassword=" + hasPassword + ", hasWill=" + hasWill + '}';
    }

}
