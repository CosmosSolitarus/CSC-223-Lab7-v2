package preprocessor;

import static org.junit.Assert.*;
import org.junit.Test;

import components.FigureNode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import geometry_objects.Segment;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import input.InputFacade;
import preprocessor.delegates.ImplicitPointPreprocessor;

public class PreprocessorTest
{
	@Test
	public void test_implicit_crossings()
	{
		FigureNode fig = InputFacade.extractFigure("figures\\fully_connected_irregular_polygon.json");

		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation(fig);

		PointDatabase points = pair.getKey();

		Set<Segment> segments = pair.getValue();

		Preprocessor pp = new Preprocessor(points, segments);

		// 5 new implied points inside the pentagon
		Set<Point> iPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<Segment>(segments));
		assertEquals(5, iPoints.size());

		//
		//
		//		               D(3, 7)
		//
		//
		//   E(-2,4)       D*      E*      C(6, 3)
		//		         C*          A*       
		//                      B*
		//		       A(2,0)        B(4, 0)
		//
		//		    An irregular pentagon with 5 C 2 = 10 segments

		Point a_star = new Point(56.0 / 15, 28.0 / 15);
		Point b_star = new Point(16.0 / 7, 8.0 / 7);
		Point c_star = new Point(8.0 / 9, 56.0 / 27);
		Point d_star = new Point(90.0 / 59, 210.0 / 59);
		Point e_star = new Point(194.0 / 55, 182.0 / 55);

		assertTrue(iPoints.contains(a_star));
		assertTrue(iPoints.contains(b_star));
		assertTrue(iPoints.contains(c_star));
		assertTrue(iPoints.contains(d_star));
		assertTrue(iPoints.contains(e_star));

		//
		// There are 15 implied segments inside the pentagon; see figure above
		//
		Set<Segment> iSegments = pp.computeImplicitBaseSegments(iPoints);

		assertEquals(15, iSegments.size());

		List<Segment> expectedISegments = new ArrayList<Segment>();

		expectedISegments.add(new Segment(points.getPoint("A"), c_star));
		expectedISegments.add(new Segment(points.getPoint("A"), b_star));

		expectedISegments.add(new Segment(points.getPoint("B"), b_star));
		expectedISegments.add(new Segment(points.getPoint("B"), a_star));

		expectedISegments.add(new Segment(points.getPoint("C"), a_star));
		expectedISegments.add(new Segment(points.getPoint("C"), e_star));

		expectedISegments.add(new Segment(points.getPoint("D"), d_star));
		expectedISegments.add(new Segment(points.getPoint("D"), e_star));

		expectedISegments.add(new Segment(points.getPoint("E"), c_star));
		expectedISegments.add(new Segment(points.getPoint("E"), d_star));

		expectedISegments.add(new Segment(c_star, b_star));
		expectedISegments.add(new Segment(b_star, a_star));
		expectedISegments.add(new Segment(a_star, e_star));
		expectedISegments.add(new Segment(e_star, d_star));
		expectedISegments.add(new Segment(d_star, c_star));

		for (Segment iSegment : iSegments)
		{
			assertTrue(expectedISegments.contains(iSegment));
		}

		//
		// Ensure we have ALL minimal segments: 20 in this figure.
		//
		List<Segment> expectedMinimalSegments = new ArrayList<Segment>(iSegments);
		expectedMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("B")));
		expectedMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("C")));
		expectedMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("D")));
		expectedMinimalSegments.add(new Segment(points.getPoint("D"), points.getPoint("E")));
		expectedMinimalSegments.add(new Segment(points.getPoint("E"), points.getPoint("A")));
		
		Set<Segment> minimalSegments = pp.identifyAllMinimalSegments(iPoints, segments, iSegments);
		assertEquals(expectedMinimalSegments.size(), minimalSegments.size());

		for (Segment minimalSeg : minimalSegments)
		{
			System.out.println(minimalSeg.toString());
			assertTrue(expectedMinimalSegments.contains(minimalSeg));
		}
		
		//
		// Construct ALL figure segments from the base segments
		//
		Set<Segment> computedNonMinimalSegments = pp.constructAllNonMinimalSegments(minimalSegments);
		
		//
		// All Segments will consist of the new 15 non-minimal segments.
		//
		assertEquals(15, computedNonMinimalSegments.size());

		//
		// Ensure we have ALL minimal segments: 20 in this figure.
		//
		List<Segment> expectedNonMinimalSegments = new ArrayList<Segment>();
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), d_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), c_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("D")));
		
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), c_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), b_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("E")));
		
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), d_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), e_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("E")));		

		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), a_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), b_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("C")));
		
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), e_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), a_star));
		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("D")));
		
		//
		// Check size and content equality
		//
		assertEquals(expectedNonMinimalSegments.size(), computedNonMinimalSegments.size());

		for (Segment computedNonMinimalSegment : computedNonMinimalSegments)
		{
			assertTrue(expectedNonMinimalSegments.contains(computedNonMinimalSegment));
		}
	}

	@Test
	public void computeImplicitBaseSegmentsTest() {
		fail();
	}

	@Test
	public void makeSegmentsTest() {
		/**
		 * 		B-------C     E
		 * 		|		|    /
		 * 		|		|   /
		 * 		A		|  /
		 * 	   (0,0)	| /
		 * 				|/
		 * 				D
		 */

		Point a = new Point(0, 0);
		Point b = new Point(0, 1);
		Point c = new Point(1, 1);
		Point d = new Point(1, -2);
		Point e = new Point(2, 1);

		List<Point> points = new ArrayList<>();

		points.add(a);
		points.add(b);
		points.add(c);
		points.add(d);
		points.add(e);

		SortedSet<Point> sortedPoints = new TreeSet(points);

		Set<Segment> segments = Preprocessor.makeSegments(sortedPoints);

		Set<Segment> expectedSegments = new HashSet<>();

		Segment ab = new Segment(a, b);
		Segment bc = new Segment(b, c);
		Segment cd = new Segment(c, d);
		Segment de = new Segment(d, e);

		expectedSegments.add(ab);
		expectedSegments.add(bc);
		expectedSegments.add(cd);
		expectedSegments.add(de);

		assertEquals(expectedSegments.size(), segments.size());

		for (Segment segment : expectedSegments) {
			assertTrue(segments.contains(segment));
		}
	}

	@Test
	public void identifyAllMinimalSegmentsTest() {
		fail();
	}

	@Test
	public void constructAllNonMinimalSegmentsTest() {
		fail();
	}

	@Test
	public void combineToNewSegmentTest() {
		// A----B----C----D
		Point a = new Point("A", 0, 0);
		Point b = new Point("B", 1, 0);
		Point c = new Point("C", 2, 0);
		Point d = new Point("D", 3, 0);

		Segment ab = new Segment(a, b);
		Segment ac = new Segment(a, c);
		Segment ad = new Segment(a, d);
		Segment bc = new Segment(b, c);
		Segment bd = new Segment(b, d);
		Segment cd = new Segment(c, d);

		assertEquals(ac, Preprocessor.combineToNewSegment(ab, ac));
		assertEquals(ac, Preprocessor.combineToNewSegment(ac, ab));
		assertEquals(ad, Preprocessor.combineToNewSegment(ab, bd));
		assertEquals(ad, Preprocessor.combineToNewSegment(ac, cd));
		assertEquals(bc, Preprocessor.combineToNewSegment(bc, bc));
	}
}