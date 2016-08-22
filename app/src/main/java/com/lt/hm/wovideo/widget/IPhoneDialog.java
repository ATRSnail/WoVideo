package com.lt.hm.wovideo.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.utils.NoDoubleClickListener;
import com.lt.hm.wovideo.utils.ScreenUtils;

/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/7/9
 */
public class IPhoneDialog extends Dialog {
    public IPhoneDialog(Context context) {
        super(context);
    }

    public IPhoneDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected IPhoneDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public static class Builder {
        private Context context;
        private String message;
        private String positiveButtonText;
        private String negativeButtonText;
        private View contentView;
        private OnClickListener positiveButtonClickListener;
        private OnClickListener negativeButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText,
                                         OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText,
                                         OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public IPhoneDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final IPhoneDialog dialog = new IPhoneDialog(context, R.style.dialog);
            View view = inflater.inflate(R.layout.iphone_dialog, null);
            int height = ScreenUtils.getScreenHeight(context);
            int width = ScreenUtils.getScreenWidth(context);
            dialog.addContentView(view, new ViewGroup.LayoutParams(width * 2 / 3, height / 5));
            ((TextView) view.findViewById(R.id.dialog_content)).setText(message);
            if (positiveButtonText != null) {
                Button confirm = (Button) view.findViewById(R.id.dialog_confirm);
                confirm.setText(positiveButtonText);
                view.findViewById(R.id.dialog_confirm).setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (positiveButtonClickListener != null)
                            positiveButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                    }
                });
            } else {
                view.findViewById(R.id.dialog_confirm).setVisibility(
                        View.GONE);
            }

            if (negativeButtonText != null) {
                Button negative = (Button) view.findViewById(R.id.dialg_cancel);
                negative.setText(negativeButtonText);
                view.findViewById(R.id.dialg_cancel).setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (negativeButtonClickListener != null)
                            negativeButtonClickListener.onClick(dialog,
                                    DialogInterface.BUTTON_POSITIVE);
                    }
                });
            } else {
                view.findViewById(R.id.dialg_cancel).setVisibility(
                        View.GONE);
            }
            // 点击外部 空白处，不消失
            dialog.setCanceledOnTouchOutside(false);
            return dialog;
        }
    }

}

