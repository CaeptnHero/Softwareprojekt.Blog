package application;

import java.util.*;

public class Kommentar extends Beitrag {
	private ArrayList<Kommentar> unterKommentare;
	
	public void addUnterKommentar(Kommentar k) {
		unterKommentare.add(k);
	}
}
