package fr.neraud.padlistener.http.parser.padherder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.http.exception.ParsingException;
import fr.neraud.padlistener.http.parser.AbstractJsonParser;

/**
 * JSON parser used to parse PADherder evolution api
 *
 * @author Neraud
 */
public class MonsterEvolutionJsonParser extends AbstractJsonParser<Map<Integer, Integer>> {

	@SuppressWarnings("unchecked")
	@Override
	protected Map<Integer, Integer> parseJsonObject(JSONObject json) throws JSONException, ParsingException {
		MyLog.entry();

		final Map<Integer, Integer> evolutions = new HashMap<Integer, Integer>();
		final Iterator<String> iter = json.keys();
		while(iter.hasNext()) {
			final String childIdString = iter.next();
			final JSONArray evolutionsForChild = json.getJSONArray(childIdString);
			for(int i = 0 ; i < evolutionsForChild.length() ; i++) {
				final JSONObject evolutionForChild = evolutionsForChild.getJSONObject(i);
				/*
				"is_ultimate": false,
				"materials": [[152,	1]],
				"evolves_to": 2
				*/

				final Integer targetId = evolutionForChild.getInt("evolves_to");
				final Integer childId = Integer.parseInt(childIdString);
				// FIXME : to prevent loop with UEVO, only store EVO with child < target
				if(childId < targetId) {
					MyLog.debug("adding " + childId + " -> " + targetId);
					evolutions.put(targetId, childId);
				} else {
					MyLog.debug("ignoring reverse UEVO : " + childId + " -> " + targetId);
				}
			}
		}

		MyLog.exit();
		return evolutions;
	}

	@Override
	protected Map<Integer, Integer> parseJsonArray(JSONArray json) throws JSONException, ParsingException {
		throw new ParsingException("Cannot parse JSONArray, JSONObject expected");
	}
}
