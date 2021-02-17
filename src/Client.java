import java.math.BigInteger;

public class Client {
private String clientId;
private String clientName;
private String appPassword;
private String accountNumber;
private float currentAccount;
private static int accountCounter = 100;

/*prime number for diffie hellman*/
private static int p = 107837;
/*generator for diffie hellman*/
private static int g = 8;
/*private key for diffie hellman*/
private static int a = 107821;
/*secret key for diffie hellman*/
private int k;


public Client (String id,String name) {
	clientId = id;
	clientName = name;
	accountNumber = genrateAccountNumber();
	currentAccount = 0;
	appPassword = clientName + "123"; 
}

public String getName() {
	return clientName;
}


public String getAppPassword() {
	return appPassword;
}

public static int getAccountCounter() {
	return accountCounter;
}


public String getId() {
	return clientId;
}

public String getAccountNumber() {
	return accountNumber;
}

public float getCurrentAccount() {
	return currentAccount;
}
public void setCurrentAccount(float currentAccount) {
	this.currentAccount = currentAccount;
}

private String genrateAccountNumber() {
	accountCounter++;
	return String.valueOf(accountCounter);
}

public static int diffieHellman() throws Exception {
	System.out.println("The parameters p and g are :" + p + " , " + g );
	BigInteger temp = BigInteger.valueOf(g);//we use BigInteger because the long type is not big enough 
	BigInteger A = temp.modPow(BigInteger.valueOf(a), BigInteger.valueOf(p));//(g^a)modp -> (gen^p_key_)mod(prime)
	System.out.println("The public key of the Application A :" + A.toString());
	BigInteger B = Main.getBank().diffieHellman(p,g,A); 
	BigInteger k = B.modPow(BigInteger.valueOf(a), BigInteger.valueOf(p));
	System.out.println("The secret key of the Application k :" + k.toString());
	return k.intValue();
}

}
