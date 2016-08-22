package com.pjcstudio.managementpoop.Fragment;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pjcstudio.managementpoop.R;
import com.pjcstudio.managementpoop.Utility.BitmapTool;

/**
 * Created by juchanpark on 2015. 9. 20..
 */
public class TutorialFragment extends Fragment {

    private Bundle arg;
    private ImageView imageView;
    private int imageId;

    @Override
    public void onDestroy() {
        imageView.setImageDrawable(null);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        arg = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_tu, container, false);

        imageView = (ImageView) rootView.findViewById(R.id.tuto);
        if(arg != null) {
            imageId = Integer.parseInt(arg.getString("imageid"));
            Drawable drawable = new BitmapDrawable(BitmapTool.resizeBitmap(getActivity(), imageId, 2));
            imageView.setImageDrawable(drawable);
        }
        return rootView;
    }
}
