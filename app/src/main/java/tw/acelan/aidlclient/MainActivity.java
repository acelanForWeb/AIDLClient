package tw.acelan.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import tw.acelan.aidlservice.IMyAidlInterface;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ServiceConnection {
    private IMyAidlInterface binder;
    private Button btn_bindService,btn_unbindService,btn_sendMsg;
    private Intent intent;

    //欲傳送至AIDLService應用程式的訊息內容
    private String msg = "Hi, 您好，這條訊息來自ClientExample應用程式";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //取得介面元件
        btn_bindService = (Button)findViewById(R.id.btn_bindService);
        btn_unbindService = (Button)findViewById(R.id.btn_unbindService);
        btn_sendMsg = (Button)findViewById(R.id.btn_sendMsg);

        //設定Intent物件
        intent = new Intent();
        intent.setComponent(new ComponentName("tw.acelan.aidlservice","tw.acelan.aidlservice.IMyAidlInterfaceImpl"));

        //設定監聽器
        btn_bindService.setOnClickListener(this);
        btn_unbindService.setOnClickListener(this);
        btn_sendMsg.setOnClickListener(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        Toast.makeText(this,"與Service取得連線",Toast.LENGTH_SHORT).show();
        binder = IMyAidlInterface.Stub.asInterface(iBinder);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Toast.makeText(this,"與Service遺失連線",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_bindService:{
                bindService(intent,this, Context.BIND_AUTO_CREATE);
                break;
            }
            case R.id.btn_unbindService:{
                unbindService(this);
                break;
            }
            case R.id.btn_sendMsg:{
                if(binder != null){
                    try{
                        binder.setData(this.msg.toString());
                        Toast.makeText(this,"訊息已發送",Toast.LENGTH_SHORT).show();
                    }catch(RemoteException e){
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }
}
