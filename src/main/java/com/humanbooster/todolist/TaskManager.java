package com.humanbooster.todolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * Created by sochipan on 06/09/17.
 */
public class TaskManager {

    //**********************************************************************************
    // Zone de déclaration des variables de la classe
    //**********************************************************************************
    //private List<String> tasks = new ArrayList<String>();
    private Vector<HelloWorldTask> taches = new Vector<HelloWorldTask>();
    private List<String> FatherIs = new ArrayList<String>();

    public Vector<HelloWorldTask> allTask(){
        return taches;
    }

    public void removeTask(int id){
        //Verification si le tableau des enfants est remplie
        if (allTask().elementAt(id).tacheEnfants.isEmpty()) {
            //Si elle est vide on retire uniquement le tache
            removeTask(id);
        } else {
            //Si elle n'est pas vide suppression des enfants puis du père
            for (int i = 0; i < allTask().elementAt(id).tacheEnfants.size(); i++) {
                allTask().remove(allTask().elementAt(id).tacheEnfants.get(i));
            }
            allTask().remove(id);
        }

        taches.remove(id);
    }

    // date should be before parents date
    public boolean compareDate(String date, String parents) {

        //Récupération date père
        String datePere = "";
        if (!parents.equals("Aucun")) {
            int idPere = nameToIntFather(parents);
            datePere = taches.elementAt(idPere).Date;

            //Récupération date fils
            String dateFils = date;

            //Conversion
            Date dateP = comversionDate(datePere);
            Date dateF = comversionDate(dateFils);

            //Comparaison
            if (dateP.compareTo(dateF) == 0) {
                return true;
            } else if (dateP.compareTo(dateF) < 0) {
                return true;
            } else {
                return false;
            }
        } else{
            return true;
        }


    }

    public Date comversionDate(String Date) {
        //Conversion de la date
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = formatter.parse(Date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    //**********************************************************************************
    // Parametre Fonction : Parents contient le nom de la tache pere
    // Description        : Recherche de l'id en fonction du nom du père
    //**********************************************************************************
    public int nameToIntFather(String Parents) {
        int id_Parents = 0;
        for (int i = 0; i < taches.size(); i++) {
            if (Parents == taches.elementAt(i).Nom) {
                id_Parents = i;
                i = taches.size();
            }
        }
        return id_Parents;
    }

    //**********************************************************************************
    // Parametre Fonction : id_Parents
    // Description        : Recherche le nom en fonction de l'id du père
    //**********************************************************************************
    public String intToNameFather(int id_Parents) {
        return taches.elementAt(id_Parents).Nom;
    }

    public void addTask(HelloWorldTask tache, String parents) {
        if (compareDate(tache.Date, parents)) {
            if (!parents.equals("Aucun")) {
                // Elle a besoin d'un pere
                int idParents = nameToIntFather(parents);
                tache.TachePere = idParents;
                //Ajout dans le tableau du pere afin d'identifier le nouveau enfant.
                taches.elementAt(idParents).tacheEnfants.add(taches.size());
            } else {
                tache.TachePere = -1;
            }
            taches.add(tache);
        }
    }
}
