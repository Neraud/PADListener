package fr.neraud.padlistener.gui.helper;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.BaseMonsterModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.model.SyncedMonsterModel;

/**
 * Adaptor to display Monsters set up as simple
 *
 * @author Neraud
 */
public class ChooseSyncMonstersSimpleAdapter extends ArrayAdapter<ChooseSyncModelContainer<SyncedMonsterModel>> {

	private Integer defaultTextColor = null;
	private final MonsterImageHelper imageHelper;

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

		imageHelper = new MonsterImageHelper(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");

		final ChooseSyncModelContainer<SyncedMonsterModel> item = super.getItem(position);
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_monsters_simple, parent, false);
			defaultTextColor = ((TextView) view.findViewById(R.id.choose_sync_monsters_item_padherder_exp)).getTextColors()
					.getDefaultColor();
			// For the contextMenu
			view.setLongClickable(true);
		}


		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.choose_sync_monsters_item_checkbox);
		checkBox.setChecked(item.isChosen());
		// the whole view is clickable, disable the checkBox to prevent missing clicks on it
		checkBox.setClickable(false);

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "onClick");
				item.setChosen(!item.isChosen());
				checkBox.setChecked(item.isChosen());
			}
		});

		imageHelper.fillMonsterImage((ImageView) view.findViewById(R.id.choose_sync_monsters_item_image), item.getSyncedModel().getDisplayedMonsterInfo().getIdJP());

		final TextView nameText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_name);
		nameText.setText(getContext().getString(R.string.choose_sync_monsters_item_name_simple,
				item.getSyncedModel().getDisplayedMonsterInfo().getIdJP(),
				item.getSyncedModel().getDisplayedMonsterInfo().getName()));

		final BaseMonsterModel padherder = item.getSyncedModel().getPadherderInfo();
		final BaseMonsterModel captured = item.getSyncedModel().getCapturedInfo();

		final TextView evoText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_evo);
		if (padherder != null && captured != null && padherder.getIdJp() != captured.getIdJp()) {
			evoText.setVisibility(View.VISIBLE);
			final MonsterInfoModel evolvedFrom = item.getSyncedModel().getPadherderMonsterInfo();

			evoText.setText(getContext().getString(R.string.choose_sync_monsters_item_evo,
					evolvedFrom.getIdJP(),
					evolvedFrom.getName()));
			evoText.setTextColor(padherder.getIdJp() < captured.getIdJp() ? Color.GREEN : Color.RED);
		} else {
			evoText.setVisibility(View.GONE);
		}

		fillTable(view, padherder, captured);

		final BaseMonsterModel modelToUse = captured != null ? captured : padherder;
		final TextView priorityText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_priority);
		final String priorityLabel = getContext().getString(modelToUse.getPriority().getLabelResId());
		priorityText.setText(getContext().getString(R.string.choose_sync_monsters_item_priority, priorityLabel));
		final TextView noteText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_note);
		if (StringUtils.isNotBlank(modelToUse.getNote())) {
			noteText.setVisibility(View.VISIBLE);
			noteText.setText(getContext().getString(R.string.choose_sync_monsters_item_note, modelToUse.getNote()));
		} else {
			noteText.setVisibility(View.GONE);
		}

		return view;
	}

	private void fillTable(View view, BaseMonsterModel padherder, BaseMonsterModel captured) {
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
