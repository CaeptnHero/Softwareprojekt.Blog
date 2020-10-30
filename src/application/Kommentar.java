package application;

import java.util.*;

public class Kommentar extends Beitrag {
	
	private ArrayList<Kommentar> unterKommentare;
	private Kommentar oberKommentar;
	
	public Kommentar() {
		oberKommentar = null;
		unterKommentare = new ArrayList<Kommentar>();
	}
	
	public void addUnterKommentar(Kommentar k) {
		unterKommentare.add(k);
	}
}