
package fr.neraud.padlistener.gui.helper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.model.BaseMonsterModel;
import fr.neraud.padlistener.model.ChooseSyncModelContainer;
import fr.neraud.padlistener.model.SyncedMonsterModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

/**
 * Adaptor to display Monsters set up as simple
 * 
 * @author Neraud
 */
public class ChooseSyncMonstersSimpleAdapter extends ArrayAdapter<ChooseSyncModelContainer<SyncedMonsterModel>> {

	private Integer defaultTextColor = null;

	public ChooseSyncMonstersSimpleAdapter(Context context,
	        List<ChooseSyncModelContainer<SyncedMonsterModel>> syncedMonstersToUpdate) {
		super(context, R.layout.choose_sync_item_monsters_simple, syncedMonstersToUpdate);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");
		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(getContext());

		final ChooseSyncModelContainer<SyncedMonsterModel> item = super.getItem(position);
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_monsters_simple, parent, false);
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

		final ImageView image = (ImageView) view.findViewById(R.id.choose_sync_monsters_item_image);
		try {
			final InputStream is = getContext().getContentResolver().openInputStream(
			        MonsterInfoDescriptor.UriHelper.uriForImage(item.getSyncedModel().getMonsterInfo().getIdJP()));
			final BitmapDrawable bm = new BitmapDrawable(null, is);

			image.setImageDrawable(bm);
		} catch (final FileNotFoundException e) {
			image.setImageResource(R.drawable.no_monster_image);
		}
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(getClass().getName(), "onClick");
				item.setChoosen(!item.isChoosen());
				checkBox.setChecked(item.isChoosen());
			}
		});

		final TextView nameText = (TextView) view.findViewById(R.id.choose_sync_monsters_item_name);
		nameText.setText(getContext().getString(R.string.choose_sync_monsters_item_name_simple,
		        item.getSyncedModel().getMonsterInfo().getId(prefHelper.getPlayerRegion()),
		        item.getSyncedModel().getMonsterInfo().getName()));

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
