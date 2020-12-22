package model;

public class Blogger extends User {

    /**
     * Erstellt einen User mit den Rechten eines Bloggers
     *
     * @param id Identifikator des Bloggers
     * @param username Nutzername des Bloggers
     * @param password Passwort des Bloggers
     */
    public Blogger(int id, String username, String password) {
        super(id, username, password);
        super.setIsBlogger(true);
    }

    /**
     * Ertstellt einen neuen Artikel verfasst von diesem Blogger
     * @param title titel des Artikels
     * @param text text des Artikels
     * @return gibt den erstellten Artikel zuruek
     */
    public Article createArticle(String title, String text) {
        return new Article(this, title, text);
    }

    /**
     * Loescht einen Artikel
     * @param art zu loeschender Artikel
     */
    public void deleteArticle(Article art) {
        art.delete();
    }

    /**
     * Loescht einen Kommentar
     * @param com zu loeschender Kommentar
     */
    public void deleteComment(Comment com) {
        com.delete();
    }


}
