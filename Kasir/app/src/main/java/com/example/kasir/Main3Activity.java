package com.example.kasir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main3Activity extends AppCompatActivity {

    TextView saldo;
    Button request;
    TextView history;
    Helper_IoT helper_iot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        saldo = findViewById(R.id.textView6);
        history = findViewById(R.id.textView9);
        request = findViewById(R.id.button3);
        helper_iot = new Helper_IoT(getApplicationContext());

        helper_iot.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

            }

            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String topic2 = ""+topic;
                if (topic2.equals("/clientSaldo")){
                    String list_payload = ""+message;
                    saldo.setText(list_payload);
                    Toast.makeText(getApplicationContext(),"Data diterima", Toast.LENGTH_SHORT).show();
                }
                else if(topic2.equals("/clientHistory")){
                    String printHistory = "";
                    String list_payload = ""+message;

                    for (int i = 0; i<list_payload.length(); i++){
                        if(list_payload.charAt(i)==' '){

                        }else if(list_payload.charAt(i)==','){
                            printHistory += "\n";
                        }else{
                            printHistory += list_payload.charAt(i);
                        }
                    }
                    history.setText(printHistory);
                    Toast.makeText(getApplicationContext(),"Data diterima", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper_iot.mqttPublish2("1");
                Toast.makeText(getApplicationContext(),"Meminta Data ke Server", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
