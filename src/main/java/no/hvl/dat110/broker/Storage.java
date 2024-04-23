package no.hvl.dat110.broker;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import no.hvl.dat110.common.TODO;
import no.hvl.dat110.common.Logger;
import no.hvl.dat110.messagetransport.Connection;
import org.apache.maven.plugin.logging.Log;

public class Storage {

	// data structure for managing subscriptions
	// maps from a topic to set of subscribed users
	protected ConcurrentHashMap<String, Set<String>> subscriptions;

	// data structure for managing currently connected clients
	// maps from user to corresponding client session object

	protected ConcurrentHashMap<String, ClientSession> clients;

	public Storage() {
		subscriptions = new ConcurrentHashMap<String, Set<String>>();
		clients = new ConcurrentHashMap<String, ClientSession>();
	}

	public Collection<ClientSession> getSessions() {
		return clients.values();
	}

	public Set<String> getTopics() {

		return subscriptions.keySet();

	}

	// get the session object for a given user
	// session object can be used to send a message to the user

	public ClientSession getSession(String user) {

		ClientSession session = clients.get(user);

		return session;
	}

	public Set<String> getSubscribers(String topic) {

		return (subscriptions.get(topic));

	}

	public void addClientSession(String user, Connection connection) {

		ClientSession clientSession = new ClientSession(user, connection);

		//legger til client session i storage
		clients.put(user, clientSession);


	}

	public void removeClientSession(String user) {
/*
Frakobler klienten tilhørende den gitte brukeren og
fjerner den tilhørende klient session fra storage
 */
		// Get the client session associated with the given user
		ClientSession clientSession = clients.get(user);

		// Check if the client session exists
		if (clientSession != null) {
			// Disconnect the client session
			clientSession.disconnect();

			// Remove the client session from the storage
			clients.remove(user);
		} else {
			// Log a warning if the client session does not exist
			Logger.log("Client session for user " + user + " does not exist.");
		}
	}

	public void createTopic(String topic) {

		//Legger til overskriften til subscriptions hvis de ikke allerede eksisterer
		if(!subscriptions.contains(topic)) {
			subscriptions.put(topic, ConcurrentHashMap.newKeySet());
		} else{
			//logger en advarsel hvis overskriften allerede eksisterer
			Logger.log("Overskriften " + topic + " eksisterer allerede.");
		}

	}

	public void deleteTopic(String topic) {

		//Fjerner overskriften fra the subscription hvis den eksisterer
		if(subscriptions.containsKey(topic)){
			subscriptions.remove(topic);
		} else {
			//Lager en advarsel hvis overskriften ikke eksisterer
			Logger.log("Overskriften " + topic + " eksisterer ikke.");
		}
	}

	public void addSubscriber(String user, String topic) {


		// Sjekk om emnet allerede eksisterer i abonnementene
		if (subscriptions.containsKey(topic)) {
			// Hent settet med abonnenter for emnet
			Set<String> subscribers = subscriptions.get(topic);
			// Legg til brukeren som abonnent
			subscribers.add(user);
		} else {
			// Opprett et nytt sett med abonnenter for emnet og legg til brukeren
			Set<String> subscribers = ConcurrentHashMap.newKeySet();
			subscribers.add(user);
			// Legg til emnet med det nye settet med abonnenter
			subscriptions.put(topic, subscribers);
		}

	}

	public void removeSubscriber(String user, String topic) {



		// Sjekk om emnet eksisterer i abonnementene
		if (subscriptions.containsKey(topic)) {
			// Hent settet med abonnenter for emnet
			Set<String> subscribers = subscriptions.get(topic);
			// Fjern brukeren som abonnent hvis den eksisterer
			subscribers.remove(user);
		} else {
			// Logg en advarsel hvis emnet ikke eksisterer
			Logger.log("Overskriften " + topic + " eksisterer ikke.");
		}
	}
}
