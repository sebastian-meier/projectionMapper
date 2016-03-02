package circus;

import java.awt.geom.Point2D;

import processing.core.PApplet;
import processing.core.PFont;
import controlP5.Button;
import controlP5.ControlEvent;
import controlP5.ControlFont;
import controlP5.ControlP5;
import controlP5.MultiList;
import controlP5.MultiListButton;
import controlP5.Textfield;
import controlP5.Textlabel;

public class Configurator {

	ProjectionMapper parent;
	
	ControlP5 controlP5;
	MultiList l;
	Textlabel newLabel;
	Textfield newField;
	Button newButton;
	Button projectionButton;
	Button originalButton;
	Button anchorButton;
	Button zeroButton;
	Button zeroProjectionButton;
	MultiListButton b;
	MultiListButton bb;
	boolean anchorPoint = false;
	boolean zeroProjectionPoint = false;
	boolean zeroPoint = false;
	boolean enabled = false;
	boolean disabled = false;
	
	PFont font;
	ControlFont cfont;
	
	boolean mousePressed = false;
	int mouseX;
	int mouseY;
	boolean mouseHit = false;
	int mouseMatch = 0;
	
	public Configurator(ProjectionMapper p, PFont f){
		parent = p;
		font = f;
		cfont = new ControlFont(font, 10);
		controlP5 = new ControlP5(parent.parent);
	}
	
	public void enable(){
		enabled = true;
		//Choose settings
		newLabel = controlP5.addTextlabel("chooseLabel","Choose settings:",5,5);
		newLabel.setColorValue(parent.parent.color(0));
		l = controlP5.addMultiList("settings",0,15,101,12);
		b = l.add(parent.settings.name(parent.current),1);
		b.setColorBackground(parent.parent.color(0));
		b.setColorLabel(parent.parent.color(255));
		for(int i=0; i<parent.settings.length(); i++){
			bb = b.add("settings_"+(i+1),(i+1));
			bb.setLabel(parent.settings.name(i));
			if(i==parent.current){
				bb.setColorLabel(parent.parent.color(0));
				bb.setColorBackground(parent.parent.color(150));
				bb.setColorActive(parent.parent.color(150));
			}else{
				bb.setColorLabel(parent.parent.color(255));
				bb.setColorBackground(parent.parent.color(0));
			}
		}
		
		//Add settings
		newLabel = controlP5.addTextlabel("newLabel","Add settings:",5,50);
		newLabel.setColorValue(parent.parent.color(0));
		newField = controlP5.addTextfield("newField", 0, 60, 100, 20);
		newButton = controlP5.addButton("add", 1, 75, 84, 25, 12);
		
		//Delete settings
		newButton = controlP5.addButton("delete settings", 1, 25, 30, 75, 12);
		
		//Current shape
		newLabel = controlP5.addTextlabel("shapeLabel","Choose shape:",5,100);
		newLabel.setColorValue(parent.parent.color(0));
		l = controlP5.addMultiList("shapes",0,110,101,12);
		b = l.add("Shape #"+(parent.currentShape+1),1);
		b.setColorBackground(parent.parent.color(0));
		b.setColorLabel(parent.parent.color(255));
		ProjectionShape cShape = null;
		for(int i=0; i<parent.settings.shapeLength(parent.current); i++){
			bb = b.add("shape_"+(i+1),(i+1));
			bb.setLabel("Shape #"+(i+1));
			if(i==parent.currentShape){
				cShape = parent.settings.getShapes(parent.current)[i];
				bb.setColorLabel(parent.parent.color(0));
				bb.setColorBackground(parent.parent.color(150));
				bb.setColorActive(parent.parent.color(150));
			}else{
				bb.setColorLabel(parent.parent.color(255));
				bb.setColorBackground(parent.parent.color(0));
			}
		}
		newButton = controlP5.addButton("add shape", 1, 45, 125, 55, 12);
		newButton = controlP5.addButton("delete shape", 1, 35, 140, 65, 12);
		
		projectionButton = controlP5.addButton("original points", 1, 0, 180, 73, 12);
		if(parent.pointMode && !anchorPoint && !zeroPoint && !zeroProjectionPoint){
			projectionButton.setColorBackground(parent.parent.color(150));
			projectionButton.setColorActive(parent.parent.color(150));
		}
		
		originalButton = controlP5.addButton("projection points", 1, 0, 195, 85, 12);
		if(!parent.pointMode && !anchorPoint && !zeroPoint && !zeroProjectionPoint){
			originalButton.setColorBackground(parent.parent.color(150));
			originalButton.setColorActive(parent.parent.color(150));
		}
		
		anchorButton = controlP5.addButton("anchor point", 1, 0, 210, 63, 12);
		if(anchorPoint && !zeroPoint && !zeroProjectionPoint){
			anchorButton.setColorBackground(parent.parent.color(150));
			anchorButton.setColorActive(parent.parent.color(150));
		}
		
		zeroButton = controlP5.addButton("zero point", 1, 0, 225, 63, 12);
		if(zeroPoint){
			zeroButton.setColorBackground(parent.parent.color(150));
			zeroButton.setColorActive(parent.parent.color(150));
		}
		
		zeroProjectionButton = controlP5.addButton("zero projection point", 1, 0, 240, 100, 12);
		if(zeroProjectionPoint){
			zeroProjectionButton.setColorBackground(parent.parent.color(150));
			zeroProjectionButton.setColorActive(parent.parent.color(150));
		}
		
		controlP5.addButton("+ add point", 1, 0, 270, 60, 12);
		controlP5.addButton("- remove point", 1, 0, 285, 73, 12);
		
		Button activeButton = controlP5.addButton("active", 1, 0, 315, 40, 12);
		Button inactiveButton = controlP5.addButton("inactive", 1, 0, 330, 50, 12);
		
		if(cShape.active){
			activeButton.setColorBackground(parent.parent.color(150));
			activeButton.setColorActive(parent.parent.color(150));
		}else{
			inactiveButton.setColorBackground(parent.parent.color(150));
			inactiveButton.setColorActive(parent.parent.color(150));
		}
		


	}
	
