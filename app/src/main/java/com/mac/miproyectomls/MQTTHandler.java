package com.mac.miproyectomls;

import org.eclipse.paho.client.mqttv3.*;

public class MQTTHandler {
    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    private static final String SERVER_URI = "tcp://broker.emqx.io:1883"; // Dirección del broker MQTT
    private static final String CLIENT_ID = "AndroidClient";                // ID único del cliente
    private static final String DEFAULT_TOPIC = "supermercado/productos";   // Tema predeterminado

    private MQTTHandlerCallback callback;

    // Interfaz para manejar eventos personalizados
    public interface MQTTHandlerCallback {
        void onMessageReceived(String topic, String message);
        void onConnectionLost(Throwable cause);
        void onDeliveryComplete(IMqttDeliveryToken token);
    }

    public MQTTHandler(MQTTHandlerCallback callback) {
        this.callback = callback;
        initMQTTClient();
    }

    private void initMQTTClient() {
        try {
            mqttClient = new MqttClient(SERVER_URI, CLIENT_ID, null);
            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    if (callback != null) {
                        callback.onConnectionLost(cause);
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    if (callback != null) {
                        String msg = new String(message.getPayload());
                        callback.onMessageReceived(topic, msg);
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    if (callback != null) {
                        callback.onDeliveryComplete(token);
                    }
                }
            });

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void connect() {
        try {
            if (mqttClient != null && !mqttClient.isConnected()) {
                mqttClient.connect(mqttConnectOptions);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.subscribe(topic != null ? topic : DEFAULT_TOPIC);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publishMessage(String topic, String messageContent) {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                MqttMessage message = new MqttMessage(messageContent.getBytes());
                mqttClient.publish(topic != null ? topic : DEFAULT_TOPIC, message);
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
