package com.anonym.module.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.anonym.constant.CommonConst;
import com.anonym.module.admin.employee.login.EmployeeLoginTokenService;
import com.anonym.module.admin.employee.login.domain.LoginTokenDTO;
import com.anonym.module.websocket.domain.WebSocketHeartBeatDTO;
import com.anonym.module.websocket.domain.WebSocketReceiveMsgDTO;
import com.anonym.module.websocket.domain.WebSocketSendMsgDTO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(CommonConst.API_PREFIX_ADMIN + "/webSocket/{token}")
@Component
public class WebSocketAdminServer {

    /**
     * 解决WebSocket无法注入bean
     */
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketAdminServer.applicationContext = applicationContext;
    }

    private EmployeeLoginTokenService employeeTokenService;

    /**
     * 当前在线用户 employee,expireTime
     */
    private static ConcurrentHashMap<Long, Long> onLineUserMap = new ConcurrentHashMap<>();

    /**
     * 当前在线用户所对应的 socket session信息
     */
    private static ConcurrentHashMap<Long, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {

        // 根据 token 获取用户信息
        if (StringUtils.isBlank(token)) {
            session.getBasicRemote().sendText("密码错误，芝麻关门！");
            session.close();
            return;
        }
        employeeTokenService = applicationContext.getBean(EmployeeLoginTokenService.class);
        LoginTokenDTO tokenDTO = employeeTokenService.getEmployeeTokenInfo(token);
        if (null == tokenDTO) {
            session.getBasicRemote().sendText("密码错误，芝麻关门！");
            session.close();
            return;
        }

        sessionMap.put(tokenDTO.getId(), session);
        log.info("连接打开");
    }

    /**
     * 不做处理如果 前台可以监听到浏览器关闭 此处处理在线人数也可
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        log.info("连接关闭");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("socket error,{}", error);
        error.printStackTrace();
    }

    /**
     * 此方法接收 前台信息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (StringUtils.isEmpty(message)) {
            return;
        }
        WebSocketReceiveMsgDTO msgDTO = JSON.parseObject(message, new TypeReference<WebSocketReceiveMsgDTO>() {
        });
        if (WebSocketMsgTypeEnum.HEART_BEAT.equalsValue(msgDTO.getMessageType())) {
            this.heartBeatHandle(msgDTO.getJsonStr());
        }
    }

    /**
     * 更新用户过期时间
     *
     * @param json
     */
    private void heartBeatHandle(String json) {
        Long currentDate = System.currentTimeMillis();
        Long expireTime = currentDate + 5 * 1000;
        WebSocketHeartBeatDTO heartBeatDTO = JSON.parseObject(json, new TypeReference<WebSocketHeartBeatDTO>() {
        });
        Long employeeId = heartBeatDTO.getEmployeeId();
        onLineUserMap.put(employeeId, expireTime);
    }

    /**
     * 移除过期用户,如果用户超过5s未获取到心跳列表则清除在线用户信息
     */
    @Scheduled(cron = "0/5 * * * * ?")
    private void removeOnLineUser() {
        Long currentDate = System.currentTimeMillis();
        Iterator<Entry<Long, Long>> it = onLineUserMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<Long, Long> entry = it.next();
            Long employeeId = entry.getKey();
            Long value = entry.getValue();
            Long userExpireTime = value + 5 * 1000;
            if (currentDate > userExpireTime) {
                onLineUserMap.remove(employeeId);
                sessionMap.remove(employeeId);
            }
        }
    }

    /**
     * 向指定用户id 发送消息
     *
     * @param employeeId
     * @param msg
     */
    public static void sendMsg(Long employeeId, String msg) {
        Session session = sessionMap.get(employeeId);
        if (session == null) {
            // 不在线
            return;
        }
        try {
            session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            log.error("推送消息到{},发送错误{}", employeeId, e);
        }
    }

    /**
     * 向后管用户推送消息
     *
     * @param messageDTO
     */
    public static void sendMsg(WebSocketSendMsgDTO messageDTO) {
        WebSocketMsgTypeEnum msgTypeEnum = messageDTO.getMsgTypeEnum();
        String content = messageDTO.getContent();
        //系统通知 : 通知所有在线用户
        if (WebSocketMsgTypeEnum.SYS_NOTICE == msgTypeEnum) {
            Long fromUserId = messageDTO.getFromUserId();
            for (Entry<Long, Session> entry : sessionMap.entrySet()) {
                Session session = entry.getValue();
                Long userId = entry.getKey();
                try {
                    // 不向消息创建人推送消息
                    if (!Objects.equals(userId, fromUserId)) {
                        session.getBasicRemote().sendText(content);
                    }
                } catch (IOException e) {
                    log.error("推送消息到{},发送错误{}", userId, e);
                }
            }
            return;
        }

        // 发送站内信
        if (WebSocketMsgTypeEnum.SITE_MSG == msgTypeEnum) {
            sendMsg(messageDTO.getToUserId(), content);
        }
    }

    /**
     * 获取所有在线用户id
     *
     * @return
     */
    public static List<Long> getOnLineUserList() {
        return Lists.newArrayList(onLineUserMap.keySet());
    }

    /**
     * 获取当前在线用户数
     *
     * @return
     */
    public static Integer getOnLineUserCount() {
        return onLineUserMap.entrySet().size();
    }

}
