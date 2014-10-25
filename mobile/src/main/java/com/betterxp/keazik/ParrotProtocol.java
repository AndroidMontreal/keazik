package com.betterxp.keazik;

public class ParrotProtocol {

	public static byte[] getRequest(String apiString) {
		return generateRequest("GET " + apiString);
	}

	public static byte[] setRequest(String apiString, String args) {
		return generateRequest("SET " + apiString + "?arg=" + args);
	}

	private static byte[] generateRequest(String requestString) {
		byte[] header1 = {0};
		byte[] header2 = {(byte) (requestString.length() + 3)};
		byte[] header3 = {Byte.MIN_VALUE};
		byte[] byteArray = requestString.getBytes();

		return concat(header1, header2, header3, byteArray);
	}

	private static byte[] concat(byte[]... arrays) {
		// Determine the length of the result array
		int totalLength = 0;
		for (int i = 0; i < arrays.length; i++) {
			totalLength += arrays[i].length;
		}

		// create the result array
		byte[] result = new byte[totalLength];

		// copy the source arrays into the result array
		int currentIndex = 0;
		for (int i = 0; i < arrays.length; i++) {
			System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
			currentIndex += arrays[i].length;
		}
		return result;
	}

}
