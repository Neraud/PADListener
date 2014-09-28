package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.http.helper.PadHerderDescriptor;
import fr.neraud.padlistener.model.CapturedMonsterFullInfoModel;

/**
 * Created by Neraud on 28/09/2014.
 */
public class ViewCapturedDataMonsterDetailDialogFragment extends DialogFragment {

	private CapturedMonsterFullInfoModel mModel;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View view = inflater.inflate(R.layout.view_captured_data_fragment_monster_detail, container, false);
		final String title = getString(R.string.view_captured_monster_detail_name, mModel.getMonsterInfo().getIdJP(), mModel.getMonsterInfo().getName());
		getDialog().setTitle(title);

		final ImageView monsterImageView = (ImageView) view.findViewById(R.id.view_captured_monster_detail_image);
		final String imageUrl = PadHerderDescriptor.serverUrl + mModel.getMonsterInfo().getImage60Url();

		Picasso.with(getActivity())
				.load(imageUrl)
				.error(R.drawable.no_monster_image)
				.into(monsterImageView);

		final TextView levelTextView = (TextView) view.findViewById(R.id.view_captured_monster_detail_level);
		levelTextView.setText(getString(R.string.view_captured_monster_detail_level_value,
				mModel.getCapturedMonster().getLevel(),
				mModel.getCapturedMonster().getExp()));

		final TextView plusesTextView = (TextView) view.findViewById(R.id.view_captured_monster_detail_pluses);
		plusesTextView.setText(getString(R.string.view_captured_monster_detail_pluses_value,
				mModel.getCapturedMonster().getPlusHp(),
				mModel.getCapturedMonster().getPlusAtk(),
				mModel.getCapturedMonster().getPlusRcv()));

		final TextView awakeningsTextView = (TextView) view.findViewById(R.id.view_captured_monster_detail_awakenings);
		awakeningsTextView.setText(getString(R.string.view_captured_monster_detail_awakenings_value, mModel.getCapturedMonster().getAwakenings()));

		final TextView skillLevelTextView = (TextView) view.findViewById(R.id.view_captured_monster_detail_skill_level);
		skillLevelTextView.setText(getString(R.string.view_captured_monster_detail_skill_level_value, mModel.getCapturedMonster().getSkillLevel()));

		return view;
	}

	public CapturedMonsterFullInfoModel getModel() {
		return mModel;
	}

	public void setModel(CapturedMonsterFullInfoModel model) {
		mModel = model;
	}
}
