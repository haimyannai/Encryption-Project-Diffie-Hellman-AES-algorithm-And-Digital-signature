import java.math.BigInteger;

public class Utils {

	public static final int AES_KEY_SIZE = 32; //size of the key in AES
	public static final int NUMBER_OF_DIGIT_IN_HEX_ID = 10;
	public static final int NUMBER_OF_DIGIT_IN_DEC_ID = 9;
	public static final int NUMBER_OF_HEX_LETTER = 32;
	
	

	public static String padMsg(String amountText) {
		int padSum = NUMBER_OF_HEX_LETTER - (2 * NUMBER_OF_DIGIT_IN_HEX_ID) - (Integer.toHexString(Integer.parseInt(amountText))).length();
		String amountTextPad = "";
		for (int i = 0; i < padSum ; i++) {//padding '0' before the amount and after the IDs 
			amountTextPad +="0";
		}
		//build the message
		return amountTextPad; 
	}
	public static String padId(String hexnum) {
		int len = hexnum.length();
		if (len < NUMBER_OF_DIGIT_IN_HEX_ID)
		{
			for (int i = NUMBER_OF_DIGIT_IN_HEX_ID - len ; i > 0; i-- )
				hexnum = '0' + hexnum;
		}
		return hexnum;
	}	
	
	public static AES setAESKey(int k) throws Exception{
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
		return new AES(keyString); //create AES with the key
	}
	
}
