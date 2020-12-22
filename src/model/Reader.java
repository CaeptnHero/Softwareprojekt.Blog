package model;

public class Reader extends User {

    /**
     * Erstellt einen User mit den Rechten eines Readers
     *
     * @param id Identifikator des Readers
     * @param username Nutzername des Readers
     * @param password Passwort des Readers
     */
    public Reader(int id, String username, String password) {
        super(id, username, password);
    }

}
