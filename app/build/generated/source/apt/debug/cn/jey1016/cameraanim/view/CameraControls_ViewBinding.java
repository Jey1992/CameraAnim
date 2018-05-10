// Generated code from Butter Knife. Do not modify!
package cn.jey1016.cameraanim.view;

import android.annotation.SuppressLint;
import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.jey1016.cameraanim.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CameraControls_ViewBinding implements Unbinder {
  private CameraControls target;

  private View view2131165306;

  private View view2131165225;

  private View view2131165226;

  private View view2131165314;

  private View view2131165216;

  @UiThread
  public CameraControls_ViewBinding(CameraControls target) {
    this(target, target);
  }

  @UiThread
  @SuppressLint("ClickableViewAccessibility")
  public CameraControls_ViewBinding(final CameraControls target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.picture_preview, "field 'picturePreview' and method 'onTouchBack'");
    target.picturePreview = Utils.castView(view, R.id.picture_preview, "field 'picturePreview'", ImageView.class);
    view2131165306 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTouchBack(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.captureButton, "field 'captureButton' and method 'onTouchCapture'");
    target.captureButton = Utils.castView(view, R.id.captureButton, "field 'captureButton'", ImageView.class);
    view2131165225 = view;
    view.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View p0, MotionEvent p1) {
        return target.onTouchCapture(p0, p1);
      }
    });
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progressBar, "field 'progressBar'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.captureSuc, "field 'captureSuc' and method 'onTouchBack'");
    target.captureSuc = Utils.castView(view, R.id.captureSuc, "field 'captureSuc'", ImageView.class);
    view2131165226 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTouchBack(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.recapture, "field 'recapture' and method 'onTouchBack'");
    target.recapture = Utils.castView(view, R.id.recapture, "field 'recapture'", TextView.class);
    view2131165314 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTouchBack(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.back, "method 'onTouchBack'");
    view2131165216 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onTouchBack(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    CameraControls target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.picturePreview = null;
    target.captureButton = null;
    target.progressBar = null;
    target.captureSuc = null;
    target.recapture = null;

    view2131165306.setOnClickListener(null);
    view2131165306 = null;
    view2131165225.setOnTouchListener(null);
    view2131165225 = null;
    view2131165226.setOnClickListener(null);
    view2131165226 = null;
    view2131165314.setOnClickListener(null);
    view2131165314 = null;
    view2131165216.setOnClickListener(null);
    view2131165216 = null;
  }
}
