package model;

public class Blogger extends User {
    public Blogger(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     * @param title
     * @param text
     * @return
     */
    public Article createArticle(String title, String text) {
        return new Article(this, title, text);
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     * @param art
     */
    public void deleteArticle(Article art) {
        art.delete();
    }

    /**
     * TODO: FINISH JAVADOC COMMENT
     * @param c
     */
    public void deleteComment(Comment c) {
        c.delete();
    }
}
