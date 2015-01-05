package controllers;

import play.mvc.Result;
import play.mvc.Controller;
import views.html.social;

/**
 * @author tihomir
 * @version 1.0.0
 */
public class SocialController extends Controller{

    public static Result showSocialView() {
        return ok(social.render());
    }

    public static Result connectWithLinkedIn() {
        //TODO: get informations from linked in
        return null;
    }

    public static Result connectWithFacebook() {
        //TODO: get informations from fb
        return null;
    }
}
