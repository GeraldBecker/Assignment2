package com.example.assignment2;

/**
 * Created by Gerald on 03/30/2015.
 */
public class Profile {
    private long id;
    private String firstname;

    public long getId()
    {
        return (id);
    }

    public void setId(final long i)
    {
        id = i;
    }

    public String getFirstname()
    {
        return (firstname);
    }

    public void setProfile(final String c)
    {
        firstname = c;
    }

    // Will be used by the ArrayAdapter in the ListView
    // WHICH IS A HORRIBLE DESIGN!!!!!
    // toString should ONLY ever be used for debugging by developers!
    // Keep that in mind when you develop code!!!!!!!!!!!
    @Override
    public String toString()
    {
        return firstname;
    }
}
