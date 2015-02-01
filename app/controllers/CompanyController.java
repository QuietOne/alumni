package controllers;

import models.Company;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.company;
import views.html.forbidden;

/**
 * @author Tihomir Radosavljevic
 * @version 1.0.0
 */
public class CompanyController extends Controller{

    public static Result showCompanyView() {
        if (MainController.isAdmin()) {
            return ok(company.render(Form.form(CompanyParameters.class)));
        } else {
            return ok(forbidden.render());
        }
    }

    public static Result addCompany() {
        Form<CompanyParameters> companyForm = Form.form(CompanyParameters.class).bindFromRequest();
        System.out.println("Form: "+companyForm);
        if (companyForm.hasErrors()) {
            return badRequest(company.render(companyForm));
        } else {
            Company company = new Company();
            company.name = companyForm.get().name;
            company.numberOfEmployees = companyForm.get().aproximateSize;
            company.location = companyForm.get().location;
            company.save();
            flash("success", company.name + " has been added.");
            return redirect(routes.CompanyController.showCompanyView());
        }
    }

    public static class CompanyParameters {
        public String name;
        public String location;
        public Integer aproximateSize;

        public String validate() {
            System.out.println("Name: "+name);
            System.out.println("Location: "+location);
            System.out.println("Size: "+aproximateSize);
            if (name == null || name.equals("")) {
                return "Name of company is not set.";
            }
            if (aproximateSize == null || aproximateSize.equals("")) {
                return "Approximate size of company was created is not set.";
            }
            if (location == null || location.equals("")) {
                return "Location of company is not set.";
            }
            return null;
        }
    }
}
