package com.chatrob.application.job;


import com.alibaba.fastjson.JSON;
import com.chatrob.domain.chatgptApi.IOpenAI;
import com.chatrob.domain.chatgptApi.service.OpenAI;
import com.chatrob.domain.zsxq.IZsxqApi;
import com.chatrob.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;
import com.chatrob.domain.zsxq.model.vo.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

/**
 * 任务体 扫描问题，并回答
 */
@EnableScheduling
@Configuration
public class ChatrobSchedule {
    private Logger logger = LoggerFactory.getLogger(ChatrobSchedule.class);

    @Value("${chatrot-api.groupId}")
    private String groupId;
    @Value("${chatrot-api.cookie}")
    private String cookie;

    @Value("${chatrot-api.openAiKey}")
    private String openAiKey;
    @Resource
    private IZsxqApi zsxqApi;
    @Resource
    private IOpenAI openAI;

    //https://cron.qqe2.com/
    // 通过这个生成器,您可以在线生成任务调度比如Quartz的Cron表达式,对Quartz Cron 表达式的可视化双向解析和生成.
    @Scheduled(cron="0 0/1 * * * ? ")//每分钟一次
    public void run() {
        try {
            //增加随机不回答，避免被检测
            if (new Random().nextBoolean()) {
                logger.info("{} 随机打烊中...");
                return;
            }
            //增加可回复时间
            GregorianCalendar calendar = new GregorianCalendar();
            int hour=calendar.get(Calendar.HOUR_OF_DAY);
            if(hour>22||hour<7){
                logger.info("休息时间，不作答");
                return;
            }
            // 1. 检索问题
            UnAnsweredQuestionsAggregates unAnsweredQuestionsAggregates = zsxqApi.queryUnAnsweredQuestionsTopicId(groupId, cookie);
            logger.info("{} 检索结果：{}", JSON.toJSONString(unAnsweredQuestionsAggregates));
            List<Topics> topics = unAnsweredQuestionsAggregates.getResp_data().getTopics();
            if (null == topics || topics.isEmpty()) {
                logger.info("{} 本次检索未查询到待会答问题");
                return;
            }

            // 2. AI 回答
            Topics topic = topics.get(topics.size() - 1);
            String answer = openAI.doChatGPT(openAiKey, topic.getQuestion().getText().trim());

            // 3. 问题回复 true表示只允许提问者查看
            boolean status = zsxqApi.answer(groupId, cookie, topic.getTopic_id(), answer, true);
            logger.info("{} 编号：{} 问题：{} 回答：{} 状态：{}", topic.getTopic_id(), topic.getQuestion().getText(), answer, status);
        } catch (Exception e) {
            logger.error("{} 自动回答问题异常", e);
        }
    }

}
