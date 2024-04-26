package preprocessor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import preprocessor.delegates.ImplicitPointPreprocessor;
import geometry_objects.Segment;

public class Preprocessor
{
	// The explicit points provided to us by the user.
	// This database will also be modified to include the implicit
	// points (i.e., all points in the figure).
	protected PointDatabase _pointDatabase;

	// Minimal ('Base') segments provided by the user
	protected Set<Segment> _givenSegments;

	// The set of implicitly defined points caused by segments
	// at implicit points.
	protected Set<Point> _implicitPoints;

	// The set of implicitly defined segments resulting from implicit points.
	protected Set<Segment> _implicitSegments;

	// Given all explicit and implicit points, we have a set of
	// segments that contain no other subsegments; these are minimal ('base') segments
	// That is, minimal segments uniquely define the figure.
	protected Set<Segment> _allMinimalSegments;

	// A collection of non-basic segments
	protected Set<Segment> _nonMinimalSegments;

	// A collection of all possible segments: maximal, minimal, and everything in between
	// For lookup capability, we use a map; each <key, value> has the same segment object
	// That is, key == value. 
	protected Map<Segment, Segment> _segmentDatabase;
	public Map<Segment, Segment> getAllSegments() { return _segmentDatabase; }

	public Preprocessor(PointDatabase points, Set<Segment> segments)
	{
		_pointDatabase  = points;
		_givenSegments = segments;
		
		_segmentDatabase = new HashMap<Segment, Segment>();
		
		analyze();
	}

	/**
	 * Invoke the precomputation procedure.
	 */
	public void analyze()
	{
		//
		// Implicit Points
		//
		_implicitPoints = ImplicitPointPreprocessor.compute(_pointDatabase, _givenSegments.stream().toList());

		//
		// Implicit Segments attributed to implicit points
		//
		_implicitSegments = computeImplicitBaseSegments(_implicitPoints);

		//
		// Combine the given minimal segments and implicit segments into a true set of minimal segments
		//     *givenSegments may not be minimal
		//     * implicitSegmen
		//
		_allMinimalSegments = identifyAllMinimalSegments(_implicitPoints, _givenSegments, _implicitSegments);

		//
		// Construct all segments inductively from the base segments
		//
		_nonMinimalSegments = constructAllNonMinimalSegments(_allMinimalSegments);

		//
		// Combine minimal and non-minimal into one package: our database
		//
		_allMinimalSegments.forEach((segment) -> _segmentDatabase.put(segment, segment));
		_nonMinimalSegments.forEach((segment) -> _segmentDatabase.put(segment, segment));
	}
	
	/**
	 * If two segments cross at an unnamed point, the result is an implicit point.
	 * 
	 * This new implicit point may be found on any of the existing segments (possibly
	 * with others implicit points on the same segment).
	 * This results in new, minimal sub-segments.
	 *
	 * For example,   A----*-------*----*---B will result in 4 new base segments.
	 *
	 * @param impPoints -- implicit points computed from segment intersections
	 * @return a set of implicitly defined segments
	 */
	protected Set<Segment> computeImplicitBaseSegments(Set<Point> impPoints)
	{
		Set<Segment> impSegments = new HashSet<Segment>();
		SortedSet<Point> points = new TreeSet<Point>();

		for (Segment segment : _givenSegments) {
			for (Point point : impPoints) {
				if (segment.pointLiesOn(point)) {
					points.add(point);
				}
			}
			
			if (points.size() != 0) {
				points.add(segment.getPoint1());
				points.add(segment.getPoint2());

				impSegments.addAll(makeSegments(points));

				points.clear();
			}
		}

		return impSegments;
	}
	
	/**
	 * A set of ordered points:
	 * 
	 *  1          2      3          4        5 
	 *
	 * in which we will create the corresponding base segments:
	 * 
	 *  1----------2------3----------4--------5
	 *   
	 * @param points -- an ordered list of points
	 * @return a set of n-1 segments between all points provided
	 */
	protected Set<Segment> makeSegments(SortedSet<Point> points)
	{
        Set<Segment> segments = new HashSet<>();

		while (points.size() > 1) {
			Point pt1 = points.first();
			points.removeFirst();

			Point pt2 = points.first();

			segments.add(new Segment(pt1, pt2));
		}
		
		return segments;
	}
	
