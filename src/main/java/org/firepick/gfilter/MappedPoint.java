package org.firepick.gfilter;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class MappedPoint implements Comparable<MappedPoint>{
    private GCoordinate domain;
    private GCoordinate range;

    @Override
	public String toString() {
		return "MappedPoint [domain=" + domain + ", range=" + range + "]";
	}

	public int compareTo(MappedPoint o) {
		return domain.compareTo(o.domain);
	}

}