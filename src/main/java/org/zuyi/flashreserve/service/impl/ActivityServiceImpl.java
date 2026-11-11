package org.zuyi.flashreserve.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zuyi.flashreserve.VO.ActivityVO;
import org.zuyi.flashreserve.VO.SessionVO;
import org.zuyi.flashreserve.VO.TicketVO;
import org.zuyi.flashreserve.common.DTO.ActivityDTO;
import org.zuyi.flashreserve.common.constants.CodeConstants;
import org.zuyi.flashreserve.common.constants.MessageConstants;
import org.zuyi.flashreserve.common.entity.Activity;
import org.zuyi.flashreserve.common.entity.Session;
import org.zuyi.flashreserve.common.entity.Ticket;
import org.zuyi.flashreserve.common.exception.BussinessException;
import org.zuyi.flashreserve.common.result.Result;
import org.zuyi.flashreserve.common.status.ActivityStatus;
import org.zuyi.flashreserve.common.status.SessionStatus;
import org.zuyi.flashreserve.common.status.TicketStatus;
import org.zuyi.flashreserve.common.utils.UserContent;
import org.zuyi.flashreserve.mapper.ActivityMapper;
import org.zuyi.flashreserve.service.ActivityService;
import org.zuyi.flashreserve.service.SessionService;
import org.zuyi.flashreserve.service.TicketService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

    @Autowired
    private SessionService sessionService;
    @Autowired
    private TicketService ticketService;

    /**
     * 创建活动
     * @param activityDTO
     * @return
     */
    @Override
    public Result create(ActivityDTO activityDTO) {
        if(activityDTO==null){
            throw new BussinessException(CodeConstants.ACTIVITY_BLANK, MessageConstants.ACTIVITY_BLANK);
        }
        Activity activity = lambdaQuery().eq(Activity::getTitle, activityDTO.getTitle()).one();
        //TODO 完善重复标准
        if(activity!=null){
            throw new BussinessException(CodeConstants.ACTIVITY_DUP,MessageConstants.ACTIVITY_DUP);
        }

        //确定没有重复之后 开始构建
        BeanUtils.copyProperties(activityDTO,activity);
        LocalDateTime now = LocalDateTime.now();
        //活动状态 创建人id 创建时间 修改时间
        activity.setStatus(ActivityStatus.DRAFT.getCode());
        activity.setCreatedBy(UserContent.getUserId());
        activity.setCreateTime(now);
        activity.setUpdateTime(now);
        boolean save = save(activity);
        if(BooleanUtil.isFalse(save)){
            throw new BussinessException(CodeConstants.ACTIVITY_SAVE_ERR,MessageConstants.ACTIVITY_SAVE_ERR);
        }
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = BussinessException.class)
    //TODO管理员同时添加场次、修改票档、重复发布时仍会产生竞态。

    public Result publish(Long activityId) {
        Activity activity = lambdaQuery().eq(Activity::getId, activityId).one();
        if(activity == null ){
            throw new BussinessException(CodeConstants.ACTIVITY_ERR,MessageConstants.ACTIVITY_BLANK);
        }
        if(!ActivityStatus.canPublish(activity)){
            throw new BussinessException(CodeConstants.ACTIVITY_ERR,MessageConstants.ACTIVITY_STATUS_ERR);
        }
        if(!activity.getCreatedBy().equals(UserContent.getUserId())){
            throw new BussinessException(CodeConstants.ACTIVITY_ERR,MessageConstants.PERMISSION_DENYED);
        }
        List<Session> sessionList = sessionService.lambdaQuery().eq(Session::getActivityId, activity.getId()).list();
        if(sessionList.isEmpty()||sessionList==null){
            throw new BussinessException(CodeConstants.SESSION_ERR,MessageConstants.SESSION_BLANK);
        }
        for(Session session:sessionList){
            if(!SessionStatus.canPublish(session)){
                throw  new BussinessException(CodeConstants.SESSION_ERR,MessageConstants.SESSION_STATUS_ERR);
            }
        }
        List<List<Ticket>> TicketListAll = new  ArrayList<>();
        for(Session session:sessionList){
            List<Ticket> list = ticketService.lambdaQuery().eq(Ticket::getSessionId, session.getId()).list();
            if(list.isEmpty()||list==null){
                throw new BussinessException(CodeConstants.TICKET_ERR,MessageConstants.TICKET_BLANK);
            }
            for(Ticket ticket:list){
                if(!TicketStatus.canPublish(ticket)){
                    throw new BussinessException(CodeConstants.TICKET_ERR,MessageConstants.TICKET_STATUS_ERR);
                }
            }
            TicketListAll.add(list);
        }
        if(TicketListAll.isEmpty()){
            throw new BussinessException(CodeConstants.TICKET_ERR,MessageConstants.TICKET_BLANK);
        }
        LocalDateTime now = LocalDateTime.now();
        activity.setStatus(ActivityStatus.PUBLISHED.getCode());
        activity.setUpdateTime(now);
        for(Session session:sessionList){
            session.setStatus(SessionStatus.PUBLISHED.getCode());
            session.setUpdateTime(now);
        }
        for(List<Ticket> list:TicketListAll){
            for(Ticket ticket:list){
                ticket.setStatus(TicketStatus.PUBLISHED.getCode());
                ticket.setUpdateTime(now);
            }
        }
        if (updateById(activity)) {
            throw new BussinessException(CodeConstants.ACTIVITY_SAVE_ERR,MessageConstants.ACTIVITY_SAVE_ERR);
        }
        if (sessionService.updateBatchById(sessionList)) {
            throw  new BussinessException(CodeConstants.SESSION_ERR,MessageConstants.SESSION_SAVE_ERR);
        }
        for(List<Ticket> list:TicketListAll){
            if (ticketService.updateBatchById(list)) {
                throw new BussinessException(CodeConstants.TICKET_ERR,MessageConstants.TICKET_SAVE_ERR);
            }
        }

        return Result.success();
    }

    @Override
    public Result selectById(Long activityId) {
        Activity activity = lambdaQuery().eq(Activity::getId, activityId).one();
        if(activity!=null){
            throw new BussinessException(CodeConstants.ACTIVITY_ERR,MessageConstants.ACTIVITY_BLANK);
        }
        if(activity.getCreatedBy()!=UserContent.getUserId()){
            throw new BussinessException(CodeConstants.ACTIVITY_ERR,MessageConstants.PERMISSION_DENYED);
        }
        ArrayList<SessionVO> sessionVOS = sessionService.lambdaQuery()
                .eq(Session::getActivityId, activity.getId())
                .list()
                .stream()
                .map(session -> {
                    SessionVO sessionVO = new SessionVO();
                    BeanUtils.copyProperties(session, sessionVO);
                    return sessionVO;
                })
                .collect(Collectors.toCollection(ArrayList::new));
//        for(SessionVO session:SessionVOList){
//            ArrayList<TicketVO> ticketVOS = ticketService.lambdaQuery()
//                    .eq(Ticket::getSessionId, session.getId())
//                    .list()
//                    .stream().map(ticket -> {
//                        TicketVO ticketVO = new TicketVO();
//                        BeanUtils.copyProperties(ticket, ticketVO);
//                        return ticketVO;
//                    })
//                    .collect(Collectors.toCollection(ArrayList::new));
//            session.setTicketVOS(ticketVOS);
//        }
        List<Long> sessionIds = sessionVOS.stream()
                .map(SessionVO::getId)
                .toList();

        Map<Long, List<Ticket>> ticketsBySessionId;

        if (sessionIds.isEmpty()) {
            ticketsBySessionId = Collections.emptyMap();
        } else {
            ticketsBySessionId = ticketService.lambdaQuery()
                    .in(Ticket::getSessionId, sessionIds)
                    .list()
                    .stream()
                    .collect(Collectors.groupingBy(Ticket::getSessionId));
        }

        sessionVOS.forEach(sessionVO -> {
            List<TicketVO> ticketVOList = ticketsBySessionId
                    .getOrDefault(sessionVO.getId(), Collections.emptyList())
                    .stream()
                    .map(ticket -> {
                        TicketVO ticketVO = new TicketVO();
                        BeanUtils.copyProperties(ticket, ticketVO);
                        return ticketVO;
                    })
                    .collect(Collectors.toCollection(ArrayList::new));

            sessionVO.setTicketVOS(ticketVOList);
        });
        ActivityVO activityVO = new ActivityVO();
        BeanUtils.copyProperties(activity, activityVO);
        activityVO.setSessionVOS(sessionVOS);
        return Result.success(activityVO);
    }
}
