package org.firepick.gfilter;

import java.io.IOException;
import java.util.ArrayList;

import lombok.extern.log4j.Log4j;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Log4j
public class JSONTest {

	@Test
	public void generate() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		// SimpleModule testModule = new SimpleModule("MyModule");
		// testModule.addSerializer(new GCoordinateSerializer());
		// mapper.registerModule(testModule);
		ArrayList<MappedPoint> list = new ArrayList<MappedPoint>();
		list.add(new MappedPoint(new GCoordinate(1, 2, 3), new GCoordinate(4, 5, 6)));
		Object json = mapper.writeValueAsString(list);
		log.debug("Result:\n" + json);

	}

	@Test
	public void parse() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		// SimpleModule testModule = new SimpleModule("MyModule");
		//
		// testModule.addDeserializer(GCoordinate.class, new GCoordinateDeserializer());
		// mapper.registerModule(testModule);
		String json = "{\"domain\":[-6.0,-140.0,0], \"range\":[-5,10,0]}";
		MappedPoint mappedPoint = mapper.readValue(json, MappedPoint.class);
		log.debug("Result:\n" + mappedPoint);

	}
}
