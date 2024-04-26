package geometry_objects;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.SortedSet;

import org.junit.Test;

import geometry_objects.points.Point;

public class SegmentTest {
    @Test
    public void hasSubSegmentTest() {
        /**
         * 
         *      E
         *      |
         *      |
         *      A-----B-----C-----D
         *    (0,0)
         * 
         */

        Point a = new Point(0, 0);
        Point b = new Point(1, 0);
        Point c = new Point(2, 0);
        Point d = new Point(3, 0);
        Point e = new Point(0, 1);

        Segment ab = new Segment(a, b);
        Segment ad = new Segment(a, d);
        Segment ae = new Segment(a, e);
        Segment bc = new Segment(b, c);
        Segment bd = new Segment(b, d);

        // null test
        assertFalse(ab.HasSubSegment(null));

        // self test
        assertTrue(ab.HasSubSegment(ab));

        // subsegment; no shared vertex
        assertTrue(ad.HasSubSegment(bc));
        assertFalse(bc.HasSubSegment(ad));

        // subsegment; one shared vertex
        assertTrue(ad.HasSubSegment(bd));
        assertFalse(bd.HasSubSegment(ad));

        // disjoint
        assertFalse(ab.HasSubSegment(ae));
        assertFalse(ae.HasSubSegment(ab));
    }

    @Test
    public void sharedVertexTest() {
        /**
         * 
         *      E
         *      |
         *      |
         *      A-----B-----C-----D
         *    (0,0)
         * 
         */

        Point a = new Point(0, 0);
        Point b = new Point(1, 0);
        Point c = new Point(2, 0);
        Point d = new Point(3, 0);
        Point e = new Point(0, 1);

        Segment ab = new Segment(a, b);
        Segment ac = new Segment(a, c);
        Segment ad = new Segment(a, d);
        Segment ae = new Segment(a, e);
        Segment bc = new Segment(b, c);
        Segment bd = new Segment(b, d);
        Segment cd = new Segment(c, d);

        // null test
        assertNull(ab.sharedVertex(null));

        // self test
        assertNull(ab.sharedVertex(ab));

        // one shared vertex
        assertEquals(a, ab.sharedVertex(ac));
        assertEquals(a, ab.sharedVertex(ad));
        assertEquals(a, ab.sharedVertex(ae));
        assertEquals(b, ab.sharedVertex(bc));
        assertEquals(b, bc.sharedVertex(bd));

        // no shared vertex
        assertNull(ab.sharedVertex(cd));
        assertNull(ae.sharedVertex(bc));
        assertNull(ae.sharedVertex(cd));
        assertNull(ae.sharedVertex(bd));
    }

    @Test
    public void coincideWithoutOverlap() {
        /**
         * 
         *      E
         *      |
         *      |
         *      A-----B-----C-----D
         *    (0,0)
         * 
         */

        Point a = new Point(0, 0);
        Point b = new Point(1, 0);
        Point c = new Point(2, 0);
        Point d = new Point(3, 0);
        Point e = new Point(0, 1);

        Segment ab = new Segment(a, b);
        Segment ac = new Segment(a, c);
        Segment ad = new Segment(a, d);
        Segment ae = new Segment(a, e);
        Segment bc = new Segment(b, c);
        Segment bd = new Segment(b, d);
        Segment cd = new Segment(c, d);

        // null test
        assertFalse(ab.coincideWithoutOverlap(null));

        // self test
        assertFalse(ab.coincideWithoutOverlap(ab));

        // not collinear; share no points
        assertFalse(ae.coincideWithoutOverlap(bc));
        assertFalse(ae.coincideWithoutOverlap(bd));
        assertFalse(ae.coincideWithoutOverlap(cd));

        assertFalse(bc.coincideWithoutOverlap(ae));
        assertFalse(bd.coincideWithoutOverlap(ae));
        assertFalse(cd.coincideWithoutOverlap(ae));

        // not collinear; share 1 point
        assertFalse(ae.coincideWithoutOverlap(ab));
        assertFalse(ae.coincideWithoutOverlap(ac));
        assertFalse(ae.coincideWithoutOverlap(ad));

        assertFalse(ab.coincideWithoutOverlap(ae));
        assertFalse(ac.coincideWithoutOverlap(ae));
        assertFalse(ad.coincideWithoutOverlap(ae));

        // collinear; share no points; overlap
        assertFalse(ad.coincideWithoutOverlap(bc));

        assertFalse(bc.coincideWithoutOverlap(ad));

        // collinear; share no points; no overlap
        assertTrue(ab.coincideWithoutOverlap(cd));

        assertTrue(cd.coincideWithoutOverlap(ab));

        // collinear; share 1 point; overlap
        assertFalse(ad.coincideWithoutOverlap(ab));
        assertFalse(ad.coincideWithoutOverlap(ac));
        assertFalse(ad.coincideWithoutOverlap(bd));
        assertFalse(ad.coincideWithoutOverlap(cd));

        assertFalse(ac.coincideWithoutOverlap(ab));
        assertFalse(ac.coincideWithoutOverlap(bc));

        assertFalse(bd.coincideWithoutOverlap(bc));
        assertFalse(bd.coincideWithoutOverlap(cd));

        // collinear; share 1 point; no overlap
        assertTrue(ab.coincideWithoutOverlap(bc));
        assertTrue(ab.coincideWithoutOverlap(bd));

        assertTrue(bc.coincideWithoutOverlap(cd));
    }

    @Test
    public void collectOrderedPointsOnSegmentTest() {
        /**
	     *                             Q *
	     *
	     *                A-------B-------C------D     E
	     *
	     *      * Z
	     *
	     *  Given:
         *	    Segment(A, D) and points {A, B, C, D, E, Q, Z},
	     *      this method will return the set {A, B, C, D} in this order
         *      since it is lexicographically sorted.
	     *
	     *      Points Q, Z, and E are NOT on the segment.
	     */

        Point a = new Point(0, 0);
        Point b = new Point(1, 0);
        Point c = new Point(2, 0);
        Point d = new Point(3, 0);
        Point e = new Point(4, 0);
        Point q = new Point(2, 1);
        Point z = new Point(-1, -1);

        Segment ad = new Segment(a, d);

        HashSet<Point> points = new HashSet<>();

        points.add(a);
        points.add(b);
        points.add(c);
        points.add(d);
        points.add(e);
        points.add(q);
        points.add(z);

        SortedSet<Point> pointsOnSegment = ad.collectOrderedPointsOnSegment(points);

        HashSet<Point> expectedPoints = new HashSet<>();

        expectedPoints.add(a);
        expectedPoints.add(b);
        expectedPoints.add(c);
        expectedPoints.add(d);

        assertEquals(expectedPoints.size(), pointsOnSegment.size());

        for (Point expectedPoint : expectedPoints) {
            assertTrue(pointsOnSegment.contains(expectedPoint));
        }
    }
}
