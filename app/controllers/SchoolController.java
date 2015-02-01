package controllers;

import models.School;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.school;
import views.html.forbidden;

import java.util.Date;

/**
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public class SchoolController extends Controller{

    public static Result showSchoolView() {
        if (MainController.isAdmin()) {
            return ok(school.render(Form.form(SchoolParameters.class)));
        } else {
            return ok(forbidden.render());
        }
    }

    public static Result addSchool() {
        Form<SchoolParameters> schoolForm = Form.form(SchoolParameters.class).bindFromRequest();
        if (schoolForm.hasErrors()) {
            return badRequest(school.render(schoolForm));
        } else {
            School school = new School();
            school.name = schoolForm.get().name;
            school.dateCreated = schoolForm.get().dateCreated;
            school.location = schoolForm.get().location;
            school.save();
            flash("success", school.name + " has been added.");
            return redirect(routes.SchoolController.showSchoolView());
        }
    }

    public static class SchoolParameters {
        public String name;
        public Date dateCreated;
        public String location;

        public String validate() {
            if (name == null || name.equals("")) {
                return "Name of school is not set.";
            }
            if (dateCreated == null) {
                return "Date when school was created is not set.";
            }
            if (location == null || location.equals("")) {
                return "Location of school is not set.";
            }
            return null;
        }
    }
}
