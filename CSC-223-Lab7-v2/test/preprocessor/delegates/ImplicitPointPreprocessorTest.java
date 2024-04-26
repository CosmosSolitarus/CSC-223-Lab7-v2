package preprocessor.delegates;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import components.FigureNode;
import geometry_objects.Segment;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import input.InputFacade;

public class ImplicitPointPreprocessorTest {
    @Test
    public void computeTest() {
		// A------B
        // | \  / |
        // |  X*  |
        // |/    \|
        // D------C

        FigureNode fig = InputFacade.extractFigure("square_four_interior_implied.json");

		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation(fig);

		PointDatabase points = pair.getKey();

		Set<Segment> segments = pair.getValue();

        Set<Point> impliedPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<>(segments));
        Point implied = new Point(2.5, 2.5);

        assertEquals(1, impliedPoints.size());
        assertTrue(impliedPoints.contains(implied));
    }
}
