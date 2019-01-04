package com.example.administrator.testwanandroid.Activity;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.administrator.testwanandroid.Activity.BaseActivity;
import com.example.administrator.testwanandroid.Fragment.BaseFragment;
import com.example.administrator.testwanandroid.Fragment.HomeFragment;
import com.example.administrator.testwanandroid.Fragment.KnowledgeFragment;
import com.example.administrator.testwanandroid.Fragment.NavFragment;
import com.example.administrator.testwanandroid.Fragment.ProjectFragment;
import com.example.administrator.testwanandroid.R;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FrameLayout frameLayout;
    private ArrayList<Fragment>fragments;
    private RelativeLayout home;
    private RelativeLayout knowledge;
    private RelativeLayout nav;
    private RelativeLayout project;
    public static final int ZERO=0;
    public static final int ONE=1;
    public static final int TWO=2;
    public static final int THREE=3;
    private int currentTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        home=findViewById(R.id.rl_home);
        knowledge=findViewById(R.id.rl_knowledge);
        nav=findViewById(R.id.rl_nav);
        project=findViewById(R.id.rl_project);

        home.setOnClickListener(this);
        knowledge.setOnClickListener(this);
        nav.setOnClickListener(this);
        project.setOnClickListener(this);

        fragments=new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new KnowledgeFragment());
        fragments.add(new NavFragment());
        fragments.add(new ProjectFragment());

        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_layout,fragments.get(ZERO));
        currentTab=ZERO;
        home.setSelected(true);
        ft.commit();



        drawerLayout = findViewById(R.id.mDrawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.opendrawer, R.string.closedrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        //设置选中item的字体颜色
        Resources resources = getResources();
        ColorStateList list = resources.getColorStateList(R.color.nav_item_color, null);
        navigationView.setItemTextColor(list);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.item_wanandroid) {
            Toast.makeText(this,"You clicked 玩Android ",Toast.LENGTH_SHORT).show();
        }else if (id==R.id.item_collect){
            Toast.makeText(this,"You clicked 收藏 ",Toast.LENGTH_SHORT).show();
        }else if (id==R.id.item_setting){
            Toast.makeText(this,"You clicked 设置 ",Toast.LENGTH_SHORT).show();
        }else if (id==R.id.item_exit){
            Toast.makeText(this,"You clicked 退出 ",Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_home:
                changeTab(ZERO);
                break;
            case R.id.rl_knowledge:
                changeTab(ONE);
                break;
            case R.id.rl_nav:
                changeTab(TWO);
                break;
            case R.id.rl_project:
                changeTab(THREE);
                break;
        }
    }


    //切换fragment
    private void changeTab(int Tab) {
       if (currentTab==Tab){
           return ;
       }
       Fragment fragment=fragments.get(Tab);
       FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
       if (!fragment.isAdded()){
           ft.add(R.id.frame_layout,fragment);
       }
       setTabStatus(currentTab,false);
       ft.replace(R.id.frame_layout,fragment);
       currentTab=Tab;
       setTabStatus(currentTab,true);
       ft.commit();

    }

    private void setTabStatus(int currentTab, boolean isSelected) {
        switch (currentTab){
            case ZERO:
                home.setSelected(isSelected);
                break;
            case ONE:
                knowledge.setSelected(isSelected);
                break;
            case TWO:
                nav.setSelected(isSelected);
                break;
            case THREE:
                project.setSelected(isSelected);
                break;
        }
    }
}
