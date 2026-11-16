package vn.edu.hcmute.dao;

import jakarta.persistence.*;
import vn.edu.hcmute.config.JpaConfig;
import vn.edu.hcmute.model.Video;
import java.util.List;

public class VideoDAO {
    public List<Video> latestTen() {
        try(EntityManager em=JpaConfig.getEntityManager()) {
            return em.createQuery("SELECT v FROM Video v JOIN FETCH v.owner ORDER BY v.createdAt DESC, v.id DESC", Video.class)
                    .setMaxResults(10).getResultList();
        }
    }
    public List<Video> byOwner(int userId,int page) {
        try(EntityManager em=JpaConfig.getEntityManager()) {
            return em.createQuery("SELECT v FROM Video v JOIN FETCH v.owner WHERE v.owner.id = :uid ORDER BY v.createdAt DESC, v.id DESC", Video.class)
                    .setParameter("uid",userId).setFirstResult((page-1)*6).setMaxResults(6).getResultList();
        }
    }
    public long countByOwner(int userId) {
        try(EntityManager em=JpaConfig.getEntityManager()) {
            return em.createQuery("SELECT COUNT(v) FROM Video v WHERE v.owner.id = :uid",Long.class)
                    .setParameter("uid",userId).getSingleResult();
        }
    }
    public Video find(long id) {
        try(EntityManager em=JpaConfig.getEntityManager()) {
            List<Video> results=em.createQuery("SELECT v FROM Video v JOIN FETCH v.owner WHERE v.id=:id",Video.class)
                    .setParameter("id",id).setMaxResults(1).getResultList();
            return results.isEmpty()?null:results.get(0);
        }
    }
    public void insert(Video video,int userId) {
        EntityTransaction tx=null;
        try(EntityManager em=JpaConfig.getEntityManager()) {
            tx=em.getTransaction();tx.begin();
            video.setOwner(em.getReference(vn.edu.hcmute.model.User.class,userId));
            em.persist(video);tx.commit();
        } catch(RuntimeException ex) { if(tx!=null&&tx.isActive())tx.rollback();throw ex; }
    }
    public boolean update(long id,int ownerId,String title,String description,String fileName) {
        EntityTransaction tx=null;
        try(EntityManager em=JpaConfig.getEntityManager()) {
            tx=em.getTransaction();tx.begin();
            Video video=em.find(Video.class,id);
            if(video==null||video.getOwner().getId()!=ownerId) {tx.rollback();return false;}
            video.setTitle(title);video.setDescription(description);
            if(fileName!=null)video.setFileName(fileName);
            tx.commit();return true;
        } catch(RuntimeException ex) {if(tx!=null&&tx.isActive())tx.rollback();throw ex;}
    }
    public boolean delete(long id,int ownerId) {
        EntityTransaction tx=null;
        try(EntityManager em=JpaConfig.getEntityManager()) {
            tx=em.getTransaction();tx.begin();
            Video video=em.find(Video.class,id);
            if(video==null||video.getOwner().getId()!=ownerId){tx.rollback();return false;}
            em.remove(video);tx.commit();return true;
        } catch(RuntimeException ex) {if(tx!=null&&tx.isActive())tx.rollback();throw ex;}
    }
    public void incrementViews(long id) {
        EntityTransaction tx=null;
        try(EntityManager em=JpaConfig.getEntityManager()) {
            tx=em.getTransaction();tx.begin();
            em.createQuery("UPDATE Video v SET v.views=v.views+1 WHERE v.id=:id")
                    .setParameter("id",id).executeUpdate();
            tx.commit();
        } catch(RuntimeException ex) {if(tx!=null&&tx.isActive())tx.rollback();throw ex;}
    }
}
