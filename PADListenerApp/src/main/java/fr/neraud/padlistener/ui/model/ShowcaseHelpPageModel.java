package fr.neraud.padlistener.ui.model;

import com.github.amlcurran.showcaseview.targets.Target;

/**
 * Created by Neraud on 29/09/2014.
 */
public class ShowcaseHelpPageModel {

	public interface HelpPageListener {

		public void onPreDisplay();

		public void onPostDisplay();
	}

	public interface TargetWrapper {
		public Target getTarget();
	}

	private TargetWrapper targetWrapper;
	private String title;
	private String content;
	private HelpPageListener pageListener;

	public Target getTarget() {
		return targetWrapper != null ? targetWrapper.getTarget() : Target.NONE;
	}

	public TargetWrapper getTargetWrapper() {
		return targetWrapper;
	}

	public void setTargetWrapper(TargetWrapper targetWrapper) {
		this.targetWrapper = targetWrapper;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public HelpPageListener getPageListener() {
		return pageListener;
	}

	public void setPageListener(HelpPageListener pageListener) {
		this.pageListener = pageListener;
	}
}
