package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.MonsterModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.ui.helper.MonsterImageHelper;

/**
 * Adaptor to display Monsters set up as grouped
 *
 * @author Neraud
 */
public class ChooseSyncMonstersGroupedAdapter extends BaseExpandableListAdapter {

	private final Context mContext;
	private final List<ChooseSyncModelContainer<SyncedMonsterModel>> mSyncedMonsters;
	private List<MonsterInfoModel> mGroups;
	private Map<MonsterInfoModel, List<ChooseSyncModelContainer<SyncedMonsterModel>>> mSortedSyncedMonsters;
	private Integer mDefaultTextColor = null;
	private MonsterImageHelper mImageHelper;

	public ChooseSyncMonstersGroupedAdapter(Context context, List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonsters) {
		this.mContext = context;
		this.mSyncedMonsters = syncedMonsters;
		refreshData();
		mImageHelper = new MonsterImageHelper(context);
	}

	private Map<MonsterInfoModel, List<ChooseSyncModelContainer<SyncedMonsterModel>>> reorgMonsters(
			List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate) {
		MyLog.entry();

		final Map<MonsterInfoModel, List<ChooseSyncModelContainer<SyncedMonsterModel>>> syncedMonsters = new HashMap<MonsterInfoModel, List<ChooseSyncModelContainer<SyncedMonsterModel>>>();

		for (final ChooseSyncModelContainer<SyncedMonsterModel> container : syncedMonstersToUpdate) {
			final MonsterInfoModel monster = container.getSyncedModel().getDisplayedMonsterInfo();
			if (!syncedMonsters.containsKey(monster)) {
				syncedMonsters.put(monster, new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>());
			}
			syncedMonsters.get(monster).add(container);
		}

		MyLog.exit();
		return syncedMonsters;
	}

	@Override
	public int getGroupCount() {
		return mGroups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mSortedSyncedMonsters.get(mGroups.get(groupPosition)).size();
	}

	@Override
	public MonsterInfoModel getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	@Override
	public ChooseSyncModelContainer<SyncedMonsterModel> getChild(int groupPosition, int childPosition) {
		return mSortedSyncedMonsters.get(mGroups.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
		MyLog.entry();

		final MonsterInfoModel monsterInfo = getGroup(groupPosition);
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_monsters_group, parent, false);
		}

		mImageHelper.fillImage(view, R.id.choose_sync_monsters_item_image, monsterInfo);

