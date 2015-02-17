package org.firepick.gfilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import lombok.extern.log4j.Log4j;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Log4j
public class MappedPointFilterTest {

	private static GCoordinate ORIGIN = new GCoordinate(0, 0, 0);

	@Test
	public void testMappedPointFilterConfigure() throws JsonParseException, JsonMappingException, IOException {
		InputStream inputStream = JSONTest.class.getResourceAsStream("/fiducial.json");
		InputStreamReader reader = new InputStreamReader(inputStream);
		MappedPointFilter mappedPointFilter = new MappedPointFilter(null, reader);
		GCoordinate coordinate = new GCoordinate(0, 0, 0);
		GCoordinate interpolate = mappedPointFilter.interpolate(coordinate);
		log.debug("Interpolated: " + interpolate);
	}

	void assertGCoordinate(GCoordinate expected, GCoordinate actual) {
		double dist2 = expected.distance2(actual);
		if (dist2 > 1e-10) {
			log.debug("assertGCoordinate expected: " + expected + " actual: " + actual);
		}
		Assert.assertTrue(dist2 < 1e-10);
	}

	@Test
	public void testMappedPointFilter() {
	    MappedPointFilter xyz = new MappedPointFilter(null);

	    ArrayList<MappedPoint> neighborhood = new ArrayList<MappedPoint>();

	    neighborhood = xyz.domainNeighborhood(ORIGIN, 2);
	    Assert.assertEquals(0 , neighborhood.size());

	    Assert.assertEquals(0 , xyz.getDomainRadius(), 0);
	    xyz.mapPoint(new GCoordinate(1,1,1), new GCoordinate(1.1,1.1,1.1));
	    Assert.assertEquals(Math.sqrt(3) , xyz.getDomainRadius(), 0);

	    neighborhood = xyz.domainNeighborhood(ORIGIN, 2);
	    Assert.assertEquals(1, neighborhood.size());
	    Assert.assertEquals(new GCoordinate(1,1,1) , neighborhood.get(0).getDomain());
	    Assert.assertEquals(new GCoordinate(1.1,1.1,1.1) , neighborhood.get(0).getRange());
	    Assert.assertEquals(new GCoordinate(1.1,1.1,1.1) , xyz.interpolate(new GCoordinate(1,1,1)));

	    /// Verify that single MappedPoint specifies a universal mapping
	    assertGCoordinate(new GCoordinate(-99.9,100.1,0.101), xyz.interpolate(new GCoordinate(-100,100,.001)));
	    assertGCoordinate(new GCoordinate(0.1,0.1,1.1), xyz.interpolate(new GCoordinate(0,0,1)));

	    // Test neighborhood calculation distance
	    xyz.mapPoint(new GCoordinate(2,2,2), new GCoordinate(-.1,-.1,.1));
	    neighborhood = xyz.domainNeighborhood(ORIGIN, 2);
	    Assert.assertEquals(1 , neighborhood.size());
	    Assert.assertEquals(new GCoordinate(1,1,1) , neighborhood.get(0).getDomain());
	    neighborhood = xyz.domainNeighborhood(new GCoordinate(2,2,2) , 2);
	    Assert.assertEquals(2 , neighborhood.size());
	    neighborhood = xyz.domainNeighborhood(new GCoordinate(3,3,3) , 2);
	    Assert.assertEquals(1 , neighborhood.size());
	    Assert.assertEquals(new GCoordinate(2,2,2) , neighborhood.get(0).getDomain());

//	    cout << "Verify that point mappings can be changed..." << endl;
	    xyz.mapPoint(new GCoordinate(1,1,1), new GCoordinate(.1,.01,.001));
	    neighborhood = xyz.domainNeighborhood(ORIGIN, 2);
	    for (int i = 0; i < neighborhood.size(); i++) {
	    	log.debug("" + neighborhood.get(i));
	    }
	    Assert.assertEquals(1 , neighborhood.size());
	    Assert.assertEquals(new GCoordinate(1,1,1) , neighborhood.get(0).getDomain());
	    Assert.assertEquals(new GCoordinate(.1,.01,.001) , neighborhood.get(0).getRange());

//	    cout << "Testing unit lattice from (1,1,1) to (2,2,2)" << endl;
	    xyz.mapPoint(new GCoordinate(1,1,1), new GCoordinate(.1,.01,.001));
	    xyz.mapPoint(new GCoordinate(1,1,2), new GCoordinate(.1,.01,.002));
	    xyz.mapPoint(new GCoordinate(1,2,1), new GCoordinate(.1,.02,.001));
	    xyz.mapPoint(new GCoordinate(1,2,2), new GCoordinate(.1,.02,.002));
	    xyz.mapPoint(new GCoordinate(2,1,1), new GCoordinate(.2,.01,.001));
	    xyz.mapPoint(new GCoordinate(2,1,2), new GCoordinate(.2,.01,.002));
	    xyz.mapPoint(new GCoordinate(2,2,1), new GCoordinate(.2,.02,.001));
	    xyz.mapPoint(new GCoordinate(2,2,2), new GCoordinate(.2,.02,.002));
	    Assert.assertEquals(Math.sqrt(3), xyz.getDomainRadius(), 0);
	    assertGCoordinate(new GCoordinate(0.15,0.015,0.0015), xyz.interpolate(new GCoordinate(1.5,1.5,1.5)));
	    assertGCoordinate(new GCoordinate(0.1,0.01,0.0015), xyz.interpolate(new GCoordinate(1,1,1.5)));
	    assertGCoordinate(new GCoordinate(-20,-20,-20), xyz.interpolate(new GCoordinate(-20,-20,-20)));
	    assertGCoordinate(new GCoordinate(1,1,-2), xyz.interpolate(new GCoordinate(1,1,-2)));
	    assertGCoordinate(new GCoordinate(.2,0.01,.002), xyz.interpolate(new GCoordinate(2,1,2)));
	    assertGCoordinate(new GCoordinate(.21,0.021,.0021), xyz.interpolate(new GCoordinate(2.1,2.1,2.1)));
	    assertGCoordinate(new GCoordinate(0.2,0.02,0.002), xyz.interpolate(new GCoordinate(2.9,2.9,2.9))); // N=1

//	    cout << "Testing neighborhood starvation with points exterior to lattice" << endl;
	    assertGCoordinate(new GCoordinate(0.19,0.019,0.0021), xyz.interpolate(new GCoordinate(1.9,1.9,2.1))); // N=8
	    // Degenerate tetrahedron (note sudden and unfortunate transition in interpolated values)
	    assertGCoordinate(new GCoordinate(0.165677,0.0165677,0.002), xyz.interpolate(new GCoordinate(1.9,1.9,2.4))); // N=7
	    assertGCoordinate(new GCoordinate(0.163,0.0163,0.002), xyz.interpolate(new GCoordinate(1.9,1.9,2.5))); // N=5
	    assertGCoordinate(new GCoordinate(0.159278,0.0159278,0.002), xyz.interpolate(new GCoordinate(1.9,1.9,2.7))); // N=5
	    assertGCoordinate(new GCoordinate(0.157965,0.0157965,0.002), xyz.interpolate(new GCoordinate(1.9,1.9,2.8))); // N=4
	    assertGCoordinate(new GCoordinate(0.156899,0.0156899,0.002), xyz.interpolate(new GCoordinate(1.9,1.9,2.9))); // N=4
	    assertGCoordinate(new GCoordinate(0.156024,0.0156024,0.002), xyz.interpolate(new GCoordinate(1.9,1.9,3.0))); // N=4
	    assertGCoordinate(new GCoordinate(0.169175,0.0169175,0.002), xyz.interpolate(new GCoordinate(1.9,1.9,3.2))); // N=3
	    assertGCoordinate(new GCoordinate(0.168862,0.0168862,0.002), xyz.interpolate(new GCoordinate(1.9,1.9,3.3))); // N=3
	    assertGCoordinate(new GCoordinate(0.168602,0.0168602,0.002), xyz.interpolate(new GCoordinate(1.9,1.9,3.4))); // N=3
	    assertGCoordinate(new GCoordinate(0.2,0.02,0.002), xyz.interpolate(new GCoordinate(1.9,1.9,3.5))); // N=1
	}
}
