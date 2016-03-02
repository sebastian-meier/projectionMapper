import circus.*;
import oscP5.*;
import netP5.*;

OscCloud cloud;

void setup(){
  size(200, 200);
  cloud = new OscCloud(this, "parent", "10.254.255.205", 1337);
  cloud.maxTime = 1000;
  frameRate = 30;
  //cloud.behaviour = "upanddown";
}

void draw(){
  background(255);
  fill(0);
  text(cloud.time, 10, 20);
}

void mousePressed(){
  cloud.running = true;
}

void oscEvent(OscMessage theOscMessage) {
  cloud.oscEvent(theOscMessage);
  println(theOscMessage);
}

public void incomingMessage(String message, String content){
  println("message:"+message);
  println("content:"+content);
}

