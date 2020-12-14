package model;

import controller.DatabaseController;

import java.time.LocalDateTime;

public class Article extends Post {
    private String title;

    // ctor for UI
    public Article(Blogger verfasser, String title, String text) {
        super(verfasser, text, null);
        this.title = title;

        String sql = String.format("INSERT INTO Artikel VALUES (%s, '%s', '%s')", this.getId(), title, text);
        DatabaseController.executeUpdate(sql);
    }

    // ctor for db
    public Article(int id, Blogger verfasser, String title, String text, LocalDateTime dateTime) {
        super(id, verfasser, text, dateTime, null);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void delete() {
        super.delete();

        //PLACEHOLDER ArtikelListe.remove(this)		//TODO: sich selbst löschen
        deleteFromDatabase();
    }

    private void deleteFromDatabase() {
        String sql = "DELETE FROM artikel WHERE AID = " + this.getId();
        DatabaseController.executeUpdate(sql);
        sql = "DELETE FROM beitrag WHERE BID = " + this.getId();
        DatabaseController.executeUpdate(sql);
    }
}