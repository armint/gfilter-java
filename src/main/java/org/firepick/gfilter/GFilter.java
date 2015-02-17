package org.firepick.gfilter;

import lombok.Getter;

public abstract class GFilter {
	@Getter
	protected String _name;

	protected GFilter() {
		_name = "IGFilter";
	}
	
//	public abstract void writeln(String value);

}
