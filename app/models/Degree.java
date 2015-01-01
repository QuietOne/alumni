package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tihomir
 * @version 1.0.0
 */
@Entity
public class Degree extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Constraints.Required
    public Date dateStarted;

    public Date dateEnded;

    public String name;

    public String fieldOfStudy;

    public Double grade;

    @ManyToOne(cascade = CascadeType.PERSIST)
    public Person person;

    @ManyToOne(cascade = CascadeType.PERSIST)
    public School school;
}
