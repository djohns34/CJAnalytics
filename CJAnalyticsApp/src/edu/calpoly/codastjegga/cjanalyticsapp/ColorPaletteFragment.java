package edu.calpoly.codastjegga.cjanalyticsapp;

import java.sql.Wrapper;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ColorPaletteFragment extends Fragment {
  public ColorPaletteFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    LinearLayout root = (LinearLayout) inflater.inflate(
        R.layout.fragment_color_palette, container, false);
    root.setOrientation(LinearLayout.VERTICAL);
    for (int palette : new int[] {/* R.array.blue_contrast,
        R.array.purple_contrast,*/ R.array.red_contrast /*, R.array.cold,
        R.array.warm */}) {
      LinearLayout view = (LinearLayout) inflater.inflate(
          R.layout.fragment_color_palette, root, false);
      for (String color : getResources().getStringArray(palette)) {
        ImageButton image = new ImageButton(getActivity());
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(0,
            50, 1);
        image.setLayoutParams(layout);
        image.setBackgroundColor(Color.parseColor(color));
        view.addView(image);
      }
      root.addView(view);
    }
    return root;
  }
}
