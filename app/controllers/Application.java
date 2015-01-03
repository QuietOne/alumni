package controllers;

import play.mvc.*;
import models.Person;
import play.data.Form;
import java.util.List;
import play.db.ebean.Model;
import views.html.index;
import views.html.routes;

import static play.libs.Json.*;

public class Application extends Controller {

  /*  public static Result index() {
        return ok(index.render());
    }
*/
    public static Result addPerson() {
      Person person = Form.form(Person.class).bindFromRequest().get();
      person.save();
      //	return redirect(routes.LoginController.index());
      return redirect(routes.LoginController.showLoginView());
    }

    public static Result getPersons() {
    	List<Person> persons = new Model.Finder(Long.class, Person.class).all();
    	return ok(toJson(persons));
    }
}
