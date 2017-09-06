package com.humanbooster.todolist;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;


@Path("/hello-world")
@Produces(MediaType.TEXT_HTML)
public class HelloWorldResource {

    private TaskManager manager = new TaskManager();

    //**********************************************************************************
    // Parametre Fonction : Aucun
    // Description        : Affiche le contenu de la page lors du lancement de la page principale
    //**********************************************************************************
    @GET //Définition de la méthode d'accès a la page
    public String sayHello() {
        String taskhtml = "";
        //Zone de listage des différents taches déja mémoriser dans le Vector
        for (int i = 0; i < manager.allTask().size(); i++) {
            taskhtml += "<div>" +
                    "<a href='/hello-world/consulter/" + i + "'>" + manager.allTask().elementAt(i).Nom + "</a>" +
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
        if (manager.allTask().size() > 0) {
            for (int i = 0; i < manager.allTask().size(); i++) {
                taskhtml += "<OPTION>" + manager.allTask().elementAt(i).Nom;
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

        manager.removeTask(id);


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
                "<br /> Name : " + manager.allTask().elementAt(id).Nom +
                "<br /> Date : " + manager.allTask().elementAt(id).Date;
        if (manager.allTask().elementAt(id).TachePere != -1) {
            taskhtml += "<br /> Tache Principale : " + manager.intToNameFather(manager.allTask().elementAt(id).TachePere);
        }
        if (!manager.allTask().elementAt(id).tacheEnfants.isEmpty()) {
            for (int i = 0; i < manager.allTask().elementAt(id).tacheEnfants.size(); i++) {
                taskhtml += "<br /> Taches Secondaire : " + manager.intToNameFather(manager.allTask().elementAt(id).tacheEnfants.get(i));
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

        HelloWorldTask Tache = new HelloWorldTask();
        Tache.Nom = taskTitle;
        Tache.Date = date;
        Tache.TachePere = manager.nameToIntFather(Parents);
        manager.addTask(Tache, Parents);
        URI redirect = UriBuilder.fromUri("/hello-world").build();
        return Response.seeOther(redirect).build();
    }






}
