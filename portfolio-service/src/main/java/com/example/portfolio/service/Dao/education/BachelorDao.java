package com.example.portfolio.service.Dao.education;

import com.example.portfolio.service.Entity.education.BachelorsEntity;
import com.example.portfolio.service.Entity.education.CollegeEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class BachelorDao {
    @PersistenceContext
    private EntityManager entityManager;

    public BachelorsEntity createBachelorsEntity(final BachelorsEntity bachelorsEntity) {
        entityManager.persist(bachelorsEntity);
        return bachelorsEntity;
    }

    public List<BachelorsEntity> getAllBachelors() {
        List<BachelorsEntity> bachelorsEntityList = entityManager.createNamedQuery("getAllBachelors").getResultList();
        return bachelorsEntityList;
    }

    public List<BachelorsEntity> getAllBachelorsByUser(final String userId) {
        List<BachelorsEntity> bachelorsEntityList = entityManager.createNamedQuery("bachelorsByUserId").setParameter("uuid",userId).getResultList();
        return bachelorsEntityList;
    }

    public BachelorsEntity getBachelorsById(String uuid){
        try {
            return entityManager.createNamedQuery("bachelorsByUuid",BachelorsEntity.class).setParameter("uuid", uuid).getSingleResult();
        }catch (NoResultException nre){
            return null;
        }
    }

    public BachelorsEntity deleteBachelors(final BachelorsEntity bachelorsEntity){
        entityManager.remove(bachelorsEntity);
        return bachelorsEntity;
    }

    public BachelorsEntity editCollege(final BachelorsEntity bachelorsEntity){
        entityManager.merge(bachelorsEntity);
        return bachelorsEntity;
    }
}
