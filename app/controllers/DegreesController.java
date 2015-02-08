package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Degree;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.showDegrees;

import java.util.Map;

/**
 * Created by Windows 8 on 08-Feb-15.
 * @author Jelena Tabas
 */
public class DegreesController  extends Controller {

    public static Result showDegreesView() {
        return ok(showDegrees.render("Your new application is ready."));

    }

    public static Result list() {
        /**
         * Get needed params
         */
        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = Degree.find.findRowCount();
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
            case 1 : sortBy = "dateStarted"; break;
            case 2 : sortBy = "dateEnded"; break;
            case 3 : sortBy = "name"; break;
            case 4 : sortBy = "fieldOfStudy"; break;
            case 5 : sortBy = "grade"; break;
        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<Degree> contactsPage = Degree.find.where(
                Expr.or(
                        Expr.ilike("name", "%" + filter + "%"),
                        Expr.or(
                                Expr.ilike("dateStarted", "%" + filter + "%"),
                                Expr.ilike("dateEnded", "%" + filter + "%")
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

        for(Degree c : contactsPage.getList()) {
            ObjectNode row = Json.newObject();
            row.put("0", c.id);
            row.put("1", String.valueOf(c.dateStarted));
            row.put("2", String.valueOf(c.dateEnded));
            row.put("3", c.name);
            row.put("4", c.fieldOfStudy);
            row.put("5", c.grade);
            an.add(row);
        }

        return ok(result);

    }

}
