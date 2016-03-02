package circus;

import java.awt.geom.Point2D;

import processing.core.PApplet;

public class ProjectionShape {
	
	ProjectionMapper parent;
	
	public int x;
	public int y;
	public int zx;
	public int zy;
	public int pzx;
	public int pzy;
	
	public Point2D[] points;
	public Point2D[] projectionPoints;
	
	public Point2D[][] pathPoints;
	public Point2D[][] pointRows;
	
	public Point2D[][] projectionPathPoints;
	public Point2D[][] projectionPointRows;
	
	public boolean active;
	
	public ProjectionShape(ProjectionMapper p){
		parent = p;
	}
	
	public void buildGeometry(){
		pathPoints = new Point2D[points.length][];
		for(int i=0; i<points.length; i++){
			Point2D p1 = points[i];
			Point2D p2;
			if(i < (points.length-1)){
				p2 = points[i+1];
			}else{
				p2 = points[0];
			}
			int x_dist = (int) (p2.getX() - p1.getX());
			int y_dist = (int) (p2.getY() - p1.getY());
			int y_multi = 1;
			if(y_dist<1){y_multi = -1;}
			if(PApplet.abs(y_dist)<1){
				pathPoints[i] = new Point2D[2];
				if(p1.getX()<p2.getX()){
					pathPoints[i][0] = p1;
					pathPoints[i][1] = p2;
				}else{
					pathPoints[i][0] = p2;
					pathPoints[i][1] = p1;
				}
			}else{
				float x_per_y = PApplet.parseFloat(x_dist)/PApplet.parseFloat(y_dist);
				pathPoints[i] = new Point2D[PApplet.abs(y_dist)];
				for(int y=0; y<PApplet.abs(y_dist); y++){
					pathPoints[i][y] = new Point2D.Double(Double.valueOf(p1.getX()+x_per_y*(y*y_multi)), Double.valueOf(p1.getY()+(y*y_multi)));
				}
			}
		}
		
		pointRows = new Point2D[parent.height][];
		for(int i=0; i<parent.height; i++){
			Point2D t_points[] = new Point2D[parent.width];
			int t_points_count = 0;
			for(int s=0; s<pathPoints.length; s++){
				for(int y=0; y<pathPoints[s].length; y++){
					if(pathPoints[s][y].getY()==i){
						t_points[t_points_count] = pathPoints[s][y];
						t_points_count++;
					}
				}
			}
			
			double smallest = 999999;
			int uniqueCount = 0;
			for(int p=0; p<t_points_count; p++){
				boolean exists = false;
				for(int pr=0; pr<p; pr++){
					if((t_points[p].getX() == t_points[pr].getX())&&(t_points[p].getY() == t_points[pr].getY())){
						exists = true;
					}
				}
				if(!exists){
					t_points[uniqueCount] = t_points[p];
					if(t_points[p].getX()<smallest){
						smallest = t_points[p].getX();
					}
					uniqueCount++;
				}
			}
			
			pointRows[i] = new Point2D[uniqueCount];
			int filledCount = 0;
			while(filledCount<uniqueCount){
				for(int p=0; p<uniqueCount; p++){
					if(t_points[p].getX()==smallest){
						pointRows[i][filledCount] = t_points[p];
					}
				}
				double t_smallest = smallest;
				smallest = 9999999;
				for(int p=0; p<uniqueCount; p++){
					if((t_points[p].getX()<smallest)&&(t_points[p].getX()>t_smallest)){
						smallest = t_points[p].getX();
					}
				}
				filledCount++;
			}
		}
		
		/* ---------------------------------------------------------------------- */
		
		projectionPathPoints = new Point2D[projectionPoints.length][];
		for(int i=0; i<projectionPoints.length; i++){
			Point2D p1 = projectionPoints[i];
			Point2D p2;
			if(i < (projectionPoints.length-1)){
				p2 = projectionPoints[i+1];
			}else{
				p2 = projectionPoints[0];
			}
			int x_dist = (int) (p2.getX() - p1.getX());
			int y_dist = (int) (p2.getY() - p1.getY());
			int y_multi = 1;
			if(y_dist<1){y_multi = -1;}
			if(PApplet.abs(y_dist)<1){
				projectionPathPoints[i] = new Point2D[2];
				if(p1.getX()<p2.getX()){
					projectionPathPoints[i][0] = p1;
					projectionPathPoints[i][1] = p2;
				}else{
					projectionPathPoints[i][0] = p2;
					projectionPathPoints[i][1] = p1;
				}
			}else{
				float x_per_y = PApplet.parseFloat(x_dist)/PApplet.parseFloat(y_dist);
				projectionPathPoints[i] = new Point2D[PApplet.abs(y_dist)];
				for(int y=0; y<PApplet.abs(y_dist); y++){
					projectionPathPoints[i][y] = new Point2D.Double(Double.valueOf(p1.getX()+x_per_y*(y*y_multi)), Double.valueOf(p1.getY()+(y*y_multi)));
				}
			}
		}
		
		projectionPointRows = new Point2D[parent.height][];
		for(int i=0; i<parent.height; i++){
			Point2D t_points[] = new Point2D[parent.width];
			int t_points_count = 0;
			for(int s=0; s<projectionPathPoints.length; s++){
				for(int y=0; y<projectionPathPoints[s].length; y++){
					if(projectionPathPoints[s][y].getY()==i){
						t_points[t_points_count] = projectionPathPoints[s][y];
						t_points_count++;
					}
				}
			}
			
			double smallest = 999999;
			int uniqueCount = 0;
			for(int p=0; p<t_points_count; p++){
				boolean exists = false;
				for(int pr=0; pr<p; pr++){
					if((t_points[p].getX() == t_points[pr].getX())&&(t_points[p].getY() == t_points[pr].getY())){
						exists = true;
					}
				}
				if(!exists){
					t_points[uniqueCount] = t_points[p];
					if(t_points[p].getX()<smallest){
						smallest = t_points[p].getX();
					}
					uniqueCount++;
				}
			}
			
			projectionPointRows[i] = new Point2D[uniqueCount];
			int filledCount = 0;
			while(filledCount<uniqueCount){
				for(int p=0; p<uniqueCount; p++){
					if(t_points[p].getX()==smallest){
						projectionPointRows[i][filledCount] = t_points[p];
					}
				}
				double t_smallest = smallest;
				smallest = 9999999;
				for(int p=0; p<uniqueCount; p++){
					if((t_points[p].getX()<smallest)&&(t_points[p].getX()>t_smallest)){
						smallest = t_points[p].getX();
					}
				}
				filledCount++;
			}
		}
	}
	
