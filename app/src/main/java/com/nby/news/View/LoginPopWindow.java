package com.nby.news.View;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.nby.news.Bean.User;
import com.nby.news.R;
import android.os.Handler;

public class LoginPopWindow extends PopupWindow{

    private View mView;
    private Context mContext;
    private FrameLayout frameLayout;
    private Handler mHandler;
    private TextView LoginText,ZhuCeText;


    public LoginPopWindow(final Context context ,Handler handler ) {
        super(context);
        mContext = context;
        mHandler = handler;

        //初始化布局
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        mView = inflater.inflate(R.layout.view_popwindow_layout,null);
        LoginText = mView.findViewById(R.id.login_popwindow);
        ZhuCeText = mView.findViewById(R.id.zhuce_popwindow);
        ZhuCeText.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "注册无服务中！", Toast.LENGTH_SHORT).show( );
            }
        });
        frameLayout = mView.findViewById(R.id.pop_frame);
        frameLayout.removeAllViews();
        View selectView = inflater.inflate(R.layout.view_login_select,null);
        frameLayout.addView(selectView);
        init_select(mView);
        init_popWindowSettings();
    }

    public void init_popWindowSettings(){
        setFocusable(true);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setAnimationStyle(R.style.PopupAnimation );
        setOutsideTouchable(false);
        setClippingEnabled(false);
        setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction( ) == MotionEvent.ACTION_UP
                        && event.getY( ) < mView.getTop( )) {
                    dismiss();
                }
                return true;
            }
        });
        setContentView(mView);
    }


    private TextView loginTextView;
    private TextView zhuCeTextView;
    private ImageView qq_img;
    private ImageView wx_img;
    private ImageView phone_img;
    public void init_select(View view){
        loginTextView = view.findViewById(R.id.login_popwindow);
        zhuCeTextView = view.findViewById(R.id.zhuce_popwindow);
        qq_img = view.findViewById(R.id.qq_img);
        qq_img.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "第三方登录正在授权检验中", Toast.LENGTH_SHORT).show( );
            }
        });
        wx_img = view.findViewById(R.id.wx_img);
        wx_img.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "尚未开发", Toast.LENGTH_SHORT).show( );
            }
        });
        phone_img = view.findViewById(R.id.phone_img);
        phone_img.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                frameLayout.removeAllViews();
                View view = LayoutInflater.from(mContext).inflate(R.layout.view_login,null);
                frameLayout.addView(view);
                init_loginView(view);
            }
        });
    }


    private EditText mimaEditText, userEditText;
    private Button loginButton;
    private String mimaStr = "";
    private String userStr = "";
    public void init_loginView(View view){
        mimaEditText = view.findViewById(R.id.mima_edit);
        userEditText = view.findViewById(R.id.user_edit);
        loginButton = view.findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                mimaStr = mimaEditText.getText().toString();
                userStr = userEditText.getText().toString();
                if(mimaStr.equals("") || userStr.equals("")){
                    Toast.makeText(mContext, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show( );
                }else if(mimaStr.equals("123456")&&userStr.equals("123456")){
                    Toast.makeText(mContext, "登陆成功！", Toast.LENGTH_SHORT).show( );
                    dismiss();
                    Message msg = Message.obtain();
                    msg.what = 12580;
                    msg.obj = new User("诺冰羽");
                    mHandler.sendMessage(msg);
                }else{
                    Toast.makeText(mContext, "密码和用户名不匹配！", Toast.LENGTH_SHORT).show( );
                }
            }
        });
    }


    public static void setBackgroundAlpha(float bgAlpha, Context mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
    }
}
