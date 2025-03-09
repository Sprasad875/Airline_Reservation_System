import packgProject.Traveller.Traveller;
import packgProject.Manager.Manager;
import javafx.application.*;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.event.*;
import javafx.geometry.*;
import java.io.*;
import java.util.Scanner;
import packgProject.Exceptions.InvalidCredentialException;


class ServerException extends Exception
{
    ServerException()
    {
        super("404 Not Found Error\n");
    }
}

class MultiThreadedTraveller implements Runnable
{
	Thread thrd;
	Traveller t1;
	MultiThreadedTraveller(Traveller t,String name)
	{
		t1=t;
		thrd=new Thread(this,name);
		thrd.start();
	}
	public void run()
	{
		try
		{		
			if (t1.CheckCredentials())
			{
				t1.MenuTraveller(thrd.getName());
			}
		}
		catch(InvalidCredentialException o)
		{
			System.out.println(o);
			System.out.println("Invalid Traveller Credentials!");
		}
	}   
}

public class ARSDemo extends Application 
{
	static boolean man_authenticated=false;
	static Manager m1=null;
	public static void main(String[] args)
	{
		Scanner sc=new Scanner(System.in);
		System.out.println("\nWelcome to Airline Reservation System!!\n");
		int ch=1;
		while(ch!=0)
		{	
			System.out.print("\nLog In As:\nTraveller-(1)\nManager-(2)\nGenerate Report-(3)\nExit-(0)\nEnter Choice: ");
			ch=sc.nextInt();
			switch(ch)
			{
				case 1:
					try
					{
						if (m1==null)
						{
							throw new ServerException();
						}
						Traveller t=new Traveller(m1);
						System.out.print("Enter the number of Travellers: ");
						int numTravel=sc.nextInt();
						MultiThreadedTraveller mtt[]=new MultiThreadedTraveller[numTravel];
						for (int i=0;i<numTravel;i++)
						{
							mtt[i]=new MultiThreadedTraveller(t,"Traveller"+(i+1));
						}
						for (int i=0;i<numTravel;i++)
						{
							mtt[i].thrd.join();
						}
						break;
					}
					catch (InterruptedException e)
					{
						System.out.print(e);
					}
					catch(ServerException o)
					{
						System.out.println(o);
						break;
					}
				case 2:
					try
					{
						m1=new Manager();
						launch(args);
						if (man_authenticated)
							m1.MenuManager();
						else
							throw new InvalidCredentialException();
						break;
					}
					catch(InvalidCredentialException o)
					{
						System.out.println(o);
						break;
					}
					catch(IllegalStateException e)
					{
						try
						{
							if(m1.CheckCredentials())
							{
								m1.MenuManager();
								break;
							}
						}
						catch(InvalidCredentialException o)
						{
							System.out.println(o);
							break;
						}
					}
				case 3:
					try
					{
						if (m1==null)
						{
							throw new ServerException();
						}					
						m1.GenerateReport();
					}
					catch(ServerException o)
					{
						System.out.println(o);
						break;
					}
				case 0:
						System.out.println("\nThank you for using our service!");
						break;
				default:
					System.out.println("\nInvalid Choice Entered!.Please Try Again");
			}
		}
		sc.close();
	}

	public void start(Stage ps) 
    {
        ps.setTitle("Manager Login");
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.setVgap(10);
        Scene sc = new Scene(gp, 400, 400);
        Label lblUser = new Label("Username: ");
        Label lblPassw = new Label("Password: ");
        TextField tfUser = new TextField();
        PasswordField pfPassw = new PasswordField();
        Button btnSignIn = new Button("Sign in");
        Label lblResponse = new Label();

        btnSignIn.setOnAction(new EventHandler<ActionEvent>() 
        {
            public void handle(ActionEvent o) 
            {
 
                if (tfUser.getText().equals("flight_manager") && pfPassw.getText().equals("airindia@123")) 
                {
                    lblResponse.setText("Welcome "+tfUser.getText());
                    man_authenticated = true;
                } 
                else 
                {
                    lblResponse.setText("Invalid Credentials!");
                    man_authenticated = false;
                }
            }
        });

        gp.add(lblUser, 0, 1);
        gp.add(tfUser, 1, 1);
        gp.add(lblPassw, 0, 2);
        gp.add(pfPassw, 1, 2);
        gp.add(lblResponse, 1, 3);
        gp.add(btnSignIn, 1, 4);
        ps.setScene(sc);
        ps.show(); 
    }
}
