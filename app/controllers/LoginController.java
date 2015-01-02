package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

/**
 *
 * @author tihomir
 * @author Jelena
 *
 * @version 1.0.0
 */
public class LoginController extends Controller {

  /*  public static Result index() {
        return redirect("login.scala.html");
    }
*/
    public static Result redirection(String url) {
        return ok(login.render());
    }
}
