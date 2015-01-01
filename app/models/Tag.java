package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tihomir
 * @version 1.0.0
 */
@Entity
public class Tag extends Model{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public String name;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Person> persons = new ArrayList<>();
}
