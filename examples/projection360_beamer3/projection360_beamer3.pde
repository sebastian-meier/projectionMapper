import circus.*;
import oscP5.*;
import netP5.*;
import processing.opengl.*;
import proxml.*;
import controlP5.*;

OscCloud cloud;
ProjectionMapper mapper;

//connections[settingsNumber][shapeNumber][0]->belongs to settingsNumber
//connections[settingsNumber][shapeNumber][1]->shapeNumber (if shape doesn't originaly belongs to the current settingsNumber this differs from shapeNumber
int connections[][][] = new int[4][][];

int ballPosition[] = new int[2];
int ballSpeed[] = new int[2];
int ballShape = 0;
boolean ballPositioned = false;

int currentSettings = 2;
int numberOfShapes = 4; //0:3, 1:6, 2:4, 3:3
int highlightShape = 0;
int highlightValue = 0;

void setup(){
  size(1280, 960, P3D);
  cloud = new OscCloud(this, "child", "192.168.1.36", 1340, "192.168.1.36", 1337);
  mapper = new ProjectionMapper(this, "projection", currentSettings);

  //Initializing the connections !!!don't change them
  connections[0] = new int[13][2];
  connections[0][0][0] = 0;		connections[0][0][1] = 0;
  connections[0][1][0] = 0;		connections[0][1][1] = 1;
  connections[0][2][0] = 0;		connections[0][2][1] = 2;
  connections[0][3][0] = 1;		connections[0][3][1] = 0;
  connections[0][4][0] = 1;		connections[0][4][1] = 1;
  connections[0][5][0] = 1;		connections[0][5][1] = 2;
  connections[0][6][0] = 1;		connections[0][6][1] = 3;
  connections[0][7][0] = 1;		connections[0][7][1] = 4;
  connections[0][8][0] = 1;		connections[0][8][1] = 5;
  connections[0][9][0] = 2;		connections[0][9][1] = 0;
  connections[0][10][0] = 2;		connections[0][10][1] = 1;
  connections[0][11][0] = 2;		connections[0][11][1] = 2;
  connections[0][12][0] = 2;		connections[0][12][1] = 3;
		
  connections[1] = new int[12][2];
  connections[1][0][0] = 1;		connections[1][0][1] = 0;
  connections[1][1][0] = 1;		connections[1][1][1] = 1;
  connections[1][2][0] = 1;		connections[1][2][1] = 2;
  connections[1][3][0] = 1;		connections[1][3][1] = 3;
  connections[1][4][0] = 1;		connections[1][4][1] = 4;
  connections[1][5][0] = 1;		connections[1][5][1] = 5;
  connections[1][6][0] = 3;		connections[1][6][1] = 1;
  connections[1][7][0] = 3;		connections[1][7][1] = 2;
  connections[1][8][0] = 2;		connections[1][8][1] = 3;
  connections[1][9][0] = 0;		connections[1][9][1] = 0;
  connections[1][10][0] = 0;		connections[1][10][1] = 1;
  connections[1][11][0] = 0;		connections[1][11][1] = 2;
		
  connections[2] = new int[11][2];
  connections[2][0][0] = 2;		connections[2][0][1] = 0;
  connections[2][1][0] = 2;		connections[2][1][1] = 1;
  connections[2][2][0] = 2;		connections[2][2][1] = 2;
  connections[2][3][0] = 2;		connections[2][3][1] = 3;
  connections[2][4][0] = 0;		connections[2][4][1] = 0;
  connections[2][5][0] = 0;		connections[2][5][1] = 1;
  connections[2][6][0] = 0;		connections[2][6][1] = 2;
  connections[2][7][0] = 3;		connections[2][7][1] = 0;
  connections[2][8][0] = 3;		connections[2][8][1] = 1;
  connections[2][9][0] = 3;		connections[2][9][1] = 2;
  connections[2][10][0] = 3;		connections[2][10][1] = 5;
		
  connections[3] = new int[9][2];
  connections[3][0][0] = 3;		connections[3][0][1] = 0;
  connections[3][1][0] = 3;		connections[3][1][1] = 1;
  connections[3][2][0] = 3;		connections[3][2][1] = 2;
  connections[3][3][0] = 2;		connections[3][3][1] = 0;
  connections[3][4][0] = 2;		connections[3][4][1] = 3;
  connections[3][5][0] = 1;		connections[3][5][1] = 0;
  connections[3][6][0] = 1;		connections[3][6][1] = 1;
  connections[3][7][0] = 1;		connections[3][7][1] = 2;
  connections[3][8][0] = 1;		connections[3][8][1] = 5;
}

