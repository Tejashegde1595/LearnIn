package com.example.portfolio.service.Dao.education;

import com.example.portfolio.service.Entity.education.CollegeEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CollegeDao {
    @PersistenceContext
    private EntityManager entityManager;

    public CollegeEntity createCollegeEntity(final CollegeEntity CollegeEntity) {
        entityManager.persist(CollegeEntity);
        return CollegeEntity;
    }

    public List<CollegeEntity> getAllColleges() {
        List<CollegeEntity> CollegeEntityList = entityManager.createNamedQuery("getAllColleges").getResultList();
        return CollegeEntityList;
    }

    public List<CollegeEntity> getAllCollegesByUser(String userId) {
        List<CollegeEntity> CollegeEntityList = entityManager.createNamedQuery("collegeByUserId").setParameter("uuid",userId).getResultList();
        return CollegeEntityList;
    }

    public CollegeEntity getCollegeById(String uuid){
        try {
            return entityManager.createNamedQuery("collegeByUuid",CollegeEntity.class).setParameter("uuid", uuid).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public CollegeEntity deleteCollege(CollegeEntity collegeEntity){
        entityManager.remove(collegeEntity);
        return collegeEntity;
    }

    public CollegeEntity editCollege(CollegeEntity collegeEntity){
        entityManager.merge(collegeEntity);
        return collegeEntity;
    }
}
