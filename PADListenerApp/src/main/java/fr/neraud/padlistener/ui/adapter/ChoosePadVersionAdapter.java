package fr.neraud.padlistener.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.constant.PADVersion;
import fr.neraud.padlistener.util.InstalledPadVersionsHelper;

/**
 * Created by Neraud on 23/11/2014.
 */
public class ChoosePadVersionAdapter extends ArrayAdapter<PADVersion> {

	private final Context mContext;
	private final InstalledPadVersionsHelper padVersionHelper;

	public ChoosePadVersionAdapter(Context context) {
		super(context, 0);
		this.mContext = context;
		padVersionHelper = new InstalledPadVersionsHelper(mContext);
		initElements();
	}

	private void initElements() {
		Log.d(getClass().getName(), "initElements");
		final InstalledPadVersionsHelper helper = new InstalledPadVersionsHelper(mContext);
		addAll(helper.getInstalledPadVersion());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(getClass().getName(), "getView : " + position);
		if (convertView == null) {
			final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.choose_pad_version_item, parent, false);
		}

		convertView.setFocusable(false);
		final ImageView image = (ImageView) convertView.findViewById(R.id.home_fragment_launch_item_icon);
		final TextView nameText = (TextView) convertView.findViewById(R.id.home_fragment_launch_item_name_text);
		final TextView runningText = (TextView) convertView.findViewById(R.id.home_fragment_launch_item_running_text);

		final PADVersion version = super.getItem(position);

		nameText.setText(version.name());

		final Drawable padIcon = padVersionHelper.getPadIcon(version);
		image.setImageDrawable(padIcon);

		final boolean padRunning = padVersionHelper.isPadRunning(version);
		runningText.setVisibility(padRunning ? View.VISIBLE : View.GONE);

		return convertView;
	}
}
