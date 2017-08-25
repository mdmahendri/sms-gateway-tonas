package com.mahendri.permata2017;

/**
 * Created by mahendri on 1/27/2017.
 * Untuk mendefinisikan data about
 */

class About {
    private String mTitle;
    private String mDescription;

    About (String title, String description){
        mTitle = title;
        mDescription = description;
    }

    String getTitle(){
        return mTitle;
    }

    String getDescription(){
        return mDescription;
    }
}
