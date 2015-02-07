package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Person;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.personSearch;

import java.util.Map;

/**
 * Created by Windows 8 on 2/3/2015.
 *
 * @author Jelena Tabas
 * @version 1.0.0
 */
public class PersonsController extends Controller {

     public static Result showPersonsSearch() {
        return ok(personSearch.render("Your new application is ready."));

    }

    public static Result list() {
        /**
         * Get needed params
         */
        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = Person.find.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "firstName";
        String order = params.get("sSortDir_0")[0];

        switch(Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0 : sortBy = "id"; break;
            case 1 : sortBy = "firstName"; break;
            case 2 : sortBy = "lastName"; break;
            case 3 : sortBy = "administrator"; break;
            case 4 : sortBy = "email"; break;
        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<Person> contactsPage = Person.find.where(
                Expr.or(
                        Expr.ilike("firstName", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("lastName", "%" + filter + "%"),
                                Expr.ilike("email", "%" + filter + "%")
                        )
                )
        )
                .orderBy(sortBy + " " + order + ", id " + order)
                .findPagingList(pageSize).setFetchAhead(false)
                .getPage(page);

        Integer iTotalDisplayRecords = contactsPage.getTotalRowCount();

        /**
         * Construct the JSON to return
         */
        ObjectNode result = Json.newObject();

        result.put("sEcho", Integer.valueOf(params.get("sEcho")[0]));
        result.put("iTotalRecords", iTotalRecords);
        result.put("iTotalDisplayRecords", iTotalDisplayRecords);

        ArrayNode an = result.putArray("aaData");

        for(Person c : contactsPage.getList()) {
            ObjectNode row = Json.newObject();
            row.put("0", c.id);
            row.put("1", c.firstName);
            row.put("2", c.lastName);
            row.put("3", c.email);
            an.add(row);
        }

        return ok(result);

    }
}
