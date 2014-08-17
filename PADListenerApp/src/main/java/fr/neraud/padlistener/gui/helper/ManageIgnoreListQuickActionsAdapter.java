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
		this.mTaskFragment = mTaskFragment;
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

		final Integer[] imageIds = {R.id.manage_ignore_list_quick_action_item_image_1, R.id.manage_ignore_list_quick_action_item_image_2, R.id.manage_ignore_list_quick_action_item_image_3, R.id.manage_ignore_list_quick_action_item_image_4, R.id.manage_ignore_list_quick_action_item_image_5};

		boolean hasAll = true;
		boolean hasNone = true;
		for (int i = 0; i <= 4; i++) {
			Integer monsterId = null;
			if (i < item.getMonsterIds().size()) {
				monsterId = item.getMonsterIds().get(i);
			}

			boolean alreadyIgnored = false;
			if(monsterId != null) {
				alreadyIgnored = mTaskFragment.getIgnoredIds().contains(monsterId);
				if (alreadyIgnored) {
					hasNone = false;
				} else {
					hasAll = false;
				}
			}

			bindOneImage(view, i, monsterId, alreadyIgnored, imageIds[i]);
		}

		final Button removeButton = (Button) view.findViewById(R.id.manage_ignore_list_quick_action_item_remove_button);
		if (hasNone) {
			removeButton.setVisibility(View.INVISIBLE);
		} else {
			removeButton.setVisibility(View.VISIBLE);
			removeButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "removeButton.onClick");
					mTaskFragment.removeIgnoredIds(item.getMonsterIds().toArray(new Integer[0]));
				}
			});
		}

		final Button addButton = (Button) view.findViewById(R.id.manage_ignore_list_quick_action_item_add_button);
		if (hasAll) {
			addButton.setVisibility(View.INVISIBLE);
		} else {
			addButton.setVisibility(View.VISIBLE);
			addButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "addButton.onClick");
					mTaskFragment.addIgnoredIds(item.getMonsterIds().toArray(new Integer[0]));
				}
			});
		}

		return view;
	}

	private void bindOneImage(View view, int position, Integer monsterId, boolean alreadyIgnored, int imageId) {
		Log.d(getClass().getName(), "bindOneImage" + position);
		final ImageView image = (ImageView) view.findViewById(imageId);

		if (monsterId != null) {
			image.setVisibility(View.VISIBLE);
			try {
				final InputStream is = getContext().getContentResolver().openInputStream(
						MonsterInfoDescriptor.UriHelper.uriForImage(monsterId));
				final BitmapDrawable bm = new BitmapDrawable(null, is);

				if (alreadyIgnored) {
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
