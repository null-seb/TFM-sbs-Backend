package es.upm.tfm_sbs.service.edu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import es.upm.tfm_sbs.service.edu.entity.CourseCollect;
import es.upm.tfm_sbs.service.edu.entity.query.CourseCollectVo;
import es.upm.tfm_sbs.service.edu.mapper.CourseCollectMapper;
import es.upm.tfm_sbs.service.edu.service.CourseCollectService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseCollectServiceImpl extends ServiceImpl<CourseCollectMapper, CourseCollect> implements CourseCollectService {
    @Override
    public boolean isCollect(String courseId, String memberId) {
        QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", courseId).eq("member_id", memberId);
        Integer count = baseMapper.selectCount(queryWrapper);
        return count > 0;
    }

    @Override
    public void saveCourseCollect(String courseId, String memberId) {
        //未收藏则收藏
        if(!this.isCollect(courseId, memberId)) {
            CourseCollect courseCollect = new CourseCollect();
            courseCollect.setCourseId(courseId);
            courseCollect.setMemberId(memberId);
            this.save(courseCollect);
        }
    }
    @Override
    public List<CourseCollectVo> selectListByMemberId(String memberId) {
        return baseMapper.selectPageByMemberId(memberId);
    }

    @Override
    public boolean removeCourseCollect(String courseId, String memberId) {
        //已收藏则删除
        if(this.isCollect(courseId, memberId)) {
            QueryWrapper<CourseCollect> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("course_id", courseId).eq("member_id", memberId);
            return this.remove(queryWrapper);
        }
        return false;
    }
}
