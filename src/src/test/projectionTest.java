package test;

import java.awt.geom.Point2D;

import javax.media.opengl.GL;

import controlP5.ControlEvent;
import circus.ProjectionMapper;
import circus.ProjectionShape;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PGraphicsOpenGL;

public class projectionTest extends PApplet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ProjectionMapper mapper;
	PGraphics graph;
	
	public void setup(){
		size(1280, 800, OPENGL);
		mapper = new ProjectionMapper(this, "projection", 0, 0);
		graph = createGraphics(width, height, JAVA2D);
	}

	public void draw(){
		if(mapper.ready && !mapper.config){
			//PGraphicsOpenGL pgl = (PGraphicsOpenGL) g; 
			//GL gl = pgl.beginGL();
			graph.background(color(255,255,255));
			mapper.textures[0]=graph.get();
			mapper.textures[1]=graph.get();
			mapper.textures[2]=graph.get();
			background(0);
			translate(200, 0);
			//mapper.drawer.draw();
			
			noStroke();
			noFill();

			ProjectionShape shape;
			for(int s=0; s<mapper.getShapeAmount(); s++){
				shape = mapper.getCurrentShape(s);
				if(shape.active){
					translate(shape.x, shape.y);
					Point2D[] p_two = shape.points;
					Point2D[] p_one = shape.projectionPoints;
						
					beginShape(PApplet.TRIANGLE_FAN); //QUADS TRIANGLES _STRIP ....
					texture(mapper.textures[s]);
					vertex(shape.pzx, shape.pzy, 0, shape.zx, shape.zy);

					for(int i=0; i<p_one.length; i++){
						vertex((int)p_one[i].getX(), (int)p_one[i].getY(), 0, (int)p_two[i].getX(), (int)p_two[i].getY());
						float t_dist;
						float x_speed;
						float y_speed;
						float px_speed;
						float py_speed;
						if(i<p_one.length-1){
							t_dist = PApplet.dist((int)p_one[i].getX(), (int)p_one[i].getY(), (int)p_one[i+1].getX(), (int)p_one[i+1].getY());
							x_speed = (int)p_one[i+1].getX()-(int)p_one[i].getX();
							y_speed = (int)p_one[i+1].getY()-(int)p_one[i].getY();
							px_speed = (int)p_two[i+1].getX()-(int)p_two[i].getX();
							py_speed = (int)p_two[i+1].getY()-(int)p_two[i].getY();
						}else{
							t_dist = PApplet.dist((int)p_one[i].getX(), (int)p_one[i].getY(), (int)p_one[0].getX(), (int)p_one[0].getY());
							x_speed = (int)p_one[0].getX()-(int)p_one[i].getX();
							y_speed = (int)p_one[0].getY()-(int)p_one[i].getY();
							px_speed = (int)p_two[0].getX()-(int)p_two[i].getX();
							py_speed = (int)p_two[0].getY()-(int)p_two[i].getY();
						}
						float min_dist = 20;
						if(t_dist>min_dist){
							int extra_count = PApplet.round(t_dist/min_dist);
							for(int e=0; e<extra_count; e++){
								vertex((int)p_one[i].getX()+((x_speed/extra_count)*e), (int)p_one[i].getY()+((y_speed/extra_count)*e), 0, (int)p_two[i].getX()+((px_speed/extra_count)*e), (int)p_two[i].getY()+((py_speed/extra_count)*e));
							}
						}
					}
					vertex((int)p_one[0].getX(), (int)p_one[0].getY(), 0, (int)p_two[0].getX(), (int)p_two[0].getY());
					endShape();
					translate(-shape.x, -shape.y);
				}
			}
			
			translate(-200, 0);
			//pgl.endGL();
		}
		
		println(frameRate);
	}
	
	//TODO needs to be inside the ProjectionMapper
	void controlEvent(ControlEvent theEvent) {
		mapper.controlEvent(theEvent);  
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { /*"--present",*/ test.projectionTest.class.getName() });
	}
	
}
