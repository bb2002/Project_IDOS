package kr.saintdev.idos.views.fragments.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.saintdev.idos.R;
import kr.saintdev.idos.views.activitys.MainActivity;
import kr.saintdev.idos.views.fragments.SuperFragment;

public class LoadingFragment extends SuperFragment {
    MainActivity control = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_main_loading, container, false);
        this.control = (MainActivity) getActivity();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 2초 뒤에 화면을 넘깁니다.
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                control.switchFragment(new MainFragment());
            }
        };
        handler.sendEmptyMessageDelayed(0, 2000);       // 2 초 뒤에 화면 전환
    }
}
