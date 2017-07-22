/** A class that represents a path via pursuit curves.
 *  @author You!
 */
public class Path {
<<<<<<< HEAD
	Point currPoint;
	Point nextPoint;
	public Path(double x,double y){
		this.currPoint=new Point();
		this.nextPoint=new Point(x,y);
	}
	public double getCurrX(){
		return this.currPoint.getX();	
	}
	public double getCurrY(){
		return this.currPoint.getY();
	}
	public double getNextX(){
		return this.nextPoint.getX();
	}
	public double getNextY(){
		return this.nextPoint.getY();
	}
	public Point getCurrentPoint(){
		return this.currPoint;
	}
	public void setCurrentPoint(Point point){
		this.currPoint.setX(point.getX());
		this.currPoint.setY(point.getY());
	}
	public void iterate(double dx,double dy){
		this.setCurrentPoint(nextPoint);
		double cx=this.getNextX()+dx;
		double cy=this.getNextY()+dy;
		this.nextPoint.setX(cx);
		this.nextPoint.setY(cy);
	}
		
	
    
=======

    /** What to do, what to do... */
>>>>>>> ae681060fd4403686810ca9b1cbf9e5942039129

}
