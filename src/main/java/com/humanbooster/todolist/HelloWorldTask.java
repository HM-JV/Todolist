package com.humanbooster.todolist;


import java.util.ArrayList;
import java.util.List;

public class HelloWorldTask {
    public String Nom ="";
    public String Date;
    public int TachePere = -1; // Permet de connaitre le père sans faire de scan
    public List<Integer> tacheEnfants = new ArrayList<Integer>(); // Permet de connaitre les enfants sans scan
}
