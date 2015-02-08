package controllers;

import com.avaje.ebean.Ebean;
import models.Degree;
import models.Person;
import models.School;

import play.mvc.Result;
import play.mvc.Controller;
import play.data.Form;
import views.html.forbidden;
import views.html.degree;
import java.util.Date;
import java.util.List;

/**
 * @author tihomir
 * @author Jelena
 * @version 1.0.0
 */
public class DegreeController extends Controller{


    public static Result showDegreeView() {
        String email = session().get("email");
        if (email == null || email.equals("")) {
            return ok(forbidden.render());
        } else {
            Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
            Form<DegreeParameters> form = Form.form(DegreeParameters.class);
            List<School> schools = Ebean.find(School.class).findList();
            return ok(degree.render(form, schools, person.degrees, "Add degree"));
        }
    }

    public static Result addDegree() {
        System.out.println("1");
        Form<DegreeParameters> form = Form.form(DegreeParameters.class).bindFromRequest();
        String email = session().get("email");
        Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
        if (form.hasErrors()) {
            List<School> schools = Ebean.find(School.class).findList();
            return badRequest(degree.render(form, schools, person.degrees, "Add degree"));
        } else {
            Degree degree = new Degree();
            degree.person = person;
            degree.dateStarted = form.get().dateStarted;
            degree.dateEnded = form.get().dateEnded;
            degree.name = form.get().name;
            degree.fieldOfStudy = form.get().fieldOfStudy;
            degree.grade = form.get().grade;
            School school = Ebean.find(School.class).where().eq("id", form.get().schoolId).findUnique();
            degree.school = school;
            degree.save();
            flash("success", "You've been successfully added a new degree");
            return redirect(routes.EditProfileController.showEditView());
        }
    }

    public static Result showEditDegreeView(String id) {
        String email = session().get("email");
        if (email == null || email.equals("")) {
            return ok(forbidden.render());
        } else {
            Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
            Degree d = Ebean.find(Degree.class).where().eq("id", id).findUnique();
            Form<DegreeParameters> form = Form.form(DegreeParameters.class);
            DegreeParameters dp = new DegreeParameters();
            dp.dateEnded = d.dateEnded;
            dp.dateStarted = d.dateStarted;
            dp.name = d.name;
            dp.grade = d.grade;
            dp.schoolId = d.school.id;
            dp.fieldOfStudy = d.fieldOfStudy;
            List<School> schools = Ebean.find(School.class).findList();
            session("degreeId",id);
            return ok(degree.render(form.fill(dp), schools, person.degrees, "Edit degree"));
        }
    }

    public static Result editDegree() {
        System.out.println("2");
        Form<DegreeParameters> form = Form.form(DegreeParameters.class).bindFromRequest();
        String email = session().get("email");
        Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
        if (form.hasErrors()) {
            List<School> schools = Ebean.find(School.class).findList();
            return badRequest(degree.render(form, schools, person.degrees, "Edit degree"));
        } else {
            String id = session().get("degreeId");
            Degree degree = Ebean.find(Degree.class).where().eq("id", id).findUnique();
            degree.person = person;
            degree.dateStarted = form.get().dateStarted;
            degree.dateEnded = form.get().dateEnded;
            degree.name = form.get().name;
            degree.fieldOfStudy = form.get().fieldOfStudy;
            School school = Ebean.find(School.class).where().eq("id", form.get().schoolId).findUnique();
            degree.school = school;
            degree.update();
            flash("success", "You've been successfully edited a degree");
            return redirect(routes.EditProfileController.showEditView());
        }
    }

    public static Result removeDegree(String id) {
        String email = session().get("email");
        if (email == null || email.equals("")) {
            return ok(forbidden.render());
        } else {
            Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
            Degree degree = Ebean.find(Degree.class).where().eq("id", id).findUnique();
            if (degree.person.id != person.id) {
                return ok(forbidden.render());
            } else {
                degree.delete();
                flash("success", "You've been successfully removed a degree");
                return redirect(routes.EditProfileController.showEditView());
            }
        }
    }

    public static class DegreeParameters {
        public Date dateStarted;
        public Date dateEnded;
        public String name;
        public String fieldOfStudy;
        public String grade;
        public Long schoolId;

        public String validate() {
            if (dateStarted == null) {
                return "Date when attendance started has not been set.";
            }
            if (dateEnded == null) {
                return "Date when attendance ended has not been set.";
            }
            if (name == null || name.equals("")) {
                return "Name of degree obtained has not been set.";
            }
            if (fieldOfStudy == null || fieldOfStudy.equals("")) {
                return "Field of study has not been set.";
            }
            if (schoolId == 0) {
                return "School has not been selected.";
            }
            return null;
        }
    }
}
