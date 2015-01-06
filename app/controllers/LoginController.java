package controllers;

import com.avaje.ebean.Ebean;
import models.Person;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

/**
 *
 * @author Tihomir Radosavljevic
 * @author Jelena
 *
 * @version 2.0.0
 */
public class LoginController extends Controller {

    public static Result showLoginView() {
        return ok(login.render(Form.form(LoginParameters.class)));
    }

    public static Result login() {
        Form<LoginParameters> loginForm = Form.form(LoginParameters.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect(routes.IndexController.showIndexView());
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.LoginController.showLoginView());
    }

    //login form parameters and validation for it
    public static class LoginParameters {
        public String email;
        public String password;

        public String validate() {
            Person dbPerson = Ebean.find(Person.class)
                                .where()
                                .eq("email", email)
                                .eq("password", password)
                                .findUnique();
            if (dbPerson == null) {
                return "Invalid email or password";
            } else {
                return null;
            }
        }
    }
}
