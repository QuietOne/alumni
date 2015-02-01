package controllers;

import com.avaje.ebean.Ebean;
import models.Person;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.register;
import views.html.forbidden;


/**
 * @author Tihomir Radosavljevic
 * @version 2.0.0
 */
public class RegisterController extends Controller {

    public static Result showRegisterView() {
        return ok(register.render(Form.form(RegisterParameters.class)));
    }

    public static Result register() {
        Form<RegisterParameters> registerForm = Form.form(RegisterParameters.class).bindFromRequest();
        if (registerForm.hasErrors()) {
            return badRequest(register.render(registerForm));
        } else {
            session().clear();
            session("email", registerForm.get().email);
            Person person = new Person();
            person.email = registerForm.get().email;
            person.firstName = registerForm.get().firstName;
            person.lastName = registerForm.get().lastName;
            person.password = registerForm.get().password;
            person.save();
            return redirect(routes.IndexController.showIndexView());
        }
    }

    //register form and validation for it
    public static class RegisterParameters {
        public String email;
        public String firstName;
        public String lastName;
        public String password;
        public String confirmPassword;

        public String validate() {
            Person dbPerson = Ebean.find(Person.class)
                    .where()
                    .eq("email", email)
                    .findUnique();
            if (dbPerson != null) {
                return "Email is already being used.";
            }
            if (firstName == null || firstName.equals("")){
                return "First name has not been set.";
            }
            if (lastName == null || lastName.equals("")){
                return "Last name has not been set.";
            }
            if (!password.equals(confirmPassword)) {
                return "Passwords do not match.";
            }
            return null;
        }
    }
}
