package model;

import controller.DatabaseController;

import java.time.LocalDateTime;

public class Article extends Post {
    private String title;

    /**
     * Konstruktor fuers user interface
     *
     * @param author verfasser des Artikels
     * @param title titel des Artikels
     * @param text text des Artikels
     */
    public Article(Blogger author, String title, String text) {
        super(author, text, null);
        this.title = title;

        String sql = String.format("INSERT INTO article VALUES (%s, '%s', '%s')", this.getId(), title, text);
        DatabaseController.executeUpdate(sql);
    }

    /**
     * Konstruktor fuer die Datenbank
     *
     * @param id Identifikator des Artikels
     * @param author verfasser des Artikels
     * @param title titel des Artikels
     * @param text text des Artikels
     * @param dateTime zeitpunkt des erstellens
     */
    public Article(int id, Blogger author, String title, String text, LocalDateTime dateTime) {
        super(id, author, text, dateTime, null);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Loescht sich selbst
     */
    public void delete() {
        super.delete();
        deleteFromDatabase();
    }

    /**
     * Loescht sich selbst aus der Datenbank
     */
    private void deleteFromDatabase() {
        String sql = "DELETE FROM article WHERE AID = " + this.getId();
        DatabaseController.executeUpdate(sql);
        sql = "DELETE FROM post WHERE PID = " + this.getId();
        DatabaseController.executeUpdate(sql);
    }
}
