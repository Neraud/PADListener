package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import fr.neraud.padlistener.model.MonsterInfoModel;
import fr.neraud.padlistener.ui.helper.MonsterImageHelper;

/**
 * Base Adapter to display monsters
 *
 * @author Neraud
 */
public abstract class AbstractMonsterCursorAdapter extends SimpleCursorAdapter {

	private Context mContext;

	protected AbstractMonsterCursorAdapter(Context context, int layoutId) {
		super(context, layoutId, null, new String[0], new int[0], 0);
		mContext = context;
	}

	protected void fillImage(ImageView imageView, MonsterInfoModel model) {
		new MonsterImageHelper(mContext).fillImage(imageView, model);
	}
}
