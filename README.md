# smart-environment-mqtt

MQTT Smart Office using [Eclipse Mosquitto](https://mosquitto.org/) message broker. Eclipse Paho is used to send subscribe and publish messages to the Mosquitto broker.

Examples of the following operations are implemented:

* messages send strictly to a specific subtopic (for example topic -> subtopic)
* any messages that are related to a topic and consequently to its subtopics
* messages that are related to a subtopic that is common for all topics
* durable subscriptions
