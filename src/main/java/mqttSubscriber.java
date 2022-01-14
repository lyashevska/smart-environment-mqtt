import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * @author olga
 * @date 14/01/2022
 * client 2
 */
public class mqttSubscriber {

    public static void main(String[] args) {

        // Declare broker/clientId
        String broker = "tcp://localhost:1883";
        String clientId = "Subscriber";

        // Create MemoryPersistence object
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            // Create client and connection options objects
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();

            connOpts.setKeepAliveInterval(5000);
            // if cleanSession is true before connecting the client,
            // then all pending publication deliveries for the client are removed
            // when the client connects.

            connOpts.setCleanSession(true);

            // Set client callback to be our SampleSubscriber where we will get the print
            // out
            sampleClient.setCallback(new SampleSubscriber());

            System.out.println("Connecting to broker: " + broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            // subscribe to topic
            // a) strictly messages send to a specific subtopic (for example office->staff)
            sampleClient.subscribe("/office/staff/admin");

            // b) any messages that are related to a topic and consequently to its subtopics
            // # wildcard for every subtopic at that level
            // we expect light, windows subtopic to show
            sampleClient.subscribe("/office/#");

            // c) messages that are related to a subtopic that is common for all topics
            // pick up staff subtopic
            sampleClient.subscribe("/+/staff/#");

        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}