	public void draw(){
		if(disabled){
			controlP5.remove("zero point");
			controlP5.remove("zero projection point");
			controlP5.remove("original points");
			controlP5.remove("projection points");
			controlP5.remove("+ add point");
			controlP5.remove("- remove point");
			controlP5.remove("anchor point");
			controlP5.remove("delete settings");

			controlP5.remove("settings");
			controlP5.remove("add shape");
			controlP5.remove("delete shape");
			controlP5.remove("shapes");
			controlP5.remove("newLabel");
			controlP5.remove("chooseLabel");
			controlP5.remove("shapeLabel");
			controlP5.remove("add");
			
			controlP5.remove("active");
			controlP5.remove("inactive");
			
			controlP5.remove("newField");
			disabled = false;
		}else if(!enabled){
			enable();
		}else if(enabled){
			controlP5.draw();
			parent.parent.stroke(0);
			parent.parent.line(200,0, 200, parent.height);
			parent.parent.translate(200,0);
			ProjectionShape shape;
			
			ProjectionShape rememberShape = null;
			
			for(int s=0; s<parent.settings.getShapes(parent.current).length; s++){
				shape = parent.settings.getShapes(parent.current)[s];
				if(parent.currentShape==s){
					rememberShape = shape;
				}else{
					drawShape(shape, false);
				}
			}
			drawShape(rememberShape, true);
			parent.parent.translate(-200,0);
		}
	}
	
