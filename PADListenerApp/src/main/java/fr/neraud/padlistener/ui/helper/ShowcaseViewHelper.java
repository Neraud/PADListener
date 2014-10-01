package fr.neraud.padlistener.ui.helper;

import android.app.Activity;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.model.ShowcaseHelpPageModel;

/**
 * Created by Neraud on 29/09/2014.
 */
public class ShowcaseViewHelper {

	private final Activity mActivity;
	private final List<ShowcaseHelpPageModel> mHelpPages;
	private final PageBuilder mBuilder;
	private int mCounter = 0;
	private ShowcaseView mShowcaseView;
	private HelpPageListener previousPageListener = null;

	public ShowcaseViewHelper(Activity activity) {
		mActivity = activity;
		mHelpPages = new ArrayList<ShowcaseHelpPageModel>();
		mBuilder = new PageBuilder();
	}

	public interface HelpPageListener {

		public void onPreDisplay();

		public void onPostDisplay();
	}

	public class PageBuilder {

		public PageBuilder addHelpPage(int titleResId, int contentResId) {
			return addHelpPage(titleResId, contentResId, null);
		}

		public PageBuilder addHelpPage(int titleResId, int contentResId, HelpPageListener listener) {
			return addHelpPage(mActivity.getString(titleResId), mActivity.getString(contentResId), null, listener);
		}

		public PageBuilder addHelpPage(int titleResId, int contentResId, int targetViewId, HelpPageListener listener) {
			return addHelpPage(mActivity.getString(titleResId), mActivity.getString(contentResId), new ViewTarget(mActivity.findViewById(targetViewId)), listener);
		}

		public PageBuilder addHelpPage(int titleResId, int contentResId, ActionViewTarget.Type actionViewTargetType, HelpPageListener listener) {
			return addHelpPage(mActivity.getString(titleResId), mActivity.getString(contentResId), new ActionViewTarget(mActivity, actionViewTargetType), listener);
		}

		private PageBuilder addHelpPage(String title, String content, Target target, HelpPageListener pageListener) {
			final ShowcaseHelpPageModel pageModel = new ShowcaseHelpPageModel();
			pageModel.setTitle(title);
			pageModel.setContent(content);
			pageModel.setTarget(target);
			pageModel.setPageListener(pageListener);
			mHelpPages.add(pageModel);
			return this;
		}
	}

	public void showHelp() {
		Log.d(getClass().getName(), "showHelp");
		if (mHelpPages.size() > 0) {
			mShowcaseView = new ShowcaseView.Builder(mActivity, true).setContentText("Fake content").setContentTitle("Fake title").build();

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
		final HelpPageListener pageListener = pageModel.getPageListener();

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


		Log.d(getClass().getName(), "fillShowcaseView : " + mCounter + " : hasShowcaseView = " + mShowcaseView.hasShowcaseView());
	}

	private ShowcaseHelpPageModel buildPage(String title, String content) {
		final ShowcaseHelpPageModel pageModel = new ShowcaseHelpPageModel();
		pageModel.setTitle(title);
		pageModel.setContent(content);
		return pageModel;
	}

	public PageBuilder getBuilder() {
		return mBuilder;
	}
}
