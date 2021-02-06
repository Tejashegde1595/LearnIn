package com.example.portfolio.service.Entity.education;

import com.example.portfolio.service.Entity.UserEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "EDUCATION_BACHELORS")
@NamedQueries(
        {
                @NamedQuery(name = "getAllBachelors", query = "select b from BachelorsEntity b"),
                @NamedQuery(name = "bachelorsByUuid", query = "select b from BachelorsEntity b where b.uuid = :uuid"),
                @NamedQuery(name = "bachelorsByName", query = "select b from BachelorsEntity b where b.bachelorsCollegeName =:bachelorsCollegeName"),
                @NamedQuery(name = "bachelorsByUserId", query = "select b from BachelorsEntity b where b.user_id.uuid = :uuid")
        }
)
public class BachelorsEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NotNull
    @Column(name = "UUID")
    private String uuid;

    @NotNull
    @Column(name = "BACHELORS_COLLEGE_NAME")
    private String bachelorsCollegeName;

    @NotNull
    @Column(name = "GRADE")
    private String grade;

    @Column(name = "FROM_DATE")
    private Date fromDate;


    @Column(name = "TO_DATE")
    private Date toDate;


    @Column(name = "SUBJECT")
    private String subject;

    @JoinColumn(name = "USER_ID")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity user_id;


    public Integer getId() {
        return Id;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCollegeName() {
        return bachelorsCollegeName;
    }

    public void setCollegeName(String bachelorsCollegeName) {
        this.bachelorsCollegeName = bachelorsCollegeName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public UserEntity getUser_id() {
        return user_id;
    }

    public void setUser_id(UserEntity user_id) {
        this.user_id = user_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
