package com.chatrob.domain.chatgptApi;

import java.io.IOException;

/**
 * ChatGPT open ai 接口：https://beta.openai.com/account/api-keys
 */
public interface IOpenAI {

    String doChatGPT(String openAiKey, String question) throws IOException;

}
