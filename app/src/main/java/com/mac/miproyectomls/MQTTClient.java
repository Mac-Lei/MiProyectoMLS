package com.mac.miproyectomls;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttCallback;

public class MQTTClient {
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    private static final String SERVER_URI = "tcp://broker.hivemq.com:1883";  // Broker MQTT
    private static final String CLIENT_ID = "AndroidClient";  // ID único para el cliente
    private static final String TOPIC = "supermercado/productos";  // Tema al que te suscribes

    public MQTTClient() {
        try {
            // Crear un nuevo cliente MQTT
            mqttClient = new MqttClient(SERVER_URI, CLIENT_ID, null);
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);
            mqttClient.connect(mqttConnectOptions);

            // Establecer un callback para manejar los mensajes recibidos
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    // Manejo de pérdida de conexión
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Recibir mensajes del broker
                    String msg = new String(message.getPayload());
                    // Aquí podrías actualizar la interfaz de usuario o realizar acciones
                    System.out.println("Mensaje recibido: " + msg);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Manejo cuando un mensaje se entrega
                }
            });

            // Suscribirse al tema de productos
            mqttClient.subscribe(TOPIC);

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Método para publicar un mensaje en el tema
    public void publishMessage(String messageContent) {
        try {
            MqttMessage message = new MqttMessage(messageContent.getBytes());
            mqttClient.publish(TOPIC, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Método para desconectar el cliente
    public void disconnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
