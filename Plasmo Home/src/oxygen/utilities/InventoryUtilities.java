package oxygen.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryUtilities {

	/**
	 * 
	 * This method takes two chest inventory slots, and returns a list of slots in
	 * between these slots in cubic area.
	 * 
	 * Second slot must be higher than the first slot and they must form a regular
	 * rectangle.
	 * 
	 * @param firstSlot
	 * @param secondSlot
	 * @return A list of chest inventory slots
	 */
	public static List<Integer> getSlotsInBetween(int firstSlot, int secondSlot) {
		List<Integer> results = new ArrayList<>();

		int[] coordPointOne = getCoordPointFromSlot(firstSlot);
		int[] coordPointTwo = getCoordPointFromSlot(secondSlot);

		for (int y = coordPointOne[1]; y < coordPointTwo[1] + 1; y++) {
			for (int x = coordPointOne[0]; x < coordPointTwo[0] + 1; x++) {
				int[] newCoord = { x, y };
				int slot = getSlotFromCoords(newCoord);
				results.add(slot);
			}
		}

		Collections.sort(results);
		return results;
	}

	/**
	 * 
	 * Calculate a slot from a coordinates array, containing an x and y.
	 * 
	 * @param coords
	 * @return A chest inventory slot
	 */
	private static int getSlotFromCoords(int[] coords) {
		int x = coords[0];
		int y = coords[1];

		return (x + (y * 9));
	}

	/**
	 * 
	 * Calculate a coordinates array from a slot, containing an x and y.
	 * 
	 * @param slot
	 * @return An array with x and y
	 */
	private static int[] getCoordPointFromSlot(int slot) {
		int y = slot / 9;
		int x = slot - (y * 9);

		return new int[] { x, y };
	}

}
