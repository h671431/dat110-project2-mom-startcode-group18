package no.hvl.dat110.iotsystem;

import no.hvl.dat110.client.Client;
import no.hvl.dat110.common.TODO;

public class TemperatureDevice {

	private static final int COUNT = 10;

	public static void main(String[] args) {

		// simulated / virtual temperature sensor
		TemperatureSensor sn = new TemperatureSensor();

		Client sensorClient = new Client("temperatureSensor", Common.BROKERHOST, Common.BROKERPORT);

		System.out.println("Temperature device starting ... ");


		if(sensorClient.connect()) {
			for (int i = 0; i < COUNT; i++) {
				int temperature = sn.read(); // Les temperatur fra sensoren
				sensorClient.publish(Common.TEMPTOPIC, Integer.toString(temperature)); // Publiser temperatur til megleren
				try {
					Thread.sleep(5000); // Vent i 5 sekunder mellom hver avlesning
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			sensorClient.disconnect(); // Koble fra megleren når alle temperaturer er publisert
		}

		System.out.println("Temperature device stopping ... ");



	}
}
