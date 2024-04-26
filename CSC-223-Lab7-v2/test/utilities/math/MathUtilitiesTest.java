package utilities.math;

import static org.junit.Assert.*;
import org.junit.Test;

public class MathUtilitiesTest {
    @Test
    public void doubleLessThanTest() {
        double dub0 = 0.0;
        double dub1 = 0.01;
        double dub2 = -0.01;
        double dub3 = 0.0000000000001;
        double dub4 = -0.0000000000001;
        double dub5 = 0.0000011;
        double dub6 = -0.0000011;

        assertFalse(MathUtilities.doubleLessThan(dub0, dub0));

        assertTrue(MathUtilities.doubleLessThan(dub0, dub1));
        assertFalse(MathUtilities.doubleLessThan(dub0, dub2));

        assertFalse(MathUtilities.doubleLessThan(dub0, dub3));
        assertFalse(MathUtilities.doubleLessThan(dub0, dub4));

        assertTrue(MathUtilities.doubleLessThan(dub0, dub5));
        assertFalse(MathUtilities.doubleLessThan(dub0, dub6));
    }

    @Test
    public void doubleGreaterThanTest() {
        double dub0 = 0.0;
        double dub1 = 0.01;
        double dub2 = -0.01;
        double dub3 = 0.0000000000001;
        double dub4 = -0.0000000000001;
        double dub5 = 0.0000011;
        double dub6 = -0.0000011;

        assertFalse(MathUtilities.doubleGreaterThan(dub0, dub0));

        assertFalse(MathUtilities.doubleGreaterThan(dub0, dub1));
        assertTrue(MathUtilities.doubleGreaterThan(dub0, dub2));

        assertFalse(MathUtilities.doubleGreaterThan(dub0, dub3));
        assertFalse(MathUtilities.doubleGreaterThan(dub0, dub4));

        assertFalse(MathUtilities.doubleGreaterThan(dub0, dub5));
        assertTrue(MathUtilities.doubleGreaterThan(dub0, dub6));
    }
}
