package pl.wroc.pwr.na.objects;

/**
 * Class to represent item in list of menu
 * 
 * @author horodysk
 */
public class ListItemObject {
    /**
     * 0 - LISTA WYDARZEŃ JSON 1 - LISTA WYDARZEŃ RSS 2 - POSTER 3 - PLAN
     * ZAJĘĆ
     */
    public int _type;

    /***/
    public String _title;

    /***/
    public int _miniature;

    /***/
    public String _url;

    /***/
    public ListItemObject(int type, String title, int miniature, String url) {
        super();
        _type = type;
        _title = title;
        _miniature = miniature;
        _url = url;
    }

}
