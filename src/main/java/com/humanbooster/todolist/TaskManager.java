package com.humanbooster.todolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sochipan on 06/09/17.
 */
public class TaskManager {

    //**********************************************************************************
    //
    // Description        : Declaration des variable de la classe TaskManager
    //**********************************************************************************
    //private List<String> tasks = new ArrayList<String>();
    //private Vector<HelloWorldTask> taches = new Vector<HelloWorldTask>();
    private List<HelloWorldTask> taches = new ArrayList<HelloWorldTask>();

    //**********************************************************************************
    // Parametre Fonction : Aucun
    // Description        : Renvoie la liste complete des tâches enregistrer
    //**********************************************************************************
    public List<HelloWorldTask> allTask(){
        return taches;
    }

    //**********************************************************************************
    // Parametre Fonction : Id de la tache a supprimer
    // Description        : Supprime une tache. Prend en compte la demande de supprimer les enfants en cas de
    //                      suppression du Parents.
    //**********************************************************************************
    public void removeTask(int id){
        //Verification si le tableau des enfants est remplie
        if (allTask().get(id).tacheEnfants.isEmpty()) {
            //Si elle est vide on retire uniquement le tache
            taches.remove(id);
        } else {
            //Si elle n'est pas vide suppression des enfants puis du père
            for (int i = 0; i < allTask().get(id).tacheEnfants.size(); i++) {
                allTask().remove(allTask().get(id).tacheEnfants.get(i));
            }
            allTask().remove(id);
            taches.remove(id);
        }
    }

    //******************************************************************************************************************
    // Parametre Fonction : DateFin de l'enfant en cours de création, Parents nom du parents associé
    // Description        : Va comparer la date de fin de l'enfant en cours et celle du parents associé (si il y en a un)
    //*******************************************************************************************************************
    public boolean compareDate(String dateFin, String parents) {

        //Récupération date père
        String datePere = "";
        if (!parents.equals("Aucun")) {
            int idPere = nameToIntFather(parents);
            datePere = taches.get(idPere).dateFin;

            //Récupération date fils
            String dateFils = dateFin;

            //Conversion
            Date dateP = conversionDate(datePere);
            Date dateF = conversionDate(dateFils);

            //Comparaison
            if (dateP.compareTo(dateF) == 0) {
                return true;
            } else if (dateP.compareTo(dateF) > 0) {
                return true;
            } else {
                return false;
            }
        } else{
            return true;
        }
    }

    //**********************************************************************************
    // Parametre Fonction : Date de type sting
    // Description        : Convertir les dates de type string en format Date
    //**********************************************************************************
    public Date conversionDate(String Date) {
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
            if (Parents == taches.get(i).nom) {
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
        return taches.get(id_Parents).nom;
    }

    //**********************************************************************************
    // Parametre Fonction : Objet Tache fille, et Parents qui represente le nom de l'eventuelle TachePere
    // Description        : Crée un nouvelle objet Task et assure la gestion de la présence ou non du pere.
    //**********************************************************************************
    public void addTask(HelloWorldTask tache, String parents) {
        if (compareDate(tache.dateFin, parents)) {
            if (!parents.equals("Aucun")) {
                // Elle a besoin d'un pere
                int idParents = nameToIntFather(parents);
                tache.tachePere = idParents;
                //Ajout dans le tableau du pere afin d'identifier le nouveau enfant.
                taches.get(idParents).tacheEnfants.add(taches.size());
            } else {
                tache.tachePere = -1;
            }
            taches.add(tache);
        }
    }
}
