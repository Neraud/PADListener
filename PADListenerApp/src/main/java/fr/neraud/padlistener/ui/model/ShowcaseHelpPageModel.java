package fr.neraud.padlistener.ui.model;

import com.github.amlcurran.showcaseview.targets.Target;

import fr.neraud.padlistener.ui.helper.ShowcaseViewHelper;

/**
 * Created by Neraud on 29/09/2014.
 */
public class ShowcaseHelpPageModel {

	private Target target;
	private String title;
	private String content;
	private ShowcaseViewHelper.HelpPageListener pageListener;

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
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

	public ShowcaseViewHelper.HelpPageListener getPageListener() {
		return pageListener;
	}

	public void setPageListener(ShowcaseViewHelper.HelpPageListener pageListener) {
		this.pageListener = pageListener;
	}
}
