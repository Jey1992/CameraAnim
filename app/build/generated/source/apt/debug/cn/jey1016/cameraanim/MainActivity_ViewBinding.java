// Generated code from Butter Knife. Do not modify!
package cn.jey1016.cameraanim;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view2131165273;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.iv_share_img, "field 'ivShareImg' and method 'onViewClicked'");
    target.ivShareImg = Utils.castView(view, R.id.iv_share_img, "field 'ivShareImg'", ImageView.class);
    view2131165273 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.mainContainer = Utils.findRequiredViewAsType(source, R.id.mainContainer, "field 'mainContainer'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivShareImg = null;
    target.mainContainer = null;

    view2131165273.setOnClickListener(null);
    view2131165273 = null;
  }
}
