/*
 * Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.ros.android.android_can_dynamix;

import com.google.common.collect.Lists;

import android.util.Log;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import org.ros.address.InetAddressFactory;
import org.ros.android.BitmapFromCompressedImage;
import org.ros.android.RosActivity;
import org.ros.android.view.RosImageView;
import org.ros.android.view.VirtualJoystickView;
import org.ros.android.view.visualization.VisualizationView;
import org.ros.android.view.visualization.layer.CameraControlLayer;
import org.ros.android.view.visualization.layer.LaserScanLayer;
import org.ros.android.view.visualization.layer.Layer;
import org.ros.android.view.visualization.layer.OccupancyGridLayer;
import org.ros.android.view.visualization.layer.PathLayer;
import org.ros.android.view.visualization.layer.PosePublisherLayer;
import org.ros.android.view.visualization.layer.PoseSubscriberLayer;
import org.ros.android.view.visualization.layer.RobotLayer;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.*;
import sensor_msgs.CompressedImage;
// import std_msgs.String;

import org.ros.android.android_can_dynamix.Gostop;
import org.ros.node.topic.Publisher;


public class MainActivity extends RosActivity {

  private VirtualJoystickView virtualJoystickView;
  private  SystemCommands systemCommands;

  public Gostop gostop;
    public String t_msg = "stop";
    public String t_name = "can_dynamix/control";
  private static final String DEBUG_TAG = "Button Pushed = ";
  public MainActivity() {
    super("CanDynamix", "CanDynamix");
    systemCommands = new SystemCommands();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.settings_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.virtual_joystick_snap:
        if (!item.isChecked()) {
          item.setChecked(true);
          virtualJoystickView.EnableSnapping();
        } else {
          item.setChecked(false);
          virtualJoystickView.DisableSnapping();
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);




    virtualJoystickView = (VirtualJoystickView) findViewById(R.id.virtual_joystick);


    OnClickListener imageButtonGreenClickListener = new OnClickListener()
    {
      @Override
      public void onClick(View v) {
        t_msg = "stop";
        systemCommands.start();
        gostop = new Gostop("can_dynamix/control", "stop1");
        Log.i (DEBUG_TAG," green");
      }
    };
    ImageButton imageButtonGreen = (ImageButton)findViewById(R.id.imageButtonGreen);
    imageButtonGreen.setOnClickListener(imageButtonGreenClickListener);

    final OnClickListener imageButtonRedClickListener = new OnClickListener()
    {
      @Override
      public void onClick(View v) {
          t_msg = "start";
        systemCommands.stop();
        gostop = new Gostop("can_dynamix/control", "start");
        Log.i (DEBUG_TAG," blue");
      }
    };
    ImageButton imageButtonRed = (ImageButton)findViewById(R.id.imageButtonRed);
    imageButtonRed.setOnClickListener(imageButtonRedClickListener);
  }


  @Override
  protected void init(NodeMainExecutor nodeMainExecutor) {
    gostop = new Gostop(t_name, t_msg);

    NodeConfiguration nodeConfiguration = NodeConfiguration.newPublic(getRosHostname());
    nodeConfiguration.setMasterUri(getMasterUri());

//    NodeConfiguration nodeConfiguration =
//        NodeConfiguration.newPublic(InetAddressFactory.newNonLoopback().getHostAddress(),
//            getMasterUri());

    nodeMainExecutor.execute(gostop, nodeConfiguration);
    nodeMainExecutor.execute(systemCommands, nodeConfiguration);
    nodeMainExecutor.execute(virtualJoystickView, nodeConfiguration.setNodeName(""));


  }
}
