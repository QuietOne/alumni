package controllers;

import play.data.Form;
import play.mvc.Result;
import play.mvc.Controller;
import views.html.facebook;

/**
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public class FacebookController extends Controller{

    public static Result showFacebookView() {
        return ok(facebook.render(Form.form(FacebookParameters.class)));
    }

    public static Result connect() {
        Form<FacebookParameters> facebookForm = Form.form(FacebookParameters.class).bindFromRequest();
        if (facebookForm.hasErrors()) {
            return badRequest(facebook.render(facebookForm));
        } else {
            //TODO: autorization and collecting data
            return redirect(routes.IndexController.showIndexView());
        }
    }

    public static class FacebookParameters {
        public String username;
        public String password;

        public String validate() {
            if (username == null || username.equals("")) {
                return "Username field is empty.";
            }
            if (password == null || password.equals("")) {
                return "Password field is empty.";
            }
            return null;
        }
    }
}
