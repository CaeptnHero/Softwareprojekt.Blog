package model;

public class Blogger extends Nutzer {
    public Blogger(int id, String nutzername, String passwort) {
        this.id = id;
        this.nutzername = nutzername;
        this.passwort = passwort;
    }

    public Artikel createArticle(String titel, String text) {
        return new Artikel(this, titel, text);
    }

    public void Artikeldelete(Artikel art) {
        art.delete();
    }
}
