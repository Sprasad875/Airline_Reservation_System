package packgProject.Exceptions;
import java.io.*;

public class InvalidCredentialException extends Exception
{
    public InvalidCredentialException()
    {
        super("Invalid User Credentials!\n");
    }
}