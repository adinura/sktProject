package com.example.kasir;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class Helper_IoT {

    public MqttAndroidClient mqttAndroidClient;
    Context kontek;

    final String serverUri = "tcp://postman.cloudmqtt.com:18112";

    final String clientId = "211cf9f6-a9a1-4e85-99db-ce78986b481a";
    final String subscriptionTopic = "/clientHistory";
    final String subscriptionTopic2 = "/clientSaldo";
    final String publishTopic = "/kasirkirim";
    final String publishTopic2 = "/req";

    final String username = "igsvtitm";
    final String password = "Lmci4fvTksP9";

    public Helper_IoT(final Context context){
        kontek = context;
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("mqtt = s = ", s);
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Mqtt = mqttHelper = ", mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        connect();
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }




    private void connect(){
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());


        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    Toast.makeText(kontek, "Berhasil Connect !", Toast.LENGTH_SHORT).show();
                    subscribeToTopic();
                    subscribeToTopic2();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Failed to connect to: " + serverUri + exception.toString());
                    exception.printStackTrace();
                }
            });


        } catch (MqttException ex){
            ex.printStackTrace();

        }
    }


    public void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exceptionst subscribing");
            ex.printStackTrace();
        }
    }

    public void subscribeToTopic2() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic2, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt","Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Subscribed fail!");
                }
            });

        } catch (MqttException ex) {
            System.err.println("Exceptionst subscribing");
            ex.printStackTrace();
        }
    }



    public void mqttPublish(String payload) {

        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            IMqttDeliveryToken published = mqttAndroidClient.publish(publishTopic,message);
            System.out.println("publishing... "+published.getMessage());
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
            System.out.println("publish error : "+ e.getMessage());
        }
    }

    public void mqttPublish2(String payload) {

        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            IMqttDeliveryToken published = mqttAndroidClient.publish(publishTopic2,message);
            System.out.println("publishing... "+published.getMessage());
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
            System.out.println("publish error : "+ e.getMessage());
        }
    }



}

