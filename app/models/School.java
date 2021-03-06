package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tihomir
 * @author Jelena
 * @version 1.0.0
 */
@Entity
public class School extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String name;

    @Constraints.Required
    public Date dateCreated;

    public String location;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    public List<Degree> degrees = new ArrayList<>();

    public static Model.Finder<Long,School> find = new Model.Finder(Long.class, School.class);

    public static List<School> findAll() {
        return find.all();
    }
}
