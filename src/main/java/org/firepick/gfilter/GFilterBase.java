package org.firepick.gfilter;

public abstract class GFilterBase extends GFilter {
	protected GFilter _next;

	protected GFilterBase(GFilter next) {
		this._next = next;
		_name = "GFilterBase";
	}
}
