/**
 * An app that can be used to control a remote robot. This app also demonstrates
 * how to use some of views from the rosjava android library.
 * 
 * @author munjaldesai@google.com (Munjal Desai)
 * @author moesenle@google.com (Lorenz Moesenlechner)
 */


package org.ros.android.android_can_dynamix;

import org.ros.concurrent.CancellableLoop;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Publisher;


public class Gostop extends AbstractNodeMain {
  public String topic_name;
  public String topic_msg;
/*
  public Gostop() {
    topic_name = "can_dynamix/control";
    topic_msg = "stop";
  }
*/
  public Gostop(String topic, String msg )
  {
    topic_name = topic;
    topic_msg = msg;
  }

  @Override
  public GraphName getDefaultNodeName() {
    return GraphName.of("can_dynamix");
  }

//  public Topic_MSG topic_msg;

  @Override
  public void onStart(final ConnectedNode connectedNode) {
    final Publisher<std_msgs.String> publisher =
            connectedNode.newPublisher(topic_name, std_msgs.String._TYPE);
    // This CancellableLoop will be canceled automatically when the node shuts
    // down.
    connectedNode.executeCancellableLoop(new CancellableLoop() {
      private int sequenceNumber;

     @Override
      protected void setup() {
        sequenceNumber = 0;
      }

      @Override
      protected void loop() throws InterruptedException {
        std_msgs.String str = publisher.newMessage();
        str.setData(topic_msg);
        publisher.publish(str);
        sequenceNumber++;
        Thread.sleep(1000);
      }
    });
  }
}
