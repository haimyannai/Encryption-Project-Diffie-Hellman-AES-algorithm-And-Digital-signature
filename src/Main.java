import java.math.BigInteger;
import java.util.Scanner;

public class Main {

	public static Bank getBank() {
		return bank;
	}

	private static Bank bank;
	private static String clientName;
	private static String clientPassword;
	private static AES aes;
	private static Client client;

	private static int key; // the shared private key



	public static void main(String[] args) throws Exception {
		bank = new Bank();
		bank.initBank();
		key = client.diffieHellman(); //calling diffie hellman function
		aes = Utils.setAESKey(key); //call function
		Scanner scan = new Scanner(System.in);
		while(true) {
			System.out.println("Please Enter your client-Name:");
			clientName = scan.nextLine();
			System.out.println("Please Enter your Password:");
			clientPassword = scan.nextLine();
			if((client = checkIfExist())!=null) {
				break;
			}
			else
				System.out.println("The client name or the password is incorrect Try again.");
		}
		System.out.println("Please enter the Reciver id: ");
		String receiverText = scan.nextLine();
		System.out.println("Enter amount to transfer: ");
		String amountText = scan.nextLine();
		
		//##### Input Finish #######
		
		String srcMsg= client.getId() + receiverText + Utils.padMsg(amountText) + amountText;
		System.out.println("\nThe text before encryption (plain text) is:" + srcMsg);
		receiverText = Utils.padId(Integer.toHexString(Integer.parseInt(receiverText)));
		String senderText = Utils.padId(Integer.toHexString(Integer.parseInt(client.getId())));
		String hexMsg = ( senderText + receiverText +  Utils.padMsg(amountText) + Integer.toHexString(Integer.parseInt(amountText))).toUpperCase();
		System.out.println("The text as a HEX number: " + hexMsg);
		String digest = hexMsg.substring(0, 7);
		String digitalSignature = DigitalSignature.hashFunction(digest.getBytes());//generate the digital signature
		System.out.println("The digital signature of the application is: " + digitalSignature);
		String encryptMessage = aes.encrypt(hexMsg);//encrypt the massage
		System.out.println("The text after encryption of AES: " + encryptMessage);
		if (!bank.getMessage(encryptMessage,digitalSignature))
		{
			System.err.println("This receiver do not exist! Try again.");
		}
		else {//if the function return true
			System.out.println("The sender "+client.getName()+" account blance after transfer is: " + client.getCurrentAccount());//set text and update the balance
			System.out.println("Transfer process completed successfully!");
		}
	}

	/**
	 * checkIfExist check if the clientname is exist in the clients
	 * @return - return NULL if the client not exist otherwise the client itself
	 */
	private static Client checkIfExist() {
		return bank.checkIfExist(clientName,clientPassword);
	}

}