		final TextView nameText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_name);
		nameText.setText(mContext.getString(R.string.choose_sync_monsters_item_name_group, mSortedSyncedMonsters.get(monsterInfo).size(),
				monsterInfo.getIdJP(), monsterInfo.getName()));

		MyLog.exit();
		return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		MyLog.entry();

		final ChooseSyncModelContainer<SyncedMonsterModel> item = getChild(groupPosition, childPosition);
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_monsters_child, parent, false);
			mDefaultTextColor = ((TextView) view.findViewById(R.id.choose_sync_monsters_item_padherder_exp)).getTextColors()
					.getDefaultColor();
			// To enable the contextMenu
			view.setLongClickable(true);
		}

		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.choose_sync_monsters_item_checkbox);
		checkBox.setChecked(item.isChosen());
		// the whole view is clickable, disable the checkBox to prevent missing clicks on it
		checkBox.setClickable(false);

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry();
				item.setChosen(!item.isChosen());
				checkBox.setChecked(item.isChosen());
				MyLog.exit();
			}
		});

		final MonsterModel padherder = item.getSyncedModel().getPadherderInfo();
		final MonsterModel captured = item.getSyncedModel().getCapturedInfo();

		final TextView evoText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_evo);
		if (padherder != null && captured != null && padherder.getIdJp() != captured.getIdJp()) {
			evoText.setVisibility(View.VISIBLE);
			final MonsterInfoModel evolvedFrom = item.getSyncedModel().getPadherderMonsterInfo();

			evoText.setText(mContext.getString(R.string.choose_sync_monsters_item_evo,
					evolvedFrom.getIdJP(),
					evolvedFrom.getName()));
			final int color = mContext.getResources().getColor(padherder.getIdJp() < captured.getIdJp() ? R.color.text_increase : R.color.text_decrease);
			evoText.setTextColor(color);
		} else {
			evoText.setVisibility(View.GONE);
		}
		fillTable(view, padherder, captured);

		final MonsterModel modelToUse = captured != null ? captured : padherder;
		final TextView priorityText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_priority);
		final String priorityLabel = mContext.getString(modelToUse.getPriority().getLabelResId());
		priorityText.setText(mContext.getString(R.string.choose_sync_monsters_item_priority, priorityLabel));
		final TextView noteText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_note);
		if (StringUtils.isNotBlank(modelToUse.getNote())) {
			noteText.setVisibility(View.VISIBLE);
			noteText.setText(mContext.getString(R.string.choose_sync_monsters_item_note, modelToUse.getNote()));
		} else {
			noteText.setVisibility(View.GONE);
		}

		MyLog.exit();
		return view;
	}

	private void fillTable(View view, MonsterModel padherder, MonsterModel captured) {
		if (padherder != null && captured != null) {
			fillBothText(view, R.id.choose_sync_monsters_item_padherder_exp, padherder.getExp(),
					R.id.choose_sync_monsters_item_captured_exp, captured.getExp());
			fillBothText(view, R.id.choose_sync_monsters_item_padherder_skill, padherder.getSkillLevel(),
					R.id.choose_sync_monsters_item_captured_skill, captured.getSkillLevel());
			fillBothText(view, R.id.choose_sync_monsters_item_padherder_awakenings, padherder.getAwakenings(),
					R.id.choose_sync_monsters_item_captured_awakenings, captured.getAwakenings());
			fillBothText(view, R.id.choose_sync_monsters_item_padherder_plusHp, padherder.getPlusHp(),
					R.id.choose_sync_monsters_item_captured_plusHp, captured.getPlusHp());
			fillBothText(view, R.id.choose_sync_monsters_item_padherder_plusAtk, padherder.getPlusAtk(),
					R.id.choose_sync_monsters_item_captured_plusAtk, captured.getPlusAtk());
			fillBothText(view, R.id.choose_sync_monsters_item_padherder_plusRcv, padherder.getPlusRcv(),
					R.id.choose_sync_monsters_item_captured_plusRcv, captured.getPlusRcv());
		} else if (padherder != null) {
			fillOneText(view, R.id.choose_sync_monsters_item_padherder_exp, padherder.getExp());
			fillOneText(view, R.id.choose_sync_monsters_item_padherder_skill, padherder.getSkillLevel());
			fillOneText(view, R.id.choose_sync_monsters_item_padherder_awakenings, padherder.getAwakenings());
			fillOneText(view, R.id.choose_sync_monsters_item_padherder_plusHp, padherder.getPlusHp());
			fillOneText(view, R.id.choose_sync_monsters_item_padherder_plusAtk, padherder.getPlusAtk());
			fillOneText(view, R.id.choose_sync_monsters_item_padherder_plusRcv, padherder.getPlusRcv());
			resetOneText(view, R.id.choose_sync_monsters_item_captured_exp);
			resetOneText(view, R.id.choose_sync_monsters_item_captured_skill);
			resetOneText(view, R.id.choose_sync_monsters_item_captured_awakenings);
			resetOneText(view, R.id.choose_sync_monsters_item_captured_plusHp);
			resetOneText(view, R.id.choose_sync_monsters_item_captured_plusAtk);
			resetOneText(view, R.id.choose_sync_monsters_item_captured_plusRcv);
		} else {
			fillOneText(view, R.id.choose_sync_monsters_item_captured_exp, captured.getExp());
			fillOneText(view, R.id.choose_sync_monsters_item_captured_skill, captured.getSkillLevel());
			fillOneText(view, R.id.choose_sync_monsters_item_captured_awakenings, captured.getAwakenings());
			fillOneText(view, R.id.choose_sync_monsters_item_captured_plusHp, captured.getPlusHp());
			fillOneText(view, R.id.choose_sync_monsters_item_captured_plusAtk, captured.getPlusAtk());
			fillOneText(view, R.id.choose_sync_monsters_item_captured_plusRcv, captured.getPlusRcv());
			resetOneText(view, R.id.choose_sync_monsters_item_padherder_exp);
			resetOneText(view, R.id.choose_sync_monsters_item_padherder_skill);
			resetOneText(view, R.id.choose_sync_monsters_item_padherder_awakenings);
			resetOneText(view, R.id.choose_sync_monsters_item_padherder_plusHp);
			resetOneText(view, R.id.choose_sync_monsters_item_padherder_plusAtk);
			resetOneText(view, R.id.choose_sync_monsters_item_padherder_plusRcv);
		}
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private void fillBothText(View view, int padherderTextViewResId, long padherderValue, int capturedTextViewResId, long capturedValue) {
		fillOneText(view, padherderTextViewResId, padherderValue);
		fillOneText(view, capturedTextViewResId, capturedValue);

		if (padherderValue < capturedValue) {
			((TextView) view.findViewById(capturedTextViewResId)).setTextColor(mContext.getResources().getColor(R.color.text_increase));
		} else if (padherderValue > capturedValue) {
			((TextView) view.findViewById(capturedTextViewResId)).setTextColor(mContext.getResources().getColor(R.color.text_decrease));
		}
	}

	private void fillOneText(View view, int textViewResId, long value) {
		fillOneText(view, textViewResId, DecimalFormat.getInstance().format(value));
	}

	private void resetOneText(View view, int textViewResId) {
		fillOneText(view, textViewResId, "-");
	}

	private void fillOneText(View view, int textViewResId, String value) {
		((TextView) view.findViewById(textViewResId)).setText(value);
		((TextView) view.findViewById(textViewResId)).setTextColor(mDefaultTextColor);
	}

	public void refreshData() {
		MyLog.entry();
		mSortedSyncedMonsters = reorgMonsters(mSyncedMonsters);
		mGroups = new ArrayList<MonsterInfoModel>(mSortedSyncedMonsters.keySet());

		final Comparator<MonsterInfoModel> comparator = new Comparator<MonsterInfoModel>() {
			@Override
			public int compare(MonsterInfoModel a, MonsterInfoModel b) {
				return a.getIdJP() - b.getIdJP();
			}
		};
		Collections.sort(mGroups, comparator);
		MyLog.exit();
	}
}
