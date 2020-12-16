package model;

import controller.DatabaseController;

import java.time.LocalDateTime;

public class Article extends Post {
    private String title;

    /**
     * TODO: FINISH JAVADOC COMMENT
     * Konstruktor fuers user interface
     * @param verfasser
     * @param title
     * @param text
     */
    public Article(Blogger verfasser, String title, String text) {
        super(verfasser, text, null);
        this.title = title;

        String sql = String.format("INSERT INTO Artikel VALUES (%s, '%s', '%s')", this.getId(), title, text);
        DatabaseController.executeUpdate(sql);
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     * Konstruktor fuer die Datenbank
     * @param id
     * @param verfasser
     * @param title
     * @param text
     * @param dateTime
     */
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

    /**
     * TODO: FINISH JAVADOC COMMENT
     */
    public void delete() {
        super.delete();
        deleteFromDatabase();
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     */
    private void deleteFromDatabase() {
        String sql = "DELETE FROM artikel WHERE AID = " + this.getId();
        DatabaseController.executeUpdate(sql);
        sql = "DELETE FROM beitrag WHERE BID = " + this.getId();
        DatabaseController.executeUpdate(sql);
    }
}
