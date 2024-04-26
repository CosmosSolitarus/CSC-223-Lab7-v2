package preprocessor.delegates;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import geometry_objects.Segment;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;

public class ImplicitPointPreprocessor
{
	/**
	 * It is possible that some of the defined segments intersect
	 * and points that are not named; we need to capture those
	 * points and name them.
	 * 
	 * Algorithm:
	 * 		Check if each segment intersects with any other. If yes,
	 * 		add the intersection point to PointDatabase.
	 */
	public static Set<Point> compute(PointDatabase givenPoints, List<Segment> givenSegments)
	{
		Set<Point> implicitPoints = new LinkedHashSet<Point>();
		int size = givenSegments.size();

        for (int i = 0; i < size; i++) {
			for (int j = i + 1; j < size; j++) {
				Segment a = givenSegments.get(i);
				Segment b = givenSegments.get(j);

				Point pt = a.segmentIntersection(b);
				
				if (pt != null) {
					implicitPoints.add(pt);
					givenPoints.put(pt.getName(), pt.getX(), pt.getY());
				}
			}
		}

		return implicitPoints;
	}

}
