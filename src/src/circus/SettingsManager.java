package circus;

import processing.core.PApplet;
import processing.core.PImage;
import proxml.*;
import java.awt.geom.Point2D;

public class SettingsManager {

	ProjectionMapper parent;
	XMLInOut xmlInOut;
	XMLElement data;
	boolean ready = false;
	String settingsPath = "";
	
	public SettingsManager(ProjectionMapper p){
		parent = p;
		xmlInOut = new XMLInOut(parent);
	}
	
	public void init(){
		settingsPath = parent.parent.dataPath("settings.xml");
		PApplet.println(settingsPath);
		setup();
	}
	
	public void setup(){
		try{
			xmlInOut.loadElement(settingsPath); 
		}catch(Exception e){
			data = new XMLElement("data");
			data.addAttribute("current", "0");
			addSettings("default");
			xmlInOut.saveElement(data,settingsPath);
			setup();
		}
	}
	
	public void xmlEvent(XMLElement element){
		data = element;
		if(!data.getName().contentEquals("data")){
			PApplet.println("SettingsManager:xmlEvent: settings file is not compatible ("+data.getName()+") -> reset");
			data = new XMLElement("data");
			addSettings("default");
			xmlInOut.saveElement(data,settingsPath);
			setup();
		}else if(!data.hasChildren()){
			PApplet.println("SettingsManager:xmlEvent: loaded settings file was empty");
			addSettings("default");
			xmlInOut.saveElement(data,settingsPath);
			setup();
		}else{
			ready = true;
			if(parent.current == 999){
				parent.current = data.getIntAttribute("current");
			}
			PApplet.println("SettingsManager:xmlEvent: successfully loaded");
			if(parent.config){
				parent.configurator.enable();
			}
			parent.textures = new PImage[data.getChild(parent.current).countChildren()];
			parent.createEmptyTextures();
		}
	}
	
	public int length(){
		XMLElement children[] = data.getChildren();
		return children.length;
	}
	
	public String name(int id){
		XMLElement t_settings = data.getChild(id);
		return t_settings.getAttribute("name");
	}
	
	public void save(){
		xmlInOut.saveElement(data,settingsPath);
	}
	
	public void delete(int id){
		if(data.countChildren()>1){
			data.removeChild(id);
		}
	}
	
	public void setCurrent(int c){
		if(c < 1){
			data.addAttribute("current", "0");
		}else{
			data.addAttribute("current", c);
		}
	}
	
	public void changePoint(int settingsNum, int shapeNum, int pointNum, int x, int y){
		data.getChild(settingsNum).getChild(shapeNum).getChild(pointNum).addAttribute("x", x);
		data.getChild(settingsNum).getChild(shapeNum).getChild(pointNum).addAttribute("y", y);
		save();
	}
	
	public void changeProjectionPoint(int settingsNum, int shapeNum, int pointNum, int x, int y){
		data.getChild(settingsNum).getChild(shapeNum).getChild(pointNum).addAttribute("px", x);
		data.getChild(settingsNum).getChild(shapeNum).getChild(pointNum).addAttribute("py", y);
		save();
	}
	
	public void changeAnchorPoint(int settingsNum, int shapeNum, int x, int y){
		data.getChild(settingsNum).getChild(shapeNum).addAttribute("x", x);
		data.getChild(settingsNum).getChild(shapeNum).addAttribute("y", y);
		save();
	}
	
	public void changeZeroPoint(int settingsNum, int shapeNum, int x, int y){
		data.getChild(settingsNum).getChild(shapeNum).addAttribute("zx", x);
		data.getChild(settingsNum).getChild(shapeNum).addAttribute("zy", y);
		save();
	}
	
	public void changeZeroProjectionPoint(int settingsNum, int shapeNum, int x, int y){
		data.getChild(settingsNum).getChild(shapeNum).addAttribute("pzx", x);
		data.getChild(settingsNum).getChild(shapeNum).addAttribute("pzy", y);
		save();
	}
	
	public void changeShapeActiveState(int settingsNum, int shapeNum, String state){
		data.getChild(settingsNum).getChild(shapeNum).addAttribute("active", state);
		save();
	}
	
	public int shapeLength(int id){
		XMLElement t_settings = data.getChild(id);
		XMLElement[] t_shapes = t_settings.getChildren();
		return t_shapes.length;
	}
	
