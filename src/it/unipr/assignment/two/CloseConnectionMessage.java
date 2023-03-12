package it.unipr.assignment.two;

import java.io.Serializable;

public class CloseConnectionMessage implements Serializable
{
    private boolean closeConnection;

    public CloseConnectionMessage(boolean closeConnection)
    {
        this.closeConnection = closeConnection;
    }

    public boolean getCloseConnection()
    {
        return this.closeConnection;
    }
}
