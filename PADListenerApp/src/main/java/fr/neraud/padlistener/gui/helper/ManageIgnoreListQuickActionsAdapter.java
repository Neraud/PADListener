package fr.neraud.padlistener.gui.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.gui.fragment.ManageIgnoreListTaskFragment;
import fr.neraud.padlistener.model.IgnoreMonsterQuickActionModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

/**
 * Created by Neraud on 16/08/2014.
 */
public class ManageIgnoreListQuickActionsAdapter extends ArrayAdapter<IgnoreMonsterQuickActionModel> {

	private ManageIgnoreListTaskFragment mTaskFragment;

	public ManageIgnoreListQuickActionsAdapter(Context context, List<IgnoreMonsterQuickActionModel> ignoreMonsterQuickActionModels, ManageIgnoreListTaskFragment mTaskFragment) {
		super(context, R.layout.manage_ignore_list_quick_action_item, ignoreMonsterQuickActionModels);
		this.mTaskFragment=mTaskFragment;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		Log.d(getClass().getName(), "getView");

		if (view == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.manage_ignore_list_quick_action_item, parent, false);
		}

		final IgnoreMonsterQuickActionModel item = super.getItem(position);
		Log.d(getClass().getName(), "getView : " + item);

		final TextView nameText = (TextView) view.findViewById(R.id.manage_ignore_list_quick_action_item_name);
		nameText.setText(getContext().getString(R.string.manage_ignore_list_quick_action_name, item.getQuickActionName()));

		bindOneImage(view, item.getMonsterIds(), 0, R.id.manage_ignore_list_quick_action_item_image_1);
		bindOneImage(view, item.getMonsterIds(), 1, R.id.manage_ignore_list_quick_action_item_image_2);
		bindOneImage(view, item.getMonsterIds(), 2, R.id.manage_ignore_list_quick_action_item_image_3);
		bindOneImage(view, item.getMonsterIds(), 3, R.id.manage_ignore_list_quick_action_item_image_4);
		bindOneImage(view, item.getMonsterIds(), 4, R.id.manage_ignore_list_quick_action_item_image_5);

		final Button removeButton = (Button) view.findViewById(R.id.manage_ignore_list_quick_action_item_remove_button);
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "removeButton.onClick");
				mTaskFragment.removeIgnoredIds(item.getMonsterIds().toArray(new Integer[0]));
			}
		});

		final Button addButton = (Button) view.findViewById(R.id.manage_ignore_list_quick_action_item_add_button);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "addButton.onClick");
				mTaskFragment.addIgnoredIds(item.getMonsterIds().toArray(new Integer[0]));
			}
		});


		return view;
	}

	private void bindOneImage(View view, List<Integer> monsterIds, int position, int imageId) {
		Log.d(getClass().getName(), "bindOneImage : " + position);
		final ImageView image = (ImageView) view.findViewById(imageId);

		if (position < monsterIds.size()) {
			final Integer monsterId = monsterIds.get(position);
			image.setVisibility(View.VISIBLE);
			try {
				final InputStream is = getContext().getContentResolver().openInputStream(
						MonsterInfoDescriptor.UriHelper.uriForImage(monsterId));
				final BitmapDrawable bm = new BitmapDrawable(null, is);

				if (mTaskFragment.getIgnoredIds().contains(monsterId)) {
					Log.d(getClass().getName(), "bindOneImage : monster at " + position + " : " + monsterId + " already in list");
					image.setImageDrawable(bm);
				} else {
					Log.d(getClass().getName(), "bindOneImage : monster at " + position + " : " + monsterId + " not yet in list");
					image.setImageBitmap(convertColorIntoBlackAndWhiteImage(bm.getBitmap()));
				}
			} catch (final FileNotFoundException e) {
				image.setImageResource(R.drawable.no_monster_image);
			}
		} else {
			Log.d(getClass().getName(), "bindOneImage : no monster at " + position + ", ignored");
			image.setVisibility(View.INVISIBLE);
		}
	}

	private Bitmap convertColorIntoBlackAndWhiteImage(Bitmap orginalBitmap) {
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);

		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
				colorMatrix);

		Bitmap blackAndWhiteBitmap = orginalBitmap.copy(Bitmap.Config.ARGB_8888, true);

		Paint paint = new Paint();
		paint.setColorFilter(colorMatrixFilter);

		Canvas canvas = new Canvas(blackAndWhiteBitmap);
		canvas.drawBitmap(blackAndWhiteBitmap, 0, 0, paint);

		return blackAndWhiteBitmap;
	}

}
