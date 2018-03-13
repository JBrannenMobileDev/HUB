package jjpartnership.hub.view_layer.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;


public class BackAwareAutofillMultiLineEditText extends android.support.v7.widget.AppCompatMultiAutoCompleteTextView {

    private BackPressedListener mOnImeBack;

    public BackAwareAutofillMultiLineEditText(Context context) {
        super(context);
    }

    public BackAwareAutofillMultiLineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BackAwareAutofillMultiLineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null) mOnImeBack.onImeBack(this);
        }
        return super.dispatchKeyEvent(event);
    }

    public void setBackPressedListener(BackPressedListener listener) {
        mOnImeBack = listener;
    }

    public interface BackPressedListener {
        void onImeBack(BackAwareAutofillMultiLineEditText editText);
    }
}