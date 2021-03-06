// Generated by view binder compiler. Do not edit!
package com.qrcode.databinding;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.viewbinding.ViewBinding;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.qrcode.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRecordBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ListView list1;

  @NonNull
  public final TextView text;

  private ActivityRecordBinding(@NonNull LinearLayout rootView, @NonNull ListView list1,
      @NonNull TextView text) {
    this.rootView = rootView;
    this.list1 = list1;
    this.text = text;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRecordBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRecordBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_record, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRecordBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.list1;
      ListView list1 = rootView.findViewById(id);
      if (list1 == null) {
        break missingId;
      }

      id = R.id.text;
      TextView text = rootView.findViewById(id);
      if (text == null) {
        break missingId;
      }

      return new ActivityRecordBinding((LinearLayout) rootView, list1, text);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
