package com.lagou.edu.dao;

import com.lagou.edu.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


/**
 *  这是个dao方法
 */
@Repository
public interface ResumeDao extends JpaRepository<Resume,Long>, JpaSpecificationExecutor<Resume> {

//    @Transactional
//    @Modifying
//    @Query(value = "update tb_resume set address = ?2,name = ?3,phone = ?4 where id = ?1",nativeQuery = true)
//    int updateOne(Long id,String address,String name,String phone);
//
//    @Transactional
//    @Modifying
//    @Query(value = "insert into tb_resume values (id = ?1,address = ?2,name = ?3,phone = ?4)",nativeQuery = true)
//    int saveOne(Long id,String address,String name,String phone);

    @Query(value = "select * from tb_resume where id = ?1",nativeQuery = true)
    Resume findOneById(Long id);
}
