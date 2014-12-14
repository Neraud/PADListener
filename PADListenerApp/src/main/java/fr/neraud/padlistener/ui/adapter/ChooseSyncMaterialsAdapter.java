package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.ChooseModelContainer;
import fr.neraud.padlistener.model.SyncedMaterialModel;
import fr.neraud.padlistener.ui.helper.MonsterImageHelper;

/**
 * Adaptor to display the choose sync materials
 *
 * @author Neraud
 */
public class ChooseSyncMaterialsAdapter extends ArrayAdapter<ChooseModelContainer<SyncedMaterialModel>> {

	private Integer mDefaultTextColor = null;
	private MonsterImageHelper mImageHelper;

	static class ViewHolder {

		@InjectView(R.id.choose_sync_materials_item_checkbox)
		CheckBox checkBox;
		@InjectView(R.id.choose_sync_materials_item_image)
		ImageView image;
		@InjectView(R.id.choose_sync_materials_item_name)
		TextView nameText;
		@InjectView(R.id.choose_sync_materials_item_quantities)
		TextView quantitiesText;

		public ViewHolder(View view) {
			ButterKnife.inject(this, view);
		}
	}

	public ChooseSyncMaterialsAdapter(Context context, List<ChooseModelContainer<SyncedMaterialModel>> syncedMaterialsToUpdate) {
		super(context, 0, syncedMaterialsToUpdate);
		mImageHelper = new MonsterImageHelper(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		MyLog.entry();

		final ChooseModelContainer<SyncedMaterialModel> item = super.getItem(position);

		final ViewHolder viewHolder;
		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.choose_sync_item_materials, parent, false);
			mDefaultTextColor = ((TextView) view.findViewById(R.id.choose_sync_materials_item_quantities)).getTextColors().getDefaultColor();
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.checkBox.setChecked(item.isChosen());
		viewHolder.checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyLog.entry();
				item.setChosen(!item.isChosen());
				MyLog.exit();
			}
		});

		mImageHelper.fillImage(viewHolder.image, item.getModel().getDisplayedMonsterInfo());
		viewHolder.image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MyLog.entry();
				item.setChosen(!item.isChosen());
				viewHolder.checkBox.setChecked(item.isChosen());
				MyLog.exit();
			}
		});

		viewHolder.nameText.setText(getContext().getString(R.string.choose_sync_materials_item_name,
				item.getModel().getDisplayedMonsterInfo().getIdJP(),
				item.getModel().getDisplayedMonsterInfo().getName()));

		viewHolder.quantitiesText.setText(getContext().getString(R.string.choose_sync_materials_item_quantities,
				item.getModel().getPadherderInfo(), item.getModel().getCapturedInfo()));

		int textColor;
		if (item.getModel().getPadherderInfo() < item.getModel().getCapturedInfo()) {
			textColor = getContext().getResources().getColor(R.color.text_increase);
		} else if (item.getModel().getPadherderInfo() > item.getModel().getCapturedInfo()) {
			textColor = getContext().getResources().getColor(R.color.text_decrease);
		} else {
			// Shouldn't happen
			textColor = mDefaultTextColor;
		}
		viewHolder.quantitiesText.setTextColor(textColor);

		MyLog.exit();
		return view;
	}
}