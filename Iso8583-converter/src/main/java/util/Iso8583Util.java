package main.java.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import main.java.dto.DataElement;
import main.java.dto.ParsedDataElement;
import main.java.dto.ParsedDataRequest;

public class Iso8583Util {

  public static final Map<String, String> TAG55_REQUEST_MAP;
  public static final Map<String, String> TAG55_RESPONSE_MAP;

  public static final String ICC_DATA = "ICC Data";
  public static final String PAN = "PAN";
  public static final String ACQUIRER_INST = "Acquiring Inst. Code";
  public static final String TRACK_2 = "Track 2 Data";
  public static final String AUTH_NUMBER = "123456";
  public static final String RESPONSE_CODE = "00";
  public static final String ICC_DATA_RESP = "911050B4BC3291184DF00012";

  public static String REQUEST_DATA =
      "0155303230303732333836363831323845303932303031363437363133343030303030303030373631323030303130303"
          + "0303030303130303030303131323137353234363030323031363039353234363131323136303131343232353131303031"
          + "303230363939393730313337343736313334303030303030303037363d323530393230313131393034373733303030303"
          + "03039303139323230303030314d54462054455354414243313233544553544d5446313942414e4b204155444920424144"
          + "41524f2d3620202020202020424549525554202020202020204c423834303330323439424646393741344331323531303"
          + "0820218008407a0000000031010950580800480009a032311239c01315f2a0208269f02060000000000009f1007060512"
          + "03a010109f1a0208269f2608c9c177b3ea0784be9f2701809f33036040209f34030000009f360200029f3704ad5278ea9f53015a";

  static {
    TAG55_REQUEST_MAP = new HashMap<>();
    TAG55_REQUEST_MAP.put("82", "Application Interchange Profile");
    TAG55_REQUEST_MAP.put("84", "Dedicated File (DF) Name");
    TAG55_REQUEST_MAP.put("95", "Terminal Verification Results");
    TAG55_REQUEST_MAP.put("9a", "Transaction Date");
    TAG55_REQUEST_MAP.put("9c", "Transaction Type");
    TAG55_REQUEST_MAP.put("5f2a", "Transaction Currency Code");
    TAG55_REQUEST_MAP.put("9f02", "Amount, Authorized (Numeric)");
    TAG55_REQUEST_MAP.put("9f10", "Issuer Application Data");
    TAG55_REQUEST_MAP.put("9f1a", "Terminal Country Code");
    TAG55_REQUEST_MAP.put("9f26", "Application Cryptogram");
    TAG55_REQUEST_MAP.put("9f27", "Cryptogram Information Data");
    TAG55_REQUEST_MAP.put("9f33", "Terminal Capabilities");
    TAG55_REQUEST_MAP.put("9f34", "Cardholder Verification Method (CVM) Results");
    TAG55_REQUEST_MAP.put("9f36", "Application Transaction Counter (ATC)");
    TAG55_REQUEST_MAP.put("9f37", "Unpredictable Number");
    TAG55_REQUEST_MAP.put("9f53", "Transaction Category Code");
  }

  static {
    TAG55_RESPONSE_MAP = new HashMap<>();
    TAG55_RESPONSE_MAP.put("91", "Issuer Authentication Data");
    TAG55_RESPONSE_MAP.put("8A", "Authorization Response Code");
    TAG55_RESPONSE_MAP.put("71", "Issuer Script Data 1");
    TAG55_RESPONSE_MAP.put("72", "Issuer Script Data 2");
  }

