package circus;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import controlP5.ControlEvent;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import proxml.XMLElement;

public class ProjectionMapper extends PApplet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	PApplet parent;
	
	public boolean config = false;
	public boolean showFrameRate = false;
	SettingsManager settings;
	public Configurator configurator;
	public int current = 999;
	public int currentShape = 0;
	public ProjectionShape currentShapeObjects[];
	public boolean currentShapeObject_init = false; 
	public int width = 800;
	public int height = 800;
	public boolean pointMode = false;
	public ProjectionDraw drawer;
	public int mouseX = 0;
	public int mouseY = 0;
	
	public boolean ready = false;
	
	public PImage textures[];
	
	String mode = "settings"; //settings, sketch, projection

	PFont font;
	
	public ProjectionMapper(PApplet p){
		mode = "settings";
		config = true;
		setup(p);
	}
	
	//m = mode
	public ProjectionMapper(PApplet p, String m){
		mode = m;
		setup(p);
	}
	
	//m = mode, c = selected settings
	public ProjectionMapper(PApplet p, String m, int c){
		mode = m;
		current = c;
		setup(p);
	}
	
	//m = mode, c = selected settings, s = selected shape
	public ProjectionMapper(PApplet p, String m, int c, int s){
		mode = m;
		current = c;
		currentShape = s;
		setup(p);
	}
	
	public void setup(PApplet p) {
		parent = p;
		
		if(mode=="settings"){
			config = true;
		}

		if((parent.g instanceof processing.opengl.PGraphicsOpenGL)||(parent.g instanceof processing.core.PGraphics3D)){
			if(mode == "settings"){
				PApplet.println("JAVA2D is recommended for the settings mode");
			}
		}else{
			mode = "settings";
			config = true;
			PApplet.println(parent.g);
			PApplet.println("WARNING: circus.ProjectionMapper: projection- and sketch-mode are only available in P3D (recommended) & OPENGL");
		}
		
		width = parent.width;
		height = parent.height;
		
		//parent.registerDraw(this);
		
		if(config){
			parent.registerMouseEvent(this);
			parent.registerKeyEvent(this);
		}
		
		//parent.registerPre(this);
		//parent.registerPost(this);
		
		font = parent.createFont("Arial", 10);
		parent.textFont(font);
		parent.textMode(PApplet.SCREEN);
		
		if(config){
			configurator = new Configurator(this, font);
		}
		
		settings = new SettingsManager(this);
		settings.init();
		drawer = new ProjectionDraw(this);
		
		//hint(ENABLE_ACCURATE_TEXTURES);
	}

	int count = 0;
	
	public void pre(){
		if(mode=="settings"){
			parent.background(255);
		}else{
			//parent.background(0);
		}
	}
	
	public void post(){
		if(showFrameRate){
			parent.text(parent.frameRate, 10, 10);
		}
	}
	
	public void draw() {
		if(ready){
			//extra border for settings elements
			//parent.translate(200,0);
			//drawer.draw();
			//parent.translate(-200,0);

			if(config){
				configurator.draw();
			}
		}
	}
	
	public ProjectionShape getCurrentShape(int s){
		ProjectionShape shape;
		if(mode=="settings"){
			shape = settings.getShapes(current)[s];
		}else{
			shape = currentShapeObjects[s];
		}
		return shape;
	}
	
	public int getShapeAmount(){
		int amount;
		if(mode=="settings"){
			amount = settings.getShapes(current).length;
		}else{
			amount = currentShapeObjects.length;
		}
		return amount;
	}
	
	public int shapeMinX(int s){
		return getCurrentShape(s).getMinX();
	}
	
	public int shapeMinY(int s){
		return getCurrentShape(s).getMinY();
	}
	
	public int shapeMaxX(int s){
		return getCurrentShape(s).getMaxX();
	}
	
	public int shapeMaxY(int s){
		return getCurrentShape(s).getMaxY();
	}
	
	public int shapeWidth(int s){
		return getCurrentShape(s).getWidth();
	}
	
	public int shapeHeight(int s){
		return getCurrentShape(s).getHeight();
	}
	
	public int[][] shapePoints(int s){
		return getCurrentShape(s).getPoints();
	}
	
	public boolean shapeIsInside(int s, int x, int y){
		return getCurrentShape(s).isInside(x, y);
	}
	
	public boolean shapeIsInsideProjection(int s, int x, int y){
		return getCurrentShape(s).isInsideProjection(x, y);
	}
	
	public int[] shapeCrossPointFromPoints(int s, int px1, int py1, int px2, int py2){
		return getCurrentShape(s).crossPointFromPoints(px1, py1, px2, py2);
	}
	
	public int[] shapeCrossPointFromPointAndSpeed(int s, int px, int py, int sx, int sy){
		return getCurrentShape(s).crossPointFromPointAndSpeed(px, py, sx, sy);
	}
	
	public int[] shapeCrossPointFromPointAndSpeed(int s, float px, float py, float sx, float sy){
		return getCurrentShape(s).crossPointFromPointAndSpeed(px, py, sx, sy);
	}
	
	public int[] shapeCrossPointNeighbour(int s, int x, int y, int n){
		return getCurrentShape(s).crossPointNeighbour(x, y, n);
	}
	
	public int[] shapeGetNeighbourSide(int s, int x, int y){
		return getCurrentShape(s).getNeighbourSide(x, y);
	}
	
	public void createEmptyTextures(){
		PImage t_texture = createImage(width, height, ARGB);
		for(int i=0; i<textures.length; i++){
			textures[i] = t_texture;
		}
		if((mode != "settings")&&(!currentShapeObject_init)){
			currentShapeObjects = settings.getShapes(current);
			for(int i=0; i<currentShapeObjects.length; i++){
				currentShapeObjects[i].buildGeometry();
			}
			currentShapeObject_init = true;
		}
		ready = true;
	}
	
	public void mouseEvent(MouseEvent e){
		if(e.paramString().contains("MOUSE_PRESSED")){
			mousePressed();
		}else if(e.paramString().contains("MOUSE_RELEASED")){
			mouseReleased();
		}else if(e.paramString().contains("MOUSE_DRAGGED")){
			mouseDragged();
		}else if(e.paramString().contains("MOUSE_MOVED")){
			mouseX = parent.mouseX;
			mouseY = parent.mouseY;
		}else if(e.paramString().contains("MOUSE_CLICKED")){
			mouseClicked();
		}
		for(int m=0; m<mouseListener.length; m++){
			if(e.paramString().contains("MOUSE_PRESSED")){
				mouseListener[m].mousePressed();
			}else if(e.paramString().contains("MOUSE_RELEASED")){
				mouseListener[m].mouseReleased();
			}else if(e.paramString().contains("MOUSE_DRAGGED")){
				mouseListener[m].mouseDragged();
			}else if(e.paramString().contains("MOUSE_CLICKED")){
				mouseListener[m].mouseClicked();
			}
		}
	}
	
	PApplet mouseListener[] = new PApplet[0];
	public void registerMouseEvent(PApplet p){
		PApplet t_mouseListener[] = new PApplet[mouseListener.length];
		for(int i=0; i<mouseListener.length; i++){
			t_mouseListener[i] = mouseListener[i];
		}
		t_mouseListener[mouseListener.length] = p;
		mouseListener = t_mouseListener;
	}
	
	public void mousePressed(){
		if(config){
			configurator.mousePressed(parent.mouseX, parent.mouseY);
		}
	}
	
	public void mouseDragged(){
		if(config){
			configurator.mouseDragged(parent.mouseX, parent.mouseY);
		}
	}
	
	public void mouseReleased(){
		if(config){
			configurator.mouseReleased(parent.mouseX, parent.mouseY);
		}
	}
	
	public void mouseClicked(){}
	
	public void keyEvent(KeyEvent e){
		
	}
	
	public void keyReleased() {}
	public void keyPressed() {}
	
	//sending xml Event to the SettingsManager Class
	public void xmlEvent(XMLElement element){
		settings.xmlEvent(element);
	}
	
	public void controlEvent(ControlEvent theEvent) {
		configurator.controlEvent(theEvent);  
	}
}
