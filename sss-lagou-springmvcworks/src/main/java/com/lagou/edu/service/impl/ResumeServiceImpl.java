package com.lagou.edu.service.impl;

import com.lagou.edu.dao.ResumeDao;
import com.lagou.edu.mapper.ResumeMapper;
import com.lagou.edu.pojo.Resume;
import com.lagou.edu.service.IResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangdm
 * @description resumeService Api实现业务类
 */
@Service
@Transactional
public class ResumeServiceImpl implements IResumeService {
    @Autowired
    private ResumeDao resumeDao;
    @Autowired
    private ResumeMapper resumeMapper;
    @Override
    public List<Resume> queryAll(){
        try {
            return resumeDao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Resume findOne(Long id) {
        Resume resume = resumeDao.findOneById(id);
        if(resume==null){
            return null;
        }
        return resume;
    }

    @Override
    public void saveOne(Resume resume) {
        resumeMapper.saveOne(resume);
    }

    @Override
    public void deleteById(Long id) {
        resumeMapper.deleteById(id);
    }

    @Override
    public void updateOne(Resume resume) {
        resumeMapper.updateOne(resume);
    }

}
