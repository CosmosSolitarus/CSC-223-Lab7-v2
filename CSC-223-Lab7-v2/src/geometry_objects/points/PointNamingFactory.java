package geometry_objects.points;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Given a pair of coordinates; generate a unique name for it;
 * return that point object.
 *
 * Names go from A..Z..AA..ZZ..AAA...ZZZ  (a name such as ABA does not occur)
 * 
 * @author	Jack, Sage
 * @date	3/26/24
 */
public class PointNamingFactory
{
	// Prefix associated with each generated name so those names are easily distinguishable
	private static final String _PREFIX = "*_";
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // Constants reflecting our naming characters for generated names.
	// private static final char START_LETTER = 'A';		// never used
	private static final char END_LETTER = 'Z';

    //
    // the number of characters in the generated names:
	// "A" and 1 -> "A"
	// "B" and 3 -> "BBB"
	//
	private String _currentName = "A";
	private int _numLetters = 1;

	//
	// A hashed container for the database of points; this requires the Point
	// class implement equals based solely on the individual coordinates and
	// not a name. We need a get() method; HashSet doesn't offer one.
	// Each entry is a <Key, Value> pair where Key == Value
	//
	protected Map<Point, Point> _database;

	public PointNamingFactory()
	{
		_database = new LinkedHashMap<>();
	}

	/**
	 * Initialize the database with points; must call put() to ensure all points are named
	 *
	 * @param points -- a list of points, named or not named
	 */
	public PointNamingFactory(List<Point> points)
	{
		_database = new LinkedHashMap<>();

		for (Point point : points) {
			put(point);
		}
	}

	/**
	 * Overloaded add / lookup mechanism for this database.
	 *
	 * @param pt -- a Point object (may or may not be named)
	 
	 * @return THE point object in the database corresponding to its coordinate pair
                    * the object in the database if it already exists or
					* a completely new point that has been added to the database
	 */
	public Point put(Point pt)
	{
		if (pt._name == Point.ANONYMOUS) {
			return put(getCurrentName(), pt.getX(), pt.getY());
		}

		return put(pt.getName(), pt.getX(), pt.getY());
	}

	/**
	 * Overloaded add / lookup mechanism for this database for an unnamed coordinate pair.
	 *
	 * @param x -- single coordinate
	 * @param y -- single coordinate

	 * @return THE point object in the database corresponding to its coordinate pair
                    * the object in the database if it already exists or
					* a completely new point that has been added to the database (with generated name)
	 */
	public Point put(double x, double y)
	{
		return put(getCurrentName(), x, y);
	}

	/**
	 * The 'main' overloaded add / lookup mechanism for this database.
	 * 
	 * @param name -- the name of the point 
	 * @param x -- single coordinate
	 * @param y -- single coordinate
	 * 
	 * @return a point (if it already exists in the database) or a completely new point that
	 *         has been added to the database.
	 *         
	 *         If the point is in the database and the name differs from what
	 *         is given, nothing in the database will be changed; essentially
	 *         this means we use the first name given for a point.
	 *            e.g., a valid name cannot overwrite an existing valid name ;
	 *                  a generated name cannot be overwritten by another generated name
	 *         
	 *         The exception is that a valid name can overwrite an unnamed point.
	 */
	public Point put(String name, double x, double y)
	{
		Point pt = new Point(name, x, y);
		
		// completely new point is added
		if (!contains(pt)) {
			_database.put(pt, pt);
			return pt;
		}

		pt = _database.get(pt);

		// valid name overwrites unnamed name
		if (pt._name.substring(0, 2).equals(_PREFIX)) {
			_database.remove(pt);

			pt._name = name;
			_database.put(pt, pt);
		}

		return _database.get(pt);
	}    

	/**
	 * Strict access (read-only of the database)
	 * 
	 * @param x
	 * @param y
	 * @return stored database Object corresponding to (x, y) 
	 */
	public Point get(double x, double y)
	{
		return get(new Point(x, y));
	}	
	public Point get(Point pt)
	{
		Set<Point> points = _database.keySet();

		for (Point point : points) {
			if (point.equals(pt)) return point;
		}

		return null;
	}

	/**
	 * @param x -- single coordinate
	 * @param y -- single coordinate
	 * @return simple containment; no updating
	 */
	public boolean contains(double x, double y) { return contains(new Point(x, y)); }
	public boolean contains(Point p) { return _database.containsKey(p); }

	/**
	 * Constructs the next (complete with prefix) generated name.
	 * Names should be of the form PREFIX + current name
	 *
	 * This method should also invoke updating of the current name
	 * to reflect the 'next' name in the sequence.
     *	 
	 * @return the next complete name in the sequence including prefix.
	 */
	private String getCurrentName()
	{
        String out = _PREFIX + _currentName;

		updateName();

		return out;
	}

	/**
	 * Advances the current generated name to the next letter in the alphabet:
	 * 'A' -> 'B' -> 'C' -> 'Z' --> 'AA' -> 'BB'
	 */
	private void updateName()
	{
		char canonical = _currentName.charAt(0);

		int indexOfNextChar = (ALPHABET.indexOf(canonical) + 1) % ALPHABET.length();
		char c = ALPHABET.charAt(indexOfNextChar);

		if (canonical == END_LETTER) _numLetters++;
		int n = _numLetters;

		_currentName = "";

		for (int i = 0; i < n; i++) {
			_currentName += c;
		}
	}

	/**
	 * @return The entire database of points.
	 */
	public Set<Point> getAllPoints()
	{
        return _database.keySet();
	}

	public void clear() { _database.clear(); }
	public int size() { return _database.size(); }

	@Override
	public String toString()
	{
        String out = "";
		Set<Point> allPoints = getAllPoints();

		for (Point point : allPoints) {
			out += point.toString() + "\n";
		}

		return out;
	}
}