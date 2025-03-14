package com.handlers.saf.utilities;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {

	static ObjectMapper mapper = new ObjectMapper();

	public static String minimizeJson(String prettyJson) {

		try {

			// Parse the pretty JSON into a JsonNode
			JsonNode tree = mapper.readTree(prettyJson);
			// Write the tree back as a compact JSON string (without indentation)
			return mapper.writeValueAsString(tree);
		} catch (Exception e) {
			return prettyJson;
		}
	}

	public static Map<String, String> extractMetadata(String callbackMetadata, String arrayTag) {
		Map<String, String> meta = new HashMap<String, String>();

		try {
			JSONArray item = new JSONArray(new JSONObject(callbackMetadata).getJSONArray(arrayTag));

			for (int i = 0; i < item.length(); i++) {
				JSONObject obj = item.getJSONObject(i);
				meta.put(obj.get("Name").toString(), obj.opt("Value") == null ? "" : obj.get("Value").toString());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return meta;
	}

}