	public boolean isInside(int x, int y){
		boolean inside = true;
		if((x<getMinX())||(x>getMaxX())||(y<getMinY())||(y>getMaxY())){
			inside = false;
		}else if(pointRows[y].length<1){
			inside = false;
		}else{
			int sectionsInRow = PApplet.round(pointRows[y].length/2);
			for(int i=0; ((i<sectionsInRow)&&(inside)); i++){
				if(i==0){
					if(x < pointRows[y][0].getX()){
						inside = false;
					}
					if((sectionsInRow==1)&&(x > pointRows[y][1].getX())){
						inside = false;
					}
				}else if(i==(sectionsInRow-1)){
					if(x > pointRows[y][i*2-1].getX()){
						inside = false;
					}
				}else{
					if((x > pointRows[y][i*2-1].getX())&&(x < pointRows[y][i*2].getX())){
						inside = false;
					}
				}
			}
		}
		return inside;
	}
	
	//TODO the above and the followed function should be combined to one function with a parameter saying which array to check against
	
	public boolean isInsideProjection(int x, int y){
		boolean inside = true;
		if((y >= projectionPointRows.length)||(y < 0)){
			inside = false;
		}else if(projectionPointRows[y].length<1){
			inside = false;
		}else{
			int sectionsInRow = PApplet.round(projectionPointRows[y].length/2);
			for(int i=0; ((i<sectionsInRow)&&(inside)); i++){
				if(i==0){
					if(x < projectionPointRows[y][0].getX()){
						inside = false;
					}
					if((sectionsInRow==1)&&(x > projectionPointRows[y][1].getX())){
						inside = false;
					}
				}else if(i==(sectionsInRow-1)){
					if(x > projectionPointRows[y][i*2-1].getX()){
						inside = false;
					}
				}else{
					if((x > projectionPointRows[y][i*2-1].getX())&&(x < projectionPointRows[y][i*2].getX())){
						inside = false;
					}
				}
			}
		}
		return inside;
	}
	
