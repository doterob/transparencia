    package com.doterob.transparencia.connector.geocoding;

    /**
     * Created by dotero on 13/05/2016.
     */
    public class GoogleGeocodingResponse {

        public String status ;
        public results[] results ;
        public GoogleGeocodingResponse() {

        }

    public static class results{
        public String formatted_address ;
        public geometry geometry ;
        public String[] types;
        public address_component[] address_components;
        public Boolean partial_match;
        public String place_id;
    }

        public static class geometry{
        public bounds bounds;
        public String location_type ;
        public location location;
        public bounds viewport;
    }

        public static class bounds {

        public location northeast ;
        public location southwest ;
    }

        public static class location{
        public String lat ;
        public String lng ;
    }

        public static class address_component{
        public String long_name;
        public String short_name;
        public String[] types ;
    }
}
