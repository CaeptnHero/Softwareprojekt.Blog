package model;

import controller.DatabaseController;

import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Post {

    private int id;
    private String text;
    private LocalDateTime dateTime;
    private User author;
    private Post parent;
    private ArrayList<Comment> Comments;

    /**
     * Konstruktor fuers user interface
     *
     * @param author verfasser des Beitrags
     * @param text   text des Beitrags
     * @param parent Oberbeitrag des Beitrags
     */
    public Post(User author, String text, Post parent) {
        this.author = author;
        this.text = text;
        this.dateTime = LocalDateTime.now();
        this.parent = parent;
        Comments = new ArrayList<>();

        String sql = String.format("INSERT INTO post VALUES (NULL,'%s', %s, %s)", this.dateTime, this.author.getId(), this.parent == null ? "NULL" : this.parent.getId());
        this.id = DatabaseController.executeUpdate(sql);
    }

    /**
     * Konstruktor fuer die Datenbank
     *
     * @param id       Identifikator des Beitrags
     * @param author   verfasser des Beitrags
     * @param text     text des Beitrags
     * @param dateTime zeitpunkt des erstellens
     * @param parent   Oberbeitrag des Beitrags
     */
    public Post(int id, User author, String text, LocalDateTime dateTime, Post parent) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.dateTime = dateTime;
        this.parent = parent;
        Comments = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return dateTime;
    }

    public User getAuthor() {
        return author;
    }

    public Post getParent() {
        return parent;
    }

    public void addComment(Comment k) {
        Comments.add(k);
    }

    public void addComment(ArrayList<Comment> kommentare) {
        this.Comments.addAll(kommentare);
    }

    public ArrayList<Comment> getComments() {
        return Comments;
    }

    /**
     * Loescht einen Kommentar aus sich selbst und aus der Datenbank
     *
     * @param k zu loeschender Kommentar
     */
    public void deleteComment(Comment k) {
        String sql = "DELETE FROM comment WHERE CID = " + k.getId();
        DatabaseController.executeUpdate(sql);
        sql = "DELETE FROM post WHERE PID = " + k.getId();
        DatabaseController.executeUpdate(sql);

        Comments.remove(k);    //kommentar löschen
    }

    /**
     * Loescht sich selbst und alle eigenen kommentare
     */
    public void delete() {
        while (Comments.size() != 0) {
            Comment k = this.Comments.get(0);
            k.delete(); //recursion
            this.deleteComment(k);    //eigene kommentare löschen
        }
    }

}
