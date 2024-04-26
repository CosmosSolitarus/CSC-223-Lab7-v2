package geometry_objects.points;

import static org.junit.Assert.*;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

public class PointNamingFactoryTest {
    @Test
    public void sizeTest() {
        PointNamingFactory pnf = new PointNamingFactory();

        assertEquals(0, pnf.size());

        pnf.put(new Point(0, 0));
        assertEquals(1, pnf.size());

        pnf.put(new Point(0, 0));
        assertEquals(1, pnf.size());

        pnf.put(new Point(0.001, 2));
        assertEquals(2, pnf.size());

        pnf.clear();
        assertEquals(0, pnf.size());

        pnf.put(new Point(-4, 2.005));
        assertEquals(1, pnf.size());
    }
    
    @Test
    public void putPTTest() {
        PointNamingFactory pnf = new PointNamingFactory();

        Point pt0 = new Point("bruh", -2, 0.337);
        Point pt1 = new Point("bro", -2, 0.337);
        Point pt2 = new Point(-6, -12.2);

        assertEquals(0, pnf.size());

        assertEquals(pt0, pnf.put(pt0));
        assertEquals(1, pnf.size());

        assertEquals(pt0, pnf.put(pt0));
        assertEquals(1, pnf.size());

        assertEquals(pt0, pnf.put(pt1));
        assertEquals(1, pnf.size());

        assertEquals(pt2, pnf.put(pt2));
        assertEquals(2, pnf.size());

        assertEquals(new Point(-1, 2), pnf.put(new Point(-1, 2)));
        assertEquals(3, pnf.size());

        assertFalse(pnf.get(pt0).isGenerated());
        assertFalse(pnf.get(pt1).isGenerated());

        System.out.println(pnf.get(pt2));

        assertTrue(pnf.get(pt2).isGenerated());
        assertEquals(pnf.get(pt2)._name, "*_A");
    }

    @Test
    public void putXYest() {
        PointNamingFactory pnf = new PointNamingFactory();

        Point pt0 = new Point("bruh", -2, 0.337);
        Point pt1 = new Point("bro", -2, 0.337);

        assertEquals(0, pnf.size());

        assertEquals(pt0, pnf.put(-2, 0.337));
        assertEquals(1, pnf.size());

        assertEquals(pt0, pnf.put(-2, 0.337));
        assertEquals(1, pnf.size());

        assertEquals(pt1, pnf.put(-2, 0.337));
        assertEquals(1, pnf.size());

        assertEquals(new Point(-1, 2), pnf.put(new Point(-1, 2)));
        assertEquals(2, pnf.size());
    }

    @Test
    public void putTest() {
        PointNamingFactory pnf = new PointNamingFactory();

        Point pt0 = new Point("bruh", -2, 0.337);

        assertEquals(0, pnf.size());

        assertEquals(pt0, pnf.put("", -2, 0.337));
        assertEquals(1, pnf.size());

        assertEquals(pt0, pnf.put("bruh", -2, 0.337));
        assertEquals(1, pnf.size());

        assertEquals(pt0, pnf.put("bruh", -2, 0.337));
        assertEquals(1, pnf.size());

        assertEquals(pt0, pnf.put("bro", -2, 0.337));
        assertEquals(1, pnf.size());

        assertEquals(new Point(-1, 2), pnf.put(new Point("hey", -1, 2)));
        assertEquals(2, pnf.size());
    }

    @Test
    public void getXYTest() {
        PointNamingFactory pnf = new PointNamingFactory();

        Point pt0 = new Point("bruh", -2, 0.337);

        pnf.put(pt0);

        assertEquals(pt0, pnf.get(-2, 0.337));

    }

    @Test
    public void getPTTest() {
        PointNamingFactory pnf = new PointNamingFactory();

        Point pt0 = new Point("bruh", -2, 0.337);
        Point pt1 = new Point("yo-yo", 11, 97);
        Point pt2 = new Point("John", 33, 37);
        Point pt3 = new Point("CS>MATH", 0.6, -4);

        Set<Point> pts = new LinkedHashSet<Point>();

        pnf.put(pt0);
        pnf.put(pt1);
        pnf.put(pt2);
        pnf.put(pt3);

        pts.add(pt0);
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);

        for (Point pt : pts) {
            assertEquals(pt, pnf.get(pt));
        }
    }

    @Test
    public void containsXYTest() {
        PointNamingFactory pnf = new PointNamingFactory();

        Point pt0 = new Point("bruh", -2, 0.337);
        Point pt1 = new Point("yo-yo", 11, 97);
        Point pt2 = new Point("John", 33, 37);
        Point pt3 = new Point("CS>MATH", 0.6, -4);

        Set<Point> pts = new LinkedHashSet<Point>();

        pnf.put(pt0);
        pnf.put(pt1);
        pnf.put(pt2);
        pnf.put(pt3);

        pts.add(pt0);
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);

        for (Point pt : pts) {
            assertTrue(pnf.contains(pt.getX(), pt.getY()));
        }
    }

    @Test
    public void containsPTTest() {
        PointNamingFactory pnf = new PointNamingFactory();

        Point pt0 = new Point("bruh", -2, 0.337);
        Point pt1 = new Point("yo-yo", 11, 97);
        Point pt2 = new Point("John", 33, 37);
        Point pt3 = new Point("CS>MATH", 0.6, -4);

        Set<Point> pts = new LinkedHashSet<Point>();

        pnf.put(pt0);
        pnf.put(pt1);
        pnf.put(pt2);
        pnf.put(pt3);

        pts.add(pt0);
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);

        for (Point pt : pts) {
            assertTrue(pnf.contains(pt));
        }
    }

    @Test
    public void getAllPoints() {
        PointNamingFactory pnf = new PointNamingFactory();

        Point pt0 = new Point("bruh", -2, 0.337);
        Point pt1 = new Point("yo-yo", 11, 97);
        Point pt2 = new Point("John", 33, 37);
        Point pt3 = new Point("CS>MATH", 0.6, -4);

        Set<Point> pts = new LinkedHashSet<Point>();

        pnf.put(pt0);
        pnf.put(pt1);
        pnf.put(pt2);
        pnf.put(pt3);

        pts.add(pt0);
        pts.add(pt1);
        pts.add(pt2);
        pts.add(pt3);

        assertEquals(pts, pnf.getAllPoints());
    }

    @Test
    public void getCurrentNameTest() {
        String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int len = ALPHABET.length();

        PointNamingFactory pnf = new PointNamingFactory();
        Point pt;

        for (int i = 0; i < 100; i++) {
            pt = new Point(i, i+1);

            pnf.put(pt);

            assertTrue(pnf.get(pt).isGenerated());
            assertEquals(pnf.get(pt)._name, "*_" + String.valueOf(ALPHABET.charAt(i % len)).repeat((i / len) + 1));
        }
    }
}
