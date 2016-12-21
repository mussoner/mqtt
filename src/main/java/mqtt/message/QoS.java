package mqtt.message;

/**
 * QoS of the messages
 */
public enum QoS {
    AT_MOST_EXACTLY(0),
    AT_LEAST_ONCE(1),
    EXACTLY_ONE(2);

    private final int val;

    QoS(int val) {
        this.val = val;
    }
    
    public int getValue(){
        return this.val;
    }

    static QoS valueOf(int val) {
        for (QoS q : values()) {
            if (q.val == val) {
                return q;
            }
        }
        throw new IllegalArgumentException("Unkwnon QoS detected : " + val);
    }
}
