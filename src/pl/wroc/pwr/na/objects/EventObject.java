package pl.wroc.pwr.na.objects;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import pl.wroc.pwr.na.tools.RequestTaskBitmap;
import pl.wroc.pwr.na.tools.UseInternalStorage;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Class to represent event
 * 
 * @author horodysk
 */
public class EventObject implements Serializable {
    private static final long serialVersionUID = 8758719650084506599L;

    /***/
    public CharSequence _name;

    /***/
    public int _id;

    /***/
    public CharSequence _content;

    /***/
    public OrganizationObject _organization;

    /***/
    public AddressObject _address;

    /***/
    public CharSequence _poster;

    /***/
    public CharSequence _bigPoster;

    /***/
    public int _likeSum;

    /***/
    public boolean _isLiked;

    /***/
    public String _startDate;

    /***/
    public String _endDate;

    BitmapDataObject _bdo_poster;

    /***/
    public EventObject(CharSequence name, int id, CharSequence content, CharSequence poster, CharSequence bigPoster, int likeSum, String startDate,
        String endDate, OrganizationObject organization, AddressObject address) {
        super();
        _name = name;
        _id = id;
        _content = content;
        _poster = poster;
        _bigPoster = bigPoster;
        _likeSum = likeSum;
        _startDate = startDate;
        _endDate = endDate;
        _organization = organization;
        _address = address;
    }

    /***/
    public Bitmap getImagePoster(Context context) {
        if (_bdo_poster != null)
            return BitmapFactory.decodeByteArray(_bdo_poster.imageByteArray, 0, _bdo_poster.imageByteArray.length);
        Bitmap poster = readPoster(context);
        if (poster == null)
            poster = invokeWritePoster(context);
        return poster;
    }

    /***/
    public void setImagePoster(Context context) {
        if (_bdo_poster == null && readPoster(context) == null)
            invokeWritePoster(context);
    }

    private Bitmap invokeWritePoster(Context context) {
        try {
            return writePoster(context);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap writePoster(Context context) throws InterruptedException, ExecutionException {
        Bitmap imagePoster = downloadPoster();
        BitmapDataObject bdo = compressPoster(imagePoster);
        writePosterToFile(context, bdo);
        _bdo_poster = bdo;
        Bitmap poster = processPoster(bdo);
        return poster;
    }

    private Bitmap processPoster(BitmapDataObject bdo) {
        return BitmapFactory.decodeByteArray(bdo.imageByteArray, 0, bdo.imageByteArray.length);
    }

    private Bitmap downloadPoster() throws InterruptedException, ExecutionException {
        String posterName = (String) _bigPoster;
        Bitmap imagePoster = new RequestTaskBitmap().execute(posterName).get();
        return imagePoster;
    }

    private BitmapDataObject compressPoster(Bitmap imagePoster) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imagePoster.compress(Bitmap.CompressFormat.PNG, 100, stream);
        BitmapDataObject bdo = new BitmapDataObject();
        bdo.imageByteArray = stream.toByteArray();
        return bdo;
    }

    private void writePosterToFile(Context context, BitmapDataObject bdo) {
        UseInternalStorage uis = new UseInternalStorage(context);
        uis.writeObject(bdo, "plakat_" + _id);
    }

    private Bitmap readPoster(Context context) {
        UseInternalStorage uis = new UseInternalStorage(context);
        BitmapDataObject bdo = (BitmapDataObject) uis.readObject("plakat_" + _id);

        if (bdo == null)
            return null;
        _bdo_poster = bdo;
        return processPoster(bdo);

    }

    protected class BitmapDataObject implements Serializable {
        private static final long serialVersionUID = 6022430163107957510L;

        public byte[] imageByteArray;
    }

}