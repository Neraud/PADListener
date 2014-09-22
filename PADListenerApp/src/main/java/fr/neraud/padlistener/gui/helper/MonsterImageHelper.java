package fr.neraud.padlistener.gui.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;

import fr.neraud.padlistener.R;
import fr.neraud.padlistener.provider.descriptor.MonsterInfoDescriptor;

/**
 * Helper to retrieve a monster image
 *
 * Created by Neraud on 22/09/2014.
 */
public class MonsterImageHelper {

	private final Context context;

	public MonsterImageHelper(Context context) {
		this.context = context;
	}

	/**
	 * Fill the imageView with the monster image
	 * @param imageView the ImageView
	 * @param monsterIdJp the monsterId (in JP)
	 */
	public void fillMonsterImage(ImageView imageView, Integer monsterIdJp) {
		fillMonsterImage(imageView, monsterIdJp, false);
	}

	/**
	 * Fill the imageView with the monster image
	 * @param imageView the ImageView
	 * @param monsterIdJp the monsterId (in JP)
	 * @param inBlackAndWhite if true, will display the image in black and white
	 */
	public void fillMonsterImage(ImageView imageView, Integer monsterIdJp, boolean inBlackAndWhite) {
		if (monsterIdJp != null && monsterIdJp > 0) {
			try {
				final InputStream is = context.getContentResolver().openInputStream(MonsterInfoDescriptor.UriHelper.uriForImage(monsterIdJp));
				final BitmapDrawable bm = new BitmapDrawable(null, is);

				if(inBlackAndWhite) {
					imageView.setImageBitmap(convertColorIntoBlackAndWhiteImage(bm.getBitmap()));
				} else {
					imageView.setImageDrawable(bm);
				}
			} catch (final FileNotFoundException e) {
				imageView.setImageResource(R.drawable.no_monster_image);
			}
		} else {
			imageView.setImageResource(R.drawable.no_monster_image);
		}
	}

	private Bitmap convertColorIntoBlackAndWhiteImage(Bitmap orginalBitmap) {
		ColorMatrix colorMatrix = new ColorMatrix();
		colorMatrix.setSaturation(0);

		ColorMatrixColorFilter colorMatrixFilter = new ColorMatrixColorFilter(
				colorMatrix);

		Bitmap blackAndWhiteBitmap = orginalBitmap.copy(Bitmap.Config.ARGB_8888, true);

		Paint paint = new Paint();
		paint.setColorFilter(colorMatrixFilter);

		Canvas canvas = new Canvas(blackAndWhiteBitmap);
		canvas.drawBitmap(blackAndWhiteBitmap, 0, 0, paint);

		return blackAndWhiteBitmap;
	}
}
