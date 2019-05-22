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
def on_connect(client, userdata, flags, rc): #callback saat berhasil connect
    print("connected to broker")

def on_message(client, obj, msg): #callback saat menerima pesan
    if str(msg.topic) == reqData: #terima permintaan untuk kirim data dari client (R1)
        global historyTransaksi
        global saldo
        print("data dikirim ke client")
        mqttc.publish(historyClient, historyTransaksi) #kirim history transaksi ke client (R2)
        mqttc.publish(saldoClient, saldo) #kirim saldo ke client (R2)
    elif str(msg.topic) == updatesaldo: #terima data saldo yang telah diupdate oleh primary server (w3)
        data = int(msg.payload)
        saldo = data
        print("saldo akhir: ", saldo)
    elif str(msg.topic) == history: #terima data history transaksi yang telah diupdate oleh primary server (w3)
        historyTransaksi = msg.payload
        print("history transaksi: " , historyTransaksi.decode('utf-8'))
        mqttc.publish(ack, "1") #kirim ack update ke primary server (w4)
        
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