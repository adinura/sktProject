package com.example.kasir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class Main2Activity extends AppCompatActivity {

    EditText nominal;
    Button kirim;
    TextView tes;
    Helper_IoT helper_iot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        nominal = findViewById(R.id.editText);
        kirim = findViewById(R.id.button4);
        tes=findViewById(R.id.textView);

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

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payload = nominal.getText().toString();
                helper_iot.mqttPublish(payload);
                Toast.makeText(getApplicationContext(),"Data Terkirim", Toast.LENGTH_SHORT).show();
                nominal.setText("");
            }
        });
    }


}
