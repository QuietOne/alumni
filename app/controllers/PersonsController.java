package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.persons;

/**
 * @author tihomir
 * @version 1.0.0
 */
public class PersonsController extends Controller {

    public static Result showPersonsView() {
        return ok(persons.render());
    }

    public static Result searchForPerson() {
        //TODO: get specific parametar and search for persons with that data
        return null;
    }

    public static Result getAllPersons() {
        //TODO: get all persons from db and paginate them
        return null;
    }
}
