package fr.neraud.padlistener.http.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.neraud.log.MyLog;
import fr.neraud.padlistener.http.exception.ParsingException;

/**
 * Base JSON parser
 *
 * @param <M> the model to return
 * @author Neraud
 */
public abstract class AbstractJsonParser<M> {

	public M parse(String jsonString) throws ParsingException {
		MyLog.entry();

		M result = null;
		try {
			if (jsonString != null) {
				// With og.json api, you MUST know before hand if the root is an object or array ...
				if (jsonString.trim().startsWith("[")) {
					final JSONArray json = new JSONArray(jsonString);
					result = parseJsonArray(json);
				} else {
					final JSONObject json = new JSONObject(jsonString);
					result = parseJsonObject(json);
				}
			}

			MyLog.exit();
			return result;
		} catch (final JSONException e) {
			throw new ParsingException(e);
		}
	}

	protected abstract M parseJsonArray(JSONArray json) throws JSONException, ParsingException;

	protected abstract M parseJsonObject(JSONObject json) throws JSONException, ParsingException;

}
