package no.hvl.dat110.broker;

import java.util.Set;
import java.util.Collection;

import no.hvl.dat110.common.TODO;
import no.hvl.dat110.common.Logger;
import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.messages.*;
import no.hvl.dat110.messagetransport.Connection;

public class Dispatcher extends Stopable {

	private final Storage storage;

	public Dispatcher(Storage storage) {
		super("Dispatcher");
		this.storage = storage;

	}

	@Override
	public void doProcess() {

		Collection<ClientSession> clients = storage.getSessions();

		Logger.lg(".");
		for (ClientSession client : clients) {

			Message msg = null;

			if (client.hasData()) {
				msg = client.receive();
			}

			// a message was received
			if (msg != null) {
				dispatch(client, msg);
			}
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void dispatch(ClientSession client, Message msg) {

		MessageType type = msg.getType();

		// invoke the appropriate handler method
		switch (type) {

			case DISCONNECT:
				onDisconnect((DisconnectMsg) msg);
				break;

			case CREATETOPIC:
				onCreateTopic((CreateTopicMsg) msg);
				break;

			case DELETETOPIC:
				onDeleteTopic((DeleteTopicMsg) msg);
				break;

			case SUBSCRIBE:
				onSubscribe((SubscribeMsg) msg);
				break;

			case UNSUBSCRIBE:
				onUnsubscribe((UnsubscribeMsg) msg);
				break;

			case PUBLISH:
				onPublish((PublishMsg) msg);
				break;

			default:
				Logger.log("broker dispatch - unhandled message type");
				break;

		}
	}

	// called from Broker after having established the underlying connection
	public void onConnect(ConnectMsg msg, Connection connection) {

		String user = msg.getUser();

		Logger.log("onConnect:" + msg);

		storage.addClientSession(user, connection);

	}

	// called by dispatch upon receiving a disconnect message
	public void onDisconnect(DisconnectMsg msg) {

		String user = msg.getUser();

		Logger.log("onDisconnect:" + msg);

		storage.removeClientSession(user);

	}

	public void onCreateTopic(CreateTopicMsg msg) {

		Logger.log("onCreateTopic:" + msg.toString());

		//Hent ut overskriftens navn fra CreateTopicMsg
		String topic = msg.getTopic();

		//Lager en overskrift i broker storage
		storage.createTopic(topic);

		//Lager en melding for å indikere at overskriften har blitt laget
		Logger.log("Overskriften med navn: " + topic + " er blitt opprettet");

	}

	public void onDeleteTopic(DeleteTopicMsg msg) {

		Logger.log("onDeleteTopic:" + msg.toString());

		//Henter ut overskrifts navnet fra DeleteTopigMsg
		String topic = msg.getTopicName();

		//Sletter overskriften fra broker storage
		storage.deleteTopic(topic);

		//LAger en melding for å informere om at overskriften er slettet
		Logger.log("Overskriften med navn: " + topic + " er nå slettet");
	}

	public void onSubscribe(SubscribeMsg msg) {

		Logger.log("onSubscribe:" + msg.toString());

		//Henter ut brukeren og overskriften fra SubscribeMsg
		String user = msg.getUser();
		String topic = msg.getTopicName();

		//Subscriber brukeren til overskriften i broker storage
		storage.addSubscriber(user, topic);

		//Lager en melding for å informere om at bruker har begynt å subscribe
		Logger.log("Bruker '" + user + "'har begynt å abbonere på emnet '" + topic + "'.");

	}

	public void onUnsubscribe(UnsubscribeMsg msg) {

		Logger.log("onUnsubscribe:" + msg.toString());

		//Henter ut brukeren fra overskriten fra UnsubscribeMsg
		String user = msg.getUser();
		String topic = msg.getTopicName();

		//Unsubscriber brukeren fra overskriften i broker storage
		storage.removeSubscriber(user, topic);

		//Lager en melding for å informere om at brukeren har sluttet å abbonere
		Logger.log("Bruker '" + user + "' har sluttet å abonnere på emnet '" + topic + "'.");

	}

	public void onPublish(PublishMsg msg) {

		Logger.log("onPublish:" + msg.toString());

		Set<String> subscribers=storage.getSubscribers(msg.getTopicName());

		for(String user : subscribers) {
			storage.getSession(user).send(msg);
		}
	}
}
