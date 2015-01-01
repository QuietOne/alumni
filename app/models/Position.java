package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tihomir
 * @version 1.0.0
 */
@Entity
public class Position extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String name;

    public String description;

    @Constraints.Required
    public Date dateStarted;

    public Date dateEnded;

    @ManyToMany (cascade = CascadeType.ALL)
    public List<Company> companiesThatHaveThisPosition = new ArrayList<>();

    @ManyToMany (cascade = CascadeType.ALL)
    public List<Person> persons = new ArrayList<>();
}
