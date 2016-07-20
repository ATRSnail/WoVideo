package com.lt.hm.wovideo.video.player;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lt.hm.wovideo.R;

/**
 * Created by KECB on 7/20/16.
 */

public class BulletSendDialog extends DialogFragment{

    static BulletSendDialog mBulletSendDialog;

    static AVController.OnInterfaceInteract mInterfaceListener;

    /**
     */
    public static BulletSendDialog newInstance(AVController.OnInterfaceInteract interfaceInteract) {
        if (mBulletSendDialog == null) {
            mBulletSendDialog = new BulletSendDialog();
            mInterfaceListener = interfaceInteract;
        }
        return mBulletSendDialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Dialog_MinWidth);

    }

    public void onResume()
    {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.TOP);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_send_bullet, container, false);

        TextView send = (TextView) v.findViewById(R.id.send_bullet);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Send", Toast.LENGTH_SHORT).show();
                //TODO send the bullet screen
                mInterfaceListener.onSendBulletClick();
                //TODO reset eidt text

                //TODO close current dialog

            }
        });

        ImageView close = (ImageView) v.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBulletSendDialog.dismiss();
            }
        });

        return v;
    }
}
