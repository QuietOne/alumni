package controllers;

import com.avaje.ebean.Ebean;
import models.Person;
import play.mvc.Controller;

/**
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public class MainController extends Controller {

    public static String PATH_CV = "public/files/cv";

    public static boolean isAdmin(){
        String email = session().get("email");
        Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
        return true;
        //return person.administrator;
    }

    public static boolean availableCV() {
        String email = session().get("email");
        Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
        return person.cvName != null;
    }
}
