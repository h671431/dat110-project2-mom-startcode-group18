package no.hvl.dat110.messages;

public class DeleteTopicMsg extends Message {
    private String topicName;

    // message sent from client to create topic on the broker

    /**
     * Bodil har endret
     * @param user
     * @param topic
     */
    public DeleteTopicMsg(String user, String topic) {

        super(MessageType.DELETETOPIC,user);
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
        return "DeleteTopicMsg{" +
                "user='" + getUser() + '\'' +
                ", topicName='" + topicName + '\'' +
                '}';
    }
}
