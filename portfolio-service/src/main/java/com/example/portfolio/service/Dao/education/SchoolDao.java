package com.example.portfolio.service.Dao.education;

import com.example.portfolio.service.Entity.education.SchoolEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public SchoolEntity getSchoolById(String uuid){
        try {
            return entityManager.createNamedQuery("schoolByUuid",SchoolEntity.class).setParameter("uuid", uuid).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public SchoolEntity deleteSchool(SchoolEntity schoolEntity){
        entityManager.remove(schoolEntity);
        return schoolEntity;
    }

    public SchoolEntity editSchool(SchoolEntity schoolEntity){
        entityManager.merge(schoolEntity);
        return schoolEntity;
    }
}
