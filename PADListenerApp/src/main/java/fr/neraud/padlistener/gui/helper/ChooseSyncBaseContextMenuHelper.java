package fr.neraud.padlistener.gui.helper;

import android.content.Context;
import android.util.Log;
import android.view.MenuItem;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import fr.neraud.padlistener.padherder.constant.MonsterPriority;

/**
 * Created by Neraud on 22/06/2014.
 */
public abstract class ChooseSyncBaseContextMenuHelper {

	/**
	 * All fragments from an Activity receive the onContextItemSelected callback.<br/>
	 * We use a generated unique ID to only use the correct helper to handle a callback
	 * @see http://adanware.blogspot.fr/2012/05/android-oncreatecontextmenu-in-multiple.html
	 */
	private static final AtomicInteger GROUP_ID_GENERATOR = new AtomicInteger();

	private final Context context;
	private final ChooseSyncDataPagerHelper.Mode mode;
	private final int groupId;

	public ChooseSyncBaseContextMenuHelper(Context context, ChooseSyncDataPagerHelper.Mode mode) {
		this.context = context;
		this.mode = mode;
		groupId = GROUP_ID_GENERATOR.getAndIncrement();
	}

	public boolean contextItemSelected(MenuItem item) {
		Log.d(getClass().getName(), "contextItemSelected : item = " + item);

		if (item.getGroupId() != groupId) {
			return false;
		} else {
			return doContextItemSelected(item);
		}
	}

	protected abstract boolean doContextItemSelected(MenuItem item);

	protected abstract void notifyDataSetChanged();

	protected ChooseSyncDataPagerHelper.Mode getMode() {
		return mode;
	}

	protected int getGroupId() {
		return groupId;
	}

	protected Context getContext() {
		return context;
	}


	protected CharSequence[] buildPriorityList() {
		final CharSequence[] priorities = new CharSequence[MonsterPriority.values().length];

		for (MonsterPriority priority : MonsterPriority.values()) {
			priorities[priority.ordinal()] = getContext().getString(priority.getLabelResId());
		}
		return priorities;
	}

}
