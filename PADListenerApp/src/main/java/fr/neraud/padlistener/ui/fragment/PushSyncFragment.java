package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.neraud.log.MyLog;
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

	public static final String EXTRA_CHOOSE_SYNC_MODEL_NAME = "sync_model";
	public static final String EXTRA_ACCOUNT_ID_NAME = "accountId";
	private static final String TAG_TASK_FRAGMENT = "push_sync_task_fragment";

	private TextView mHead;
	private ProgressBar mProgress;
	private TextView mSummaryUserInfoUpdated;
	private TextView mSummaryMaterialsUpdated;
	private TextView mSummaryMonstersUpdated;
	private TextView mSummaryMonstersCreated;
	private TextView mSummaryMonstersDeleted;

	private final PushSyncTaskFragment.CallBacks callbacks = new PushSyncTaskFragment.CallBacks() {

		@Override
		public void updateState(PushSyncStatModel pushModel) {
			MyLog.entry();

			if (pushModel != null) {
				MyLog.debug(pushModel.getElementPushedCount() + pushModel.getElementErrorCount() + " / " + pushModel.getElementToPushCount());

				mProgress.setIndeterminate(false);
				mProgress.setMax(pushModel.getElementToPushCount());
				mProgress.setProgress(pushModel.getElementPushedCount() + pushModel.getElementErrorCount());
				updateText(pushModel, mSummaryUserInfoUpdated, R.string.push_sync_summary_userinfo_updated, ElementToPush.USER_INFO);
				updateText(pushModel, mSummaryMaterialsUpdated, R.string.push_sync_summary_materials_updated,
						ElementToPush.MATERIAL_TO_UPDATE);
				updateText(pushModel, mSummaryMonstersUpdated, R.string.push_sync_summary_monsters_updated,
						ElementToPush.MONSTER_TO_UPDATE);
				updateText(pushModel, mSummaryMonstersCreated, R.string.push_sync_summary_monsters_created,
						ElementToPush.MONSTER_TO_CREATE);
				updateText(pushModel, mSummaryMonstersDeleted, R.string.push_sync_summary_monsters_deleted,
						ElementToPush.MONSTER_TO_DELETE);

				if (pushModel.getElementPushedCount() + pushModel.getElementErrorCount() >= pushModel.getElementToPushCount()) {
					mHead.setText(R.string.push_sync_head_done);
				} else {
					mHead.setText(R.string.push_sync_head_pushing);
				}
			}

			MyLog.exit();
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
		MyLog.entry();

		final View view = inflater.inflate(R.layout.push_sync_fragment, container, false);

		mHead = (TextView) view.findViewById(R.id.push_sync_head);
		mProgress = (ProgressBar) view.findViewById(R.id.push_sync_progress);
		mSummaryUserInfoUpdated = (TextView) view.findViewById(R.id.push_sync_summary_userinfo_updated);
		mSummaryMaterialsUpdated = (TextView) view.findViewById(R.id.push_sync_summary_materials_updated);
		mSummaryMonstersUpdated = (TextView) view.findViewById(R.id.push_sync_summary_monsters_updated);
		mSummaryMonstersCreated = (TextView) view.findViewById(R.id.push_sync_summary_monsters_created);
		mSummaryMonstersDeleted = (TextView) view.findViewById(R.id.push_sync_summary_monsters_deleted);

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

		MyLog.exit();
		return view;
	}
}
