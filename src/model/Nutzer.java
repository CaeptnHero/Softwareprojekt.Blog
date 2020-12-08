package model;

public abstract class Nutzer {
    protected int id;
    protected String nutzername;
    protected String passwort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNutzername() {
        return nutzername;
    }

    public void setNutzername(String nutzername) {
        this.nutzername = nutzername;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public Kommentar createKommentar(String text, Beitrag oberbeitrag) {
        return new Kommentar(this, text, oberbeitrag);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
