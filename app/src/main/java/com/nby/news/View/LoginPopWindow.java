package com.nby.news.View;

import android.content.Context;
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


import com.nby.news.Bean.UserBean;
import com.nby.news.R;

public class LoginPopWindow extends PopupWindow{

    private View mView;
    private Context mContext;
    private FrameLayout frameLayout;
    //private Handler mHandler;
    private TextView loginText;
    private TextView registerText;
    private ImageView qq_img;
    private ImageView wx_img;
    private ImageView phoneLoginImg;
    private EditText PassWordText, userIDText;
    private Button loginButton;
    private String passWord = "";
    private String userID = "";
    private ILoginStatusListener loginStatusListener;

    public interface ILoginStatusListener{
        void onSuccess(UserBean userBean);
        void onFail(String errorString);
    }


    public LoginPopWindow(final Context context , ILoginStatusListener iLoginStatusListener) {
        super(context);
        mContext = context;
        loginStatusListener = iLoginStatusListener;

        //初始化布局
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        mView = inflater.inflate(R.layout.pop_login_layout,null);
        loginText = mView.findViewById(R.id.login_popwindow);
        registerText = mView.findViewById(R.id.zhuce_popwindow);
        frameLayout = mView.findViewById(R.id.pop_frame);
        frameLayout.removeAllViews();
        View selectView = inflater.inflate(R.layout.view_login_select,null);
        frameLayout.addView(selectView);
        qq_img = selectView.findViewById(R.id.qq_img);
        wx_img = selectView.findViewById(R.id.wx_img);
        phoneLoginImg = selectView.findViewById(R.id.phone_img);
        registerText.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                frameLayout.removeAllViews();
                Toast.makeText(context, "注册无服务中！", Toast.LENGTH_SHORT).show( );
            }
        });
        qq_img.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "第三方登录正在授权检验中", Toast.LENGTH_SHORT).show( );
            }
        });
        wx_img.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "尚未开发", Toast.LENGTH_SHORT).show( );
            }
        });
        phoneLoginImg.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                frameLayout.removeAllViews();
                View view = LayoutInflater.from(mContext).inflate(R.layout.view_login_phone,null);
                frameLayout.addView(view);
                PassWordText = view.findViewById(R.id.mima_edit);
                userIDText = view.findViewById(R.id.user_edit);
                loginButton = view.findViewById(R.id.login_btn);
                loginButton.setOnClickListener(new View.OnClickListener( ) {
                    @Override
                    public void onClick(View v) {
                       login();
                    }
                });
            }
        });
        init_popWindowSettings();
    }

    public void login(){
        passWord = PassWordText.getText().toString();
        userID = userIDText.getText().toString();
        if(passWord.equals("") || userID.equals("")){
            loginStatusListener.onFail("用户名和密码不能为空！");
            Toast.makeText(mContext, "用户名和密码不能为空！", Toast.LENGTH_SHORT).show( );
        }else if(passWord.equals("123456")&& userID.equals("123456")){
            Toast.makeText(mContext, "登陆成功！", Toast.LENGTH_SHORT).show( );
            dismiss();
            loginStatusListener.onSuccess(new UserBean("诺冰羽"));
        }else{
            loginStatusListener.onFail("密码和用户名不匹配！");
            Toast.makeText(mContext, "密码和用户名不匹配！", Toast.LENGTH_SHORT).show( );
        }
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

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
    }
}
