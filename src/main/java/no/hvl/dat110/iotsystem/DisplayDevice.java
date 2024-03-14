package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.messages.Message;
import no.hvl.dat110.messages.PublishMsg;

public class DisplayDevice {

	private static final int COUNT = 10;

	public static void main(String[] args) {

		System.out.println("Display starting ...");

		Client displayClient = new Client("display", Common.BROKERHOST, Common.BROKERPORT);

		try {
			// Kobler til megleren
			displayClient.connect();

			// Oppretter et temperaturtema på megleren
			displayClient.createTopic(Common.TEMPTOPIC);

			// Abonnerer på temperaturtemaet
			displayClient.subscribe(Common.TEMPTOPIC);

			// Starter displayenheten for å motta temperaturdata
			startDisplay(displayClient);

			// Avslutter abonnement og kobler fra megleren
			displayClient.unsubscribe(Common.TEMPTOPIC);
			displayClient.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("Display stopping ... ");
	}

	public static void startDisplay(Client client) {
		if (client != null) {
			try {
				// Logikk for å motta temperaturdata fra sensoren
				for (int i = 0; i < COUNT; i++) {
					Message message = client.receive();
					if (message instanceof PublishMsg) {
						PublishMsg publishMsg = (PublishMsg) message;
						System.out.println("Received temperature: " + publishMsg.getMessage());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