	/**
	 * From the 'given' segments we remove any non-minimal segment.
	 * 
	 * @param impPoints -- the implicit points for the figure
	 * @param givenSegments -- segments provided by the user
	 * @param minimalImpSegments -- minimal implicit segments computed from the implicit points
	 * @return -- a 
	 */

	// Given the entire set of segments (givenSegments) ... remove any that are not minimal
	protected Set<Segment> identifyAllMinimalSegments(Set<Point> impPoints,
			Set<Segment> givenSegments,
			Set<Segment> minimalImpSegments)
	{
		Set<Segment> minimal = new HashSet<Segment>(minimalImpSegments);
		
		for (Segment segment : givenSegments) {
			minimal.add(segment);

			for (Point point : impPoints) {
				if (segment.pointLiesBetweenEndpoints(point)) {
					minimal.remove(segment);
					break;
				}
			}
		}

		return minimal;
	}
	
	/**
	 * Given a set of minimal segments, build non-minimal segments by appending
	 * minimal segments (one at a time).
	 * 
	 * (Recursive construction of segments.)
	 */
	public Set<Segment> constructAllNonMinimalSegments(Set<Segment> minimalSegs)
	{
		if (minimalSegs == null) return null;
		
		Set<Segment> nonMinimalSegs = new HashSet<Segment>();

		constructAllNonMinimalSegments(minimalSegs, minimalSegs.stream().toList(), nonMinimalSegs);

		return nonMinimalSegs;
	}

	// non minimal means it DOES contain other implicit or explicit points along the line
	// *-------*----------*
	// A       B          C    ... line AC is NON minimal, segments AB and BC are minimal
	private void constructAllNonMinimalSegments(Set<Segment> lastLevelSegs, List<Segment> minimalSegs, Set<Segment> nonMinimalSegs)
	{
		//System.out.println("--------- LAST -----------");

		for (Segment segment : lastLevelSegs) {
			//System.out.println("seg: " + segment.toString());
		}
		
		//System.out.println("--------- MINI -----------");

		for (Segment segment : minimalSegs) {
			//System.out.println("seg: " + segment.toString());
		}

		Segment potentialSegment;
		boolean changed = false;

		for (Segment segment : lastLevelSegs) {
			for (Segment minSeg : minimalSegs) {
				potentialSegment = combineToNewSegment(minSeg, segment);

				//System.out.println("========");
				//System.out.println("lastSeg: " + segment.toString());
				//System.out.println("minSeg: " + minSeg.toString());
				
				if (potentialSegment != null && !lastLevelSegs.contains(potentialSegment)) {
					//System.out.println("!! Seg added !!");
					nonMinimalSegs.add(potentialSegment);
					changed = true;
				}
			}
		}

		if (!changed) {
			return;
		}

		constructAllNonMinimalSegments(nonMinimalSegs, minimalSegs, nonMinimalSegs);
	}
	
	//
	// Our goal is to stitch together segments that are on the same line:
	//                       A---------B----------C
	// resulting in the segment AC.
	//
	// To do so we ask:
	//    * Are each segment on the same (infinite) line?
	//    * If so, do they share an endpoint?
	// If both criteria are satisfied we have a new segment.
	private Segment combineToNewSegment(Segment left, Segment right)
	{	
		// 1 - check equality
		if (left.equals(right)) return left;
		
		// 2 - check collinearity
		if (!left.isCollinearWith(right)) return null;

		// 3 - check for shared point
		Point shared = left.sharedVertex(right);

		if (shared == null) return null;

		// 4 - check if one is subsegment of other
		if (left.HasSubSegment(right)) return left;
		if (right.HasSubSegment(left)) return right;

		// 5 - find points of new segment and return
		return new Segment(left.other(shared), right.other(shared));
	}
}
