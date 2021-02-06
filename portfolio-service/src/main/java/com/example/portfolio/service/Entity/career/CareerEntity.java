package com.example.portfolio.service.Entity.career;
import com.example.portfolio.service.Entity.UserEntity;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "CAREER")
@NamedQueries(
        {
                @NamedQuery(name = "getAllCareerInfo", query = "select c from CareerEntity c"),
                @NamedQuery(name = "careerByUuid", query = "select c from CareerEntity c where c.uuid = :uuid"),
                @NamedQuery(name = "careerByCompanyName", query = "select c from CareerEntity c where c.companyName =:companyName"),
                @NamedQuery(name = "careerByUserId", query = "select c from CareerEntity c where c.user_id.uuid = :uuid")
        }
)
public class CareerEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NotNull
    @Column(name = "UUID")
    private String uuid;

    @NotNull
    @Column(name = "COMPANY_NAME")
    private String companyName;

    @NotNull
    @Column(name = "DESIGNATION")
    private String designation;

    @Column(name = "FROM_DATE")
    private Date fromDate;


    @Column(name = "TO_DATE")
    private Date toDate;


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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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



}
