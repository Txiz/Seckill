package com.txiz.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.txiz.seckill.entity.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Txiz
 * @since 2021 -06-22
 */
public interface UserService extends IService<User> {

    /**
     * Gets verify hash.
     *
     * @param sid the sid
     * @param uid the uid
     * @return the verify hash
     */
    String getVerifyHash(Integer sid, Integer uid);

    /**
     * Add user count int.
     *
     * @param uid the uid
     * @return the int
     */
    int addUserCount(Integer uid);

    /**
     * Check user count boolean.
     *
     * @param uid the uid
     * @return the boolean
     */
    boolean checkUserCount(Integer uid);
}
