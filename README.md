# CatService
Android Service的使用


## 本地service(见代码`LocalServiceActivity`)
>本例演示了本地service的绑定和解绑(bind and unbind)，以及`Binder`的使用（client通过`Binder`访问server，server访问client通过`LocalBroadcast`）

>基本流程为：
```
绑定service  ----> 获取binder  ----->  通过binder发送消息给service
sendBroadcast(intent)  ----> onReceive
解绑service
```

## remote service(通过aidl通信，见代码`ServiceActivity`)
>把aidl文件放在`serverapi`包下就省去了copy，这个包下也放一些server向client开放的接口、常量


## IntentService的使用(见代码`IntentServiceActivity`)
>`Service`的`onStartCommand()`方法默认在主线程上运行，而`IntentService`继承自`Service`，内部通过`HandlerThread`来实现在子线程里完成耗时任务，
并且在完成工作后自动关闭服务。


>`IntentService`通过`startService()`方式启动service，并传入参数。
同时本例演示了server向client返回结果的三种方式：Messenger或ResultReceiver或BroadcastReceiver，见代码`MyIntentService`和`IntentServiceActivity`，当然也可以用`BroadcastReceiver`返回结果，比较简单。

## Messenger最常见用法(见代码`MessengerServiceActivity`)
>通过`Messenger`在client和server之间双向通信。同时Service使用`HandlerThread`来创建子线程，完成耗时任务。



## PULL REQUEST要求
- 类注释
    ```
    /**
     * $desc$
     * 
     * @author your name
     * Created at $date$ $time$
     */
    ```
    ```
    date = date()
    time = time()
    ```
- tab size = 4
- Indent = 4

## 参考
- http://blog.csdn.net/lmj623565791/article/details/47017485