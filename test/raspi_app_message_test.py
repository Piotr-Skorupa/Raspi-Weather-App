import base64
import os
import random
import time
import _thread
import pytest

import paho.mqtt.client as paho

print('Testing getting messages by RaspiWeatherApp')
print('press "ctrl + c" to stop')

host = '213.222.211.83'
port = 1883
temperature = 'SENSORS/TEMPERATURE'
pressure = 'SENSORS/PRESSURE'
humidity = 'SENSORS/HUMIDITY'
camera = 'SENSORS/CAMERA_PIC'
tested_camera_off = 'SENSORS/CAMERA_ON_OFF'

def on_publish(client, userdata, result):
    print('publishing: ' + str(result))
    pass

def on_connect(client, userdata, flags, rc):
    client.subscribe(tested_camera_off)

def on_subscribe(client, userdata, mid, granted_qos):
    print('correctly subscribed')

def on_message(client, userdata, message):
    print(str(message.payload.decode('UTF-8')))
    if (str(message.payload.decode('UTF-8')) == 'OFF'):
        client.startflag = False

def image_to_base64(path_to_file):
    encoded_string = ''
    with open(path_to_file, 'rb') as image_file:
        encoded_string = base64.b64encode(image_file.read())
    return encoded_string

def subscribe_camera(client_sub):
    client_sub.on_connect = on_connect
    client_sub.on_subscribe = on_subscribe
    client_sub.on_message = on_message
    client_sub.connect(host, port)
    client_sub.loop_forever()

def simulate_sensors():
    client = paho.Client('test_client')
    client_sub = paho.Client('test_client_for_subscribe')
    client_sub.startflag = True
    client.on_publish = on_publish

    try:
        client.connect(host, port)
        _thread.start_new_thread(subscribe_camera, (client_sub,))

        i = 0
        while (client_sub.startflag == True):
            images = []
            images.append(image_to_base64('1.jpg'))
            images.append(image_to_base64('2.jpg'))
            images.append(image_to_base64('3.jpg'))
            messages = {
                'temp': random.uniform(-10.0, 33.5),
                'press': random.uniform(998.0, 1060.0),
                'hum': random.uniform(20.0, 85.5)
            }

            result = client.publish(temperature, str(messages['temp']))
            result = client.publish(pressure, str(messages['press']))
            result = client.publish(humidity, str(messages['hum']))
            result = client.publish(camera, images[i])
            i += 1
            if i == 3:
                i = 0
            time.sleep(1)
    except:
        print('Can not connect to server')
        return 0

    return 1

@pytest.mark.timeout(30)
def test_camera_off():
    result = simulate_sensors()
    assert result == 1
