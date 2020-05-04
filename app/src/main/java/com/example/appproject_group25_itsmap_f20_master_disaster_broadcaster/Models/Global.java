package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models;
//https://eonet.sci.gsfc.nasa.gov/how-to-guide
//https://eonet.sci.gsfc.nasa.gov/docs/v3
public final class Global {

    private Global(){

    }
    //NASA API
    public static final String NASAEONET = "https://eonet.sci.gsfc.nasa.gov/api/v3/events?limit=20&status=open";
    public static final String NASAEONET_CLOSED = "https://eonet.sci.gsfc.nasa.gov/api/v3/events?limit=20&days=365?status=closed";
    public static final String NASAAPIKEY = "EOHsZNiaBak37Fs9s041f8o3Kgexf9LrARc4s13q";

    //Permissions
    public static final int REQUEST_CAM_AUDIO_STORAGE_MIC = 101;
    public static final int REQUEST_CAMERA_AUDIO_STORAGE = 102;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 103;
    public static final int REQUEST_FINE_LOCATION = 106;
    public static final int REQUEST_RECORD_AUDIO = 107;
}