  /*        Below Data Elements with Length in Byte                 */
  public static List<DataElement> dataElements() {
    List<DataElement> dataElements = new ArrayList<>();
    dataElements.add(new DataElement(2, "PAN", 0));
    dataElements.add(new DataElement(3, "PCODE", 6));
    dataElements.add(new DataElement(4, "Transaction Amount", 12));
    dataElements.add(new DataElement(7, "Transmission Date and Time", 10));
    dataElements.add(new DataElement(11, "Trace", 6));
    dataElements.add(new DataElement(12, "Local Transaction Time", 6));
    dataElements.add(new DataElement(13, "Local Transaction Date", 4));
    dataElements.add(new DataElement(18, "Merchant Type", 4));
    dataElements.add(new DataElement(19, "Acquiring Inst. Country Code", 3));
    dataElements.add(new DataElement(22, "POS Entry Mode", 3));
    dataElements.add(new DataElement(23, "Card Sequence Number", 3));
    dataElements.add(new DataElement(25, "POS Condition Code", 2));
    dataElements.add(new DataElement(32, "Acquiring Inst. Code", 0));
    dataElements.add(new DataElement(35, "Track 2 Data", 0));
    dataElements.add(new DataElement(37, "Retrieval Reference Number", 12));
    dataElements.add(new DataElement(38, "Authorization Number", 6));
    dataElements.add(new DataElement(39, "Response Code", 2));
    dataElements.add(new DataElement(41, "Card Acceptor Terminal ID", 8));
    dataElements.add(new DataElement(42, "Card Acceptor ID Code", 15));
    dataElements.add(new DataElement(43, "Card Acceptor Name", 40));
    dataElements.add(new DataElement(49, "Transaction Currency Code", 3));
    dataElements.add(new DataElement(52, "Personal Identification Number", 16));
    dataElements.add(new DataElement(55, "ICC Data", 0));
    /*  More Data Elements can be added     */
    return dataElements;
  }

  public static void printTag55RequestDescription(String tag, int length, String value) {
    String description = TAG55_REQUEST_MAP.get(tag);
    if (description != null) {
      System.out.println(
          "  " + tag + " (" + description + "): Length = " + length + ", Value = " + value);
    } else {
      System.out.println(
          "Unknown Tag: " + tag + " (Length = " + length + ", Value = " + value + ")");
    }
  }

  public static void printTag55ResponseDescription(String tag, int length, String value) {
    String description = TAG55_RESPONSE_MAP.get(tag);
    if (description != null) {
      System.out.println(
          "  " + tag + " (" + description + "): Length = " + length + ", Value = " + value);
    } else {
      System.out.println(
          "Unknown Tag: " + tag + " (Length = " + length + ", Value = " + value + ")");
    }
  }

