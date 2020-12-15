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

    // ctor for UI
    public Post(User author, String text, Post parent) {
        this.author = author;
        this.text = text;
        this.dateTime = LocalDateTime.now();
        this.parent = parent;
        Comments = new ArrayList<>();

        String sql = String.format("INSERT INTO Beitrag (Datum, Verfasser, Oberbeitrag) VALUES ('%s', %s, %s)", this.dateTime, this.author.getId(), this.parent == null ? "NULL" : this.parent.getId());
        this.id = DatabaseController.executeUpdate(sql);
    }

    // ctor for db
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

    public void deleteComment(Comment k) {
        //#region Kommentar aus DB löschen
        String sql = "DELETE FROM kommentar WHERE KID = " + k.getId();
        DatabaseController.executeUpdate(sql);
        sql = "DELETE FROM beitrag WHERE BID = " + k.getId();
        DatabaseController.executeUpdate(sql);
        //#endregion


        Comments.remove(k);    //kommentar löschen
    }

    public void delete() {
        for (int i = 0; i < Comments.size(); i++) {
            Comment k = this.Comments.get(i);
            k.delete(); //recursion
            this.deleteComment(k);    //eigene kommentare löschen
        }
    }
}