	public ProjectionShape[] getShapes(int id){
		XMLElement t_settings = data.getChild(id);
		XMLElement[] t_shapes = t_settings.getChildren();
		ProjectionShape[] shapes = new ProjectionShape[t_shapes.length];
		XMLElement t_shape;
		XMLElement[] t_points;
		XMLElement t_point;
		for(int i=0; i<t_shapes.length; i++){
			t_shape = t_shapes[i];
			shapes[i] = new ProjectionShape(parent);
			shapes[i].x = Integer.parseInt(t_shape.getAttribute("x"));
			shapes[i].y = Integer.parseInt(t_shape.getAttribute("y"));
			shapes[i].zx = Integer.parseInt(t_shape.getAttribute("zx"));
			shapes[i].zy = Integer.parseInt(t_shape.getAttribute("zy"));
			shapes[i].pzx = Integer.parseInt(t_shape.getAttribute("pzx"));
			shapes[i].pzy = Integer.parseInt(t_shape.getAttribute("pzy"));
			shapes[i].active = Boolean.parseBoolean(t_shape.getAttribute("active"));
			t_points = t_shape.getChildren();
			shapes[i].points = new Point2D[t_points.length];
			shapes[i].projectionPoints = new Point2D[t_points.length];
			for(int j=0; j<t_points.length; j++){
				t_point = t_points[j];
				shapes[i].points[j] = new Point2D.Double(Double.valueOf(t_point.getAttribute("x")), Double.valueOf(t_point.getAttribute("y")));
				shapes[i].projectionPoints[j] = new Point2D.Double(Double.valueOf(t_point.getAttribute("px")), Double.valueOf(t_point.getAttribute("py")));
			}
			
		}
		return shapes;
	}
	
	public void addPoint(int settingsNum, int shapeNum){
		XMLElement t_point;
		t_point = new XMLElement("t_point");
		t_point.addAttribute("x", 0);
		t_point.addAttribute("y", 0);
		t_point.addAttribute("px", 0);
		t_point.addAttribute("py", 0);
		data.getChild(settingsNum).getChild(shapeNum).addChild(t_point);
		save();
	}
	
	public void removeLastPoint(int settingsNum, int shapeNum){
		removePoint(settingsNum, shapeNum, (data.getChild(settingsNum).getChild(shapeNum).getChildren().length-1));
	}
	
	public void removePoint(int settingsNum, int shapeNum, int pointNum){
		data.getChild(settingsNum).getChild(shapeNum).removeChild(pointNum);
		save();
	}
	
	public void addShape(int id){
		XMLElement t_shape = new XMLElement("shape");
		t_shape.addAttribute("x", 0);
		t_shape.addAttribute("y", 0);
		t_shape.addAttribute("zx", 0);
		t_shape.addAttribute("zy", 0);
		t_shape.addAttribute("pzx", 0);
		t_shape.addAttribute("pzy", 0);
		t_shape.addAttribute("active", "true");
		XMLElement t_point;
		t_point = new XMLElement("t_point");
		t_point.addAttribute("x", 0);
		t_point.addAttribute("y", 0);
		t_point.addAttribute("px", 0);
		t_point.addAttribute("py", 0);
		t_shape.addChild(t_point);
		t_point = new XMLElement("t_point");
		t_point.addAttribute("x", 100);
		t_point.addAttribute("y", 0);
		t_point.addAttribute("px", 100);
		t_point.addAttribute("py", 0);
		t_shape.addChild(t_point);
		t_point = new XMLElement("t_point");
		t_point.addAttribute("x", 100);
		t_point.addAttribute("y", 100);
		t_point.addAttribute("px", 100);
		t_point.addAttribute("py", 100);
		t_shape.addChild(t_point);
		t_point = new XMLElement("t_point");
		t_point.addAttribute("x", 0);
		t_point.addAttribute("y", 100);
		t_point.addAttribute("px", 0);
		t_point.addAttribute("py", 100);
		t_shape.addChild(t_point);
		data.getChild(id).addChild(t_shape);
	}
	
	public void deleteShape(int settingsNum, int shapeNum){
		data.getChild(settingsNum).removeChild(shapeNum);
	}
	
	public void addSettings(String name){
		XMLElement t_settings = new XMLElement("settings");
		t_settings.addAttribute("name", name);
		data.addChild(t_settings);
		addShape(data.countChildren()-1);
	}
	
}
