package fr.neraud.padlistener.gui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.ChooseSyncModel;
import fr.neraud.padlistener.model.PushSyncStatModel;
import fr.neraud.padlistener.model.PushSyncStatModel.ElementToPush;

/**
 * Main fragment for PushSync
 *
 * @author Neraud
 */
public class PushSyncFragment extends Fragment {

	protected static final String EXTRA_CHOOSE_SYNC_MODEL_NAME = "sync_model";
	protected static final String EXTRA_ACCOUNT_ID_NAME = "accountId";
	private static final String TAG_TASK_FRAGMENT = "push_sync_task_fragment";

	private TextView head;
	private ProgressBar progress;
	private TextView summaryUserInfoUpdated;
	private TextView summaryMaterialsUpdated;
	private TextView summaryMonstersUpdated;
	private TextView summaryMonstersCreated;
	private TextView summaryMonstersDeleted;

	private final PushSyncTaskFragment.CallBacks callbacks = new PushSyncTaskFragment.CallBacks() {

		@Override
		public void updateState(PushSyncStatModel pushModel) {
			Log.d(getClass().getName(), "updateState");
			if (pushModel != null) {
				Log.d(getClass().getName(), "updateState : " + pushModel.getElementPushedCount() + pushModel.getElementErrorCount()
						+ " / " + pushModel.getElementToPushCount());

				progress.setIndeterminate(false);
				progress.setMax(pushModel.getElementToPushCount());
				progress.setProgress(pushModel.getElementPushedCount() + pushModel.getElementErrorCount());
				updateText(pushModel, summaryUserInfoUpdated, R.string.push_sync_summary_userinfo_updated, ElementToPush.USER_INFO);
				updateText(pushModel, summaryMaterialsUpdated, R.string.push_sync_summary_materials_updated,
						ElementToPush.MATERIAL_TO_UPDATE);
				updateText(pushModel, summaryMonstersUpdated, R.string.push_sync_summary_monsters_updated,
						ElementToPush.MONSTER_TO_UPDATE);
				updateText(pushModel, summaryMonstersCreated, R.string.push_sync_summary_monsters_created,
						ElementToPush.MONSTER_TO_CREATE);
				updateText(pushModel, summaryMonstersDeleted, R.string.push_sync_summary_monsters_deleted,
						ElementToPush.MONSTER_TO_DELETE);

				if (pushModel.getElementPushedCount() + pushModel.getElementErrorCount() >= pushModel.getElementToPushCount()) {
					head.setText(R.string.push_sync_head_done);
				} else {
					head.setText(R.string.push_sync_head_pushing);
				}
			}
		}

		private void updateText(PushSyncStatModel pushModel, TextView tv, int resId, ElementToPush element) {
			final int pushedCount = pushModel.getElementsPushed().get(element);
			final int max = pushModel.getElementsToPush().get(element);
			final int errorCount = pushModel.getElementsError().get(element);
			tv.setText(getString(resId, pushedCount + errorCount, max, errorCount));
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.push_sync_fragment, container, false);

		head = (TextView) view.findViewById(R.id.push_sync_head);
		progress = (ProgressBar) view.findViewById(R.id.push_sync_progress);
		summaryUserInfoUpdated = (TextView) view.findViewById(R.id.push_sync_summary_userinfo_updated);
		summaryMaterialsUpdated = (TextView) view.findViewById(R.id.push_sync_summary_materials_updated);
		summaryMonstersUpdated = (TextView) view.findViewById(R.id.push_sync_summary_monsters_updated);
		summaryMonstersCreated = (TextView) view.findViewById(R.id.push_sync_summary_monsters_created);
		summaryMonstersDeleted = (TextView) view.findViewById(R.id.push_sync_summary_monsters_deleted);

		final FragmentManager fm = getFragmentManager();
		PushSyncTaskFragment mTaskFragment = (PushSyncTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
		if (mTaskFragment == null) {
			mTaskFragment = new PushSyncTaskFragment();
			fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();

			final ChooseSyncModel result = (ChooseSyncModel) getActivity().getIntent().getSerializableExtra(
					EXTRA_CHOOSE_SYNC_MODEL_NAME);
			final int accountId = getActivity().getIntent().getIntExtra(EXTRA_ACCOUNT_ID_NAME, 0);

			mTaskFragment.setChooseSyncModel(result);
			mTaskFragment.setAccountId(accountId);
		}
		mTaskFragment.registerCallbacks(callbacks);

		return view;
	}
}
