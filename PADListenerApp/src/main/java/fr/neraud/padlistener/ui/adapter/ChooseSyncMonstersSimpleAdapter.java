package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
 * Adaptor to display Monsters set up as simple
 *
 * @author Neraud
 */
public class ChooseSyncMonstersSimpleAdapter extends ArrayAdapter<ChooseSyncModelContainer<SyncedMonsterModel>> {

	private Integer mDefaultTextColor = null;
	private final MonsterImageHelper mImageHelper;

	static class ViewHolder {

		@InjectView(R.id.choose_sync_monsters_item_checkbox)
		CheckBox checkBox;
		@InjectView(R.id.choose_sync_monsters_item_image)
		ImageView imageView;
		@InjectView(R.id.choose_sync_monsters_item_name)
		TextView nameText;
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

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	public ChooseSyncMonstersSimpleAdapter(Context context,
			List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate) {
		super(context, R.layout.choose_sync_item_monsters_simple, syncedMonstersToUpdate);

		final Comparator<ChooseSyncModelContainer<SyncedMonsterModel>> comparator = new Comparator<ChooseSyncModelContainer<SyncedMonsterModel>>() {
			@Override
			public int compare(ChooseSyncModelContainer<SyncedMonsterModel> a, ChooseSyncModelContainer<SyncedMonsterModel> b) {
				return a.getSyncedModel().getDisplayedMonsterInfo().getIdJP() - b.getSyncedModel().getDisplayedMonsterInfo().getIdJP();
			}
		};
		Collections.sort(syncedMonstersToUpdate, comparator);

		mImageHelper = new MonsterImageHelper(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		MyLog.entry();

		final ChooseSyncModelContainer<SyncedMonsterModel> item = super.getItem(position);

		final ViewHolder viewHolder;
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_monsters_simple, parent, false);
			mDefaultTextColor = ((TextView) view.findViewById(R.id.choose_sync_monsters_item_padherder_exp)).getTextColors().getDefaultColor();
			// For the contextMenu
			view.setLongClickable(true);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
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
				MyLog.exit();
			}
		});

		mImageHelper.fillImage(viewHolder.imageView, item.getSyncedModel().getDisplayedMonsterInfo());

		viewHolder.nameText.setText(getContext().getString(R.string.choose_sync_monsters_item_name_simple,
				item.getSyncedModel().getDisplayedMonsterInfo().getIdJP(),
				item.getSyncedModel().getDisplayedMonsterInfo().getName()));

		final MonsterModel padherder = item.getSyncedModel().getPadherderInfo();
		final MonsterModel captured = item.getSyncedModel().getCapturedInfo();

		if (padherder != null && captured != null && padherder.getIdJp() != captured.getIdJp()) {
			viewHolder.evoText.setVisibility(View.VISIBLE);
			final MonsterInfoModel evolvedFrom = item.getSyncedModel().getPadherderMonsterInfo();

			viewHolder.evoText.setText(getContext().getString(R.string.choose_sync_monsters_item_evo,
					evolvedFrom.getIdJP(),
					evolvedFrom.getName()));
			final int color = getContext().getResources().getColor(padherder.getIdJp() < captured.getIdJp() ? R.color.text_increase : R.color.text_decrease);
			viewHolder.evoText.setTextColor(color);
		} else {
			viewHolder.evoText.setVisibility(View.GONE);
		}

		fillTable(viewHolder, padherder, captured);

		final MonsterModel modelToUse = captured != null ? captured : padherder;
		final String priorityLabel = getContext().getString(modelToUse.getPriority().getLabelResId());
		viewHolder.priorityText.setText(getContext().getString(R.string.choose_sync_monsters_item_priority, priorityLabel));
		if (StringUtils.isNotBlank(modelToUse.getNote())) {
			viewHolder.noteText.setVisibility(View.VISIBLE);
			viewHolder.noteText.setText(getContext().getString(R.string.choose_sync_monsters_item_note, modelToUse.getNote()));
		} else {
			viewHolder.noteText.setVisibility(View.GONE);
		}

		MyLog.exit();
		return view;
	}

	private void fillTable(ViewHolder viewHolder, MonsterModel padherder, MonsterModel captured) {
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

	private void fillBothText(TextView padherderTextView, long padherderValue, TextView capturedTextView, long capturedValue) {
		fillOneText(padherderTextView, padherderValue);
		fillOneText(capturedTextView, capturedValue);

		if (padherderValue < capturedValue) {
			capturedTextView.setTextColor(getContext().getResources().getColor(R.color.text_increase));
		} else if (padherderValue > capturedValue) {
			capturedTextView.setTextColor(getContext().getResources().getColor(R.color.text_decrease));
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
}
