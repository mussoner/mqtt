package mqtt.message;

/**
 * Abstract Message object in MQTT
 */
public abstract class Message {

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
        
        public int getValue(){
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
        
    }
}
