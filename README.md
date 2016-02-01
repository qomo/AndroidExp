#Android 例程库 

> 专门用于收集有用的Android功能代码例程

## socketServerExample 

### 一个socket服务器例程 

接受来自client的信息，并返回相应信息 

### runOnUiThread 

在service class中刷新UI界面  
<pre><code>
socketServerExample.this.runOnUiThread(new Runnable() {
    @Override
    public void run() {
        msg.setText(message);
    }
});
</code></pre>

### MultiDo&Multibarometer ==>> MultiThings

这是一个想法，希望通过广播（多播）的方式给多个Android终端发送相同的命令，让多个终端执行同样的动作。

MultiDo既是这个想法的命令发送者，它的原型是一个UDP多播发送者——UdpSendExample

MultiBarometer既是这个想法的命令接收者，它的原型是一个UDP多播接收端——UdpReceiveExample


