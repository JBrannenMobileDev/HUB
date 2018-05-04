package jjpartnership.hub.view_layer.custom_views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import jjpartnership.hub.R;
import jjpartnership.hub.utils.DpUtil;
import jjpartnership.hub.utils.UserColorUtil;

/**
 * Created by Jonathan on 5/3/2018.
 */

public class GroupIcon extends LinearLayout {
    private ImageView topLeftIv;
    private ImageView topRightIv;
    private ImageView bottomLeftIv;
    private ImageView bottomRightIv;
    private TextView topLeftTv;
    private TextView topRightTv;
    private TextView bottomLeftTv;
    private TextView bottomRightTv;
    private LinearLayout topRowLayout;
    private LinearLayout bottomRowLayout;
    private FrameLayout topLeftFl;
    private FrameLayout topRightFl;
    private FrameLayout bottomLeftFl;
    private FrameLayout bottomRightFl;

    public GroupIcon(Context context) {
        super(context);
        initializeViews(context);
    }

    public GroupIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public GroupIcon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    public GroupIcon(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.group_icon, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topLeftIv = findViewById(R.id.top_left_image);
        topRightIv = findViewById(R.id.top_right_image);
        bottomLeftIv = findViewById(R.id.bottom_right_image);
        bottomRightIv = findViewById(R.id.bottom_right_image);
        topLeftTv = findViewById(R.id.top_left_tv);
        topRightTv = findViewById(R.id.top_right_tv);
        bottomLeftTv = findViewById(R.id.bottom_left_tv);
        bottomRightTv = findViewById(R.id.bottom_right_tv);
        topRowLayout = findViewById(R.id.top_row_linear);
        bottomRowLayout = findViewById(R.id.bottom_row_linear);
        topLeftFl = findViewById(R.id.top_left_frame);
        topRightFl = findViewById(R.id.top_right_frame);
        bottomLeftFl = findViewById(R.id.bottom_left_frame);
        bottomRightFl = findViewById(R.id.bottom_right_frame);
    }

    public void initAllIcons(List<String> names, List<Integer> userColors){
        if(names.size() > 0 && userColors.size() > 0) {
            if (names.size() > 3) {
                setTopLeftTv(names.get(0));
                setTopRightTv(names.get(1));
                setBottomLeftTv(names.get(2));
                setBottomRightTv(names.get(3));
                setTopLeftIconColor(userColors.get(0));
                setTopRightIconColor(userColors.get(1));
                setBottomLeftIconColor(userColors.get(2));
                setBottomRightIconColor(userColors.get(3));
            } else {
                switch (names.size()) {
                    case 3:
                        topRightFl.setVisibility(View.GONE);
                        setTopLeftTv(names.get(0));
                        setBottomLeftTv(names.get(1));
                        setBottomRightTv(names.get(2));
                        setTopLeftIconColor(userColors.get(0));
                        setBottomLeftIconColor(userColors.get(1));
                        setBottomLeftIconColor(userColors.get(2));
                        break;
                    case 2:
                        bottomRowLayout.setVisibility(View.GONE);
                        setTopLeftTv(names.get(0));
                        setTopRightTv(names.get(1));
                        setTopLeftIconColor(userColors.get(0));
                        setTopRightIconColor(userColors.get(1));
                        break;
                    case 1:
                        bottomRowLayout.setVisibility(View.GONE);
                        topRightFl.setVisibility(View.GONE);
                        topLeftTv.setTextSize(18);
                        setTopLeftTv(names.get(0));
                        setTopLeftIconColor(userColors.get(0));
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) topLeftIv.getLayoutParams();
                        params.height = (int)DpUtil.pxFromDp(getContext(), 32);
                        params.width = (int)DpUtil.pxFromDp(getContext(), 32);
                        topLeftIv.setLayoutParams(params);
                        break;
                }
            }
        }
    }

    public void setTopLeftIconColor(int userColor){
        topLeftIv.setImageTintList(getContext().getResources().getColorStateList(UserColorUtil.getUserColor(userColor)));
    }
    public void setTopRightIconColor(int userColor){
        topRightIv.setImageTintList(getContext().getResources().getColorStateList(UserColorUtil.getUserColor(userColor)));
    }
    public void setBottomLeftIconColor(int userColor){
        bottomLeftIv.setImageTintList(getContext().getResources().getColorStateList(UserColorUtil.getUserColor(userColor)));
    }
    public void setBottomRightIconColor(int userColor){
        bottomRightIv.setImageTintList(getContext().getResources().getColorStateList(UserColorUtil.getUserColor(userColor)));
    }
    public void setTopLeftTv(String userName){
        topLeftTv.setText(String.valueOf(userName.charAt(0)));
    }
    public void setTopRightTv(String userName){
        topRightTv.setText(String.valueOf(userName.charAt(0)));
    }
    public void setBottomLeftTv(String userName){
        bottomLeftTv.setText(String.valueOf(userName.charAt(0)));
    }
    public void setBottomRightTv(String userName){
        bottomRightTv.setText(String.valueOf(userName.charAt(0)));
    }
}
