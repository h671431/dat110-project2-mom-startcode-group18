package no.hvl.dat110.messages;

public class SubscribeMsg extends Message {

    // message sent from client to subscribe on a topic

    private String topicName;

    public SubscribeMsg(String user, String topic) {

        super(MessageType.SUBSCRIBE, user);
        this.topicName = topic;
        //konstruktør
    }


    //Task A - getter og setter + toString

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    @Override
    public String toString() {
        return "SubscribeMsg{" +
                "user='" + getUser() + '\'' +
                ", topicName='" + topicName + '\'' +
                '}';
    }
}
