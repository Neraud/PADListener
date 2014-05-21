
package fr.neraud.padlistener.http.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import fr.neraud.padlistener.http.exception.ParsingException;

public abstract class AbstractJsonParser<M> {

	public M parse(String jsonString) throws ParsingException {
		Log.d(getClass().getName(), "parse");
		M result = null;
		try {
			if (jsonString != null) {
				if (jsonString.trim().startsWith("[")) {
					final JSONArray json = new JSONArray(jsonString);
					result = parseJsonArray(json);
				} else {
					final JSONObject json = new JSONObject(jsonString);
					result = parseJsonObject(json);
				}
			}

			return result;
		} catch (final JSONException e) {
			throw new ParsingException(e);
		}
	}

	protected abstract M parseJsonArray(JSONArray json) throws JSONException, ParsingException;

	protected abstract M parseJsonObject(JSONObject json) throws JSONException, ParsingException;

}
