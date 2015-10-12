package ledongli.cn.mockgpspath.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.InjectView;
import ledongli.cn.mockgpspath.R;
import ledongli.cn.mockgpspath.common.GlobalConfig;
import ledongli.cn.mockgpspath.common.preference.PreferenceUtils;
import ledongli.cn.mockgpspath.controller.LocationsProvider;
import ledongli.cn.mockgpspath.event.LocationFetchEvent;
import ledongli.cn.mockgpspath.service.MockGpsService;
import ledongli.cn.mockgpspath.util.LogUtils;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "MainActivityFragment";

    @InjectView(R.id.editText_url)
    EditText mUrl1View;
    @InjectView(R.id.button_request)
    Button mBtnRequestLocations;
    @InjectView(R.id.textView_result)
    TextView mTxtViewResult;
    @InjectView(R.id.button_toggle)
    ToggleButton mBtnToggle;
    @InjectView(R.id.editText_speed)
    EditText mSpeedSetView;
    @InjectView(R.id.button_setspeed)
    Button mBtnSetSpeed;

    private static final String PREF_DRAFT = "pref_draft";

    Handler checkHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (MockGpsService.instance != null && MockGpsService.instance.isMocking()) {
                mBtnToggle.setChecked(true);
            } else {
                mBtnToggle.setChecked(false);
            }

            checkHandler.sendEmptyMessageDelayed(0, 1000);
        }
    };

    public MainActivityFragment() {
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_main;
    }

    @Override
    public void setupUI(View view, Bundle bundle) {

        mBtnRequestLocations.setOnClickListener(this);
        mBtnToggle.setOnClickListener(this);
        mBtnSetSpeed.setOnClickListener(this);

        mUrl1View.setText(PreferenceUtils.getPrefString(PREF_DRAFT, "http://enterprise.ledongli.cn/imgs/locations.json"));

        checkHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void registerListeners() {
        if (MockGpsService.instance != null && MockGpsService.instance.isMocking()) {
            mBtnToggle.setChecked(true);
        } else {
            mBtnToggle.setChecked(false);
        }
        mTxtViewResult.setText("");
        GlobalConfig.getBus().register(this);
    }

    @Override
    public void unregisterListeners() {
        GlobalConfig.getBus().unregister(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_request:
                loadLocations();
                break;
            case R.id.button_toggle:
                ToggleButton toggleButton = (ToggleButton)view;
                if (toggleButton.isChecked()) {
                    startService();
                } else {
                    stopService();
                }
                break;
            case R.id.button_setspeed:
                String speed = mSpeedSetView.getText().toString();
                try {
                    setSpeed(Double.parseDouble(speed));
                } catch (Exception e) {

                }

            default:
                break;
        }
    }

    private void loadLocations() {
        String url = mUrl1View.getText().toString();
        if (null == url || url.equals("")) {
            return;
        }
        PreferenceUtils.setPrefString(PREF_DRAFT, url);
        LocationsProvider.getInstance().getLocationFromUrl(url);
    }

    private void setSpeed(double speed) {
        if (MockGpsService.instance != null && MockGpsService.instance.isMocking()) {
            MockGpsService.instance.setSpeed(speed);
        }
    }

    private void startService() {
        Intent intent = new Intent(GlobalConfig.getAppContext(), MockGpsService.class);
        intent.setAction(MockGpsService.START_MOCK_CMD);
        GlobalConfig.getAppContext().startService(intent);

    }

    private void stopService() {
        Intent intent = new Intent(GlobalConfig.getAppContext(), MockGpsService.class);
        intent.setAction(MockGpsService.STOP_MOCK_CMD);
        GlobalConfig.getAppContext().startService(intent);
    }

    public void onEventMainThread(LocationFetchEvent event) {
        if (null == event.getmLocationList() || event.getmLocationList().size() <= 0) {
            mTxtViewResult.setText("获取失败");
        } else {
            mTxtViewResult.setText("获取成功");
            mTxtViewResult.setVisibility(View.VISIBLE);
        }

    }
}
