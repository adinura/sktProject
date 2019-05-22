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
def on_connect(client, userdata, flags, rc): #callback saat berhasil connect
    print("connected to broker")

def on_message(client, obj, msg): #callback saat menerima pesan
    if str(msg.topic) == topic: #terima pesan yg dikirim client (W1)
        print("data dikirim ke server primary :", msg.payload.decode('utf-8'))
        mqttc.publish(datakirim, msg.payload) #kirim data yang diterima ke primary server (W2)
    elif str(msg.topic) == updatesaldo: #terima data saldo yang telah diupdate oleh primary server (w3)
        data = int(msg.payload)
        global saldo
        saldo = data
        print("saldo akhir: ", saldo)
    elif str(msg.topic) == topic2: #terima data history transaksi yang telah diupdate oleh primary server (w3)
        historyTransaksi = msg.payload
        print("history transaksi: " , historyTransaksi.decode('utf-8'))
        mqttc.publish(ack, "1") #kirim ack update ke primary server (W4)
    
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