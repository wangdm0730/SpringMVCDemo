package com.lagou.edu.mapper;

import com.lagou.edu.pojo.Resume;

import java.util.List;

public interface ResumeMapper {

    //  定义dao层接口方法--> 查询tb_resume表所有数据
    List<Resume> queryResumeList();
    //新增
    void saveOne(Resume resume);
    //通过id删除
    void deleteById(Long id);
    //修改
    void updateOne(Resume resume);
    //查询单个对象
    Resume findOne(Long id);
}
