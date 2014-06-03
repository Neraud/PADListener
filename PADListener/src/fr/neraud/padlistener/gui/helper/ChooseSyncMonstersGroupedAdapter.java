
package fr.neraud.padlistener.gui.helper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.BaseMonsterModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

/**
 * Adaptor to display Monsters set up as grouped
 * 
 * @author Neraud
 */
public class ChooseSyncMonstersGroupedAdapter extends BaseExpandableListAdapter {

	private final Context context;
	private Integer defaultTextColor = null;
	private final List<MonsterInfoModel> groups;
	private final Map<MonsterInfoModel, List<ChooseSyncModelContainer<SyncedMonsterModel>>> syncedMonsters;

	public ChooseSyncMonstersGroupedAdapter(Context context,
	        List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate) {
		this.context = context;
		syncedMonsters = reorgMonsters(syncedMonstersToUpdate);
		groups = new ArrayList<MonsterInfoModel>(syncedMonsters.keySet());
	}

	private Map<MonsterInfoModel, List<ChooseSyncModelContainer<SyncedMonsterModel>>> reorgMonsters(
	        List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate) {
		Log.d(getClass().getName(), "reorgMonsters");
		final Map<MonsterInfoModel, List<ChooseSyncModelContainer<SyncedMonsterModel>>> syncedMonsters = new HashMap<MonsterInfoModel, List<ChooseSyncModelContainer<SyncedMonsterModel>>>();

		for (final ChooseSyncModelContainer<SyncedMonsterModel> container : syncedMonstersToUpdate) {
			final MonsterInfoModel monster = container.getSyncedModel().getMonsterInfo();
			if (!syncedMonsters.containsKey(monster)) {
				syncedMonsters.put(monster, new ArrayList<ChooseSyncModelContainer<SyncedMonsterModel>>());
			}
			syncedMonsters.get(monster).add(container);
		}

		return syncedMonsters;
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return syncedMonsters.get(groups.get(groupPosition)).size();
	}

	@Override
	public MonsterInfoModel getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public ChooseSyncModelContainer<SyncedMonsterModel> getChild(int groupPosition, int childPosition) {
		return syncedMonsters.get(groups.get(groupPosition)).get(childPosition);
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
		Log.d(getClass().getName(), "getGroupView");
		final MonsterInfoModel monsterInfo = getGroup(groupPosition);
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_monsters_group, parent, false);
		}

		final ImageView image = (ImageView) view.findViewById(R.id.choose_sync_monsters_item_image);
		try {
			final InputStream is = context.getContentResolver().openInputStream(
			        MonsterInfoDescriptor.UriHelper.uriForImage(monsterInfo.getId()));
			final BitmapDrawable bm = new BitmapDrawable(null, is);

			image.setImageDrawable(bm);
		} catch (final FileNotFoundException e) {
			image.setImageResource(R.drawable.no_monster_image);
		}

		final TextView nameText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_name);
		nameText.setText(context.getString(R.string.choose_sync_monsters_item_name_group, syncedMonsters.get(monsterInfo).size(),
		        monsterInfo.getId(), monsterInfo.getName()));

		return view;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getChildView");
		final ChooseSyncModelContainer<SyncedMonsterModel> item = getChild(groupPosition, childPosition);
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_monsters_child, parent, false);
			defaultTextColor = ((TextView) view.findViewById(R.id.choose_sync_monsters_item_padherder_exp)).getTextColors()
			        .getDefaultColor();
		}

		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.choose_sync_monsters_item_checkbox);
		checkBox.setChecked(item.isChoosen());
		checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				item.setChoosen(!item.isChoosen());
			}
		});

		final BaseMonsterModel padherder = item.getSyncedModel().getPadherderInfo();
		final BaseMonsterModel captured = item.getSyncedModel().getCapturedInfo();

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

		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	private void fillBothText(View view, int padherderTextViewResId, long padherderValue, int capturedTextViewResId,
	        long capturedValue) {
		fillOneText(view, padherderTextViewResId, padherderValue);
		fillOneText(view, capturedTextViewResId, capturedValue);

		if (padherderValue < capturedValue) {
			((TextView) view.findViewById(capturedTextViewResId)).setTextColor(Color.GREEN);
		} else if (padherderValue > capturedValue) {
			((TextView) view.findViewById(capturedTextViewResId)).setTextColor(Color.RED);
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
		((TextView) view.findViewById(textViewResId)).setTextColor(defaultTextColor);
	}
}
