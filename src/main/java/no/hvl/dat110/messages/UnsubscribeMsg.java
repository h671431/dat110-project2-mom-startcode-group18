package no.hvl.dat110.messages;

public class UnsubscribeMsg extends Message {

    // message sent from client to unsubscribe on a topic
    private String topicName;

    /**
     * Bodil endret
     * @param user
     * @param topic
     */
    public UnsubscribeMsg(String user, String topic) {

        super(MessageType.UNSUBSCRIBE, user);
        this.topicName = topic;
    }


    /*
     * Task A - getter og setter + toString
     */
    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String toString() {
        return "UnsubscribeMsg{" +
                "user='" + getUser() + '\'' +
                ", topicName='" + topicName + '\'' +
                '}';
    }
}
