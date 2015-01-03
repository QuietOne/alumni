package controllers;

import models.Person;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.register;


/**
 * @author tihomir
 * @version 1.0.0
 */
public class RegisterController extends Controller {

    public static Result showRegisterView() {
        return ok(register.render());
    }

    public static Result register() {
        Person person = Form.form(Person.class).bindFromRequest().get();
        //validation
        person.save();
        return IndexController.showIndexView();
    }
}
