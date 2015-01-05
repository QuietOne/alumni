package controllers;

import models.Person;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.edit;

/**
 * @author tihomir
 * @version 1.0.0
 */
public class EditProfileController extends Controller {

    public static Result showEditView() {
        return ok(edit.render());
    }

    public static Result editProfile() {
        Person person = Form.form(Person.class).bindFromRequest().get();
        person.update();
        //TODO: show confirmation message
        return showEditView();
    }
}
