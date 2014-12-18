package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.CapturedFriendLeaderModel;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.helper.MonsterImageHelper;

/**
 * Created by Neraud on 14/12/2014.
 */
public class FilterFriendsChooseLeadersAdapter extends BaseExpandableListAdapter {

	private final Context mContext;
	private List<MonsterInfoModel> mGroups;
	private Map<MonsterInfoModel, List<ChooseModelContainer<CapturedFriendLeaderModel>>> mChildrenByGroup;
	private final MonsterImageHelper mImageHelper;

	public final class GroupViewHolder {

		@InjectView(R.id.filter_friends_choose_leaders_item_parent_image)
		ImageView image;
		@InjectView(R.id.filter_friends_choose_leaders_item_parent_name)
		TextView nameText;
		@InjectView(R.id.filter_friends_choose_leaders_item_parent_table_header)
		ViewGroup tableHeaderGroup;
		@InjectView(R.id.filter_friends_choose_leaders_item_parent_select_all_button)
		Button selectAllButton;
		@InjectView(R.id.filter_friends_choose_leaders_item_parent_select_none_button)
		Button selectNoneButton;

		public GroupViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	public final class ChildViewHolder {

		@InjectView(R.id.filter_friends_choose_leaders_item_child_checkbox)
		CheckBox checkBox;
		@InjectView(R.id.filter_friends_choose_leaders_item_child_padherder_level)
		TextView levelText;
		@InjectView(R.id.filter_friends_choose_leaders_item_child_padherder_skill)
		TextView skillText;
		@InjectView(R.id.filter_friends_choose_leaders_item_child_padherder_awakenings)
		TextView awakeningsText;
		@InjectView(R.id.filter_friends_choose_leaders_item_child_padherder_plusHp)
		TextView plusHpText;
		@InjectView(R.id.filter_friends_choose_leaders_item_child_padherder_plusAtk)
		TextView PlusAtkText;
		@InjectView(R.id.filter_friends_choose_leaders_item_child_padherder_plusRcv)
		TextView plusRcvText;


		public ChildViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	private static class GroupComparator implements Comparator<MonsterInfoModel> {
		@Override
		public int compare(MonsterInfoModel lhs, MonsterInfoModel rhs) {
			if (lhs.getBaseMonsterId() == rhs.getBaseMonsterId()) {
				return lhs.getIdJP() - rhs.getIdJP();
			} else {
				return lhs.getBaseMonsterId() - rhs.getBaseMonsterId();
			}
		}
	}

	private static class ChildComparator implements Comparator<ChooseModelContainer<CapturedFriendLeaderModel>> {
		@Override
		public int compare(ChooseModelContainer<CapturedFriendLeaderModel> lhs, ChooseModelContainer<CapturedFriendLeaderModel> rhs) {
			return (int) (lhs.getModel().getExp() - rhs.getModel().getExp());
		}
	}

	public FilterFriendsChooseLeadersAdapter(Context context) {
		mContext = context;
		mImageHelper = new MonsterImageHelper(context);
	}

	public void updateLeaderModels(Map<MonsterInfoModel, List<ChooseModelContainer<CapturedFriendLeaderModel>>> childrenByGroup) {
		MyLog.entry();

		mGroups = new ArrayList<>(childrenByGroup.keySet());
		Collections.sort(mGroups, new GroupComparator());
		mChildrenByGroup = childrenByGroup;

		for (final MonsterInfoModel group : mGroups) {
			Collections.sort(mChildrenByGroup.get(group), new ChildComparator());
		}

		MyLog.exit();
	}

	@Override
	public int getGroupCount() {
		return mGroups != null ? mGroups.size() : 0;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mChildrenByGroup != null ? mChildrenByGroup.get(getGroup(groupPosition)).size() : 0;
	}

	@Override
	public MonsterInfoModel getGroup(int groupPosition) {
		return mGroups.get(groupPosition);
	}

	@Override
	public ChooseModelContainer<CapturedFriendLeaderModel> getChild(int groupPosition, int childPosition) {
		return mChildrenByGroup.get(getGroup(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return getGroup(groupPosition).getIdJP();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return getChild(groupPosition, childPosition).getModel().getIdJp();
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
		MyLog.entry(groupPosition + " : " + isExpanded);

		final MonsterInfoModel monsterInfo = getGroup(groupPosition);

		final GroupViewHolder viewHolder;
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.filter_friends_choose_leaders_item_parent, parent, false);
			viewHolder = new GroupViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (GroupViewHolder) view.getTag();
		}

		mImageHelper.fillImage(viewHolder.image, monsterInfo);

		viewHolder.nameText.setText(mContext.getString(R.string.filter_friends_choose_leaders_item_parent_name_group,
				mChildrenByGroup.get(monsterInfo).size(), monsterInfo.getIdJP(), monsterInfo.getName()));

		viewHolder.tableHeaderGroup.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

		final Boolean allChildrenStates = allChildrenStates(groupPosition);

		if (allChildrenStates == Boolean.TRUE) {
			viewHolder.selectAllButton.setEnabled(false);
			viewHolder.selectAllButton.setTextColor(mContext.getResources().getColor(R.color.body_text_disabled));
		} else {
			viewHolder.selectAllButton.setEnabled(true);
			viewHolder.selectAllButton.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
			viewHolder.selectAllButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					flagAllChildren(groupPosition, true);
				}
			});
		}

