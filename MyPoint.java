
public class MyPoint {
	public int x;
	public int y;
	
	public MyPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	 @Override 
	 public boolean equals(Object o) {
        if (!(o instanceof MyPoint))
            return false;
        MyPoint p = (MyPoint) o;
        return p.x == x && p.y == y;
	}
	 @Override
	 public int hashCode() {
		 int result = 17;
	     result = 31 * result + x;
	     result = 31 * result + y;
	     return result;
	 }
	 
	 
}
