package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tihomir
 * @author Jelena
 * @version 1.0.0
 */
@Entity
public class Company extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String name;

    public String location;

    public Integer numberOfEmployees;

    @ManyToMany(mappedBy = "companiesThatHaveThisPosition", cascade = CascadeType.ALL)
    public List<Position> positions = new ArrayList<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    public List<Project> projects = new ArrayList<>();

    public static Model.Finder<Long,Company> find = new Model.Finder(Long.class, Company.class);

    public static List<Company> findAll() {
        return find.all();
    }

}
