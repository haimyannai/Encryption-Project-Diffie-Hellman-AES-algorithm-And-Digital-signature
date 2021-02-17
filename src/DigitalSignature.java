
public class DigitalSignature {
	
	private static final char[] hexArr = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	/**
	 * hash function for the digital signature
	 * @param bytes
	 * @return
	 */
	public static String hashFunction(byte[] bytes) {
	    String str = "";
	    for(final byte b : bytes) {
	    	str = str + hexArr[(b & 0xF0) >> 4];
	    	str = str + hexArr[b & 0x0F];
	    }
	    return str;
	}
}



