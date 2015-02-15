package org.firepick.gfilter;

import lombok.Getter;

public abstract class GFilter {
	@Getter
	protected String _name;

	protected GFilter() {
		_name = "IGFilter";
	}

}
