package org.firepick.gfilter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import lombok.extern.log4j.Log4j;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Log4j
public class MappedPointFilterTest {

	@Test
	public void testMappedPointFilter() throws JsonParseException, JsonMappingException, IOException {
		InputStream inputStream = JSONTest.class.getResourceAsStream("/fiducial.json");
		InputStreamReader reader = new InputStreamReader(inputStream);
		MappedPointFilter mappedPointFilter = new MappedPointFilter(null, reader);
		GCoordinate coordinate = new GCoordinate(0,0,0);
		GCoordinate interpolate = mappedPointFilter.interpolate(coordinate);
		log.debug("Interpolated: " + interpolate);
	}
}
