package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.School;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.findSchool;

import java.util.Map;

/**
 * Created by Windows 8 on 08-Feb-15.
 *
 * @author Jelena Tabas
 * @version 1.0.0
 */
public class SchoolsController extends Controller {

    public static Result showFindSchools() {
        return ok(findSchool.render("Your new application is ready."));

    }

    public static Result list() {
        /**
         * Get needed params
         */
        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = School.find.findRowCount();
        String filter = params.get("sSearch")[0];
        Integer pageSize = Integer.valueOf(params.get("iDisplayLength")[0]);
        Integer page = Integer.valueOf(params.get("iDisplayStart")[0]) / pageSize;

        /**
         * Get sorting order and column
         */
        String sortBy = "name";
        String order = params.get("sSortDir_0")[0];

        switch(Integer.valueOf(params.get("iSortCol_0")[0])) {
            case 0 : sortBy = "id"; break;
            case 1 : sortBy = "name"; break;
            case 2 : sortBy = "dateCreated"; break;
            case 3 : sortBy = "location"; break;
        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<School> contactsPage = School.find.where(
                Expr.or(
                        Expr.ilike("name", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("dateCreated", "%" + filter + "%"),
                                Expr.ilike("location", "%" + filter + "%")
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

        for(School c : contactsPage.getList()) {
            ObjectNode row = Json.newObject();
            row.put("0", c.id);
            row.put("1", c.name);
            row.put("2", String.valueOf(c.dateCreated));
            row.put("3", c.location);
            an.add(row);
        }

        return ok(result);

    }

}
