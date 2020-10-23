// package org.jeecg.modules.demo.test.controller;
//
// import lombok.extern.slf4j.Slf4j;
// import org.jeecg.modules.demo.test.entity.MpHlpDemoEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// /**
//  * @author dafei
//  * @version 0.1
//  * @date 2020/4/21 21:59
//  */
// @RestController
// @RequestMapping("/test/mphlp")
// @Slf4j
// public class MphlpController {
//
//
//     @PostMapping("/post")
//     public MpHlpDemoEntity post(@RequestBody MpHlpDemoEntity params) {
//         System.out.println(params);
//
//         params.setUname("张三,李四  , 王五,");
//         return params;
//     }
//
// }
