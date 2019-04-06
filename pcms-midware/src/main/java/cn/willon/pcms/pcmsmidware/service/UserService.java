package cn.willon.pcms.pcmsmidware.service;

import cn.willon.pcms.pcmsmidware.service.rollback.UserServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserService
 *
 * @author Willon
 * @since 2019-04-06
 */
@Transactional(rollbackFor = UserServiceException.class)
@Service
public class UserService {


}
