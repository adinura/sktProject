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
def on_connect(client, userdata, flags, rc): #callback saat berhasil connect
    print("connected to broker")

def on_message(client, obj, msg):
    if str(msg.topic) == topic: #terima data yg akan di tulis dari server1
        global saldoTotal
        nominal = (int(msg.payload))
        saldoTotal = saldoTotal + nominal #update nilai saldo
        print("saldo total: ", saldoTotal)
        mqttc.publish(updateSaldo, saldoTotal) #kirim saldo yang telah diupdate (W3)
        List.append(nominal) #masukkan data yang masuk ke list untuk membuat history transaksi
        str2 = ', '.join(str(e) for e in List) #ubah list ke string
        mqttc.publish(updateHistory, str2) #kirim history transaksi yang telah diupdate (W3)
        print("history transaksi: ", str2)
    elif str(msg.topic) == ack1: #terima ack dari server1 (W4)
        print("server1 done updating")
    elif str(msg.topic) == ack2: #terima ack dari server2 (W4)
        print("server2 done updating")
    
def on_log(client, obj, level, string):
    print(string)

#connect ke broker mqtt
mqttc.on_message = on_message
mqttc.on_connect = on_connect
mqttc.username_pw_set("igsvtitm" , "Lmci4fvTksP9" )
mqttc.connect( "postman.cloudmqtt.com", 18112 )

#subscribe ke semua topic
mqttc.subscribe('#', 0)

#looping client mqtt
rc = 0
while rc == 0:
    rc = mqttc.loop()
