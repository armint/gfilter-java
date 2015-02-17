package org.firepick.gfilter;

import java.util.ArrayList;

public class StringSink extends GFilter {
	public ArrayList<String> strings = new ArrayList<String>();
	public StringSink() {
		_name = "StringSink";
	}

	public void writeln(String value) {
		strings.add(value);
	}

	public String get(int index) {
		return strings.get(index);
	}

}
