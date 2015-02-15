package org.firepick.gfilter;

public abstract class GCodeMatcher {

	/**
	 * Return the length of the longest text prefix that can be parsed as a number
	 */
	public static int matchNumber(String text) {
		throw new RuntimeException("Not implemented!");
	}

	/**
	 * Return the length of the longest text prefix that matches whatever the implementation class is looking for
	 */
	public abstract int match(String text);

}
