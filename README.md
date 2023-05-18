# chatRob-api
chatGPT 自动回答机器人

四个模块

application包含job任务，按照设定自动查询问题并执行回答程序。

domain包括两个子服务，一个是对接ChatGPT API的任务，一个是对接知识星球的任务，两个任务的model类和service接口独立，互不影响。

interfaces是程序的启动类，还包括配置和测试类。

此项目只是学习简单的对接ChatGPT API使用，需要配合知识星球（并不免费，免费开通需要邀请码），触类旁通，公众号平台或者自己写个前端也是可以使用的。