	void drawShape(ProjectionShape shape, boolean current){
		parent.parent.translate(shape.x, shape.y);
		parent.parent.ellipseMode(PApplet.CENTER);
		if(current){
			if(parent.pointMode){
				//parent.parent.image(parent.textures[s], 0, 0);
			}
			parent.parent.fill(parent.parent.color(255,0,0));
			parent.parent.ellipse(0, 0, 10, 10);
		}

		Point2D[] p_one = new Point2D[1];
		Point2D[] p_two = new Point2D[1];

		if(parent.pointMode){
			p_one = shape.points;
			p_two = shape.projectionPoints;
		}else{
			p_two = shape.points;
			p_one = shape.projectionPoints;
		}

		if(current){
			parent.parent.fill(150);
			parent.parent.stroke(150);
		}else{
			parent.parent.fill(200);
			parent.parent.stroke(200);
		}
		for(int i=0; i<p_two.length; i++){
			parent.parent.ellipse((int)p_two[i].getX(), (int)p_two[i].getY(), 5, 5);
			if(current){
				parent.parent.line((int)p_two[i].getX(), (int)p_two[i].getY(), (int)p_one[i].getX(), (int)p_one[i].getY());
			}
			if(i==(p_two.length-1)){
				parent.parent.line((int)p_two[i].getX(), (int)p_two[i].getY(), (int)p_two[0].getX(), (int)p_two[0].getY());
			}else{
				parent.parent.line((int)p_two[i].getX(), (int)p_two[i].getY(), (int)p_two[i+1].getX(), (int)p_two[i+1].getY());
			}
		}
		
		if(current){
			parent.parent.fill(0);
			parent.parent.stroke(0);
		}else{
			parent.parent.fill(200);
			parent.parent.stroke(200);
		}
		
		for(int i=0; i<p_one.length; i++){
			parent.parent.ellipse((int)p_one[i].getX(), (int)p_one[i].getY(), 10, 10);
			if(i==(p_two.length-1)){
				parent.parent.line((int)p_one[i].getX(), (int)p_one[i].getY(), (int)p_one[0].getX(), (int)p_one[0].getY());
			}else{
				parent.parent.line((int)p_one[i].getX(), (int)p_one[i].getY(), (int)p_one[i+1].getX(), (int)p_one[i+1].getY());
			}
		}
		
		if(current){
			if(anchorPoint){
				parent.parent.fill(parent.parent.color(255,0,0));
				parent.parent.ellipse(0, 0, 10, 10);
			}else if(zeroPoint){
				parent.parent.fill(parent.parent.color(0,0,255));
				parent.parent.ellipse(shape.zx, shape.zy, 10, 10);
			}else if(zeroProjectionPoint){
				parent.parent.fill(parent.parent.color(0,0,255));
				parent.parent.ellipse(shape.pzx, shape.pzy, 10, 10);
			}
		}
		
		parent.parent.translate(-shape.x, -shape.y);
	}
	
	public void disable(){
		disabled = true;
	}
	
	public void controlEvent(ControlEvent theEvent){
		if(theEvent.controller().name().contentEquals("add")){
			parent.settings.addSettings(newField.getText());
			parent.settings.save();
			restart(parent.settings.length()-1, 0);
		}
		if(theEvent.controller().name().contentEquals("original points")){
			parent.pointMode = true;
			anchorPoint = false;
			zeroPoint = false;
			zeroProjectionPoint = false;
			restart(parent.current, parent.currentShape);
		}
		if(theEvent.controller().name().contentEquals("+ add point")){
			parent.settings.addPoint(parent.current, parent.currentShape);
			restart(parent.current, parent.currentShape);
		}
		if(theEvent.controller().name().contentEquals("- remove point")){
			parent.settings.removeLastPoint(parent.current, parent.currentShape);
			restart(parent.current, parent.currentShape);
		}
		if(theEvent.controller().name().contentEquals("anchor point")){
			anchorPoint = true;
			zeroPoint = false;
			zeroProjectionPoint = false;
			restart(parent.current, parent.currentShape);
		}
		if(theEvent.controller().name().contentEquals("zero point")){
			zeroPoint = true;
			zeroProjectionPoint = false;
			restart(parent.current, parent.currentShape);
		}
		if(theEvent.controller().name().contentEquals("zero projection point")){
			zeroPoint = false;
			zeroProjectionPoint = true;
			restart(parent.current, parent.currentShape);
		}
		if(theEvent.controller().name().contentEquals("projection points")){
			parent.pointMode = false;
			zeroPoint = false;
			zeroProjectionPoint = false;
			anchorPoint = false;
			restart(parent.current, parent.currentShape);
		}
		if(theEvent.controller().name().contentEquals("active")){
			parent.settings.changeShapeActiveState(parent.current, parent.currentShape, "true");
			restart(parent.current, parent.currentShape);
		}
		if(theEvent.controller().name().contentEquals("inactive")){
			parent.settings.changeShapeActiveState(parent.current, parent.currentShape, "false");
			restart(parent.current, parent.currentShape);
		}
		if(theEvent.controller().name().contentEquals("add shape")){
			parent.settings.addShape(parent.current);
			parent.settings.save();
			restart(parent.current, parent.settings.shapeLength(parent.current)-1);
		}
		if(theEvent.controller().name().contentEquals("delete shape")){
			if(parent.settings.shapeLength(parent.current)>1){
				parent.settings.deleteShape(parent.current, parent.currentShape);
				parent.settings.save();
				if(parent.currentShape == 0){
					restart(parent.current, 0);
				}else{
					restart(parent.current, parent.currentShape-1);
				}
			}
		}
		if(theEvent.controller().name().contentEquals("delete settings")){
			if(parent.settings.length()>1){
				parent.settings.delete(parent.current);
				parent.settings.save();
				if(parent.current==0){
					restart(0, 0);
				}else{
					restart(parent.current-1, 0);
				}
			}
		}
		if(theEvent.controller().name().contains("settings_")){
			String[] settings_num  = theEvent.controller().name().split("settings_");
			restart(Integer.parseInt(settings_num[1])-1, 0);
		}
		if(theEvent.controller().name().contains("shape_")){
			String[] shape_num  = theEvent.controller().name().split("shape_");
			restart(parent.current, Integer.parseInt(shape_num[1])-1);
		}
		PApplet.println("Configurator:controlEvent:"+theEvent.controller().name()+" = "+theEvent.value());
	}
	
