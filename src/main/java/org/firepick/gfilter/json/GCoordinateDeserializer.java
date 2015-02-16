package org.firepick.gfilter.json;

import java.io.IOException;

import org.firepick.gfilter.GCoordinate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class GCoordinateDeserializer extends JsonDeserializer<GCoordinate> {

	@Override
	public GCoordinate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		GCoordinate gCoordinate = new GCoordinate();
		double[] readValue = ctxt.readValue(p, double[].class);
		gCoordinate.setX(readValue[0]);
		gCoordinate.setY(readValue[1]);
		gCoordinate.setZ(readValue[2]);
		return gCoordinate;
	}

	@Override
	public Class<?> handledType() {
		return GCoordinate.class;
	}
}
