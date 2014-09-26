package fr.neraud.padlistener.ui.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Neraud on 26/09/2014.
 */
public class ActionsCard extends Card {

	private String mContentText;
	private String mPrimaryActionTitle;
	private boolean mPrimaryActionEnabled = true;
	private View.OnClickListener mPrimaryActionOnClickListener;
	private String mSecondaryActionTitle;
	private boolean mSecondaryActionEnabled = true;
	private View.OnClickListener mSecondaryActionOnClickListener;

	public ActionsCard(Context context) {
		super(context, R.layout.card_with_actions);
	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View view) {
		final TextView content = (TextView) parent.findViewById(R.id.card_with_actions_content_text);
		final Button primaryButton = (Button) parent.findViewById(R.id.card_with_actions_primary_button);
		final Button secondaryButton = (Button) parent.findViewById(R.id.card_with_actions_secondary_button);

		if (mContentText != null) {
			content.setText(mContentText);
		}

		fillButton(primaryButton, mPrimaryActionTitle, mPrimaryActionEnabled, mPrimaryActionOnClickListener, R.drawable.button_primary);
		fillButton(secondaryButton, mSecondaryActionTitle, mSecondaryActionEnabled, mSecondaryActionOnClickListener, R.drawable.button_secondary);
	}

	private void fillButton(Button button, String title, boolean enabled, View.OnClickListener listener, int drawableResId) {
		if (title != null) {
			button.setText(title);

			if (enabled) {
				button.setBackgroundResource(drawableResId);
			} else {
				// if the button is disabled, the listener won't be triggered, so we just swap the background to the disabled one
				Drawable draw = mContext.getResources().getDrawable(drawableResId);
				draw.setState(new int[]{-android.R.attr.state_enabled});

				// deprecated since API 16 which introduced setBackgound(Drawable), but to remain compatible with API15 it still must be used
				//noinspection deprecation
				button.setBackgroundDrawable(draw.getCurrent());
			}

			if (listener != null) {
				button.setOnClickListener(listener);
			}
		} else {
			button.setVisibility(View.GONE);
		}
	}

	public String getContentText() {
		return mContentText;
	}

	public void setContentText(String contentText) {
		mContentText = contentText;
	}

	public String getPrimaryActionTitle() {
		return mPrimaryActionTitle;
	}

	public void setPrimaryActionTitle(String primaryActionTitle) {
		mPrimaryActionTitle = primaryActionTitle;
	}

	public boolean isPrimaryActionEnabled() {
		return mPrimaryActionEnabled;
	}

	public void setPrimaryActionEnabled(boolean primaryActionEnabled) {
		mPrimaryActionEnabled = primaryActionEnabled;
	}

	public View.OnClickListener getPrimaryActionOnClickListener() {
		return mPrimaryActionOnClickListener;
	}

	public void setPrimaryActionOnClickListener(View.OnClickListener primaryActionOnClickListener) {
		mPrimaryActionOnClickListener = primaryActionOnClickListener;
	}

	public String getSecondaryActionTitle() {
		return mSecondaryActionTitle;
	}

	public void setSecondaryActionTitle(String secondaryActionTitle) {
		mSecondaryActionTitle = secondaryActionTitle;
	}

	public boolean isSecondaryActionEnabled() {
		return mSecondaryActionEnabled;
	}

	public void setSecondaryActionEnabled(boolean secondaryActionEnabled) {
		mSecondaryActionEnabled = secondaryActionEnabled;
	}

	public View.OnClickListener getSecondaryActionOnClickListener() {
		return mSecondaryActionOnClickListener;
	}

	public void setSecondaryActionOnClickListener(View.OnClickListener secondaryActionOnClickListener) {
		mSecondaryActionOnClickListener = secondaryActionOnClickListener;
	}
}
