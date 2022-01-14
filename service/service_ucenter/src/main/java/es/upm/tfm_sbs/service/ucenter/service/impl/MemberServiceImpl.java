package es.upm.tfm_sbs.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import es.upm.tfm_sbs.common.base.result.ResultCode;
import es.upm.tfm_sbs.common.base.util.FormUtils;
import es.upm.tfm_sbs.common.base.util.MD5;
import es.upm.tfm_sbs.service.base.exception.SbsException;
import es.upm.tfm_sbs.service.ucenter.entity.Member;
import es.upm.tfm_sbs.service.ucenter.entity.vo.RegisterVo;
import es.upm.tfm_sbs.service.ucenter.mapper.MemberMapper;
import es.upm.tfm_sbs.service.ucenter.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    private final RedisTemplate redisTemplate;

    @Autowired
    public MemberServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterVo registerVo) {

        String nickname = registerVo.getNickname();
        String mobile = registerVo.getMobile();
        String password = registerVo.getPassword();

        //校验参数
        if (StringUtils.isEmpty(mobile)
                || !FormUtils.isMobile(mobile)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(nickname)) {
            throw new SbsException(ResultCode.PARAM_ERROR);
        }

        //校验验证码
//        String checkCode = (String)redisTemplate.opsForValue().get(mobile);
//        if(!code.equals(checkCode)){
//            throw new SbsException(ResultCode.CODE_ERROR);
//        }

        //是否被注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", mobile);
        Integer count = baseMapper.selectCount(queryWrapper);
        if(count > 0){
            throw new SbsException(ResultCode.REGISTER_MOBLE_ERROR);
        }

        //注册
        Member member = new Member();
        member.setNickname(nickname);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setDisabled(false);
        member.setAvatar("https://sbs-file.oss-cn-qingdao.aliyuncs.com/avatar/2022/default/pig.jpg");
        baseMapper.insert(member);
    }
}
