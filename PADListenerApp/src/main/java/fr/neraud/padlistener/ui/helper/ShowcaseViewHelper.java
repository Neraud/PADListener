package fr.neraud.padlistener.ui.helper;

import android.app.Activity;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.view.View;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;

import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.model.ShowcaseHelpPageModel;

/**
 * Created by Neraud on 29/09/2014.
 */
public class ShowcaseViewHelper {

	private final Activity mActivity;
	private final List<ShowcaseHelpPageModel> mHelpPages;
	private final ShowcaseViewListener mListener;
	private int mCounter;
	private boolean mIsShowingHelp = false;
	private ShowcaseView mShowcaseView;
	private ShowcaseHelpPageModel.HelpPageListener mPreviousPageListener = null;

	public static interface ShowcaseViewListener {

		public void showingHelp(int pageId);

		public void finished();
	}

	public ShowcaseViewHelper(Activity activity, List<ShowcaseHelpPageModel> helpPages, ShowcaseViewListener listener) {
		mActivity = activity;
		mHelpPages = helpPages;
		mListener = listener;
	}

	public void showHelp() {
		MyLog.entry();

		if (mHelpPages.size() > 0) {
			mCounter = 0;
			mShowcaseView = new ShowcaseView.Builder(mActivity, true).build();

			showPage();
			mShowcaseView.overrideButtonClick(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					MyLog.entry();
					mCounter++;

					if (mCounter < mHelpPages.size()) {
						showPage();
					} else {
						closeHelp();
					}
					MyLog.exit();
				}
			});
		}

		MyLog.exit();
	}

	private void fillShowcaseView() {
		MyLog.entry("counter = " + mCounter);

		final ShowcaseHelpPageModel pageModel = mHelpPages.get(mCounter);
		final ShowcaseHelpPageModel.HelpPageListener pageListener = pageModel.getPageListener();

		if (mPreviousPageListener != null) {
			mPreviousPageListener.onPostDisplay();
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

		mPreviousPageListener = pageListener;

		MyLog.exit();
	}

	public boolean isShowingHelp() {
		return mIsShowingHelp;
	}

	private void showPage() {
		mIsShowingHelp = true;
		if (mListener != null) {
			mListener.showingHelp(mCounter);
		}
		fillShowcaseView();
	}

	public void closeHelp() {
		MyLog.entry();

		mIsShowingHelp = false;

		if (mListener != null) {
			mListener.finished();
		}

		mShowcaseView.hide();

		if (mPreviousPageListener != null) {
			mPreviousPageListener.onPostDisplay();
			mPreviousPageListener = null;
		}

		MyLog.exit();
	}

}
