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
        return null;
    }

    public static class EditParameters {
        
    }
}
