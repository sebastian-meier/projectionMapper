package circus;

import java.awt.geom.Point2D;
import processing.core.PApplet;

public class ProjectionDraw {
	
	ProjectionMapper parent;
	
	public ProjectionDraw(ProjectionMapper p){
		parent = p;
	}
	
	public void draw(){
		if(parent.mode != "projection"){
			parent.parent.fill(255);
			parent.parent.stroke(255, 0, 0);
		}else{
			parent.parent.noStroke();
			parent.parent.noFill();
		}
		ProjectionShape shape;
		for(int s=0; s<parent.getShapeAmount(); s++){
			shape = parent.getCurrentShape(s);
			if(shape.active){
				parent.parent.translate(shape.x, shape.y);
				if(parent.mode=="projection" || parent.mode=="settings"){
		
					Point2D[] p_one = new Point2D[1];
					Point2D[] p_two = new Point2D[1];
					if(parent.pointMode){
						p_one = shape.points;
						p_two = shape.projectionPoints;
					}else{
						p_two = shape.points;
						p_one = shape.projectionPoints;
					}
					
					if(!parent.pointMode){
						parent.parent.beginShape(PApplet.TRIANGLE_FAN); //QUADS TRIANGLES _STRIP ....
						if(parent.mode != "settings"){
							parent.parent.texture(parent.textures[s]);
							parent.parent.vertex(shape.pzx, shape.pzy, 0, shape.zx, shape.zy);
						}else{
							parent.parent.vertex(shape.pzx, shape.pzy);
						}
						for(int i=0; i<p_one.length; i++){
							if(parent.mode != "settings"){
								parent.parent.vertex((int)p_one[i].getX(), (int)p_one[i].getY(), 0, (int)p_two[i].getX(), (int)p_two[i].getY());
							}else{
								parent.parent.vertex((int)p_one[i].getX(), (int)p_one[i].getY());
							}
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
							float min_dist = 10;
							if(t_dist>min_dist){
								int extra_count = PApplet.round(t_dist/min_dist);
								for(int e=0; e<extra_count; e++){
									if(parent.mode != "settings"){
										parent.parent.vertex((int)p_one[i].getX()+((x_speed/extra_count)*e), (int)p_one[i].getY()+((y_speed/extra_count)*e), 0, (int)p_two[i].getX()+((px_speed/extra_count)*e), (int)p_two[i].getY()+((py_speed/extra_count)*e));
									}else{
										parent.parent.vertex((int)p_one[i].getX()+((x_speed/extra_count)*e), (int)p_one[i].getY()+((y_speed/extra_count)*e));
									}
								}
								
							}
						}
						if(parent.mode != "settings"){
							parent.parent.vertex((int)p_one[0].getX(), (int)p_one[0].getY(), 0, (int)p_two[0].getX(), (int)p_two[0].getY());
						}else{
							parent.parent.vertex((int)p_one[0].getX(), (int)p_one[0].getY());
						}
						parent.parent.endShape();
					}
				}else if((parent.mode=="sketch")&&(parent.currentShape == s)){
					parent.parent.noStroke();
					parent.parent.image(parent.textures[s], 0, 0);
					parent.parent.fill(0, 150);
					parent.parent.rect(shape.x*-1-200, shape.y*-1-200, parent.width, parent.height);
					
					parent.parent.noFill();
					parent.parent.stroke(255,0,0);
					parent.parent.strokeWeight(1);
					parent.parent.beginShape();
					parent.parent.texture(parent.textures[s]);
					for(int i=0; i<shape.points.length; i++){
						parent.parent.vertex((int) shape.points[i].getX(), (int) shape.points[i].getY(), 0, (int) shape.points[i].getX(), (int) shape.points[i].getY());
					}
					parent.parent.endShape(PApplet.CLOSE);
				}
				parent.parent.translate(-shape.x, -shape.y);
			}
		}
	}
}
