package model;

public class Blogger extends Nutzer {
    public Blogger(int id, String nutzername, String passwort) {
        this.id = id;
        this.nutzername = nutzername;
        this.passwort = passwort;
    }

    public Blogger(String nutzername, String passwort) {
        this.id = -1;
        this.nutzername = nutzername;
        this.passwort = passwort;
    }

    public Blogger() {
        // TODO Auto-generated constructor stub
        this.id = 1;
    }

    public Artikel createArticle(String titel, String text) {
        return new Artikel(this, titel, text);
    }

    public void Artikeldelete(Artikel art) {
        art.delete();
    }
}
