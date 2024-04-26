package geometry_objects.points;

import utilities.math.MathUtilities;

/**
 * A 2D Point (x, y) only.
 * 
 * Points are ordered lexicographically (thus implementing the Comparable interface)
 * 
 * @author	Jack
 * @date	3/19/24
 */
public class Point implements Comparable<Point>
{
	public static final String ANONYMOUS = "__UNNAMED";

	public static final Point ORIGIN;
	static
	{
		ORIGIN = new Point("origin", 0, 0);
	}

	protected double _x;
	public double getX() { return this._x; }

	protected double _y; 
	public double getY() { return this._y; }

	protected String _name; 
	public String getName() { return _name; }

	// BasicPoint objects are named points (from input)
	// ImpliedPoint objects are unnamed points (from input)
	public boolean isGenerated() { return _name.substring(0, 2).equals("*_"); }

	/**
	 * Create a new Point with the specified coordinates.
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 */
	public Point(double x, double y)
	{
		this(ANONYMOUS, x, y);
	}

	/**
	 * Create a new Point with the specified coordinates.
	 * @param name -- The name of the point. (Assigned by the UI)
	 * @param x -- The X coordinate
	 * @param y -- The Y coordinate
	 */
	public Point(String name, double x, double y)
	{
		_name = (name == null || name == "") ? ANONYMOUS : name;
		_x = MathUtilities.removeLessEpsilon(x);
		_y = MathUtilities.removeLessEpsilon(y);
	}

	/**
	 * @return if this point has not user-defined name associated with it
	 */
	public boolean isUnnamed()
	{
		return _name == ANONYMOUS;
	}

	@Override
	public int hashCode()
	{
		return Double.valueOf(MathUtilities.removeLessEpsilon(_x)).hashCode() +
			   Double.valueOf(MathUtilities.removeLessEpsilon(_y)).hashCode();
	}

	/**
	 * 
	 * @param p1 Point 1
	 * @param p2 Point 2
	 * @return Lexicographically: p1 < p2 return -1 : p1 == p2 return 0 : p1 > p2 return 1
	 *         Order of X-coordinates first; order of Y-coordinates second
	 */
	public static int LexicographicOrdering(Point p1, Point p2)
	{
		if (p1 == null && p2 == null) {
			return 0;
		} else if (p1 == null) {
			return -1;
		} else if (p2 == null) {
			return 1;
		}
		
		if (MathUtilities.doubleLessThan(p1._x, p2._x)) return -1;
		if (MathUtilities.doubleGreaterThan(p1._x, p2._x)) return 1;

		if (MathUtilities.doubleLessThan(p1._y, p2._y)) return -1;
		if (MathUtilities.doubleGreaterThan(p1._y, p2._y)) return 1;

		return 0;
	}

	@Override
	public int compareTo(Point that)
	{
		if (that == null) return 1;

		return Point.LexicographicOrdering(this, that);
	}
	
	/**
	 * Determines if this point equals a given point.
	 * Two points are equal if they share the same
	 * x-values and y-values. Name does not matter.
	 * 
	 * @param obj -- the given points
	 * @return boolean -- if the points are equal
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Point)) {
			return false;
		}
		
		Point that = (Point) obj;

		return MathUtilities.doubleEquals(_x, that.getX()) && MathUtilities.doubleEquals(_y, that.getY());
	}

	@Override
	public String toString() {
		String outName = _name;
		String outX = _x + "";
		String outY = _y + "";
    	
		// Formatting
		// ----------------------------------------------------------------
		// check if outname points to ANONYMOUS for empty name of point
		if (outName == ANONYMOUS) {
			outName = "";
		}
		
		// removes ".0" from integers stored as doubles for x-value
		if (_x == (int) _x) {
			outX = (int) _x + "";
		}
		
		// removes ".0" from integers stored as doubles for y-value
		if (_y == (int) _y) {
			outY = (int) _y + "";
		}
		// ----------------------------------------------------------------
		
    	return outName + "(" + outX + ", " + outY + ")";
	}
}