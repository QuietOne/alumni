package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.*;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person extends Model {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

    @Constraints.Required
	public String firstName;

    @Constraints.Required
    public String lastName;

    @Constraints.Required
    public Boolean administrator = false;

    @Constraints.Email
    public String email;

    @Constraints.Required
    public String password;

    public String linkedInUsername;
    public String linkedInPassword;
    public String linkedInToken;

    public String facebookToken;

    @OneToMany (mappedBy = "person", cascade = CascadeType.ALL)
    public List<Degree> degrees = new ArrayList<>();

    @ManyToMany (mappedBy = "persons", cascade = CascadeType.ALL)
    public List<Position> positions = new ArrayList<>();

    @ManyToMany (mappedBy = "employees", cascade = CascadeType.ALL)
    public List<Project> projects = new ArrayList<>();

    @ManyToMany (mappedBy = "persons", cascade = CascadeType.ALL)
    public List<Tag> tags = new ArrayList<>();

    public String cvName;

//    public File portfolio;
//    public File publicationFile;

}