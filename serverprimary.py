import os, urllib.parse
import paho.mqtt.client as mqtt

mqttc = mqtt.Client()
topic = '/kirimprimary'
updateHistory = '/cekupdate'
updateSaldo = '/saldo'
ack1 = '/ackServ1'
ack2 = '/ackServ2'

List = []
saldoTotal = 0

# Define event callbacks
def on_connect(client, userdata, flags, rc):
    print("connected to broker")

def on_message(client, obj, msg):
    if str(msg.topic) == topic:
        global saldoTotal
        nominal = (int(msg.payload))
        saldoTotal = saldoTotal + nominal
        print("saldo total: ", saldoTotal)
        mqttc.publish(updateSaldo, saldoTotal)
        List.append(nominal)
        str2 = ', '.join(str(e) for e in List)
        mqttc.publish(updateHistory, str2)
        print("history transaksi: ", str2)
    elif str(msg.topic) == ack1:
        print("server1 done updating")
    elif str(msg.topic) == ack2:
        print("server2 done updating")
    
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
