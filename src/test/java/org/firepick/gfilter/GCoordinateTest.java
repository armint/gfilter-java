package org.firepick.gfilter;

import org.junit.Assert;
import org.junit.Test;

public class GCoordinateTest {

	@Test
	public void testConstructor() {
		Assert.assertEquals(new GCoordinate(), new GCoordinate(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
		Assert.assertEquals(new GCoordinate(1, 2, 3), new GCoordinate(1.0, 2.0, 3.0));
		Assert.assertNotEquals(new GCoordinate(2, 2, 2), new GCoordinate(1.0, 2.0, 3.0));
	}

	@Test
	public void testSimpleOperators() {
		Assert.assertEquals(new GCoordinate(3, 3, 3), new GCoordinate(-1, 0, 1).add(new GCoordinate(4, 3, 2)));
		Assert.assertEquals(new GCoordinate(3, 3, 3), new GCoordinate(4, 3, 2).subtract(new GCoordinate(1, 0, -1)));
		Assert.assertEquals(new GCoordinate(3, 30, 300), new GCoordinate(1, 10, 100).preMultiply(3.0));
	}

	@Test
	public void testBarycentric() {
		GCoordinate a = new GCoordinate(0, 1, 2);
		GCoordinate b = new GCoordinate(0, 1, 3);
		GCoordinate c = new GCoordinate(0, 2, 3);
		GCoordinate d = new GCoordinate(-1, 0, 0);

		Assert.assertEquals(new GCoordinate(1, 0, 0), new GCoordinate(a).barycentric(a, b, c, d));
		Assert.assertEquals(new GCoordinate(0, 1, 0), new GCoordinate(b).barycentric(a, b, c, d));
		Assert.assertEquals(new GCoordinate(0, 0, 1), new GCoordinate(c).barycentric(a, b, c, d));
		Assert.assertEquals(new GCoordinate(0, 0, 0), new GCoordinate(d).barycentric(a, b, c, d));
	}

	@Test
	public void testDistance2() {
		GCoordinate a = new GCoordinate(0, 1, 2);
		GCoordinate b = new GCoordinate(0, 1, 3);
		GCoordinate c = new GCoordinate(0, 2, 3);
		GCoordinate d = new GCoordinate(-1, 0, 0);

		GCoordinate pt1 = new GCoordinate(0, 1.5, 2.6);
		double pt1_d2 = new GCoordinate(.4, .1, .5).distance2(pt1.barycentric(a, b, c, d));
		Assert.assertTrue(pt1_d2 < 1e-30);
		GCoordinate pt2 = new GCoordinate(-.01, 1.5, 2.6);
		Assert.assertTrue(new GCoordinate(0.37, 0.11, 0.51).distance2(pt2.barycentric(a, b, c, d)) < 1e-10);
		GCoordinate pt3 = new GCoordinate(+.01, 1.5, 2.6);
		Assert.assertTrue(new GCoordinate(0.43, 0.09, 0.49).distance2(pt3.barycentric(a, b, c, d)) < 1e-10);
	}
}
