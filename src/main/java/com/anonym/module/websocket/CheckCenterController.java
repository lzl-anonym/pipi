package com.anonym.module.websocket;

/**
 * @author lizongliang
 * @date 2019-09-09 10:32
 */

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CheckCenterController {

    /**
     * 页面请求
     *
     * @param cid
     * @return
     */
    @GetMapping("/checkcenter/socket/{cid}")
    public ModelAndView socket(@PathVariable String cid) {
        ModelAndView mav = new ModelAndView("/socket");
        mav.addObject("cid", cid);
        return mav;
    }
//    //推送数据接口
//    @ResponseBody
//    @GetMapping("/checkcenter/socket/push/{cid}")
//    public ApiReturnObject pushToWeb(@PathVariable String cid,String message) {
//        try {
//            WebSocketServer.sendInfo(message,cid);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ApiReturnUtil.error(cid+"#"+e.getMessage());
//        }
//        return ApiReturnUtil.success(cid);
//    }
}

