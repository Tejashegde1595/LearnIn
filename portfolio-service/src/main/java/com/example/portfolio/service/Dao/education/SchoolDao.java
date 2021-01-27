package com.example.portfolio.service.Dao.education;

import com.example.portfolio.service.Entity.education.SchoolEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SchoolDao {
    @PersistenceContext
    private EntityManager entityManager;

    public SchoolEntity createSchoolEntity(final SchoolEntity schoolEntity) {
        entityManager.persist(schoolEntity);
        return schoolEntity;
    }

    public List<SchoolEntity> getAllSchools() {
        List<SchoolEntity> schoolEntityList = entityManager.createNamedQuery("getAllSchools").getResultList();
        return schoolEntityList;
    }

    public List<SchoolEntity> getAllSchoolsByUser(String userId) {
        List<SchoolEntity> schoolEntityList = entityManager.createNamedQuery("schoolByUserId").setParameter("uuid",userId).getResultList();
        return schoolEntityList;
    }
}
