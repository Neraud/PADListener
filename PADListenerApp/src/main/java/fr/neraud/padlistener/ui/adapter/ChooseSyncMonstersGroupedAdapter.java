package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
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

	static class GroupViewHolder {

		@InjectView(R.id.choose_sync_monsters_item_image)
		ImageView imageView;

		@InjectView(R.id.choose_sync_monsters_item_name)
		TextView nameText;

		public GroupViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	static class ChildViewHolder {

		@InjectView(R.id.choose_sync_monsters_item_checkbox)
		CheckBox checkBox;
		@InjectView(R.id.choose_sync_monsters_item_evo)
		TextView evoText;
		@InjectView(R.id.choose_sync_monsters_item_priority)
		TextView priorityText;
		@InjectView(R.id.choose_sync_monsters_item_note)
		TextView noteText;

		@InjectView(R.id.choose_sync_monsters_item_padherder_exp)
		TextView padherderExpText;
		@InjectView(R.id.choose_sync_monsters_item_padherder_skill)
		TextView padherderSkillText;
		@InjectView(R.id.choose_sync_monsters_item_padherder_awakenings)
		TextView padherderAwakeningsText;
		@InjectView(R.id.choose_sync_monsters_item_padherder_plusHp)
		TextView padherderPlusHpText;
		@InjectView(R.id.choose_sync_monsters_item_padherder_plusAtk)
		TextView padherderPlusAtkText;
		@InjectView(R.id.choose_sync_monsters_item_padherder_plusRcv)
		TextView padherderPlusRcvText;

		@InjectView(R.id.choose_sync_monsters_item_captured_exp)
		TextView capturedExpText;
		@InjectView(R.id.choose_sync_monsters_item_captured_skill)
		TextView capturedSkillText;
		@InjectView(R.id.choose_sync_monsters_item_captured_awakenings)
		TextView capturedAwakeningsText;
		@InjectView(R.id.choose_sync_monsters_item_captured_plusHp)
		TextView capturedPlusHpText;
		@InjectView(R.id.choose_sync_monsters_item_captured_plusAtk)
		TextView capturedPlusAtkText;
		@InjectView(R.id.choose_sync_monsters_item_captured_plusRcv)
		TextView capturedPlusRcvText;

		public ChildViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

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

		final GroupViewHolder viewHolder;
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_monsters_group, parent, false);
			viewHolder = new GroupViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (GroupViewHolder) view.getTag();
		}

		mImageHelper.fillImage(viewHolder.imageView, monsterInfo);

		viewHolder.nameText.setText(mContext.getString(R.string.choose_sync_monsters_item_name_group, mSortedSyncedMonsters.get(monsterInfo).size(),
				monsterInfo.getIdJP(), monsterInfo.getName()));

		MyLog.exit();
		return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		MyLog.entry();

		final ChooseSyncModelContainer<SyncedMonsterModel> item = getChild(groupPosition, childPosition);

		final ChildViewHolder viewHolder;
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_monsters_child, parent, false);
			mDefaultTextColor = ((TextView) view.findViewById(R.id.choose_sync_monsters_item_padherder_exp)).getTextColors()
					.getDefaultColor();
			// To enable the contextMenu
			view.setLongClickable(true);
			viewHolder = new ChildViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ChildViewHolder) view.getTag();
		}

		viewHolder.checkBox.setChecked(item.isChosen());
		// the whole view is clickable, disable the checkBox to prevent missing clicks on it
		viewHolder.checkBox.setClickable(false);

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry();
				item.setChosen(!item.isChosen());
				viewHolder.checkBox.setChecked(item.isChosen());
				MyLog.exit();
			}
		});

		final MonsterModel padherder = item.getSyncedModel().getPadherderInfo();
		final MonsterModel captured = item.getSyncedModel().getCapturedInfo();

		if (padherder != null && captured != null && padherder.getIdJp() != captured.getIdJp()) {
			viewHolder.evoText.setVisibility(View.VISIBLE);
			final MonsterInfoModel evolvedFrom = item.getSyncedModel().getPadherderMonsterInfo();

			viewHolder.evoText.setText(mContext.getString(R.string.choose_sync_monsters_item_evo,
					evolvedFrom.getIdJP(),
					evolvedFrom.getName()));
			final int color = mContext.getResources().getColor(padherder.getIdJp() < captured.getIdJp() ? R.color.text_increase : R.color.text_decrease);
			viewHolder.evoText.setTextColor(color);
		} else {
			viewHolder.evoText.setVisibility(View.GONE);
		}
		fillTable(viewHolder, padherder, captured);

		final MonsterModel modelToUse = captured != null ? captured : padherder;
		final String priorityLabel = mContext.getString(modelToUse.getPriority().getLabelResId());
		viewHolder.priorityText.setText(mContext.getString(R.string.choose_sync_monsters_item_priority, priorityLabel));
		if (StringUtils.isNotBlank(modelToUse.getNote())) {
			viewHolder.noteText.setVisibility(View.VISIBLE);
			viewHolder.noteText.setText(mContext.getString(R.string.choose_sync_monsters_item_note, modelToUse.getNote()));
		} else {
			viewHolder.noteText.setVisibility(View.GONE);
		}

		MyLog.exit();
		return view;
	}

	private void fillTable(ChildViewHolder viewHolder, MonsterModel padherder, MonsterModel captured) {
		if (padherder != null && captured != null) {
			fillBothText(viewHolder.padherderExpText, padherder.getExp(), viewHolder.capturedExpText, captured.getExp());
			fillBothText(viewHolder.padherderSkillText, padherder.getSkillLevel(), viewHolder.capturedSkillText, captured.getSkillLevel());
			fillBothText(viewHolder.padherderAwakeningsText, padherder.getAwakenings(), viewHolder.capturedAwakeningsText, captured.getAwakenings());
			fillBothText(viewHolder.padherderPlusHpText, padherder.getPlusHp(), viewHolder.capturedPlusHpText, captured.getPlusHp());
			fillBothText(viewHolder.padherderPlusAtkText, padherder.getPlusAtk(), viewHolder.capturedPlusAtkText, captured.getPlusAtk());
			fillBothText(viewHolder.padherderPlusRcvText, padherder.getPlusRcv(), viewHolder.capturedPlusRcvText, captured.getPlusRcv());
		} else if (padherder != null) {
			fillOneText(viewHolder.padherderExpText, padherder.getExp());
			fillOneText(viewHolder.padherderSkillText, padherder.getSkillLevel());
			fillOneText(viewHolder.padherderAwakeningsText, padherder.getAwakenings());
			fillOneText(viewHolder.padherderPlusHpText, padherder.getPlusHp());
			fillOneText(viewHolder.padherderPlusAtkText, padherder.getPlusAtk());
			fillOneText(viewHolder.padherderPlusRcvText, padherder.getPlusRcv());
			resetOneText(viewHolder.capturedExpText);
			resetOneText(viewHolder.capturedSkillText);
			resetOneText(viewHolder.capturedAwakeningsText);
			resetOneText(viewHolder.capturedPlusHpText);
			resetOneText(viewHolder.capturedPlusAtkText);
			resetOneText(viewHolder.capturedPlusRcvText);
		} else {
			fillOneText(viewHolder.capturedExpText, captured.getExp());
			fillOneText(viewHolder.capturedSkillText, captured.getSkillLevel());
			fillOneText(viewHolder.capturedAwakeningsText, captured.getAwakenings());
			fillOneText(viewHolder.capturedPlusHpText, captured.getPlusHp());
			fillOneText(viewHolder.capturedPlusAtkText, captured.getPlusAtk());
			fillOneText(viewHolder.capturedPlusRcvText, captured.getPlusRcv());
			resetOneText(viewHolder.padherderExpText);
			resetOneText(viewHolder.padherderSkillText);
			resetOneText(viewHolder.padherderAwakeningsText);
			resetOneText(viewHolder.padherderPlusHpText);
			resetOneText(viewHolder.padherderPlusAtkText);
			resetOneText(viewHolder.padherderPlusRcvText);
		}
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private void fillBothText(TextView padherderTextView, long padherderValue, TextView capturedTextView, long capturedValue) {
		fillOneText(padherderTextView, padherderValue);
		fillOneText(capturedTextView, capturedValue);

		if (padherderValue < capturedValue) {
			capturedTextView.setTextColor(mContext.getResources().getColor(R.color.text_increase));
		} else if (padherderValue > capturedValue) {
			capturedTextView.setTextColor(mContext.getResources().getColor(R.color.text_decrease));
		}
	}

	private void fillOneText(TextView textView, long value) {
		fillOneText(textView, DecimalFormat.getInstance().format(value));
	}

	private void resetOneText(TextView textView) {
		fillOneText(textView, "-");
	}

	private void fillOneText(TextView textView, String value) {
		textView.setText(value);
		textView.setTextColor(mDefaultTextColor);
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
