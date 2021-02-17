import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.sun.javafx.webkit.ThemeClientImpl;

public class Bank {
private Map<String, Client> clients;
/*private key for diffie hellman*/
private int b = 107112;
/*secret key for diffie hellman*/
private int k;

private AES aes;

public Bank() {
	
	clients = new HashMap<>(); //Initialize the client collection with key and value.
}

//initialize of the clients 
public void initBank() {
	Client Dorin = new Client("205714272", "Dorin");
	Client Shahar = new Client("308289396","Shahar");
	Client Remez = new Client("316312503", "Remez");
	Client Haim = new Client("307962340", "Haim");

	
	//initialize of clients money 
	Dorin.setCurrentAccount(3400);
	Shahar.setCurrentAccount(7800);
	Remez.setCurrentAccount(1200);
	Haim.setCurrentAccount(2300);;
	
	//insert to the collection
	clients.put(Dorin.getId(), Dorin);
	clients.put(Shahar.getId(), Shahar);
	clients.put(Remez.getId(), Remez);
	clients.put(Haim.getId(), Haim);
}

/**
 * check if the client is exist in the App
 * @param username - username of the client in the app 
 * @param password - password of the client in the app 
 * @return - return NULL if the client is not exist otherwise return the client itself
 */
public Client checkIfExist(String username , String password) {
	if (clients.containsKey(username))
	{
		Client user = clients.get(username);
		 if (user.getAppPassword().equals(password))
			 return user;
	}
	return null;
}

/**
 * update the amount of the client 
 * @param client - the client that need to update his amount
 * @param amount 
 */

private void updateCurrentAmount(Client client , int amount)
{
	client.setCurrentAccount(client.getCurrentAccount() + amount);
}

/**
 * diffieHellman function calculate parameter B and k.
 * send B to the application.
 * k in this function and in the bank function need to be equal.
 * @param p - the prime number
 * @param g - the generator
 * @param A - the public key of the app
 * @return B - the public key of the bank
 * @throws Exception 
 */

/* Key exchange between pairs */
public BigInteger diffieHellman(int p, int g, BigInteger A) throws Exception {
	BigInteger temp = BigInteger.valueOf(g);
	BigInteger B = temp.modPow(BigInteger.valueOf(b), BigInteger.valueOf(p));//(g^b)mod(p)
	System.out.println("The public key of the bank B :" + B);
	BigInteger k = A.modPow(BigInteger.valueOf(b), BigInteger.valueOf(p));//
	System.out.println("The secret key of the bank K :" + k);
	this.k = k.intValue();
	setAESKey(this.k);
	return B;
}

/**
 * getMessage function decrypt with AES alg the massage and then check
 * if the app sign on the massage
 * @param encryptMessage - the encrypt massage from the app
 * @param digitalSignature - the digital signature from the original massage
 * @return - return true if success to decrypt the massage
 * and the digital signature from the decrypt massage is equal to
 * the parameter digitalSignature otherwise return false. 
 */
public boolean getMessage(String encryptMessage, String digitalSignature) {
	String plainText = aes.decrypt(encryptMessage); //doing the decryption
	String digest = plainText.substring(0, 7);
	String digitalSig = DigitalSignature.hashFunction(digest.getBytes());//generate the digital signature from the decrypt massage 
	System.out.println("The digital signature of the bank: " + digitalSig);
	if (!(digitalSig.equals(digitalSignature))) 
		return false;
	else {
		String hexSender = plainText.substring(0, 10); //get the sender 
		String hexReceiver = plainText.substring(10, 20); //get the receiver
		String hexAmount = plainText.substring(20);
		String sender = String.valueOf(Integer.parseInt(hexSender, 16)); //convert to decimal number 
		String receiver = String.valueOf(Integer.parseInt(hexReceiver, 16)); //convert to decimal number 
		int amount = Integer.parseInt(hexAmount, 16);

		if (!(clients.containsKey(receiver))) { // check if the receiver client is exist in the bank
			System.out.println("Reciver '"+receiver+"' - not found!");
			return false;
		}
		else {
			updateCurrentAmount(clients.get(sender), 0-amount); 
			updateCurrentAmount(clients.get(receiver), amount);
			return true;
		}
	}
}




/**
 * setAESKet function check the key length.
  if length(k)<32 need to padding the rest until 32 bits.
  else length(k)>32 need to delete from the index 33 until the end
 * @param k - the key
 * @throws Exception
 */
private void setAESKey(int k) throws Exception{
	String keyString = String.valueOf(k);
	char [] hexArr = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'}; //for padding if needed
	if (keyString.length() < Utils.AES_KEY_SIZE)
	{
		int len = Utils.AES_KEY_SIZE - keyString.length();
		for (int i = 0 ; i < len ; i++)
		{
			char ch = hexArr[i % 16];
			keyString = keyString + ch;
		}	
	}
	else 
		keyString=keyString.substring(0, 32);
	aes = new AES(keyString); //create AES with the key
}


}
