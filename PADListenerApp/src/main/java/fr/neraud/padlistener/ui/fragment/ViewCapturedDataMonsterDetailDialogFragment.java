package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.model.BaseMonsterStatsModel;
import fr.neraud.padlistener.model.MonsterInfoModel;

/**
 * Created by Neraud on 28/09/2014.
 */
public class ViewCapturedDataMonsterDetailDialogFragment extends DialogFragment {

	private BaseMonsterStatsModel mMonsterStatsModel;
	private MonsterInfoModel mMonsterInfoModel;

	@InjectView(R.id.view_captured_monster_detail_level)
	TextView mLevelTextView;
	@InjectView(R.id.view_captured_monster_detail_pluses)
	TextView mPlusesTextView;
	@InjectView(R.id.view_captured_monster_detail_awakenings)
	TextView mAwakeningsTextView;
	@InjectView(R.id.view_captured_monster_detail_skill_level)
	TextView mSkillLevelTextView;
	@InjectView(R.id.view_captured_monster_detail_image)
	ImageView monsterImageView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyLog.entry();
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mMonsterStatsModel = (BaseMonsterStatsModel) savedInstanceState.getSerializable("mMonsterStatsModel");
			mMonsterInfoModel = (MonsterInfoModel) savedInstanceState.getSerializable("mMonsterInfoModel");
		}
		MyLog.exit();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		MyLog.entry();
		super.onSaveInstanceState(outState);

		outState.putSerializable("mMonsterStatsModel", mMonsterStatsModel);
		outState.putSerializable("mMonsterInfoModel", mMonsterInfoModel);
		MyLog.exit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		MyLog.entry();

		final View view = inflater.inflate(R.layout.view_captured_data_fragment_monsters_detail, container, false);
		ButterKnife.inject(this, view);

		final String title = getString(R.string.view_captured_monster_detail_name, mMonsterInfoModel.getIdJP(), mMonsterInfoModel.getName());
		getDialog().setTitle(title);

		final String imageUrl = PadHerderDescriptor.serverUrl + mMonsterInfoModel.getImage60Url();

		Picasso.with(getActivity())
				.load(imageUrl)
				.error(R.drawable.no_monster_image)
				.into(monsterImageView);

		mLevelTextView.setText(getString(R.string.view_captured_monster_detail_level_value,
				mMonsterStatsModel.getLevel(),
				mMonsterStatsModel.getExp()));
		mPlusesTextView.setText(getString(R.string.view_captured_monster_detail_pluses_value,
				mMonsterStatsModel.getPlusHp(),
				mMonsterStatsModel.getPlusAtk(),
				mMonsterStatsModel.getPlusRcv()));
		mAwakeningsTextView.setText(getString(R.string.view_captured_monster_detail_awakenings_value, mMonsterStatsModel.getAwakenings()));
		mSkillLevelTextView.setText(getString(R.string.view_captured_monster_detail_skill_level_value, mMonsterStatsModel.getSkillLevel()));

		MyLog.exit();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	public BaseMonsterStatsModel getMonsterStatsModel() {
		return mMonsterStatsModel;
	}

	public void setMonsterStatsModel(BaseMonsterStatsModel monsterStatsModel) {
		mMonsterStatsModel = monsterStatsModel;
	}

	public MonsterInfoModel getMonsterInfoModel() {
		return mMonsterInfoModel;
	}

	public void setMonsterInfoModel(MonsterInfoModel monsterInfoModel) {
		mMonsterInfoModel = monsterInfoModel;
	}
}
