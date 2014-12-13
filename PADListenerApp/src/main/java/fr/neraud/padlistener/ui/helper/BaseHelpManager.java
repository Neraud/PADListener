package fr.neraud.padlistener.ui.helper;

import android.app.Activity;

import com.github.amlcurran.showcaseview.targets.ActionViewTarget;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.helper.TechnicalSharedPreferencesHelper;
import fr.neraud.padlistener.ui.model.ShowcaseHelpPageModel;

/**
 * Created by Neraud on 01/10/2014.
 */
public abstract class BaseHelpManager {

	private final Activity mActivity;
	private final String mHelpTag;
	private final int mHelpVersion;
	private final TechnicalSharedPreferencesHelper mTechHelper;
	private ShowcaseViewHelper mShowcaseViewHelper = null;

	public class PageBuilder {

		private final List<ShowcaseHelpPageModel> mHelpPages = new ArrayList<ShowcaseHelpPageModel>();

		public PageBuilder addHelpPage(int titleResId, int contentResId) {
			return addHelpPage(titleResId, contentResId, null);
		}

		public PageBuilder addHelpPage(int titleResId, int contentResId, ShowcaseHelpPageModel.HelpPageListener listener) {
			return addHelpPage(mActivity.getString(titleResId), mActivity.getString(contentResId), null, listener);
		}

		public PageBuilder addHelpPage(int titleResId, int contentResId, int targetViewId, ShowcaseHelpPageModel.HelpPageListener listener) {
			return addHelpPage(mActivity.getString(titleResId), mActivity.getString(contentResId), TargetBuilder.createViewTarget(mActivity, targetViewId), listener);
		}

		public PageBuilder addHelpPage(int titleResId, int contentResId, ActionViewTarget.Type actionViewTargetType, ShowcaseHelpPageModel.HelpPageListener listener) {
			return addHelpPage(mActivity.getString(titleResId), mActivity.getString(contentResId), TargetBuilder.createActionViewTarget(mActivity, actionViewTargetType), listener);
		}

		private PageBuilder addHelpPage(String title, String content, ShowcaseHelpPageModel.TargetWrapper targetWrapper, ShowcaseHelpPageModel.HelpPageListener pageListener) {
			final ShowcaseHelpPageModel pageModel = new ShowcaseHelpPageModel();
			pageModel.setTitle(title);
			pageModel.setContent(content);
			pageModel.setTargetWrapper(targetWrapper);
			pageModel.setPageListener(pageListener);
			mHelpPages.add(pageModel);
			return this;
		}

		List<ShowcaseHelpPageModel> getHelpPages() {
			return mHelpPages;
		}
	}

	protected BaseHelpManager(Activity activity, String helpTag, int helpVersion) {
		mActivity = activity;
		mHelpTag = helpTag;
		mHelpVersion = helpVersion;
		mTechHelper = new TechnicalSharedPreferencesHelper(mActivity);
	}

	public void showHelpFirstTime() {
		MyLog.entry();

		final int lastHelpDisplayedVersion = mTechHelper.getLastHelpDisplayedVersionForTag(mHelpTag);
		if (lastHelpDisplayedVersion < mHelpVersion) {
			final PageBuilder builder = new PageBuilder();
			buildDeltaHelpPages(builder, lastHelpDisplayedVersion);

			doShow(builder);
		}

		MyLog.exit();
	}

	public void showHelp() {
		MyLog.entry();
		final PageBuilder builder = new PageBuilder();
		buildHelpPages(builder);

		doShow(builder);
		MyLog.exit();
	}

	private void doShow(PageBuilder builder) {
		mShowcaseViewHelper = new ShowcaseViewHelper(mActivity, builder.getHelpPages(), new ShowcaseViewHelper.ShowcaseViewListener() {
			@Override
			public void showingHelp(int pageId) {
			}

			@Override
			public void finished() {
				mShowcaseViewHelper = null;
			}
		});
		mShowcaseViewHelper.showHelp();
		mTechHelper.setLastHelpDisplayedVersionForTag(mHelpTag, mHelpVersion);
	}

	public abstract void buildHelpPages(PageBuilder builder);

	public abstract void buildDeltaHelpPages(PageBuilder builder, int lastDisplayedVersion);

	public boolean isShowingHelp() {
		return mShowcaseViewHelper != null && mShowcaseViewHelper.isShowingHelp();
	}

	public void closeHelp() {
		if (mShowcaseViewHelper != null) mShowcaseViewHelper.closeHelp();
	}

}
