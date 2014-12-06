package fr.neraud.padlistener.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.R;
import fr.neraud.padlistener.model.IgnoreMonsterQuickActionModel;

/**
 * Created by Neraud on 16/08/2014.
 */
public class IgnoreMonsterQuickActionsHelper {

	private final Context context;

	public IgnoreMonsterQuickActionsHelper(Context context) {
		this.context = context;
	}

	public List<IgnoreMonsterQuickActionModel> extractQuickActions() {
		MyLog.entry();

		final String[] quickActions = context.getResources().getStringArray(R.array.ignore_monster_quick_actions);

		final List<IgnoreMonsterQuickActionModel> quickActionModels = new ArrayList<IgnoreMonsterQuickActionModel>();
		for(final String quickAction : quickActions)  {
			final String[] split1 = quickAction.split("\\|");
			final String quickActionName = split1[0];
			final String[] ids = split1[1].split(",");

			final IgnoreMonsterQuickActionModel quickActionModel = new IgnoreMonsterQuickActionModel();
			quickActionModel.setQuickActionName(quickActionName);

			final List<Integer> monsterIds = new ArrayList<Integer>();
			for(final String id : ids)  {
				monsterIds.add(Integer.parseInt(id));
			}

			quickActionModel.setMonsterIds(monsterIds);

			MyLog.debug(" - " + quickActionModel);
			quickActionModels.add(quickActionModel);
		}

		MyLog.exit();
		return quickActionModels;
	}
}
