package pl.wroc.pwr.na.fragments;

import pl.wroc.pwr.na.R;
import pl.wroc.pwr.na.activities.MenuActivity;
import pl.wroc.pwr.na.objects.EventObject;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Class manages creating view for single event
 * 
 * @author horodysk
 */
public class EventObjectFragment extends Fragment {
    /***/
    public static final String ARG_OBJECT = "object";

    private View _rootView;

    private MenuActivity _menuActivity = MenuActivity.activityMain;

    private EventObject _event;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @SuppressWarnings("unused") Bundle savedInstanceState) {
        _rootView = getRootView(inflater, container);
        TextView titleView = getTitleView();
        TextView fromDateView = getFromDateView();
        TextView toDateView = getToDateView();
        TextView addressView = getAddressView();
        TextView organizationView = getOrganizationView();
        TextView contentView = getContentView();

        setFontOf(titleView);

        _event = getSelectedEvent();
        setNameIn(titleView);
        setStartDateIn(fromDateView);
        setEndDateIn(toDateView);
        setAddressIn(addressView);
        serOrganizationIn(organizationView);
        setContentIn(contentView);

        return _rootView;
    }

    private View getRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    private TextView getTitleView() {
        return (TextView) _rootView.findViewById(R.id.event_title);
    }

    private TextView getFromDateView() {
        return (TextView) _rootView.findViewById(R.id.event_fromDate);
    }

    private TextView getToDateView() {
        return (TextView) _rootView.findViewById(R.id.event_toDate);
    }

    private TextView getAddressView() {
        return (TextView) _rootView.findViewById(R.id.event_address);
    }

    private TextView getOrganizationView() {
        return (TextView) _rootView.findViewById(R.id.event_organizer);
    }

    private TextView getContentView() {
        return (TextView) _rootView.findViewById(R.id.event_content);
    }

    private void setFontOf(TextView titleView) {
        Typeface fontType = _menuActivity.getTypeFace();
        titleView.setTypeface(fontType);
    }

    private EventObject getSelectedEvent() {
        Bundle args = getArguments();
        return _menuActivity.current.get(args.getInt(ARG_OBJECT));
    }

    private void setNameIn(TextView titleView) {
        if (_event._name != null)
            titleView.setText(_event._name.toString());
    }

    private void setStartDateIn(TextView fromDateView) {
        if (_event._startDate != null)
            fromDateView.setText("Od: " + _event._startDate.toString());
        else
            fromDateView.setVisibility(View.GONE);
    }

    private void setEndDateIn(TextView toDateView) {
        if (_event._endDate != null)
            toDateView.setText("Do: " + _event._endDate.toString());
        else
            toDateView.setVisibility(View.GONE);
    }

    private void setAddressIn(TextView addressView) {
        if (_event._address != null)
            addressView.setText("Adres: " + _event._address._address);
        else
            addressView.setVisibility(View.GONE);
    }

    private void serOrganizationIn(TextView organizaerView) {
        if (_event._organization != null)
            organizaerView.setText("Organizator: " + _event._organization._name);
        else
            organizaerView.setVisibility(View.GONE);
    }

    private void setContentIn(TextView contentView) {
        if (_event._content != null)
            contentView.setText(_event._content.toString());
    }
}
