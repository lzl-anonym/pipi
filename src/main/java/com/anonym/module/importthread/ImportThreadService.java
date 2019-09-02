package com.anonym.module.importthread;

import com.anonym.common.domain.ResponseDTO;
import org.springframework.stereotype.Service;

/**
 * @author lizongliang
 * @date 2019-09-02 11:34
 */
@Service
public class ImportThreadService {


    /**
     * 线程处理
     */
    public void init() {
        new Thread(() -> System.out.println("慢慢处理吧！"));


    }


    /**
     * controller调用
     *
     * @return
     */
    public synchronized ResponseDTO<String> handleData() {

        this.init();
        System.out.println("我先走，你慢慢处理！");

        return ResponseDTO.succ();
    }


}
