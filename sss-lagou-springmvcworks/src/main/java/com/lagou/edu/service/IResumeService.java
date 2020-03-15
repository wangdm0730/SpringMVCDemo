package com.lagou.edu.service;

import com.lagou.edu.pojo.Resume;

import java.util.List;

/**
 * @author wangdm
 * @description resumeApi
 */
public interface IResumeService {
    //查询所有
    List<Resume> queryAll();
    //新增
    void saveOne(Resume resume);
    //删除
    void deleteById(Long id);
    //修改
    void updateOne(Resume resume);
    //查询单条记录
    Resume findOne(Long id);
}