  public static String hexToAscii(String hex) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < hex.length(); i += 2) {
      String hexByte = hex.substring(i, i + 2);
      int decimal = Integer.parseInt(hexByte, 16);
      char asciiChar = (char) decimal;
      result.append(asciiChar);
    }
    return result.toString();
  }

  public static String convertToHex(String value) {
    StringBuilder hexString = new StringBuilder();

    for (char ch : value.toCharArray()) {
      hexString.append(String.format("%02X", (int) ch));
    }

    return hexString.toString();
  }

  public static String hexToBinary(String hex) {
    StringBuilder binaryStringBuilder = new StringBuilder();
    for (int i = 0; i < hex.length(); i++) {
      String binary = Integer.toBinaryString(Integer.parseInt(hex.substring(i, i + 1), 16));
      binaryStringBuilder.append(String.format("%4s", binary).replace(' ', '0'));
    }
    return binaryStringBuilder.toString();
  }

  public static List<DataElement> requiredDataElements(String bitmap) {
    List<DataElement> dataElements = new ArrayList<>();

    for (int i = 1; i <= bitmap.length(); i++) {
      if (bitmap.charAt(i - 1) == '1') {
        DataElement dataElement = findDataElementByNumber(i);
        if (dataElement != null) {
          dataElements.add(dataElement);
        }
      }
    }

    return dataElements;
  }

  public static DataElement findDataElementByNumber(int number) {

    for (DataElement element : dataElements()) {
      if (element.getNumber() == number) {
        return element;
      }
    }
    return null;
  }

  public static String binaryToHex(String binary) {
    long decimal = Long.parseLong(binary, 2);
    return Long.toHexString(decimal).toUpperCase();
  }

  public static void parseDE55Request(String de55Data) {
    Map<String, String> parsedData = new HashMap<>();

    for (int i = 0; i < de55Data.length(); ) {
      for (String tag : TAG55_REQUEST_MAP.keySet()) {
        int tagIndex = de55Data.indexOf(tag);

        if (tagIndex != -1) {

          int startIndex = tagIndex + tag.length();
          int length = parseLength(de55Data, startIndex) * 2;
          startIndex += 2;

          String value = de55Data.substring(startIndex, startIndex + length);
          parsedData.put(tag, value);

          i = i + tag.length() + 2 + value.length();
        } else {
          continue;
        }
      }
    }

    parsedData.forEach(
        (tag, value) -> printTag55RequestDescription(tag, value.length() / 2, value));
  }

  public static void parseDE55Response(String de55Data) {
    Map<String, String> parsedData = new HashMap<>();

    for (int i = 0; i < de55Data.length(); ) {
      for (String tag : TAG55_RESPONSE_MAP.keySet()) {
        int tagIndex = de55Data.indexOf(tag);

        if (tagIndex != -1) {

          int startIndex = tagIndex + tag.length();
          int length = parseLength(de55Data, startIndex) * 2;
          startIndex += 2;

          String value = de55Data.substring(startIndex, startIndex + length);
          parsedData.put(tag, value);

          i = i + tag.length() + 2 + value.length();
        } else {

          continue;
        }
      }
    }

    parsedData.forEach(
        (tag, value) -> printTag55ResponseDescription(tag, value.length() / 2, value));
  }

  private static int parseLength(String data, int index) {
    String lengthHex = data.substring(index, index + 2);
    return Integer.parseInt(lengthHex);
  }

  public static String createISO8583Response(ParsedDataRequest parsedDataRequest) {

    StringBuilder response = new StringBuilder();

    String messageTypeHex =
        parsedDataRequest.getMsgtype().equals("0200")
            ? convertToHex("0210")
            : convertToHex(parsedDataRequest.getMsgtype());
    response.append(messageTypeHex);

    String bitmapBinary = hexToBinary(parsedDataRequest.getBitmap());

    StringBuilder modifiedBitmap = new StringBuilder(bitmapBinary);
    modifiedBitmap.setCharAt(37, '1');
    modifiedBitmap.setCharAt(38, '1');

    modifiedBitmap.setCharAt(51, '0');

    String bitmapHex = binaryToHex(modifiedBitmap.toString());

    parsedDataRequest.addParsedDataElement(
        new ParsedDataElement(38, "Authorization Number", AUTH_NUMBER));
    parsedDataRequest.addParsedDataElement(
        new ParsedDataElement(39, "Response Code", RESPONSE_CODE));

    response.append(convertToHex(bitmapHex));

    for (int deNumber = 1; deNumber <= modifiedBitmap.length(); deNumber++) {

      ParsedDataElement parsedElement = getParsedDataElement(parsedDataRequest, deNumber);

      if (parsedElement != null && modifiedBitmap.charAt(deNumber - 1) == '1') {

        if (deNumber == 2) {
          response.append(convertToHex(String.valueOf(parsedElement.getValue().length())));
        }

        if (deNumber == 32) {
          String lengthHex = String.format("%02d", parsedElement.getValue().length());
          response.append(convertToHex(lengthHex));
        }

        if (deNumber == 35) {
          response.append(convertToHex(String.valueOf(parsedElement.getValue().length())));
        }

        if (deNumber == 55) {
          parsedElement.setValue(ICC_DATA_RESP);
          String lengthHex = String.format("%03d", parsedElement.getValue().length() / 2);
          response.append(convertToHex(lengthHex));
          response.append(parsedElement.getValue());
        } else response.append(convertToHex(parsedElement.getValue()));
      }
    }

    String totalLengthHex = String.format("%04X", response.length() / 2);

    response.insert(0, totalLengthHex);

    return response.toString();
  }

  private static ParsedDataElement getParsedDataElement(
      ParsedDataRequest parsedDataRequest, int deNumber) {
    for (ParsedDataElement parsedElement : parsedDataRequest.getParsedDataElements()) {
      if (parsedElement.getNumber() == deNumber) {
        return parsedElement;
      }
    }
    return null;
  }
}