	public int[] getNeighbourSide(int x, int y){
		int neighbourShape[] = {99999, 99999, 99999};
		int side = 0;
		double tx = x;
		double ty = y;
		
		float smallestDist = 999999; 
		
		for(int i=0; i<points.length; i++){
			Point2D p1 = points[i];
			Point2D p2;
			if(i < (points.length-1)){
				p2 = points[i+1];
			}else{
				p2 = points[0];
			}
			float x_dist = (float) (p2.getX() - p1.getX());
			float y_dist = (float) (p2.getY() - p1.getY());
			
			if((PApplet.abs(y_dist)>1)&&(PApplet.abs(x_dist)>1)){
				float t_dist = PApplet.abs((float) (PApplet.parseFloat(x)-(((PApplet.parseFloat(y)-p1.getY())/y_dist)*x_dist+p1.getX())));
				if(t_dist < smallestDist){
					smallestDist = t_dist;
					side = i;
				}
			}else{
				if(PApplet.abs(y_dist)<=1){
					if((PApplet.abs((float) (ty-p1.getY()))<=1)){
						side = i;
					}
				}else if(PApplet.abs(x_dist)<=1){
					if((PApplet.abs((float) (tx-p1.getX())))<=1){
						side = i;
					}
				}
			}
		}

		Point2D p1 = points[side];
		Point2D p2;
		if(side < (points.length-1)){
			p2 = points[side+1];
		}else{
			p2 = points[0];
		}
		
		Point2D pp1 = projectionPoints[side];
		Point2D pp2;
		if(side < (projectionPoints.length-1)){
			pp2 = projectionPoints[side+1];
		}else{
			pp2 = projectionPoints[0];
		}
		
		float sx = (float) (pp2.getX() - pp1.getX());
		float sy = (float) (pp2.getY() - pp1.getY());
		
		float originalFullDist = PApplet.dist((float) p1.getX(), (float) p1.getY(), (float) p2.getX(), (float) p2.getY());
		float originalDist = PApplet.dist((float) p1.getX(), (float) p1.getY(), (float) x, y);
		float percentageDist = originalDist/originalFullDist;
		
		//TODO replace with the end of the next function...
		float tpx = (float) p1.getX()+ (float) sx * (float) percentageDist;
		float tpy = (float) p1.getY()+ (float) sy * (float) percentageDist;
		
		for(int s=0; s<parent.settings.getShapes(parent.current).length; s++){
			ProjectionShape shape = parent.getCurrentShape(s);
			if(shape != this){
				if(		(shape.isInsideProjection((int) tpx+4, (int) tpy+4))||
						(shape.isInsideProjection((int) tpx-4, (int) tpy+4))||
						(shape.isInsideProjection((int) tpx+4, (int) tpy-4))||
						(shape.isInsideProjection((int) tpx-4, (int) tpy-4))){
					neighbourShape[0] = s;
				}
			}
		}
		neighbourShape[1] = (int) tpx;
		neighbourShape[2] = (int) tpy;
		return neighbourShape;
	}
	
	public int[] crossPointNeighbour(int x, int y, int neighbourShape){
		int neighbourCrossPoint[] = new int[2];
		
		if(neighbourShape==99999){
			neighbourCrossPoint[0] = 99999;
			neighbourCrossPoint[1] = 99999;
		}else{
			int side = 0;
			float smallestDist = 999999; 
			ProjectionShape nShape = parent.getCurrentShape(neighbourShape);
			
			for(int i=0; i<nShape.projectionPoints.length; i++){
				Point2D p1 = nShape.projectionPoints[i];
				Point2D p2;
				if(i < (nShape.projectionPoints.length-1)){
					p2 = nShape.projectionPoints[i+1];
				}else{
					p2 = nShape.projectionPoints[0];
				}
				float x_dist = (float) (p2.getX() - p1.getX());
				float y_dist = (float) (p2.getY() - p1.getY());
				
				if((PApplet.abs(y_dist)>1)&&(PApplet.abs(x_dist)>1)){
					float t_dist = PApplet.abs((float) (PApplet.parseFloat(x)-(((PApplet.parseFloat(y)-p1.getY())/y_dist)*x_dist+p1.getX())));
					if(t_dist < smallestDist){
						smallestDist = t_dist;
						side = i;
					}
				}else{
					if(PApplet.abs(y_dist)<=1){
						if((PApplet.abs((float) (y-p1.getY()))<=1)){
							side = i;
						}
					}else if(PApplet.abs(x_dist)<=1){
						if((PApplet.abs((float) (x-p1.getX())))<=1){
							side = i;
						}
					}
				}
			}
			
			Point2D p1 = nShape.points[side];
			Point2D p2;
			if(side < (nShape.points.length-1)){
				p2 = nShape.points[side+1];
			}else{
				p2 = nShape.points[0];
			}
			
			Point2D pp1 = nShape.projectionPoints[side];
			Point2D pp2;
			if(side < (nShape.projectionPoints.length-1)){
				pp2 = nShape.projectionPoints[side+1];
			}else{
				pp2 = nShape.projectionPoints[0];
			}
			
			float sx = (float) (p2.getX() - p1.getX());
			float sy = (float) (p2.getY() - p1.getY());
			
			float projectionDist = PApplet.dist((float) pp1.getX(), (float) pp1.getY(), (float) x, y);
			float projectionFullDist = PApplet.dist((float) pp1.getX(), (float) pp1.getY(), (float) pp2.getX(), (float) pp2.getY());
			float percentageDist = projectionDist/projectionFullDist;
			
			neighbourCrossPoint[0] = (int) (p1.getX()+ sx*percentageDist);
			neighbourCrossPoint[1] = (int) (p1.getY()+ sy*percentageDist);
		}
		
		return neighbourCrossPoint;
	}
	
