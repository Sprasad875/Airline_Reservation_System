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
import javafx.collections.*;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;

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
	String[] args;
	MultiThreadedTraveller(Traveller t,String name,String[] args)
	{
		t1=t;
		this.args=args;
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
							mtt[i]=new MultiThreadedTraveller(t,"Traveller"+(i+1),args);
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

	public void start(Stage ps) {
	    ps.setTitle("Manager Login");
	    GridPane gp = new GridPane();
	    gp.setAlignment(Pos.CENTER);
	    gp.setHgap(10);
	    gp.setVgap(10);
	    Scene sc = new Scene(gp, 1000, 1000);
	    
	    Label lblUser = new Label("Username: ");
	    Label lblPassw = new Label("Password: ");
	    TextField tfUser = new TextField();
	    PasswordField pfPassw = new PasswordField();
	    Button btnSignIn = new Button("Sign in");
	    Label lblResponse = new Label();
	    
	    gp.add(lblUser, 3, 0);
	    gp.add(tfUser, 4, 0);
	    gp.add(lblPassw, 3, 1);
	    gp.add(pfPassw, 4, 1);
	    gp.add(btnSignIn, 4, 5);
	    
	    btnSignIn.setOnAction(new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent o) {
	            if (tfUser.getText().equals("flight_manager") && pfPassw.getText().equals("airindia@123")) {
	                lblResponse.setText("Welcome " + tfUser.getText());
	                gp.add(lblResponse, 3, 2);
	                man_authenticated = true;
	                
	                Label lblRespOption = new Label();
	                
	                ComboBox<String> cbox = new ComboBox<>();
	                ObservableList<String> ol = FXCollections.observableArrayList(
	                    "Add Flight", "Schedule Flight", "Update Flight Schedule", "Sign Out");
	                cbox.setItems(ol);
	                
	                // Clear previous content on ComboBox selection to avoid duplicate nodes
	                cbox.setOnAction(new EventHandler<ActionEvent>() {
	                    public void handle(ActionEvent o) {
	                        String opt = cbox.getValue();
	                        lblRespOption.setText("Option: " + opt);
	                        
	                        // Clear previous results to avoid duplicate components
	                        gp.getChildren().remove(lblRespOption);
	                        gp.add(lblRespOption, 3, 3);
	                        
	                        if (opt.equals("Add Flight")) {
	                            // Call method to add flight interface
	                            m1.JavaFxAddFlight(gp);
	                        } else if (opt.equals("Schedule Flight")) {
	                            // Call method to schedule flight interface
	                            m1.JavaFxScheduleFlight(gp);
	                        } else if (opt.equals("Update Flight Schedule")) {
	                            // Call method to update flight schedule interface
	                            m1.JavaFxUpdateFlight(gp);
	                        } else if (opt.equals("Sign Out")) {
	                            Label lblSignOut = new Label("Signing Out of Manager!");
	                            gp.getChildren().remove(lblSignOut); // remove if already exists
	                            gp.add(lblSignOut, 3, 4);
	                        }
	                    }
	                });

	                gp.add(cbox, 4, 6);
	            } else {
	                lblResponse.setText("Invalid Credentials!");
	                man_authenticated = false;
	                gp.getChildren().remove(lblResponse); // avoid re-adding the label multiple times
	                gp.add(lblResponse, 4, 2);
	            }
	        }
	    });

	    ps.setScene(sc);
	    ps.show();
	}

    public static boolean CheckCredentials()  throws InvalidCredentialException
    {
        if (man_authenticated)
        {
            System.out.println("Successfully Logged In as Manager!");
            return true;
        }
        else
            throw new InvalidCredentialException();    
    }  
}
