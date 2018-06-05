package com.walmartlabs.ern.showcase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ErnShowcaseNavigationApi.ern.api.ErnNavigationApi;
import com.ErnShowcaseNavigationApi.ern.model.ErnRoute;
import com.ErnShowcaseNavigationApi.ern.model.NavBar;
import com.ernnavigation.ern.api.NavigateData;
import com.ernnavigation.ern.api.NavigationApi;
import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeRequestHandler;
import com.walmartlabs.electrode.reactnative.bridge.ElectrodeBridgeResponseListener;
import com.walmartlabs.electrode.reactnative.bridge.FailureMessage;
import com.walmartlabs.electrode.reactnative.bridge.None;
import com.walmartlabs.electrode.reactnative.bridge.helpers.Logger;
import com.walmartlabs.ern.container.ElectrodeMiniAppActivity;
import com.walmartlabs.ern.container.miniapps.MiniAppsConfig;
import com.walmartlabs.ern.showcase.fragment.ColorPickerMiniAppFragment;
import com.walmartlabs.ern.showcase.fragment.ElectrodeMiniAppFragment;
import com.walmartlabs.ern.showcase.fragment.GenericMiniAppFragment;
import com.walmartlabs.ern.showcase.fragment.InitialFragment;

import static com.walmartlabs.ern.showcase.apis.NavigationApiRequestHanlder.ERN_ROUTE;

public class MainActivity extends ElectrodeCoreActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ElectrodeMiniAppFragment.OnFragmentInteractionListener {


    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment initialFragment = InitialFragment.newInstance();
        switchToThisFragment(initialFragment);

        registerNavigationApi();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        ErnRoute route = new ErnRoute(intent.getBundleExtra(ERN_ROUTE));
        Fragment fragment;
        if (route.getPath().equals("colorpickerdemominiapp")) {
            fragment = ColorPickerMiniAppFragment.newInstance(route);
        } else {
            fragment = GenericMiniAppFragment.newInstance(route);
        }

        updateActionBarTitle(route);

        switchToThisFragment(fragment);
    }

    private void updateActionBarTitle(@NonNull ErnRoute route) {
        if(getSupportActionBar() != null && route.getNavBar() != null) {
            getSupportActionBar().setTitle(route.getNavBar().getTitle());
        }
    }

    private void switchToThisFragment(@NonNull final Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.main_content, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Implementation of the navigation API, this helps any MiniApp to easily navigate to other MiniApps and also pass an additional input data if necessary.
     */
    //FIXME: Update this code to use the new ErnNavigationApi. This would need the miniapps inside the container(MovieListMiniApp) tobe updated.
    private void registerNavigationApi() {
        NavigationApi.requests().registerNavigateRequestHandler(new ElectrodeBridgeRequestHandler<NavigateData, Boolean>() {
            @Override
            public void onRequest(@Nullable NavigateData navigateData, @NonNull ElectrodeBridgeResponseListener<Boolean> responseListener) {
                if (!MainActivity.this.isFinishing()) {
                    if (navigateData != null) {
                        Class activityClass = MiniAppsConfig.MINIAPP_ACTIVITIES.get(navigateData.getminiAppName());
                        if (activityClass != null) {
                            Bundle bundle = new Bundle();
                            bundle.putString("payload", navigateData.getinitialPayload());
                            Intent intent = new Intent(MainActivity.this, activityClass);
                            ElectrodeMiniAppActivity.addInitialProps(intent, bundle);
                            MainActivity.this.startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "No activity found to navigate for: " + navigateData.getminiAppName(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Log.e(TAG, "Not enough data provided to navigate");
                    }
                } else {
                    Log.i(TAG, "Activity is finishing, cannot get a valid activity context");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String miniAppName;
        String title;

        switch (id) {
            case R.id.nav_movies:
                miniAppName = "MovieListMiniApp";
                title = "Movie List";
                break;
            case R.id.nav_color_picker:
                miniAppName = "colorpickerdemominiapp";
                title = "Color Picker";
                break;
            case R.id.nav_view_builder:
                miniAppName = "NavDemoMiniApp";
                title = "Nav Demo";
                break;
            default:
                throw new IllegalStateException("Menu action not supported");
        }

        navigateTo(miniAppName, title);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigateTo(final String path, String title) {
        if (!TextUtils.isEmpty(path)) {
            NavBar navBar = new NavBar.Builder(title).build();
            ErnRoute ernRoute = new ErnRoute.Builder(path).navBar(navBar).build();
            ErnNavigationApi.requests().navigate(ernRoute, new ElectrodeBridgeResponseListener<None>() {
                @Override
                public void onSuccess(@Nullable None responseData) {
                    Toast.makeText(MainActivity.this, "Successfully navigated to " + path, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(@NonNull FailureMessage failureMessage) {
                    Toast.makeText(MainActivity.this, "Failed to navigate to " + path + ". Error:" + failureMessage.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Logger.w(TAG, "Unable to navigate. Empty path received for navigation.");
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        throw new IllegalStateException("TODO: handle fragment interactions");
    }
}
