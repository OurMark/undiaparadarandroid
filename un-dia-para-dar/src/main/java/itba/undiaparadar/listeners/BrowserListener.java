package itba.undiaparadar.listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class BrowserListener implements View.OnClickListener {
  private final Uri uri;
  private final Context context;

  public BrowserListener(final Context context, final String stringUri) {
    this.uri = Uri.parse(stringUri);
    this.context = context;
  }

  @Override
  public void onClick(final View v) {
    final Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
    context.startActivity(browserIntent);
  }
}