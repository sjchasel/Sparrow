package com.swufe.sparrow.Memo;

public class MemoItem {
    private int id;
    private String content;
    private String date;

    public MemoItem() {
        super();
        content = "";
        date = "";
    }
    public MemoItem(String content, String date) {
        super();
        this.content = content;
        this.date = date;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

}