		if (allChildrenStates == Boolean.FALSE) {
			viewHolder.selectNoneButton.setEnabled(false);
			viewHolder.selectNoneButton.setTextColor(mContext.getResources().getColor(R.color.body_text_disabled));
		} else {
			viewHolder.selectNoneButton.setEnabled(true);
			viewHolder.selectNoneButton.setTextColor(mContext.getResources().getColor(R.color.theme_primary));
			viewHolder.selectNoneButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					flagAllChildren(groupPosition, false);
				}
			});
		}
		MyLog.exit();
		return view;
	}

	private Boolean allChildrenStates(int groupPosition) {
		boolean hasOneSelected = false;
		boolean hasOneNotSelected = false;

		for (final ChooseModelContainer<CapturedFriendLeaderModel> child : mChildrenByGroup.get(mGroups.get(groupPosition))) {
			if (child.isChosen()) {
				hasOneSelected = true;
				if (hasOneNotSelected) {
					break;
				}
			} else {
				hasOneNotSelected = true;
				if (hasOneSelected) {
					break;
				}
			}
		}

		return (hasOneSelected && hasOneNotSelected) ? null : hasOneSelected;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		MyLog.entry(groupPosition + " : " + childPosition);

		final ChooseModelContainer<CapturedFriendLeaderModel> item = getChild(groupPosition, childPosition);

		final ChildViewHolder viewHolder;
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.filter_friends_choose_leaders_item_child, parent, false);
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

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MyLog.entry();
				item.setChosen(!item.isChosen());
				viewHolder.checkBox.setChecked(item.isChosen());
				notifyDataSetChanged();
				MyLog.exit();
			}
		});

		viewHolder.levelText.setText(item.getModel().getLevel() + "");
		viewHolder.skillText.setText(item.getModel().getSkillLevel() + "");
		viewHolder.awakeningsText.setText(item.getModel().getAwakenings() + "");
		viewHolder.plusHpText.setText(item.getModel().getPlusHp() + "");
		viewHolder.PlusAtkText.setText(item.getModel().getPlusAtk() + "");
		viewHolder.plusRcvText.setText(item.getModel().getPlusRcv() + "");

		MyLog.exit();
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public void flagAllChildren(int groupPosition, boolean chosen) {
		MyLog.entry();

		for (final ChooseModelContainer<CapturedFriendLeaderModel> child : mChildrenByGroup.get(mGroups.get(groupPosition))) {
			child.setChosen(chosen);
		}

		notifyDataSetChanged();
		MyLog.exit();
	}

}
