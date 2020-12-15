package model;

public class Blogger extends User {
    public Blogger(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Article createArticle(String title, String text) {
        return new Article(this, title, text);
    }

    public void articleDelete(Article art) {
        art.delete();
    }

    public void commentDelete(Comment c) {
        c.delete();
    }
}
