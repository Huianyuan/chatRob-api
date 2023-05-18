package com.chatrob.domain.zsxq;

import com.chatrob.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates;

import java.io.IOException;

/**
 * @author lhy
 * @description :知识星球 API 接口
 * @date 2023年05月18日 下午 05:10
 */
public interface IZsxqApi {
    /**
     * 获取未回答列表
     * @param groupId 列表id
     * @param cookie 个人cookie
     * @return com.chatrob.domain.zsxq.model.aggregates.UnAnsweredQuestionsAggregates
     */
    UnAnsweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String  groupId, String cookie) throws IOException;

    /**
     *
     * @param groupId 列表id
     * @param cookie 个人cookie
     * @param topicId 帖子id
     * @param text 回答文本
     * @param silenced 是否开启仅提问人可以看到
     * @return boolean
     */
    boolean answer(String groupId,String cookie,String topicId,String text,boolean silenced) throws IOException;
}
