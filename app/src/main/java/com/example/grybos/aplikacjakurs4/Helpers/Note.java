package com.example.grybos.aplikacjakurs4.Helpers;

public class Note {

    private String Title;
    private String Ticolor;
    private String Text;
    private String Tecolor;
    private String File;
    private String Id;

    public Note(String id, String title, String ticolor, String text, String tecolor, String file) {
        Id = id;
        Title = title;
        Ticolor = ticolor;
        Text = text;
        Tecolor = tecolor;
        File = file;
    }

    public String getId(){
        return Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getTicolor() {
        return Ticolor;
    }

    public void setTicolor(String ticolor) {
        Ticolor = ticolor;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getTecolor() {
        return Tecolor;
    }

    public void setTecolor(String tecolor) {
        Tecolor = tecolor;
    }

    public String getFile(){
        return File;
    }

    public void setFile(String file){
        File = file;
    }
}
