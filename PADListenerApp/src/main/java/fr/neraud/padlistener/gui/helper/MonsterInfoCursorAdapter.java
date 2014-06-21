
package fr.neraud.padlistener.gui.helper;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.helper.DefaultSharedPreferencesHelper;
import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;
import fr.neraud.padlistener.provider.helper.MonsterInfoHelper;

/**
 * Adapter to display the ViewMonsterInfo fragment for the Info tab
 * 
 * @author Neraud
 */
public class MonsterInfoCursorAdapter extends SimpleCursorAdapter {

	public MonsterInfoCursorAdapter(Context context, int layout) {
		super(context, layout, null, new String[0], new int[0], 0);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		Log.d(getClass().getName(), "bindView");

		final MonsterInfoModel model = MonsterInfoHelper.cursorToModel(cursor);
		final DefaultSharedPreferencesHelper prefHelper = new DefaultSharedPreferencesHelper(context);

		final String lineName = context.getString(R.string.view_monster_info_name, model.getId(prefHelper.getPlayerRegion()),
		        model.getName());
		((TextView) view.findViewById(R.id.view_monster_info_item_name)).setText(lineName);

		try {
			final InputStream is = context.getContentResolver().openInputStream(
			        MonsterInfoDescriptor.UriHelper.uriForImage(model.getIdJP()));
			final BitmapDrawable bm = new BitmapDrawable(null, is);

			((ImageView) view.findViewById(R.id.view_monster_info_item_image)).setImageDrawable(bm);
		} catch (final FileNotFoundException e) {
			((ImageView) view.findViewById(R.id.view_monster_info_item_image)).setImageResource(R.drawable.no_monster_image);
		}
	}
}
