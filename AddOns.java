package packgProject.Addons;

public class AddOns//must be put into a pckg made public
{
    public int addonID;
    public String addOnName;
    public double addOnPrice;
    public AddOns(int n,String name,double price)
    {
        addonID=n;
        addOnName=name;
        addOnPrice=price;
    }
}