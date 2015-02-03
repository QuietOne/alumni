import play.*;
import play.libs.*;
import java.util.*;
import com.avaje.ebean.*;
import models.*;

/**
 * Created by Windows 8 on 2/1/2015.
 *
 * @author Jelena Tabas
 * @version 1.0.0
 */

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {

        /**
         * Here we load the initial data into the database
         */
        if(Ebean.find(Company.class).findRowCount() == 0) {
            Map<String,List<Object>> all = (Map<String,List<Object>>)Yaml.load("initial-data.yml");
            Ebean.save(all.get("companies"));
        }

    }
}