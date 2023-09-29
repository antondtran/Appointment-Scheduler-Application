package com.example.appointmentscheduler.model;

public class Country {
    private int countryID;
    private String country;

    /**
     *
     * @param countryID sets the country ID with user input
     * @param country sets the country name with user input
     */
    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }


    /**
     *
     * @return method returns country ID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     *
     * @param countryID method sets the country ID with the value passed in as a parameter
     */

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    /**
     *
     * @return  method returns the country name
     */

    public String getCountry() {
        return country;
    }


    /**
     *
     * @param country method sets the country name with the value passed in as a parameter
     */
    public void setCountry(String country) {
        this.country = country;
    }
}
