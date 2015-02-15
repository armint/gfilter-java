package org.firepick.gfilter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GMoveMatcher extends GCodeMatcher {

	private String code;
	private GCoordinate coord;

	@Override
	public int match(String text) {
		int index;
		int endIndex = 0;
		boolean loop = true;

		code = "";
		coord = new GCoordinate();
		for (index = 0; loop; index++) {
			switch (text.charAt(index)) {
			case ' ':
				continue;
			case '\t':
				continue;
			case 'x':
			case 'X': {
				index++;
				int digits = GCodeMatcher.matchNumber(text.substring(index));
				if (digits > 0) {
					endIndex = index + digits - 1;
					coord.setX(Double.parseDouble(text.substring(index, endIndex)));
				}
				index = endIndex - 1;
				break;
			}
			case 'y':
			case 'Y': {
				index++;
				int digits = GCodeMatcher.matchNumber(text.substring(index));
				if (digits > 0) {
					endIndex = index + digits - 1;
					coord.setY(Double.parseDouble(text.substring(index, endIndex)));
				}
				index = endIndex - 1;
				break;
			}
			case 'z':
			case 'Z': {
				index++;
				int digits = GCodeMatcher.matchNumber(text.substring(index));
				if (digits > 0) {
					endIndex = index + digits - 1;
					coord.setZ(Double.parseDouble(text.substring(index, endIndex)));
				}
				index = endIndex - 1;
				break;
			}
			case 'g':
			case 'G':
				index++;
				char c = text.charAt(index);
				if ((c == '0' || c == '1') && !isDigit(text.charAt(index + 1))) {
					code += text.substring(index - 1, index + 1);
				} else if (c == '2' && text.charAt(index + 1) == '8' && !isDigit(text.charAt(index + 2))) {
					code += text.substring(index - 1, index + 2);
					index++;
				} else {
					index--;
				}
				break;
			default:
				loop = false;
				break;
			}
		}

		return code.isEmpty() ? 0 : index - 1;
	}

	private boolean isDigit(char c) {
		return "1234567890".indexOf(c) >= 0;
	}

}