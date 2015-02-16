package org.firepick.gfilter.json;

import java.io.IOException;

import org.firepick.gfilter.GCoordinate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class GCoordinateSerializer extends JsonSerializer<GCoordinate> {

	@Override
	public void serialize(GCoordinate value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		double[] values = new double[] { value.getX(), value.getY(), value.getZ() };
		serializers.defaultSerializeValue(values, gen);
	}

	@Override
	public Class<GCoordinate> handledType() {
		return GCoordinate.class;
	}

}
