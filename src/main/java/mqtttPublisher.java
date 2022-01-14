import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author olga
 * @date 14/01/2022
 * Question 1.
 * By using the MQTT protocol implement the Publisher/Subscriber parts for a smart
 * environment. Where a publisher announces messages that are organized in different topics
 * and subtopics, such as office/printer, office/lights, banking/balance, banking/exchange/eur
 * etc. In turn, the subscriber listens for messages on different topics as follows.
 * <p>
 * a) strictly messages send to a specific subtopic (for example topic->subtopic)
 * b) any messages that are related to a topic and consequently to its subtopics
 * c) messages that are related to a subtopic that is common for all topics
 * d) Finally, show and explain how you can implement durable subscriptions?
 */
public class mqtttPublisher {

    // Declare client global (outside of main) as used in multiple methods
    private static MqttClient sampleClient;

    public static void main(String[] args) {

        // Declare broker/clientId
        String broker = "tcp://localhost:1883"; // mosquitto broker port default
        String clientId = "Publisher"; // can be anything

        // Create MemoryPersistence object
        MemoryPersistence persistence = new MemoryPersistence();

        try {

            // Initialise client and connection options objects
            sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();


            // d) show and explain how you can implement durable subscriptions
            // cleanSession = true --> not durable connection
            // false --> will not clean the session
            connOpts.setCleanSession(false);

            // after 180 sec the client is dead
            connOpts.setKeepAliveInterval(180);

            System.out.println("Connecting to broker: " + broker);
            // Connects to an MQTT server using the specified options
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            // sending messages

            // a) strictly messages send to a specific subtopic (for example office->staff)
            publishMessage("/office/staff/admin", "There are 5 people in the room", 1, false);

            // b) any messages that are related to a topic and consequently to its subtopics
            publishMessage("/office", "Welcome!", 1, false);
            publishMessage("/office/lights", "on", 1, false);
            publishMessage("/office/windows", "on", 1, false);

            // c) messages that are related to a subtopic that is common for all topics
            publishMessage("/lab/staff", "There are 15 people in the room", 1, false);
            publishMessage("/office/staff", "There are 25 people in the room", 1, false);

            //disconnect
            sampleClient.disconnect();
            System.out.println("Disconnected");

            // close
            sampleClient.close();
            System.exit(0);

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc-msg " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("exception " + me);
            me.printStackTrace();
        }

    }

    // payload is content
    // retain flag tells the server to store the message and its QoS level so it can be sent to a future subscriber for the smae topic
    // in which it was originally published

    private static void publishMessage(String topic, String payload, int qos, boolean retained) {

        System.out.println("Publishing message: " + payload + " on topic " + topic);

        // Create message payload to be published, setting retained and qos
        MqttMessage message = new MqttMessage(payload.getBytes());

        message.setRetained(retained);
        message.setQos(qos);

        try {
            // publish message to topic
            sampleClient.publish(topic, message);

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc-msg " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("exception " + me);
            me.printStackTrace();
        }

        System.out.println("Message published");

    }

}
