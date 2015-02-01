package controllers;

import com.avaje.ebean.Ebean;
import models.Person;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.changePassword;
import views.html.forbidden;

/**
 * @author tihomir
 * @version 1.0.0
 */
public class ChangePasswordController extends Controller {

    public static Result showChangePasswordView() {
        String email = session().get("email");
        if (email == null || email.equals("")) {
            return ok(forbidden.render());
        } else {
            return ok(changePassword.render(Form.form(PasswordParameters.class)));
        }
    }

    public static Result changePassword() {
        Form<PasswordParameters> form = Form.form(PasswordParameters.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(changePassword.render(form));
        } else {
            String email = session().get("email");
            Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
            person.password = form.get().newPassword;
            person.update();
            flash("success", "Password has been successfully added.");
            return redirect(routes.EditProfileController.showEditView());
        }
    }


    public static class PasswordParameters {
        public String oldPassword;
        public String newPassword;
        public String confirmNewPassword;

        public String validate() {
            if (oldPassword == null || oldPassword.equals("")) {
                return "Old password has not been set.";
            } else {
                String email = session().get("email");
                Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
                if (!oldPassword.equals(person.password)) {
                    return "You didn't put right password.";
                }
            }
            if (newPassword == null || newPassword.equals("")) {
                return "New password has not been set.";
            }
            if (confirmNewPassword == null || confirmNewPassword.equals("")) {
                return "New password has not been set.";
            }
            if (newPassword.equals(confirmNewPassword)) {
                return "New passwords do not match each other.";
            }
            return null;
        }
    }
}
