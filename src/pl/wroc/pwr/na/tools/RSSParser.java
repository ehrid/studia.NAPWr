package pl.wroc.pwr.na.tools;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.wroc.pwr.na.objects.AddressObject;
import pl.wroc.pwr.na.objects.EventObject;
import pl.wroc.pwr.na.objects.OrganizationObject;
import android.util.Log;

public class RSSParser {
	public Document getDomElement(String xml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (SAXException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		} catch (IOException e) {
			Log.e("Error: ", e.getMessage());
			return null;
		}
		// return DOM
		return doc;
	}

	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}

	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}
	
	static final String KEY_ITEM = "item";
	static final String KEY_ID = "napwr:id";
	static final String KEY_TAG = "category";
	static final String KEY_CONTENT = "napwr:tresc";
	static final String KEY_TITLE = "napwr:tytul";
	static final String KEY_LIKES = "slash:comments";
	static final String KEY_ORG = "napwr:organizacja";
	static final String KEY_PRICE = "napwr:cena";
	static final String KEY_DATE_START = "napwr:dataPoczatek";
	static final String KEY_DATE_END = "napwr:dataKoniec";
	static final String KEY_ENTER_START = "napwr:zapisyPoczatek";
	static final String KEY_ENTER_END = "napwr:zapisyKoniec";
	static final String KEY_ADDRESS = "napwr:adres";
	static final String KEY_FACULT = "napwr:wydzial";
	static final String KEY_POSTER_SMALL = "napwr:plakatMiniatura";
	static final String KEY_POSTER_BIG = "napwr:plakat";
	
	public String getXML(String URL) {
		try {
			return (String) new RequestTaskString().execute(URL).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public ArrayList<EventObject> getEventsRSS(String URL) {
		ArrayList<EventObject> eventList = new ArrayList<EventObject>();
		
		String xml = getXML(URL);
		Document doc = getDomElement(xml); // getting DOM element
		NodeList nl = doc.getElementsByTagName(KEY_ITEM);
		 
		// looping through all item nodes <item>
		for (int i = 0; i < nl.getLength(); i++) {
			eventList.add(getItem((Element) nl.item(i)));
		}
		
		return eventList;
	}
	
	private EventObject getItem(Element e) {
	    int wydarzenieId = Integer.parseInt(getValue(e, KEY_ID));
		String wydarzenieTytul = getValue(e, KEY_TITLE);
		String wydarzenieTresc = getValue(e, KEY_CONTENT);
		int wydarzenieSumaLajkow = Integer.parseInt(getValue(e, KEY_LIKES));
		String wydarzenieDataPoczatek = getValue(e, KEY_DATE_START);
		String wydarzenieDataKoniec = getValue(e, KEY_DATE_END);
		String linkToSmallPoster = getValue(e, KEY_POSTER_SMALL);
		String linkToBigPoster = getValue(e, KEY_POSTER_BIG);
		String nazwaOrganizacji = getValue(e, KEY_ORG);
		String adresWydarzenia = getValue(e, KEY_ADDRESS);
		AddressObject address = new AddressObject(adresWydarzenia);
		OrganizationObject organizacja = new OrganizationObject(nazwaOrganizacji);
	    
	    return new EventObject(
				wydarzenieTytul,
				wydarzenieId,
				wydarzenieTresc,
				"http://www.napwr.pl/" + linkToSmallPoster,
				"http://www.napwr.pl/" + linkToBigPoster, wydarzenieSumaLajkow, wydarzenieDataPoczatek, wydarzenieDataKoniec,
				organizacja, address);
	}
}
