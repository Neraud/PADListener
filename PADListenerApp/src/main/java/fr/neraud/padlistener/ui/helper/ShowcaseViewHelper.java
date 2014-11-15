package fr.neraud.padlistener.ui.helper;

import android.app.Activity;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;

import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.model.ShowcaseHelpPageModel;

/**
 * Created by Neraud on 29/09/2014.
 */
public class ShowcaseViewHelper {

	private final Activity mActivity;
	private final List<ShowcaseHelpPageModel> mHelpPages;
	private int mCounter;
	private ShowcaseView mShowcaseView;
	private ShowcaseHelpPageModel.HelpPageListener previousPageListener = null;

	public ShowcaseViewHelper(Activity activity, List<ShowcaseHelpPageModel> helpPages) {
		mActivity = activity;
		mHelpPages = helpPages;
	}

	public void showHelp() {
		Log.d(getClass().getName(), "showHelp");
		if (mHelpPages.size() > 0) {
			mCounter = 0;
			mShowcaseView = new ShowcaseView.Builder(mActivity, true).build();

			fillShowcaseView();
			mShowcaseView.overrideButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Log.d(getClass().getName(), "onClick");
					mCounter++;

					if (mCounter < mHelpPages.size()) {
						fillShowcaseView();
					} else {
						mShowcaseView.hide();

						if (previousPageListener != null) {
							previousPageListener.onPostDisplay();
							previousPageListener = null;
						}
					}
				}
			});
		}
	}

	private void fillShowcaseView() {
		Log.d(getClass().getName(), "fillShowcaseView : " + mCounter);
		final ShowcaseHelpPageModel pageModel = mHelpPages.get(mCounter);
		final ShowcaseHelpPageModel.HelpPageListener pageListener = pageModel.getPageListener();

		if (previousPageListener != null) {
			previousPageListener.onPostDisplay();
		}

		if (pageListener != null) {
			pageListener.onPreDisplay();
		}

		final SpannableString contentSpannable = new SpannableString(pageModel.getContent());
		Linkify.addLinks(contentSpannable, Linkify.ALL);

		mShowcaseView.setContentTitle(pageModel.getTitle());
		mShowcaseView.setContentText(contentSpannable);

		if (pageModel.getTarget() != null) {
			mShowcaseView.setTarget(pageModel.getTarget());
		} else {
			mShowcaseView.setTarget(Target.NONE);
		}

		if (mCounter < mHelpPages.size() - 1) {
			mShowcaseView.setButtonText(mActivity.getString(R.string.help_button_next));
		} else {
			mShowcaseView.setButtonText(mActivity.getString(R.string.help_button_close));
		}

		// If the target is not modified, the contentText and contentTitle are not updated.
		// hack : calling setShouldCentreText flags "hasAlteredText = true" and the values are properly updated
		// TODO : find a cleaner way
		mShowcaseView.setShouldCentreText(false);

		previousPageListener = pageListener;
	}

}
