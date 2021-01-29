package com.example.portfolio.service.Entity.education;

import com.example.portfolio.service.Entity.UserEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "EDUCATION_COLLEGE")
@NamedQueries(
        {
                @NamedQuery(name = "getAllColleges", query = "select s from CollegeEntity s"),
                @NamedQuery(name = "collegeByUuid", query = "select s from CollegeEntity s where s.uuid = :uuid"),
                @NamedQuery(name = "collegeByName", query = "select s from CollegeEntity s where s.collegeName =:collegeName"),
                @NamedQuery(name = "collegeByUserId", query = "select s from CollegeEntity s where s.user_id.uuid = :uuid")
        }
)
public class CollegeEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NotNull
    @Column(name = "UUID")
    private String uuid;

    @NotNull
    @Column(name = "college_NAME")
    private String collegeName;

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
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
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
