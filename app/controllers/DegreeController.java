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
            return ok(degree.render(form, schools, person.degrees));
        }
    }

    public static Result addDegree() {
        Form<DegreeParameters> form = Form.form(DegreeParameters.class).bindFromRequest();
        String email = session().get("email");
        Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
        if (form.hasErrors()) {
            List<School> schools = Ebean.find(School.class).findList();
            return badRequest(degree.render(form, schools, person.degrees));
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
            return redirect(routes.DegreeController.showDegreeView());
        }
    }

//    public static Result editDegree(Long id) {
//        Form<DegreeParameters> form = Form.form(DegreeParameters.class).bindFromRequest();
//        String email = session().get("email");
//        Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
//        if (form.hasErrors()) {
//            List<School> schools = Ebean.find(School.class).findList();
//            return badRequest(degree.render(form, schools, person.degrees));
//        } else {
//            Degree degree = Ebean.find(Degree.class).where().eq("id", id).findUnique();
//            degree.person = person;
//            degree.dateStarted = form.get().dateStarted;
//            degree.dateEnded = form.get().dateEnded;
//            degree.name = form.get().name;
//            degree.fieldOfStudy = form.get().fieldOfStudy;
//            degree.school = form.get().school;
//            degree.update();
//            return redirect(routes.DegreeController.showDegreeView());
//        }
////    }
//
//    public static Result removeDegree(Long id) {
//        Form<DegreeParameters> form = Form.form(DegreeParameters.class).bindFromRequest();
//        String email = session().get("email");
//        Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
//        if (form.hasErrors()) {
//            List<School> schools = Ebean.find(School.class).findList();
//            return badRequest(degree.render(form, schools, person.degrees));
//        } else {
//            Degree degree = Ebean.find(Degree.class).where().eq("id", id).findUnique();
//            degree.person = person;
//            degree.dateStarted = form.get().dateStarted;
//            degree.dateEnded = form.get().dateEnded;
//            degree.name = form.get().name;
//            degree.fieldOfStudy = form.get().fieldOfStudy;
//            degree.school = DegreeController.selectedSchool;
//            degree.delete();
//            return redirect(routes.DegreeController.showDegreeView());
//        }
//    }

    public static class DegreeParameters {
        public Date dateStarted;
        public Date dateEnded;
        public String name;
        public String fieldOfStudy;
        public String grade;
        public Long schoolId;

        public String validate() {
            System.out.println("dateStarted: "+dateStarted);
            System.out.println("dateEnded: "+dateEnded);
            System.out.println("name: "+name);
            System.out.println("fieldOfStudy: "+fieldOfStudy);
            System.out.println("school: "+schoolId);
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
