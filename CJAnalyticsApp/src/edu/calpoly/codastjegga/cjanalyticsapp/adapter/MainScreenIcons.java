package edu.calpoly.codastjegga.cjanalyticsapp.adapter;

import edu.calpoly.codastjegga.cjanalyticsapp.R;

public enum MainScreenIcons {

  Dashboards(R.string.dashboards, R.drawable.dashboard_icon),
  Favorites(R.string.favorites, R.drawable.favorites_icon),
  Recents(R.string.recents, R.drawable.recents_icon);
  
  public final int name;
  public final int image;
  MainScreenIcons(int nameId, int iconId) {
          this.name = nameId;
          this.image = iconId;
  }
  
}
