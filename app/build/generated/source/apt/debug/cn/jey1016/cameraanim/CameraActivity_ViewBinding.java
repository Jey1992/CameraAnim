// Generated code from Butter Knife. Do not modify!
package cn.jey1016.cameraanim;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import cn.jey1016.cameraanim.view.CameraControls;
import com.wonderkiln.camerakit.CameraView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CameraActivity_ViewBinding implements Unbinder {
  private CameraActivity target;

  private View view2131165260;

  private View view2131165253;

  private View view2131165225;

  @UiThread
  public CameraActivity_ViewBinding(CameraActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CameraActivity_ViewBinding(final CameraActivity target, View source) {
    this.target = target;

    View view;
    target.cameraView = Utils.findRequiredViewAsType(source, R.id.camera, "field 'cameraView'", CameraView.class);
    target.topbar = Utils.findRequiredViewAsType(source, R.id.title_bar_container_ll, "field 'topbar'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.flash_button, "field 'flashButton' and method 'onViewClicked'");
    target.flashButton = Utils.castView(view, R.id.flash_button, "field 'flashButton'", ImageView.class);
    view2131165260 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.cameraControls = Utils.findRequiredViewAsType(source, R.id.camera_controls, "field 'cameraControls'", CameraControls.class);
    target.blackCover = Utils.findRequiredView(source, R.id.blackCover, "field 'blackCover'");
    target.fl_cover_container = Utils.findRequiredViewAsType(source, R.id.fl_cover_container, "field 'fl_cover_container'", FrameLayout.class);
    target.fl_controls_container = Utils.findRequiredViewAsType(source, R.id.fl_controls_container, "field 'fl_controls_container'", FrameLayout.class);
    target.fl_camera = Utils.findRequiredViewAsType(source, R.id.fl_camera, "field 'fl_camera'", FrameLayout.class);
    target.coverImage = Utils.findRequiredViewAsType(source, R.id.coverImage, "field 'coverImage'", ImageView.class);
    target.coverImage1 = Utils.findRequiredViewAsType(source, R.id.coverImage1, "field 'coverImage1'", ImageView.class);
    target.coverImageContainer = Utils.findRequiredViewAsType(source, R.id.coverImageContainer, "field 'coverImageContainer'", LinearLayout.class);
    target.ivShareTarget = Utils.findRequiredViewAsType(source, R.id.iv_share_target, "field 'ivShareTarget'", ImageView.class);
    target.tvShareTarget = Utils.findRequiredViewAsType(source, R.id.tv_share_target, "field 'tvShareTarget'", TextView.class);
    target.tvShareTargetDay = Utils.findRequiredViewAsType(source, R.id.tv_share_target_day, "field 'tvShareTargetDay'", TextView.class);
    target.tvShareTargetInsistDay = Utils.findRequiredViewAsType(source, R.id.tv_share_target_insist_day, "field 'tvShareTargetInsistDay'", TextView.class);
    view = Utils.findRequiredView(source, R.id.facing_button, "method 'onViewClicked'");
    view2131165253 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.captureButton, "method 'onViewClicked'");
    view2131165225 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    CameraActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.cameraView = null;
    target.topbar = null;
    target.flashButton = null;
    target.cameraControls = null;
    target.blackCover = null;
    target.fl_cover_container = null;
    target.fl_controls_container = null;
    target.fl_camera = null;
    target.coverImage = null;
    target.coverImage1 = null;
    target.coverImageContainer = null;
    target.ivShareTarget = null;
    target.tvShareTarget = null;
    target.tvShareTargetDay = null;
    target.tvShareTargetInsistDay = null;

    view2131165260.setOnClickListener(null);
    view2131165260 = null;
    view2131165253.setOnClickListener(null);
    view2131165253 = null;
    view2131165225.setOnClickListener(null);
    view2131165225 = null;
  }
}
