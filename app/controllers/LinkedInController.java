package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Person;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.F.Function;
import play.libs.XML;
import play.libs.XPath;
import play.mvc.Result;
import play.mvc.Controller;
import views.html.linkedin;
import play.libs.ws.*;

/**
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public class LinkedInController extends Controller{

    public static Result showLinkedInView() {
        return ok(linkedin.render(Form.form(LinkedInParameters.class)));
    }

    public static Result autorizationStep1() {
        Form<LinkedInParameters> linkedInForm = Form.form(LinkedInParameters.class).bindFromRequest();
        if (linkedInForm.hasErrors()) {
            return badRequest(linkedin.render(linkedInForm));
        } else {
            String email = session().get("email");
            Person person = Ebean.find(Person.class).where().eq("email", email).findUnique();
            person.linkedInUsername = linkedInForm.get().email;
            person.linkedInPassword = linkedInForm.get().password;
            person.update();
            return redirect("https://www.linkedin.com/uas/oauth2/authorization?response_type=code" +
                    "&client_id=77xql59kd9sgsn" +
                    "&scope=r_fullprofile" +
                    "&state=DCEsdasdEFWFdds45453sadsdffef424" +
                    "&redirect_uri="+IndexController.BASE+routes.LinkedInController.autorizationStep2());
        }
    }

    public static Result autorizationStep2() {
        String uri = request().uri();
        if(uri.contains("error")){
            Person person = Ebean.find(Person.class).where().eq("email",session().get("email")).findUnique();
            person.linkedInToken = null;
            person.update();
            return redirect(routes.LinkedInController.showLinkedInView());
        }
        if (uri.substring(uri.lastIndexOf('=')).equals("DCEsdasdEFWFdds45453sadsdffef424")) {
            return redirect(routes.IndexController.showIndexView());
        }
        String code = uri.substring(uri.indexOf('=') + 1, uri.indexOf('&'));
        WS.url("https://www.linkedin.com/uas/oauth2/accessToken?grant_type=authorization_code" +
                        "&code=" + code +
                        "&redirect_uri=" + IndexController.BASE + routes.LinkedInController.autorizationStep2() +
                        "&client_id=77xql59kd9sgsn" +
                        "&client_secret=ZoUH0FvAiVJFT5Eq")
                        .get()
                        .map(new Function<WSResponse, JsonNode>(){
                            public JsonNode apply(WSResponse response) {
                                JsonNode json = response.asJson();
                                Person person = Ebean.find(Person.class).where().eq("email",session().get("email")).findUnique();
                                person.linkedInToken = json.get("access_token").asText();
                                person.update();
                                return json;
                            }
                        });
        return redirect(routes.IndexController.showIndexView());
    }

    public static Result showLinkedInData() {
        Person person = Ebean.find(Person.class).where().eq("email",session().get("email")).findUnique();
        if (person.linkedInToken == null) {
            return redirect(routes.LinkedInController.showLinkedInView());
        }
        String url = "https://api.linkedin.com/v1/people/~?oauth2_access_token=" + person.linkedInToken;
        Promise<String> redirectUrl = WS.url(url).get().map(new Function<WSResponse, String>(){
           public String apply(WSResponse response) {
               return XPath.selectText("//url", response.asXml());
           }
        });
        return redirect(redirectUrl.get(1000));
    }

    public static class LinkedInParameters {
        public String email;
        public String password;

        public String validate() {
            if (email == null || email.equals("")) {
                return "Email field is empty.";
            }
            if (password == null || password.equals("")) {
                return "Password field is empty.";
            }
            return null;
        }
    }
}
