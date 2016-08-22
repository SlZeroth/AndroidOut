package com.morning.morning.dungeon.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.morning.morning.dungeon.R;

import java.util.List;

/**
 * Created by juchan on 2015. 11. 22..
 */
public class ShareDialogFragment extends DialogFragment {

    final String[] appName = {"페이스북","카카오톡"};

    static final String FACEBOOK = "facebook";
    static final String KAKAOTALK = "kaka";

    public static ShareDialogFragment newInstance(String appName, String content) {
        ShareDialogFragment frag = new ShareDialogFragment();
        Bundle args = new Bundle();
        args.putString("appname", appName);
        args.putString("content", content);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle("공유하기")
                .setItems(appName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(appName[which].equals("페이스북")) {
                            initShareIntent(FACEBOOK);
                        }
                        else if(appName[which].equals("카카오톡")) {
                            initShareIntent(KAKAOTALK);
                        }
                    }
                }).create();
    }

    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/jpeg");

        List<ResolveInfo> resInfo = getActivity().getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type) ) {
                    share.putExtra(Intent.EXTRA_SUBJECT,  "subject");
                    share.putExtra(Intent.EXTRA_TEXT,     getArguments().getString("content"));
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return;

            startActivity(Intent.createChooser(share, "Select"));
        }
    }
}
