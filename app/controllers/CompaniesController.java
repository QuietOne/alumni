package controllers;

import com.avaje.ebean.Expr;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Company;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.companySearch;

import java.util.Map;

/**
 * Created by Windows 8 on 2/1/2015.
 *
 * @author Jelena Tabas
 * @version 1.0.0
 */
public class CompaniesController extends Controller {

    public static Result showCompaniesSearch() {
                   return ok(companySearch.render("Your new application is ready."));

    }

    public static Result list() {
        /**
         * Get needed params
         */
        Map<String, String[]> params = request().queryString();

        Integer iTotalRecords = Company.find.findRowCount();
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
            case 2 : sortBy = "location"; break;
            case 3 : sortBy = "numberOfEmployees"; break;
        }

        /**
         * Get page to show from database
         * It is important to set setFetchAhead to false, since it doesn't benefit a stateless application at all.
         */
        Page<Company> contactsPage = Company.find.where(
                Expr.or(
                        Expr.ilike("name", "%"+filter+"%"),
                        Expr.or(
                                Expr.ilike("location", "%"+filter+"%"),
                                Expr.ilike("numberOfEmployees", "%"+filter+"%")
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

        for(Company c : contactsPage.getList()) {
            ObjectNode row = Json.newObject();
            row.put("0", c.id);
            row.put("1", c.name);
            row.put("2", c.location);
            row.put("3", c.numberOfEmployees);
            an.add(row);
        }

        return ok(result);

    }

}
