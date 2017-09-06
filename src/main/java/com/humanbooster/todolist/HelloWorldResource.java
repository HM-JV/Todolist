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
    // Fonction: Permet l'affichage de la page WEB. Contenant les boutons et la zone de saisie
    //**********************************************************************************
    //private List<String> tasks = new ArrayList<String>();
    private Vector<HelloWorldTask> Taches = new Vector<HelloWorldTask>();
    private List<String> FatherIs = new ArrayList<String>();

    @GET //Définition de la méthode d'accès a la page
    public String sayHello() {
        String taskhtml = "";
        for (int i = 0; i < Taches.size(); i++) {
            taskhtml += "<div>" +
                    "<a href='/hello-world/consulter/"+i+"'>" + Taches.elementAt(i).Nom + "</a>" +
                    "<a href='/hello-world/delete/" + i + "'>X</a>" +
                    "</div>";
        }
        //Gestion de l'enregistrement 0
        if(Taches.size() > 0){
            taskhtml +="<form action='/hello-world' " +
                    "method='POST'>Add Task: <input type='text' name='taskTitle'>" +
                    "<br />Add Date Task : <input type='date' name='date'> " +
                    "<br />Task Father :"+
                    "<SELECT name='Parents' size='1'>\n";
                    for (int i = 0; i < Taches.size(); i++) {
                        taskhtml += "<OPTION>" +Taches.elementAt(i).Nom ;
                    }
            taskhtml +="</SELECT>"+
                    "<input type='submit'>" +
                    "</form>";
        }
        else{
             taskhtml +="<form action='/hello-world' " +
                    "method='POST'>Add Task: <input type='text' name='taskTitle'>" +
                    "<br />Add Date Task : <input type='date' name='date'> " +
                    "<input type='submit'>" +
                    "</form>";
        }
        return taskhtml;
    }

    // Gestion de la suppression d'élément des tâches mémoriser.
    @GET
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") int id) {
        IsFather(id);
        if(!FatherIs.isEmpty()){
            for (int i=0; i < FatherIs.size(); i++ ){
                Taches.remove(NameToIntFather(FatherIs.get(i)));
            }
            Taches.remove(id);
        }else {
            Taches.remove(id);
        }

        URI redirect = UriBuilder.fromUri("/hello-world").build();
        return Response.seeOther(redirect).build();
    }

    //**********************************************************************************
    // Fonction d'affichage de contenu de tache
    //**********************************************************************************
    @GET
    @Path("/consulter/{id}")
    public String consulter(@PathParam("id") int id) {
        String taskhtml = "Task information " +
                "<br /> Name : " + Taches.elementAt(id).Nom +
                "<br /> Date : " + Taches.elementAt(id).Date +
                "<br /> Tache Principale : " + IntToNameFather(Taches.elementAt(id).TachePere);
        return taskhtml;
    }

    // Création d'une tache
    @POST
    public Response CreateTask(@FormParam("taskTitle") String taskTitle, @FormParam("date") String date, @FormParam("Parents") String Parents) {
        if (CompareDate(date, Parents)){
            HelloWorldTask Tache = new HelloWorldTask();
            Tache.Nom = taskTitle;
            Tache.Date = date;
            Tache.TachePere = NameToIntFather(Parents);
            Taches.add(Tache);
            URI redirect = UriBuilder.fromUri("/hello-world").build();
            return Response.seeOther(redirect).build();
            //return "Task "+ taskTitle +" created";
        }
        else {
            URI redirect = UriBuilder.fromUri("/hello-world").build();
            return Response.seeOther(redirect).build();
        }

    }

    public int NameToIntFather (String Parents){
        int id_Parents = 0;
        for (int i = 0; i < Taches.size(); i++) {
            if (Parents == Taches.elementAt(i).Nom){
                id_Parents = i;
                i = Taches.size();
            }
        }
        return id_Parents;
    }

    public String IntToNameFather (int id_Parents){
        return Taches.elementAt(id_Parents).Nom;
    }

    public void IsFather(int Id_Father){ ;
        for (int i = 0; i < Taches.size(); i++) {
            if (Id_Father == Taches.elementAt(i).TachePere){
                FatherIs.add(Taches.elementAt(i).Nom);
            }
        }
    }

    public boolean CompareDate (String date, String parents) {

        //Récupération date père
        int idPere = NameToIntFather(parents);
        String datePere = Taches.elementAt(idPere).Date;

        //Récupération date fils
        String dateFils = date;

        //Conversion
        Date dateP = ComversionDate(datePere);
        Date dateF = ComversionDate(dateFils);

        //Comparaison
        if (dateP.compareTo(dateF) == 0) {
            return true;
        }
        else if (dateP.compareTo(dateF) < 0) {
            return true;
        }
        else {
            return false;
        }

    }

    public Date ComversionDate (String Date) {
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
