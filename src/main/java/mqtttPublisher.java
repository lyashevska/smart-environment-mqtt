import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author olga
 * client 1
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

            // if cleanSession is true before connecting the client,
            // then all pending publication deliveries for the client are removed
            // when the client connects.
            // cleanSession = true --> durable connection
            connOpts.setCleanSession(true);

            // set keep alive interval
            // after 180 sec the client is dead
            connOpts.setKeepAliveInterval(180);

            System.out.println("Connecting to broker: " + broker);
            // Connects to an MQTT server using the specified options
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            // sending messages
            publishMessage("/house/power/switches", "on", 2, false);
            publishMessage("/house/water", "off", 1, false);
            publishMessage("/house", "this house is great", 0, true);
            publishMessage("/garden/water", "on", 1, false);


            //disconnect
            sampleClient.disconnect();
            System.out.println("Disconnected");

            // Close
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
