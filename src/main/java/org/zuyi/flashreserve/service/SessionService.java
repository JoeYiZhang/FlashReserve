package org.zuyi.flashreserve.service;

import com.baomidou.mybatisplus.spring.service.IService;
import jakarta.validation.Valid;
import org.zuyi.flashreserve.common.DTO.SessionDTO;
import org.zuyi.flashreserve.common.entity.Session;
import org.zuyi.flashreserve.common.result.Result;

public interface SessionService extends IService<Session> {
    Result create(@Valid SessionDTO sessionDTO);
}
