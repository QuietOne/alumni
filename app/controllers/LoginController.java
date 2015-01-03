package controllers;

import com.avaje.ebean.Ebean;
import models.Person;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

/**
 *
 * @author Tihomir
 * @author Jelena
 *
 * @version 1.0.0
 */
public class LoginController extends Controller {

    //changed name into something with more meaning
    public static Result showLoginView() {
        return ok(login.render());
    }

    public static Result login() {
        Person person = Form.form(Person.class).bindFromRequest().get();
        Person dbPerson =
                Ebean.find(Person.class)
                .where()
                        .eq("email", person.email)
                        .eq("password", person.password)
                .findUnique();
        //check if the query is valid
        if (dbPerson == null) {
            //check not found, I don't know if I used it right
            return notFound("not found");
        } else {
            return IndexController.showIndexView();
        }
    }

    public static Result goToRegisterView() {
        return RegisterController.showRegisterView();
    }

    public static Result logout() {
        //TODO: destroy data from user
        return showLoginView();
    }
}
