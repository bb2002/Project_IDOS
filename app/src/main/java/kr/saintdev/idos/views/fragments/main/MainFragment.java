package kr.saintdev.idos.views.fragments.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import kr.saintdev.idos.R;
import kr.saintdev.idos.views.activitys.MainActivity;
import kr.saintdev.idos.views.fragments.SuperFragment;

public class MainFragment extends SuperFragment {
    MainActivity control = null;

    LinearLayout[] options = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmn_main_main, container, false);
        this.control = (MainActivity) getActivity();

        // 선택 가능한 옵션을 정의 한다.
        this.options = new LinearLayout[] {
                v.findViewById(R.id.main_btnlayout_record),
                v.findViewById(R.id.main_btnlayout_recordplay),
                v.findViewById(R.id.main_btnlayout_stt),
                v.findViewById(R.id.main_btnlayout_sttplay),
                v.findViewById(R.id.main_btnlayout_translate),
                v.findViewById(R.id.main_btnlayout_settings)
        };

        // Button click 에 대한 핸들러를 정의한다.
        OnButtonClickHandler handler = new OnButtonClickHandler();
        for(LinearLayout l : options) {
            l.setOnClickListener(handler);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    class OnButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
}
