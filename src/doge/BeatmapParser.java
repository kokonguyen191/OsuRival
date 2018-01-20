package doge;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class BeatmapParser {
	private String m_url;
	private String m_id;
	private JSONArray m_jsonarray = null;
	private ArrayList<JSONObject> m_al = null;
	private JSONObject map = null;

	public BeatmapParser(Integer id) throws Exception {
		m_apikey = ""; // Put your API key here
		m_url = "https://osu.ppy.sh/api/get_beatmaps?k=" + m_apikey + "&b=" + id.toString();
		m_id = id.toString();
		m_al = new ArrayList<JSONObject>();
		parse();
		map = m_al.get(0);
	}

	public String getData(String para) {
		return map.getString(para);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getData("artist"));
		sb.append(" - ");
		sb.append(getData("title"));
		sb.append(" [");
		sb.append(getData("version"));
		sb.append("]\n");
		sb.append("https://osu.ppy.sh/b/");
		sb.append(m_id);
		return sb.toString();
	}

	private void parse() throws Exception {
		try {
			URL url = new URL(m_url);
			Scanner s = new Scanner(url.openStream());
			m_jsonarray = new JSONArray(s.nextLine());
			if (m_jsonarray.isNull(0)) {
				System.out.println("Invalid beatmap.");
			} else {
				m_al.add(new JSONObject(m_jsonarray.get(0).toString()));
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		BeatmapParser bp = new BeatmapParser(1023967);
		System.out.println(bp);
	}
}
