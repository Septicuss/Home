package oxygen.objects;

import oxygen.utilities.MessageUtilities;

public class ColorPalette {

	private String[] hexColors;

	public ColorPalette(String... hexColors) {
		this.hexColors = hexColors;
	}

	public String[] getHexColors() {
		return hexColors;
	}

	public String getFirstColor() {
		return getColor(-1);
	}

	public String getSecondColor() {
		return getColor(0);
	}

	public String getColor(int index) {
		return MessageUtilities.color(hexColors[index + 1]);
	}

}