	public int[] crossPointFromPoints(int px1, int py1, int px2, int py2){
		return crossPointFromPointAndSpeed(px1, py1, px2-px1, py2-py1);
	}
	
	public int[] crossPointFromPointAndSpeed(int px, int py, int sx, int sy){
		return crossPointFromPointAndSpeed(PApplet.parseFloat(px), PApplet.parseFloat(py), PApplet.parseFloat(sx), PApplet.parseFloat(sy));
	}
	
	public int[] crossPointFromPointAndSpeed(float px, float py, float sx, float sy){
		int crosspoint[] = new int[2];
		float nsx;
		float nsy;
		float checkpoint[] = new float[2];
		
		if(sx>sy){
			if(sx<0){
				nsx = -1;
				nsy = sy/-sx;
			}else{
				nsx = 1;
				nsy = sy/sx;
			}
		}else{
			
			if(sy<0){
				nsy = -1;
				nsx = sx/-sy;
			}else{
				nsy = 1;
				nsx = sx/sy;
			}
		}
		
		checkpoint[0] = px;
		checkpoint[1] = py;
		
		while(isInside((int)checkpoint[0], (int)checkpoint[1])){
			crosspoint[0] = (int) checkpoint[0];
			crosspoint[1] = (int) checkpoint[1];
			checkpoint[0] += nsx;
			checkpoint[1] += nsy;
		}
			
		return crosspoint;
	}
	
	public int getWidth(){
		int tMin = 9999999;
		int tMax = 0;
		for(int i=0; i<points.length; i++){
			if(tMin>points[i].getX()){
				tMin = (int) points[i].getX();
			}
			if(tMax<points[i].getX()){
				tMax = (int) points[i].getX();
			}
		}
		int width = tMax - tMin;
		return width;
	}
	
	public int getHeight(){
		int tMin = 9999999;
		int tMax = 0;
		for(int i=0; i<points.length; i++){
			if(tMin>points[i].getY()){
				tMin = (int) points[i].getY();
			}
			if(tMax<points[i].getY()){
				tMax = (int) points[i].getY();
			}
		}
		int height = tMax - tMin;
		return height;
	}
	
	public int getMaxX(){
		int tMax = 0;
		for(int i=0; i<points.length; i++){
			if(tMax<points[i].getX()){
				tMax = (int) points[i].getX();
			}
		}
		return tMax;
	}
	
	public int getMaxY(){
		int tMax = 0;
		for(int i=0; i<points.length; i++){
			if(tMax<points[i].getY()){
				tMax = (int) points[i].getY();
			}
		}
		return tMax;

	}
	
	public int getMinX(){
		int tMin = 9999999;
		for(int i=0; i<points.length; i++){
			if(tMin>points[i].getX()){
				tMin = (int) points[i].getX();
			}
		}
		return tMin;
	}
	
	public int getMinY(){
		int tMin = 9999999;
		for(int i=0; i<points.length; i++){
			if(tMin>points[i].getY()){
				tMin = (int) points[i].getY();
			}
		}
		return tMin;

	}
	
	public int[][] getPoints(){
		int intPoints[][] = new int[points.length][2];
		for(int i=0; i<points.length; i++){
			intPoints[i][0] = (int) points[i].getX();
			intPoints[i][1] = (int) points[i].getY();
		}
		return intPoints;
	}
	
}
