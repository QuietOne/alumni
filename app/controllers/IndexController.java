package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * @author tihomir
 * @version 1.0.0
 */
public class IndexController extends Controller {

    public static Result showIndexView() {
        return ok(index.render());
    }
}
