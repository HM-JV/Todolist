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
                    "<a href='/hello-world/consulter/" + i + "'>" + manager.allTask().get(i).nom + "</a>" +
                    "<a href='/hello-world/delete/" + i + "'>X</a>" +
                    "</div>";
        }
        //Gestion de l'enregistrement 0 afin d'afficher ou non le menu déroulant.
        taskhtml += "<form action='/hello-world' " +
                "method='POST'>Add Task: <input type='text' name='taskTitle'>" +
                "<br />Add Start Date Task : <input type='date' name='dateDebut'> " +
                "<br />Add End Date Task : <input type='date' name='dateFin'> " +
                "<br />Task Father :" +
                "<SELECT name='Parents' size='1'>\n" +
                "<OPTION> Aucun";
        if (manager.allTask().size() > 0) {
            for (int i = 0; i < manager.allTask().size(); i++) {
                taskhtml += "<OPTION>" + manager.allTask().get(i).nom;
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
                "<br /> Name : " + manager.allTask().get(id).nom +
                "<br /> Begin Date : " + manager.allTask().get(id).dateDebut +
                "<br /> End Date : " + manager.allTask().get(id).dateFin;
        if (manager.allTask().get(id).tachePere != -1) {
            taskhtml += "<br /> Tache Principale : " + manager.intToNameFather(manager.allTask().get(id).tachePere);
        }
        if (!manager.allTask().get(id).tacheEnfants.isEmpty()) {
            for (int i = 0; i < manager.allTask().get(id).tacheEnfants.size(); i++) {
                taskhtml += "<br /> Taches Secondaire : " + manager.intToNameFather(manager.allTask().get(id).tacheEnfants.get(i));
            }
        }
        return taskhtml;
    }

    //**********************************************************************************
    // Parametre Fonction : taskTitle , date, Parents récupere du formulaire de saisi de l'utilisateur
    // Description        : Permet d'enregistre les informations dans un objet de Tache
    //**********************************************************************************
    @POST
    public Response createTask(@FormParam("taskTitle") String taskTitle, @FormParam("dateDebut") String dateDebut, @FormParam("dateFin") String dateFin, @FormParam("Parents") String Parents) {

        HelloWorldTask Tache = new HelloWorldTask();
        Tache.nom = taskTitle;
        Tache.dateDebut = dateDebut;
        Tache.dateFin = dateFin;
        Tache.tachePere = manager.nameToIntFather(Parents);
        manager.addTask(Tache, Parents);
        URI redirect = UriBuilder.fromUri("/hello-world").build();
        return Response.seeOther(redirect).build();
    }
}