void draw(){
  if(mapper.ready && !mapper.config){
    if(!ballPositioned){
      repositionBall();
    }
    for(int i=0; i<numberOfShapes; i++){
      if(i==highlightShape){
        fill(255-highlightValue);
        if(highlightValue>0){
          highlightValue-=10;
        }else{
          hightlightValue = 0;
        }
      }else{
        fill(255);
      }
      noStroke();
      rect(mapper.shapeMinX(i), mapper.shapeMinY(i), mapper.shapeWidth(i), mapper.shapeHeight(i));
      if(i==ballShape){
        if(mapper.shapeIsInside(i, ballPosition[0]+ballSpeed[0], ballPosition[1]+ballSpeed[1])){
          fill(0);
          ballPosition[0] += ballSpeed[0];
          ballPosition[1] += ballSpeed[1];
          ellipse(ballPosition[0], ballPosition[1], 10, 10);
        }else{
          int crosspoint[] = mapper.shapeCrossPointFromPoints(i, ballPosition[0]-(ballSpeed[0]*2), ballPosition[1]-(ballSpeed[1]*2), ballPosition[0], ballPosition[1]);
          int neighbourSide[] = mapper.shapeGetNeighbourSide(i, crosspoint[0], crosspoint[1]);
          if(neighbourSide[0]==99999){
            ballPositioned = false;
          }else{
            int neighbour[] = mapper.shapeCrossPointNeighbour(0, neighbourSide[1], neighbourSide[2], neighbourSide[0]);
            if(connections[currentSettings][neighbourSide[0]][0] == currentSettings){
              ballShape = connections[currentSettings][neighbourSide[0]][1];
              ballPosition[0] = neighbour[0];
              ballPosition[1] = neighbour[1];
            }else{
              cloud.send("cornerTouch", connections[currentSettings][neighbourSide[0]][0]+","+connections[currentSettings][neighbourSide[0]][1]);
              ballPositioned = false;
            }
          }
        }
      }        
      mapper.textures[i] = get();
      background(0);
    }
  }
}

void repositionBall(){
  boolean isinside = false;
  while(!isinside){
    for(int i=(int) random(0, (numberOfShapes-1)); (i<numberOfShapes)&&(!isinside); i++){
      int tx = (int) random(mapper.shapeMinX(i), mapper.shapeMaxX(i));
      int ty = (int) random(mapper.shapeMinY(i), mapper.shapeMaxY(i));
      if(mapper.shapeIsInside(i, tx, ty)){
        isinside = true;
        ballShape = i;
        ballPosition[0] = tx;
        ballPosition[1] = ty;
        ballSpeed[0] = (int) random(-10, 10);
        ballSpeed[1] = (int) random(-10, 10);
      }
    }
  }
  ballPositioned = true;
}

void oscEvent(OscMessage theOscMessage) {
  cloud.oscEvent(theOscMessage);
}

void controlEvent(ControlEvent theEvent) {
  mapper.controlEvent(theEvent);
}

public void incomingMessage(String message, String content){
  if(message.contains("cornerTouch")){
    String values[] = content.split(",");
    if((Integer.parseInt(values[0]) == currentSettings)){
      highlightShape = Integer.parseInt(values[1]);
      highlightValue = 255;
    }
  }
}

