package com.humanbooster.todolist;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.net.URI;
import java.util.List;
import java.util.Vector;
import java.util.Date;


@Path("/hello-world")
@Produces(MediaType.TEXT_HTML)
public class HelloWorldResource {

    //**********************************************************************************
    // Zone de déclaration des variables de la classe
    //**********************************************************************************
    //private List<String> tasks = new ArrayList<String>();
    private Vector<HelloWorldTask> Taches = new Vector<HelloWorldTask>();
    private List<String> FatherIs = new ArrayList<String>();

    //**********************************************************************************
    // Parametre Fonction : Aucun
    // Description        : Affiche le contenu de la page lors du lancement de la page principale
    //**********************************************************************************
    @GET //Définition de la méthode d'accès a la page
    public String sayHello() {
        String taskhtml = "";
        //Zone de listage des différents taches déja mémoriser dans le Vector
        for (int i = 0; i < Taches.size(); i++) {
            taskhtml += "<div>" +
                    "<a href='/hello-world/consulter/" + i + "'>" + Taches.elementAt(i).Nom + "</a>" +
                    "<a href='/hello-world/delete/" + i + "'>X</a>" +
                    "</div>";
        }
        //Gestion de l'enregistrement 0 afin d'afficher ou non le menu déroulant.
        taskhtml += "<form action='/hello-world' " +
                "method='POST'>Add Task: <input type='text' name='taskTitle'>" +
                "<br />Add Date Task : <input type='date' name='date'> " +
                "<br />Task Father :" +
                "<SELECT name='Parents' size='1'>\n" +
                "<OPTION> Aucun";
        if (Taches.size() > 0) {
            for (int i = 0; i < Taches.size(); i++) {
                taskhtml += "<OPTION>" + Taches.elementAt(i).Nom;
            }
        }
        taskhtml += "</SELECT>" +
                "<input type='submit'>" +
                "</form>";

        return taskhtml;
    }

    //**********************************************************************************
    // Parametre Fonction : Id de la tâche
    // Description        : Lance une page delete permettant la suppression d'un tache.
    //                    : avec la gestion de suppression des enfants en cas de suppresion du père
    //**********************************************************************************
    @GET
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") int id) {
        //Verification si le tableau des enfants est remplie
        if (Taches.elementAt(id).TacheEnfants.isEmpty()) {
            //Si elle est vide on retire uniquement le tache
            Taches.remove(id);
        } else {
            //Si elle n'est pas vide suppression des enfants puis du père
            for (int i = 0; i < Taches.elementAt(id).TacheEnfants.size(); i++) {
                Taches.remove(Taches.elementAt(id).TacheEnfants.get(i));
            }
            Taches.remove(id);
        }
        URI redirect = UriBuilder.fromUri("/hello-world").build();
        return Response.seeOther(redirect).build();
    }

    //**********************************************************************************
    // Parametre Fonction : Id de la Tache
    // Description        : Affiche le détails de la tache passer en paramètre
    //**********************************************************************************
    @GET
    @Path("/consulter/{id}")
    public String consulter(@PathParam("id") int id) {
        String taskhtml = "Task information " +
                "<br /> Name : " + Taches.elementAt(id).Nom +
                "<br /> Date : " + Taches.elementAt(id).Date;
        if (Taches.elementAt(id).TachePere != -1) {
            taskhtml += "<br /> Tache Principale : " + IntToNameFather(Taches.elementAt(id).TachePere);
        }
        if (!Taches.elementAt(id).TacheEnfants.isEmpty()) {
            for (int i = 0; i < Taches.elementAt(id).TacheEnfants.size(); i++) {
                taskhtml += "<br /> Taches Secondaire : " + IntToNameFather(Taches.elementAt(id).TacheEnfants.get(i));
            }
        }
        return taskhtml;
    }

    //**********************************************************************************
    // Parametre Fonction : taskTitle , date, Parents récupere du formulaire de saisi de l'utilisateur
    // Description        : Permet d'enregistre les informations dans un objet de Tache
    //**********************************************************************************
    @POST
    public Response CreateTask(@FormParam("taskTitle") String taskTitle, @FormParam("date") String date, @FormParam("Parents") String Parents) {

        if (CompareDate(date, Parents)) {
            HelloWorldTask Tache = new HelloWorldTask();
            Tache.Nom = taskTitle;
            Tache.Date = date;
            Tache.TachePere = NameToIntFather(Parents);
            if (!Parents.equals("Aucun")) {
                // Elle a besoin d'un pere
                int id_Parents = NameToIntFather(Parents);
                Tache.TachePere = id_Parents;
                //Ajout dans le tableau du pere afin d'identifier le nouveau enfant.
                Taches.elementAt(id_Parents).TacheEnfants.add(Taches.size());
            } else {
                Tache.TachePere = -1;
            }
            Taches.add(Tache);
            URI redirect = UriBuilder.fromUri("/hello-world").build();
            return Response.seeOther(redirect).build();
            //return "Task "+ taskTitle +" created";
        } else {
            URI redirect = UriBuilder.fromUri("/hello-world").build();
            return Response.seeOther(redirect).build();
        }
    }

    //**********************************************************************************
    // Parametre Fonction : Parents contient le nom de la tache pere
    // Description        : Recherche de l'id en fonction du nom du père
    //**********************************************************************************
    public int NameToIntFather(String Parents) {
        int id_Parents = 0;
        for (int i = 0; i < Taches.size(); i++) {
            if (Parents == Taches.elementAt(i).Nom) {
                id_Parents = i;
                i = Taches.size();
            }
        }
        return id_Parents;
    }

    //**********************************************************************************
    // Parametre Fonction : id_Parents
    // Description        : Recherche le nom en fonction de l'id du père
    //**********************************************************************************
    public String IntToNameFather(int id_Parents) {
        return Taches.elementAt(id_Parents).Nom;
    }


    public boolean CompareDate(String date, String parents) {

        //Récupération date père
        String datePere = "";
        if (!parents.equals("Aucun"))
        {
            int idPere = NameToIntFather(parents);
            datePere = Taches.elementAt(idPere).Date;

            //Récupération date fils
            String dateFils = date;

            //Conversion
            Date dateP = ComversionDate(datePere);
            Date dateF = ComversionDate(dateFils);

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

    public Date ComversionDate(String Date) {
        //Conversion de la date
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            Date date = formatter.parse(Date);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

}
