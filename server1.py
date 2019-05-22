import os, urllib.parse
import paho.mqtt.client as mqtt

saldo = 0
mqttc = mqtt.Client()
topic = '/kasirkirim'
topic2 = '/cekupdate'
ack = '/ackServ1'
updatesaldo = '/saldo'
datakirim = '/kirimprimary'
historyTransaksi = ""

# Define event callbacks
def on_connect(client, userdata, flags, rc):
    print("connected to broker")

def on_message(client, obj, msg):
    if str(msg.topic) == topic:
        print("data dikirim ke server primary :", msg.payload.decode('utf-8'))
        mqttc.publish(datakirim, msg.payload)
    elif str(msg.topic) == updatesaldo:
        data = int(msg.payload)
        global saldo
        saldo = data
        print("saldo akhir: ", saldo)
    elif str(msg.topic) == topic2:
        historyTransaksi = msg.payload
        print("history transaksi: " , historyTransaksi.decode('utf-8'))
        mqttc.publish(ack, "1")
    
def on_log(client, obj, level, string):
    print(string)

mqttc.on_message = on_message
mqttc.on_connect = on_connect
mqttc.username_pw_set("igsvtitm" , "Lmci4fvTksP9" )
mqttc.connect( "postman.cloudmqtt.com", 18112 )
mqttc.subscribe('#', 0)

rc = 0
while rc == 0:
    rc = mqttc.loop()