	public void restart(int settingNum, int shapeNum){
		disable();
		parent.current = settingNum;
		parent.settings.setCurrent(settingNum);
		parent.currentShape = shapeNum;
		enabled = false;
	}
	
	public void mousePressed(int x, int y){
		mousePressed = true;
		mouseX = x;
		mouseY = y;
		ProjectionShape shape = parent.settings.getShapes(parent.current)[parent.currentShape];
		Point2D[] p;
		if(parent.pointMode){
			p = shape.points;
		}else{
			p = shape.projectionPoints;
		}
		if(!anchorPoint && !zeroPoint && !zeroProjectionPoint){
			for(int i=0; i<p.length; i++){
				if(((mouseX-200)<=p[i].getX()+shape.x+5)&&((mouseX-200)>=p[i].getX()+shape.x-5)&&(mouseY<=p[i].getY()+shape.y+5)&&(mouseY>=p[i].getY()+shape.y-5)){
					mouseHit = true;
					mouseMatch = i;
				}
			}
		}else if(anchorPoint && !zeroPoint && !zeroProjectionPoint){
			if(((mouseX-200)<=shape.x+5)&&((mouseX-200)>=shape.x-5)&&(mouseY<=shape.y+5)&&(mouseY>=shape.y-5)){
				mouseHit = true;
				mouseMatch = 999;
			}
		}else if(zeroPoint){
			if(((mouseX-200)<=shape.zx+shape.x+5)&&((mouseX-200)>=shape.zx+shape.x-5)&&(mouseY<=shape.zy+shape.y+5)&&(mouseY>=shape.zy+shape.y-5)){
				mouseHit = true;
				mouseMatch = 999;
			}
		}else if(zeroProjectionPoint){
			if(((mouseX-200)<=shape.pzx+shape.x+5)&&((mouseX-200)>=shape.pzx+shape.x-5)&&(mouseY<=shape.pzy+shape.y+5)&&(mouseY>=shape.pzy+shape.y-5)){
				mouseHit = true;
				mouseMatch = 999;
			}
		}
	}
	
	public void mouseDragged(int x, int y){
		mousePressed = true;
		mouseX = x;
		mouseY = y;
		ProjectionShape shape = parent.settings.getShapes(parent.current)[parent.currentShape];
		if(mouseHit){
			if(!anchorPoint && !zeroPoint && !zeroProjectionPoint){
				if(parent.pointMode){
					parent.settings.changePoint(parent.current, parent.currentShape, mouseMatch, mouseX-200-shape.x, mouseY-shape.y);
				}else{
					parent.settings.changeProjectionPoint(parent.current, parent.currentShape, mouseMatch, mouseX-200-shape.x, mouseY-shape.y);
				}
			}else if(anchorPoint && !zeroPoint && !zeroProjectionPoint){
				parent.settings.changeAnchorPoint(parent.current, parent.currentShape, mouseX-200, mouseY);
			}else if(zeroPoint){
				parent.settings.changeZeroPoint(parent.current, parent.currentShape, mouseX-200-shape.x, mouseY-shape.y);
			}else if(zeroProjectionPoint){
				parent.settings.changeZeroProjectionPoint(parent.current, parent.currentShape, mouseX-200-shape.x, mouseY-shape.y);
			}
		}
	}

	public void mouseReleased(int x, int y){
		mousePressed = false;
		mouseX = x;
		mouseY = y;
		mouseHit = false;
	}
	
}
