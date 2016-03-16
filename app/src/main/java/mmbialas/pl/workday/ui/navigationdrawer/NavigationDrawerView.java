package mmbialas.pl.workday.ui.navigationdrawer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import mmbialas.pl.workday.R;
import mmbialas.pl.workday.data.model.NavigationDrawerItem;
import mmbialas.pl.workday.ui.misc.BetterViewAnimator;

/**
 * Created by Michal Bialas on 19/07/14.
 */
public class NavigationDrawerView extends BetterViewAnimator {

    @InjectView(R.id.leftDrawerListView)
    ListView leftDrawerListView;

    private final NavigationDrawerAdapter adapter;


    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new NavigationDrawerAdapter(context);
    }

    public void replaceWith(List<NavigationDrawerItem> items) {
        adapter.replaceWith(items);
        setDisplayedChildId(R.id.leftDrawerListView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.inject(this);
        leftDrawerListView.setAdapter(adapter);
    }

    public NavigationDrawerAdapter getAdapter() {
        return adapter;
    }
}
