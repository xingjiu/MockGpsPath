package ledongli.cn.mockgpspath.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by wangyida on 15-5-28.
 */
public abstract class BaseFragment extends Fragment {

    protected static boolean DEBUG_UMENG = true;

    protected static final String TAG = BaseFragment.class.getSimpleName();

    private boolean mVisibleForUser = false;
    private boolean mIsDestroyed = false;

    public void onVisibleChanged() {
    }

    public void setVisibleForUser(boolean visibleForUser) {
        mVisibleForUser = visibleForUser;
    }

    public boolean isVisibleForUser() {
        return mVisibleForUser;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(getLayoutResId(), null);
        ButterKnife.inject(this, contentView);
        setupUI(contentView, savedInstanceState);
        return contentView;
    }

    abstract public int getLayoutResId();

    abstract public void setupUI(View view, Bundle bundle);

    abstract public void registerListeners();

    abstract public void unregisterListeners();

    public boolean onBackPressed() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        onVisibleChanged();
        registerListeners();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterListeners();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsDestroyed = true;
    }
}
