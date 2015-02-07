package controllers;

import com.avaje.ebean.Ebean;
import models.Person;
import play.data.Form;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.edit;
import views.html.forbidden;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author tihomir
 * @version 1.0.0
 */
public class EditProfileController extends Controller {

    public static Result showEditView() {
        String email = session().get("email");
        if (email == null || email.equals("")) {
            return ok(forbidden.render());
        } else {
            Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
            Form<EditParameters> form = Form.form(EditParameters.class);
            EditParameters ep = new EditParameters();
            ep.email = person.email;
            ep.firstName = person.firstName;
            ep.lastName = person.lastName;
            ep.file = new File(MainController.PATH_CV,person.id+"");
            return ok(edit.render(form.fill(ep)));
        }
    }

    public static Result edit() {
        Form<EditParameters> form = Form.form(EditParameters.class).bindFromRequest();
        if (form.hasErrors()) {
            return badRequest(edit.render(form));
        } else {
            String email = session().get("email");
            Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
            session().clear();
            session("email", form.get().email);
            person.email = form.get().email;
            person.firstName = form.get().firstName;
            person.lastName = form.get().lastName;
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart cv = body.getFile("cv");
            if (cv != null) {
                try {
                    person.cvName = cv.getFilename();
                    Files.move(cv.getFile().toPath(), Paths.get(MainController.PATH_CV, person.id + ""), StandardCopyOption.REPLACE_EXISTING);
                    flash("success", "Your CV has been saved.");
                } catch (Exception ex) {
                    person.cvName = null;
                    try {
                        Files.deleteIfExists(Paths.get(MainController.PATH_CV, person.id + ""));
                    } catch (IOException ioex) {

                    }
                    flash("error", "Failed to save file.");
                }
            } else {
                flash("success", "Your profile has been updated.");
            }
            person.update();
            return redirect(routes.EditProfileController.showEditView());
        }
    }

    public static Result removeCV() {
        String email = session().get("email");
        Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
        try {
            Files.deleteIfExists(Paths.get(MainController.PATH_CV, person.id + ""));
        } catch (Exception ex) {

        }
        person.cvName = null;
        person.update();
        return redirect(routes.EditProfileController.showEditView());
    }

    public static Result getMyCV() {
        String email = session().get("email");
        Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
        try {
            return ok(new FileInputStream(Paths.get(MainController.PATH_CV, person.id+"").toFile())).as("application/pdf");
        } catch (Exception ex) {
            return ok(forbidden.render());
        }

    }

    public static Result getCV(String id) {
        try {
            String email = session().get("email");
            if (email == null || email.equals("")) {
                throw new Exception("Not logged in");
            }
            return ok(new FileInputStream(Paths.get(MainController.PATH_CV, id).toFile())).as("application/pdf");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ok(forbidden.render());
        }
    }

    public static class EditParameters {
        public String email;
        public String firstName;
        public String lastName;
        public File file;

        public String validate() {
            Person dbPerson = Ebean.find(Person.class)
                    .where()
                    .eq("email", email)
                    .findUnique();
            if (!session().get("email").equals(email)) {
                if (dbPerson != null) {
                    return "Email is already being used.";
                }
            }
            if (firstName == null || firstName.equals("")){
                return "First name has not been set.";
            }
            if (lastName == null || lastName.equals("")){
                return "Last name has not been set.";
            }
            Http.MultipartFormData body = request().body().asMultipartFormData();
            Http.MultipartFormData.FilePart cv = body.getFile("cv");
            if (cv != null) {
                if (!cv.getContentType().equals("application/pdf")) {
                    return "File must be in pdf format.";
                }
            }
            return null;
        }
    }
}
