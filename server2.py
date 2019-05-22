import os, urllib.parse
import paho.mqtt.client as mqtt

mqttc = mqtt.Client()
saldo = 0
history = '/cekupdate'
ack = '/ackServ2'
reqData = '/req'
updatesaldo = '/saldo'
historyClient = '/clientHistory'
saldoClient = '/clientSaldo'
historyTransaksi = ""

# Define event callbacks
def on_connect(client, userdata, flags, rc):
    print("connected to broker")

def on_message(client, obj, msg):
    if str(msg.topic) == reqData:
        global historyTransaksi
        global saldo
        print("data dikirim ke client")
        mqttc.publish(historyClient, historyTransaksi)
        mqttc.publish(saldoClient, saldo)
    elif str(msg.topic) == updatesaldo:
        data = int(msg.payload)
        saldo = data
        print("saldo akhir: ", saldo)
    elif str(msg.topic) == history:
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