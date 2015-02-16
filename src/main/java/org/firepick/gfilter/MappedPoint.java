package org.firepick.gfilter;

import org.firepick.gfilter.json.GCoordinateDeserializer;
import org.firepick.gfilter.json.GCoordinateSerializer;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
public class MappedPoint implements Comparable<MappedPoint> {
	@JsonSerialize(using = GCoordinateSerializer.class)
	@JsonDeserialize(using = GCoordinateDeserializer.class)
	private GCoordinate domain;
	@JsonSerialize(using = GCoordinateSerializer.class)
	@JsonDeserialize(using = GCoordinateDeserializer.class)
	private GCoordinate range;

	@Override
	public String toString() {
		return "MappedPoint [domain=" + domain + ", range=" + range + "]";
	}

	public int compareTo(MappedPoint o) {
		return domain.compareTo(o.domain);
	}

}