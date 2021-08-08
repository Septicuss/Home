package oxygen.objects.menu;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import oxygen.utils.Inventory;

public class Button {

	private String id;
	private Integer[] buttonSlots;

	public Button(String id, int firstSlot, int secondSlot) {

		List<Integer> slots = Inventory.getSlotsInBetween(firstSlot, secondSlot);
		this.buttonSlots = slots.toArray(new Integer[0]);
		this.id = id;
	}

	public Button(String id, Integer[] buttonSlots) {

		this.buttonSlots = buttonSlots;
		this.id = id;

	}

	public Button(String id, Collection<Integer> buttonSlots) {

		this.buttonSlots = buttonSlots.toArray(new Integer[0]);
		this.id = id;

	}

	public String getId() {
		return id;
	}

	public Integer[] getButtonSlots() {
		return buttonSlots;
	}

	public boolean containsSlot(int slot) {
		return Arrays.stream(buttonSlots).anyMatch(i -> i == slot);
	}

}
