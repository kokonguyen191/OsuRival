package doge;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class UrlParser {
	private String m_url;
	private JSONArray m_jsonarray = null;
	private ArrayList<JSONObject> m_pp = null;
	private HashMap<Integer, JSONObject> ppMap = null;

	public UrlParser(String username) throws Exception {
		m_apikey = ""; // Put your API key here
		m_url = "https://osu.ppy.sh/api/get_user_best?k=" + m_apikey + "&m=3&limit=100&type=string&u="
				+ username;
		m_pp = new ArrayList<JSONObject>();
		ppMap = new HashMap<Integer, JSONObject>();
		parse();
		toHashMap();
	}

	public HashMap<Integer, JSONObject> getPP() {
		return ppMap;
	}

	public double getPP(Integer map_id) {
		return ppMap.get(map_id).getDouble("pp");
	}

	public int getData(Integer map_id, String key) {
		return ppMap.get(map_id).getInt(key);
	}

	public double getAccuracy(Integer map_id) {
		JSONObject map = ppMap.get(map_id);
		double hit_0 = map.getDouble("countmiss");
		double hit_50 = map.getDouble("count50");
		double hit_100 = map.getDouble("count100");
		double hit_200 = map.getDouble("countkatu");
		double hit_300 = map.getDouble("count300");
		double hit_max = map.getDouble("countgeki");

		return (50 * hit_50 + 100 * hit_100 + 200 * hit_200 + 300 * hit_300 + 300 * hit_max)
				/ (3 * (hit_0 + hit_50 + hit_100 + hit_200 + hit_300 + hit_max)) * 100;
	}

	private void toHashMap() {
		for (JSONObject jo : m_pp) {
			ppMap.put(jo.getInt("beatmap_id"), jo);
		}
	}

	private void parse() throws Exception {
		try {
			URL url = new URL(m_url);
			Scanner s = new Scanner(url.openStream());
			m_jsonarray = new JSONArray(s.nextLine());
			if (m_jsonarray.isNull(0)) {
				System.out.println("Invalid user or this user does not play mania.");
			} else {
				for (Object ppStr : m_jsonarray) {
					m_pp.add(new JSONObject(ppStr.toString()));
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		UrlParser up = new UrlParser("Vladimir Puchin");
		System.out.println(up.getAccuracy(923665));
	}
}
