package gr.teilam.smartcity.parking;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import gr.teilam.smartcity.parking.R;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.Dialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import gr.teilam.smartcity.parking.adapter.ParkingArrayAdapter;
import gr.teilam.smartcity.parking.adapter.ParkingFavAdapter;
import gr.teilam.smartcity.parking.db.orm.ParkingResolver;
import gr.teilam.smartcity.parking.dialogs.LoadingDialog;
import gr.teilam.smartcity.parking.fragments.ParkingFavorFragment;
import gr.teilam.smartcity.parking.fragments.ParkingListFragment;
import gr.teilam.smartcity.parking.fragments.ParkingMapFragment;
import gr.teilam.smartcity.parking.fragments.irequests.IRequestHandler;
import gr.teilam.smartcity.parking.listeners.LocationChangeListener;
import gr.teilam.smartcity.parking.loaders.ParkingLoader;
import gr.teilam.smartcity.parking.model.Parking;
import gr.teilam.smartcity.parking.views.ParkingView;
import android.widget.Filter;

public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, OnSharedPreferenceChangeListener, LoaderCallbacks<Map<String, List<Parking>>> {
	
	private MenuItem myActionMenuItem;
	private EditText myActionEditText;
	private ParkingResolver resolver;
	private static final int PARKING_LOADER = 0;
	
	private static final int REQUEST_PREF_RESULT = 1;
	private static final String RADIUS = "Radius";
	private static final String SHOW_PARKING_NEAR_ME = "ShowParkingNearMe";
	  
	private String mStrRadius;
	private int mIntRadius;
	private boolean mbShowParkingNearMe;
	
	public static final String ALL = "ALL";
	public static final String FAVORITES = "FAVORITES";
	
	private LocationManager locationManager;
	private Location location;
	private String provider;
	
	private ParkingListFragment parkingListFragment = new ParkingListFragment();
	private ParkingMapFragment parkingMapFragment = new ParkingMapFragment();
	private ParkingFavorFragment parkingNearMeFragment = new ParkingFavorFragment();
	private Filter textFilter;
	private LocationChangeListener locationChangeListener;
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private final List<Parking> parkingList = new ArrayList<Parking>();
	private ParkingArrayAdapter parkingAdapter;
	
	private final List<Parking> parkingFavList = new ArrayList<Parking>();
	private ParkingFavAdapter parkingFavAdapter;
	
	LoadingDialog loadingDialog;
	
	//////////   implementation of IRequestHandler /////////////////////////
	private IRequestHandler rhParkingListFragment = new IRequestHandler(){
		@Override
		public ParkingArrayAdapter getParkingAdapter() {
			return parkingAdapter;
		}

		@Override
		public ParkingFavAdapter getFavParkingAdapter() {
			return parkingFavAdapter;
		}		
	};
	
	public IRequestHandler getRequestHandler(){
		return rhParkingListFragment;
	}
	/////////////////////////////////////////////////////////////////////////
	
	///////////////  RequestMapUpdateHandler  ///////////////////////////////
	public static interface RequestMapUpdateHandler{
		public void updateMap();
		public void updateParking(Parking k);
	}
	
	RequestMapUpdateHandler requestMapUpdateHandler;
	
	public void setRequestMapUpdateHandler(RequestMapUpdateHandler requestMapUpdateHandler){
		this.requestMapUpdateHandler = requestMapUpdateHandler;
	}
	
	public RequestMapUpdateHandler getRequestMapUpdateHandler(){
		return this.requestMapUpdateHandler;
	}
	//////////////////////////////////////////////////////////////////////////
	
	public List<Parking> getParkingList(){
		return parkingList;
	}
	
	public ParkingFavAdapter getParkingFavAdapter(){
		return parkingFavAdapter;
	}
	
	public synchronized void setLocation(Location location){
		this.location = location;
	}
	
	public synchronized Location getLocation(){
		return this.location;
	}
	
	public LocationChangeListener getLocationChangeListener(){
		return locationChangeListener;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	    
		resolver = new ParkingResolver(this);
		loadingDialog = new LoadingDialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		loadingDialog.show();
		loadPrefs();
		parkingAdapter = new ParkingArrayAdapter(this, 0, R.layout.view_parking, parkingList, this);
		textFilter = parkingAdapter.getFilter();
		parkingFavAdapter = new ParkingFavAdapter(this, 0, R.layout.view_parking, parkingFavList, this);
		
		// Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status != ConnectionResult.SUCCESS){ // Google Play Services are not available
        	 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        }else { // Google Play Services are available
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			provider = locationManager.getBestProvider(criteria, true);
			if(provider != null){
				location = locationManager.getLastKnownLocation(provider);
				
				locationChangeListener = new LocationChangeListener(this, null);
				locationManager.requestLocationUpdates(provider, 2500, Float.parseFloat("2.0"), locationChangeListener);
				setLocation(locationManager.getLastKnownLocation(provider));
			}
        }
		
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		parkingAdapter.setNotifyOnChange(true);
		parkingFavAdapter.setNotifyOnChange(true);
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		getLoaderManager().initLoader(PARKING_LOADER, null, this);
	}
	
	private void loadPrefs(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		mStrRadius = prefs.getString(RADIUS, "2000");
		mIntRadius = Integer.parseInt(mStrRadius);
		mbShowParkingNearMe = prefs.getBoolean(SHOW_PARKING_NEAR_ME, false);
	}
	 
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		switch (key) {
		case RADIUS:
			mStrRadius = prefs.getString(RADIUS, "2000");
			mIntRadius = Integer.parseInt(mStrRadius);
			break;
		case SHOW_PARKING_NEAR_ME:
			mbShowParkingNearMe = prefs.getBoolean(SHOW_PARKING_NEAR_ME, false);
			break;
		}
		if(locationChangeListener != null){
			locationChangeListener.onLocationChanged(location);
		}
	}
	
	@Override
	protected void onDestroy() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.unregisterOnSharedPreferenceChangeListener(this);
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_bar, menu);
	    	    	
	 // Here we get the action view we defined
	    myActionMenuItem = menu.findItem(R.id.Itm_SearchAction_Menu);
	    View actionView = myActionMenuItem.getActionView();
	    
	    // We then get the edit text view that is part of the action view
	    if(actionView != null) {
	    	myActionEditText = (EditText) actionView.findViewById(R.id.myActionEditText);
	    	if(myActionEditText != null) {
	    		myActionEditText.addTextChangedListener(new TextWatcher() {
	    			
	    			@Override
	    			public void onTextChanged(CharSequence s, int start, int before, int count) {
	    				String filterWord = myActionEditText.getText().toString();			
	    				textFilter.filter(filterWord);
	    			}
	    			
	    			@Override
	    			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	    				
	    			}
	    			
	    			@Override
	    			public void afterTextChanged(Editable s) {
	    				
	    			}
	    		});
	    	}
	    }
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
    	case R.id.action_settings:
    		Intent intentSetPref = new Intent(this, SetPreferenceActivity.class);
    	    startActivityForResult(intentSetPref, REQUEST_PREF_RESULT);
    	    return true;
    	case R.id.Itm_SearchAction_Menu:
            return true;
    	case R.id.action_infos:
    		Intent infoIntent = new Intent(MainActivity.this, InfoActivity.class);
    		startActivity(infoIntent);
    		return true;
           default:
        	   return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_PREF_RESULT){
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
			mStrRadius = sharedPreferences.getString(RADIUS, "0");
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(@SuppressWarnings("deprecation") ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(@SuppressWarnings("deprecation") ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch(position){
			case 0:
				fragment = parkingListFragment;
				break;
			case 1:
				fragment = parkingMapFragment;
				break;
			case 2:
				fragment = parkingNearMeFragment;
			}
			
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	public void favoriteButtonClicked(ParkingView parkingView){
		if(parkingView != null){
			parkingView.toggleCheckin();
			try {
				parkingFavAdapter.clear();
				Parking parking = parkingView.getParking();
				if(parking.isIscheckIn()) {
					parking.setCheckInCount(parking.getCheckInCount() + 1);
					resolver.updateParkingWithID(parking);
				}
				
				ArrayList<Parking> mostCheckInParkings = resolver.getMostCheckInParkingData();
				if(mostCheckInParkings != null && mostCheckInParkings.size() > 0) {
					parkingFavAdapter.addAll(mostCheckInParkings);
				}
				
				parkingFavAdapter.notifyDataSetChanged();
				parkingAdapter.notifyDataSetChanged();
				if(requestMapUpdateHandler != null){
					requestMapUpdateHandler.updateParking(parking);
				}													
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public double getRadius() {
		return mIntRadius;
	}

	public boolean getShowParkingNearMe() {
		return mbShowParkingNearMe;
	}

	public ParkingArrayAdapter getParkingAdapter() {
		return parkingAdapter;
	}
	
	@Override
	public Loader<Map<String, List<Parking>>> onCreateLoader(int id, Bundle args) {
		ParkingLoader parkingLoader = null;
		if(id == PARKING_LOADER){
			parkingLoader = new ParkingLoader(getApplicationContext());
		}
		return parkingLoader;
	}

	@Override
	public void onLoadFinished(Loader<Map<String, List<Parking>>> loader, Map<String, List<Parking>> data) {
		switch(loader.getId()){
		case PARKING_LOADER:
			parkingAdapter.clear();
			parkingAdapter.addAll(data.get(ALL));
			parkingAdapter.notifyDataSetChanged();
			
			parkingFavAdapter.clear();
			if(data.get(FAVORITES) != null)
				parkingFavAdapter.addAll(data.get(FAVORITES));
			parkingFavAdapter.notifyDataSetChanged();
			
			loadingDialog.dismiss();
			
			if(requestMapUpdateHandler != null)
				requestMapUpdateHandler.updateMap();
			break;
		}
	}

	@Override
	public void onLoaderReset(Loader<Map<String, List<Parking>>> loader) {
		switch(loader.getId()){
		case PARKING_LOADER:
			parkingAdapter.clear();
			parkingAdapter.notifyDataSetChanged();
			parkingFavAdapter.clear();
			parkingFavAdapter.notifyDataSetChanged();
			break;
		}
	}
}
