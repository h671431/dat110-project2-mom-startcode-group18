package no.hvl.dat110.messages;

public class PublishMsg extends Message {

	// message sent from client to create publish a message on a topic
	private String topicName;
	private String message;


	public PublishMsg(String user, String topicName, String message) {
		super(MessageType.PUBLISH, user);
		this.topicName = topicName;
		this.message = message;
		//konstruktør
	}

	//task a - getter & setter + toString
	public String getMessage() {

		return message;
	}

	public void setMessage(String message){
		this.message = message;
	}
	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Override
	public String toString() {
		return "PublishMsg{" +
				"user='" + getUser() + '\'' +
				", topicName='" + topicName + '\'' +
				", message='" + message + '\'' +
				'}';
	}
}

