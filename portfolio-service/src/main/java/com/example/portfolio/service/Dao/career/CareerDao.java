package com.example.portfolio.service.Dao.career;

import com.example.portfolio.service.Entity.career.CareerEntity;
import com.example.portfolio.service.Entity.education.BachelorsEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CareerDao {
    @PersistenceContext
    private EntityManager entityManager;

    public CareerEntity createCareerEntity(final CareerEntity careerEntity) {
        entityManager.persist(careerEntity);
        return careerEntity;
    }

    public List<CareerEntity> getAllCareers() {
        List<CareerEntity> careerEntityList = entityManager.createNamedQuery("getAllCareerInfo").getResultList();
        return careerEntityList;
    }

    public List<CareerEntity> getAllCareersByUser(final String userId) {
        List<CareerEntity> careerEntityList = entityManager.createNamedQuery("careerByUserId").setParameter("uuid",userId).getResultList();
        return careerEntityList;
    }

    public CareerEntity getCareersById(String uuid){
        try {
            return entityManager.createNamedQuery("careerByUuid",CareerEntity.class).setParameter("uuid", uuid).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public CareerEntity deleteCareerInfo(final CareerEntity careerEntity){
        entityManager.remove(careerEntity);
        return careerEntity;
    }

    public CareerEntity editCareerInfo(final CareerEntity careerEntity){
        entityManager.merge(careerEntity);
        return careerEntity;
    }
}
