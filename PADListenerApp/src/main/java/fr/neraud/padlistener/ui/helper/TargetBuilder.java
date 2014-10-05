package fr.neraud.padlistener.ui.helper;

import android.app.Activity;

import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import fr.neraud.padlistener.ui.model.ShowcaseHelpPageModel;

/**
 * Created by Neraud on 05/10/2014.
 */
public class TargetBuilder {

	public static ShowcaseHelpPageModel.TargetWrapper createViewTarget(final Activity activity, final int viewId) {
		return new ShowcaseHelpPageModel.TargetWrapper() {
			@Override
			public Target getTarget() {
				return new ViewTarget(activity.findViewById(viewId));
			}
		};
	}

	public static ShowcaseHelpPageModel.TargetWrapper createActionViewTarget(final Activity activity, final ActionViewTarget.Type actionViewTargetType) {
		return new ShowcaseHelpPageModel.TargetWrapper() {
			@Override
			public Target getTarget() {
				return new ActionViewTarget(activity, actionViewTargetType);
			}
		};
	}
}
