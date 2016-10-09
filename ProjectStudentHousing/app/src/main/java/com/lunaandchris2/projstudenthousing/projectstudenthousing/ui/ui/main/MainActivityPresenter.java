package com.lunaandchris2.projstudenthousing.projectstudenthousing.ui.ui.main;

/**
 * Created by CFitzpatrick on 10/9/16.
 */

public class MainActivityPresenter {

    private MainActivityView view;

    public MainActivityPresenter(MainActivityView View){
        this.view = View;
    }

    public void onCreate(){

    }

    public void onDestroy(){
        view = null;
    }

}
