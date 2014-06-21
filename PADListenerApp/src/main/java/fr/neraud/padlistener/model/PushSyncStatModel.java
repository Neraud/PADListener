package fr.neraud.padlistener.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model containing statistics for a PushSync operation
 *
 * @author Neraud
 */
public class PushSyncStatModel {

	private final Map<ElementToPush, Integer> elementsToPush;
	private final Map<ElementToPush, Integer> elementsPushed;
	private final Map<ElementToPush, Integer> elementsError;
	private final Map<ElementToPush, List<String>> errorMessages;
	private int elementToPushCount = 0;
	private int elementPushedCount = 0;
	private int elementErrorCount = 0;

	/**
	 * Enum of element types to push
	 *
	 * @author Neraud
	 */
	public static enum ElementToPush {
		USER_INFO,
		MATERIAL_TO_UPDATE,
		MONSTER_TO_UPDATE,
		MONSTER_TO_CREATE,
		MONSTER_TO_DELETE
	}

	public PushSyncStatModel() {
		elementsToPush = new HashMap<ElementToPush, Integer>();
		elementsPushed = new HashMap<ElementToPush, Integer>();
		elementsError = new HashMap<ElementToPush, Integer>();
		errorMessages = new HashMap<ElementToPush, List<String>>();
	}

	public void initElementsToPush(ElementToPush element, int count) {
		elementsToPush.put(element, count);
		elementsPushed.put(element, 0);
		elementsError.put(element, 0);
		errorMessages.put(element, new ArrayList<String>());
		elementToPushCount += count;
	}

	public void incrementElementsPushed(ElementToPush element) {
		elementPushedCount++;
		elementsPushed.put(element, elementsPushed.get(element) + 1);
	}

	public void incrementElementsError(ElementToPush element, String errorMessage) {
		elementErrorCount++;
		elementsError.put(element, elementsError.get(element) + 1);
		errorMessages.get(element).add(errorMessage);
	}

	public int getElementToPushCount() {
		return elementToPushCount;
	}

	public void setElementToPushCount(int elementToPushCount) {
		this.elementToPushCount = elementToPushCount;
	}

	public int getElementPushedCount() {
		return elementPushedCount;
	}

	public void setElementPushedCount(int elementPushedCount) {
		this.elementPushedCount = elementPushedCount;
	}

	public int getElementErrorCount() {
		return elementErrorCount;
	}

	public void setElementErrorCount(int elementErrorCount) {
		this.elementErrorCount = elementErrorCount;
	}

	public Map<ElementToPush, Integer> getElementsToPush() {
		return elementsToPush;
	}

	public Map<ElementToPush, Integer> getElementsPushed() {
		return elementsPushed;
	}

	public Map<ElementToPush, Integer> getElementsError() {
		return elementsError;
	}

	public Map<ElementToPush, List<String>> getErrorMessages() {
		return errorMessages;
	}

}
