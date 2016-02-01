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

