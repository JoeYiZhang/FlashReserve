package org.zuyi.flashreserve.service.impl;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zuyi.flashreserve.common.DTO.SessionDTO;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import org.zuyi.flashreserve.common.constants.MessageConstants;
import org.zuyi.flashreserve.common.entity.Activity;
import org.zuyi.flashreserve.common.entity.Session;
import org.zuyi.flashreserve.common.exception.BussinessException;
import org.zuyi.flashreserve.common.result.Result;
import org.zuyi.flashreserve.common.status.SessionStatus;
import org.zuyi.flashreserve.common.utils.UserContent;
import org.zuyi.flashreserve.mapper.SessionMapper;
import org.zuyi.flashreserve.service.ActivityService;
import org.zuyi.flashreserve.service.SessionService;

import java.time.LocalDateTime;

@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, Session> implements SessionService {
    @Autowired
    private ActivityService activityService;
    /**
     * 创建场次
     * @param sessionDTO
     * @return
     */
    @Override
    public Result create(SessionDTO sessionDTO) {
        Activity activity = activityService.lambdaQuery()
                .eq(Activity::getId, sessionDTO.getActivityId())
                //.eq(Activity::getStatus, 1)
                .eq(Activity::getCreatedBy, UserContent.getUserId()).one();
        if(activity==null){
            throw new BussinessException(CodeConstants.SESSION_ERR, MessageConstants.SESSION_CREATE_ERR);
        }

        LocalDateTime now = LocalDateTime.now();

        if(sessionDTO.getEndTime().isBefore(sessionDTO.getStartTime())){
            throw new BussinessException(CodeConstants.SESSION_ERR,MessageConstants.SESSION_TIME_ERR);
        }
        if(sessionDTO.getSaleEndTime().isBefore(sessionDTO.getSaleStartTime())){
            throw new BussinessException(CodeConstants.SESSION_ERR,MessageConstants.SESSION_TIME_ERR);
        }
        if(sessionDTO.getStartTime().isAfter(sessionDTO.getSaleEndTime())){
            throw new BussinessException(CodeConstants.SESSION_ERR,MessageConstants.SESSION_TIME_ERR);
        }
        Session session = new Session();
        BeanUtils.copyProperties(sessionDTO,session);
        session.setStatus(SessionStatus.DRAFT.getCode());
        session.setCreateTime(now);
        session.setUpdateTime(now);
        if (!save(session)) {
            throw new BussinessException(CodeConstants.SESSION_ERR, MessageConstants.SESSION_CREATE_ERR);
        }
        return Result.success();
    }


}
