package doge;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

public class OsuRival {

	private UrlParser m_player1 = null;
	private UrlParser m_player2 = null;
	private HashMap<Integer, JSONObject> m_pp1 = null;
	private HashMap<Integer, JSONObject> m_pp2 = null;

	private Set<Integer> sharedSongs = null;
	private Set<Integer> exclusive1 = null;
	private Set<Integer> exclusive2 = null;

	OsuRival(String user1, String user2) throws Exception {
		m_player1 = new UrlParser(user1);
		m_player2 = new UrlParser(user2);
		m_pp1 = m_player1.getPP();
		m_pp2 = m_player2.getPP();
		getShared();
		getExclusive();
		System.out.println("Your name: " + user1);
		System.out.println("Your rival: " + user2);
	}

	private void getShared() {
		sharedSongs = new HashSet<Integer>(m_pp1.keySet());
		sharedSongs.retainAll(m_pp2.keySet());
	}

	private void getExclusive() {
		exclusive1 = new HashSet<Integer>(m_pp1.keySet());
		exclusive2 = new HashSet<Integer>(m_pp2.keySet());
		exclusive1.removeAll(sharedSongs);
		exclusive2.removeAll(sharedSongs);
	}

	private void printWinHeader() {
		System.out.println("======================================================");
		System.out.println("===================Songs you win at===================");
		System.out.println("======================================================");
		System.out.println();
	}

	private void printLoseHeader() {
		System.out.println("======================================================");
		System.out.println("===================Songs you lose at==================");
		System.out.println("======================================================");
		System.out.println();
	}

	private void printResult(String key, Integer map_id) throws Exception {
		if (key.equals("PP")) {
			System.out.println("Your pp: " + String.format("%.3f", m_player1.getPP(map_id)) + "\t Your rival's pp: "
					+ String.format("%.3f", m_player2.getPP(map_id)));
			System.out.println();
			return;
		}
		if (key.equals("Combo")) {
			System.out.println("Your combo: " + m_player1.getData(map_id, "maxcombo") + "\t Your rival's combo: "
					+ m_player2.getData(map_id, "maxcombo"));
			System.out.println();
			return;

		}
		if (key.equals("Score")) {
			System.out.println("Your score: " + m_player1.getData(map_id, "score") + "\t Your rival's combo: "
					+ m_player2.getData(map_id, "score"));
			System.out.println();
			return;

		}
		if (key.equals("Acc") || key.equals("Accuracy")) {
			System.out.println("Your accuracy: " + String.format("%.2f", m_player1.getAccuracy(map_id))
					+ "\t Your rival's accuracy: " + String.format("%.2f", m_player2.getAccuracy(map_id)));
			System.out.println();
			return;

		}
		throw new IllegalArgumentException("Invalid comparison criterion");
	}

	private boolean compareOne(String key, Integer map_id) throws Exception {
		if (key.equals("PP")) {
			return m_player1.getPP(map_id) > m_player2.getPP(map_id);
		}
		if (key.equals("Combo")) {
			return m_player1.getData(map_id, "maxcombo") > m_player2.getData(map_id, "maxcombo");
		}
		if (key.equals("Score")) {
			return m_player1.getData(map_id, "score") > m_player2.getData(map_id, "score");

		}
		if (key.equals("Acc") || key.equals("Accuracy")) {
			return m_player1.getAccuracy(map_id) > m_player2.getAccuracy(map_id);
		}
		throw new IllegalArgumentException("Invalid comparison criterion");

	}

	private void compareAndPrintOne(String key, Integer map_id, boolean isWin, boolean printMap) throws Exception {
		if (compareOne(key, map_id) == isWin) {
			if (printMap) {
				System.out.println(new BeatmapParser(map_id));
			}
			printResult(key, map_id);
		}
	}

	private void compare(String key) throws Exception {
		if (key.equals("PP") || key.equals("Combo") || key.equals("Score") || key.equals("Acc")
				|| key.equals("Accuracy")) {
			printWinHeader();
			for (Integer map_id : sharedSongs) {
				compareAndPrintOne(key, map_id, true, true);
			}
			printLoseHeader();
			for (Integer map_id : sharedSongs) {
				compareAndPrintOne(key, map_id, false, true);
			}
		} else {

			throw new IllegalArgumentException("Invalid comparison criterion");
		}

	}

	public static void main(String[] args) throws Exception {
		OsuRival os = new OsuRival("Vladimir Puchin", "Raxbot");
		os.compare("PP");
	}
}
