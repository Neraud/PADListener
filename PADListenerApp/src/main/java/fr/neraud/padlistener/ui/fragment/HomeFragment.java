package fr.neraud.padlistener.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.ui.activity.HomeActivity;
import fr.neraud.padlistener.ui.constant.UiScreen;
import fr.neraud.padlistener.ui.model.ActionsCard;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.view.CardView;

/**
 * Main fragment for MainMenu
 *
 * @author Neraud
 */
public class HomeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.d(getClass().getName(), "onCreateView");

		final View mainView = inflater.inflate(R.layout.home_fragment, container, false);

		fillCaptureCard(mainView);
		//fillMonsterInfoCard(mainView);
		//fillCapturedDataCard(mainView);
		fillSyncCard(mainView);

		return mainView;
	}

	private void fillCaptureCard(View mainView) {
		final ActionsCard captureCard = new ActionsCard(getActivity());
		captureCard.setContentText(getString(R.string.home_capture_content));
		captureCard.setPrimaryActionEnabled(false);
		captureCard.setPrimaryActionTitle(getString(R.string.home_capture_button_auto));
		captureCard.setPrimaryActionOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "captureCard.primaryActionOnClickListener.onClick");
				// TODO
			}
		});
		captureCard.setSecondaryActionTitle(getString(R.string.home_capture_button_manual));
		captureCard.setSecondaryActionEnabled(true);
		captureCard.setSecondaryActionOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "captureCard.secondaryActionOnClickListener.onClick");
				((HomeActivity)getActivity()).goToScreen(UiScreen.SWITCH_LISTENER);
			}
		});
		fillCardInView(mainView, captureCard, R.id.home_card_capture);
	}

	/*
	private void fillMonsterInfoCard(View mainView) {
		final ActionsCard monsterInfoCard = new ActionsCard(getActivity());
		monsterInfoCard.setContentText("Monster info");
		monsterInfoCard.setSecondaryActionTitle("View");
		monsterInfoCard.setSecondaryActionOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "monsterInfoCard.secondaryActionOnClickListener.onClick");
				((HomeActivity)getActivity()).goToScreen(UiScreen.VIEW_MONSTER_INFO);
			}
		});
		fillCardInView(mainView, monsterInfoCard, R.id.home_card_monster_info);
	}
	*/

	/*
	private void fillCapturedDataCard(View mainView) {
		final ActionsCard capturedDataCard = new ActionsCard(getActivity());
		capturedDataCard.setContentText("Captured data");
		capturedDataCard.setSecondaryActionTitle("View");
		capturedDataCard.setSecondaryActionOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "capturedDataCard.secondaryActionOnClickListener.onClick");
				((HomeActivity)getActivity()).goToScreen(UiScreen.VIEW_CAPTURED_DATA);
			}
		});
		fillCardInView(mainView, capturedDataCard, R.id.home_card_captured_data);
	}
	*/

	private void fillSyncCard(View mainView) {
		final ActionsCard syncCard = new ActionsCard(getActivity());
		syncCard.setContentText(getString(R.string.home_sync_content));
		syncCard.setPrimaryActionTitle(getString(R.string.home_sync_button_auto));
		syncCard.setPrimaryActionEnabled(false);
		syncCard.setPrimaryActionOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "syncCard.primaryActionOnClickListener.onClick");
				// TODO
			}
		});
		syncCard.setSecondaryActionTitle(getString(R.string.home_sync_button_manual));
		syncCard.setSecondaryActionEnabled(true);
		syncCard.setSecondaryActionOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.d(getClass().getName(), "syncCard.secondaryActionOnClickListener.onClick");
				((HomeActivity)getActivity()).goToScreen(UiScreen.COMPUTE_SYNC);
			}
		});
		fillCardInView(mainView, syncCard, R.id.home_card_sync);
	}

	private void fillCardInView(View mainView, Card card, int cardViewId) {
		final CardView cardView = (CardView) mainView.findViewById(cardViewId);
		cardView.setCard(card);
	}

}
