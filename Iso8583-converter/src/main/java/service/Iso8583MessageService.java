package main.java.service;

import static main.java.util.Iso8583Util.ACQUIRER_INST;
import static main.java.util.Iso8583Util.ICC_DATA;
import static main.java.util.Iso8583Util.PAN;
import static main.java.util.Iso8583Util.TRACK_2;
import static main.java.util.Iso8583Util.createISO8583Response;
import static main.java.util.Iso8583Util.hexToAscii;
import static main.java.util.Iso8583Util.hexToBinary;
import static main.java.util.Iso8583Util.parseDE55Request;
import static main.java.util.Iso8583Util.parseDE55Response;
import static main.java.util.Iso8583Util.requiredDataElements;

import java.util.ArrayList;
import java.util.List;
import main.java.dto.DataElement;
import main.java.dto.ParsedDataElement;
import main.java.dto.ParsedDataRequest;

public class Iso8583MessageService {

  public static void processISO8583RequestAndResponse(String requestData)
      throws InterruptedException {

    ParsedDataRequest parsedDataRequest = parseISO8583RequestMessage(requestData);

    Thread.sleep(1000);

    String responseMessage = createISO8583Response(parsedDataRequest);
    System.out.println("Receiving Response Message <== [" + responseMessage + "]");

    parseISO8583Response(responseMessage);
  }

  public static ParsedDataRequest parseISO8583RequestMessage(String rawContent) {

    System.out.println("Sending Request Message ==> [" + rawContent + "]");
    int currentIndex = 0;

    String msgTotalLengthHex = rawContent.substring(currentIndex, currentIndex + 4);
    int totalLength = Integer.parseInt(msgTotalLengthHex, 16) * 2;
    currentIndex += 4;

    System.out.println(totalLength + " total length of the message");

    String messageTypeHex = rawContent.substring(currentIndex, currentIndex + 8);
    currentIndex += 8;

    String msgType = hexToAscii(messageTypeHex);
    System.out.println("Message Type: " + hexToAscii(messageTypeHex));

    String bitmapHex = rawContent.substring(currentIndex, currentIndex + 32);
    currentIndex += 32;

    String bitmap = hexToAscii(bitmapHex);
    System.out.println("Bitmap: " + bitmap);

    String bitmapBinary = hexToBinary(bitmap);

    List<ParsedDataElement> parsedDataElements = new ArrayList<>();
    List<DataElement> dataElements = requiredDataElements(bitmapBinary);

    for (DataElement dataElement : dataElements) {

      String deNumber = String.format(" out [ %d ]", dataElement.getNumber());
      String fieldName = dataElement.getName();
      String fieldValue = "";

      if (ICC_DATA.equals(dataElement.getName())) {
        String iccDataLenHex = rawContent.substring(currentIndex, currentIndex + 6);
        currentIndex += 6;

        int iccDataLength = Integer.parseInt(hexToAscii(iccDataLenHex));
        dataElement.setLength(iccDataLength * 2);

        String iccData = rawContent.substring(currentIndex, currentIndex + iccDataLength * 2);
        currentIndex += iccDataLength * 2;

        fieldValue = "(" + iccDataLength + ") " + iccData;
        System.out.println(deNumber + " " + fieldName + " : " + fieldValue);

        parseDE55Request(iccData);
      } else if (PAN.equals(dataElement.getName())) {

        String panLenHex = rawContent.substring(currentIndex, currentIndex + 4);
        currentIndex += 4;

        int panLength = Integer.parseInt(hexToAscii(panLenHex));
        dataElement.setLength(panLength * 2);

        String panValue = rawContent.substring(currentIndex, currentIndex + panLength * 2);
        currentIndex += panLength * 2;

        fieldValue = hexToAscii(panValue);
        System.out.println(
            deNumber + " " + fieldName + " : " + "(" + panLength + ") " + hexToAscii(panValue));

      } else if (ACQUIRER_INST.equals(dataElement.getName())) {

        String acqInstLenHex = rawContent.substring(currentIndex, currentIndex + 4);
        currentIndex += 4;

        int acqInstLength = Integer.parseInt(hexToAscii(acqInstLenHex));
        dataElement.setLength(acqInstLength * 2);

        String acqInstValue = rawContent.substring(currentIndex, currentIndex + acqInstLength * 2);
        currentIndex += acqInstLength * 2;

        fieldValue = hexToAscii(acqInstValue);
        System.out.println(
            deNumber
                + " "
                + fieldName
                + " : "
                + "("
                + acqInstLength
                + ") "
                + hexToAscii(acqInstValue));

      } else if (TRACK_2.equals(dataElement.getName())) {

        String track2Hex = rawContent.substring(currentIndex, currentIndex + 4);
        currentIndex += 4;

        int track2Length = Integer.parseInt(hexToAscii(track2Hex));
        dataElement.setLength(track2Length * 2);

        String track2Value = rawContent.substring(currentIndex, currentIndex + track2Length * 2);
        currentIndex += track2Length * 2;

        fieldValue = hexToAscii(track2Value);
        System.out.println(
            deNumber
                + " "
                + fieldName
                + " : "
                + "("
                + track2Length
                + ") "
                + hexToAscii(track2Value));

      } else {

        String fieldValueHex =
            rawContent.substring(currentIndex, currentIndex + dataElement.getLength() * 2);
        currentIndex += dataElement.getLength() * 2;

        fieldValue = hexToAscii(fieldValueHex);
        System.out.println(deNumber + " " + fieldName + " : " + fieldValue);
      }

      parsedDataElements.add(new ParsedDataElement(dataElement.getNumber(), fieldName, fieldValue));
    }

    return new ParsedDataRequest(msgType, bitmap, parsedDataElements);
  }

  private static void parseISO8583Response(String rawMessage) {
    int currentIndex = 0;

    String msgTotalLengthHex = rawMessage.substring(currentIndex, currentIndex + 4);
    int totalLength = Integer.parseInt(msgTotalLengthHex, 16) * 2;
    currentIndex += 4;

    System.out.println(totalLength + " total length of the message");

    String messageTypeHex = rawMessage.substring(currentIndex, currentIndex + 8);
    currentIndex += 8;
    System.out.println("Message Type: " + hexToAscii(messageTypeHex));

    String primaryBitmapHex = rawMessage.substring(currentIndex, currentIndex + 32);
    currentIndex += 32;

    String bitmap = hexToAscii(primaryBitmapHex);
    System.out.println("Bitmap: " + bitmap);

    String primaryBitmapBinary = hexToBinary(bitmap);

    List<DataElement> dataElements = requiredDataElements(primaryBitmapBinary);

    for (DataElement dataElement : dataElements) {
      String deNumber = String.format(" in [ %d ]", dataElement.getNumber());
      String fieldName = dataElement.getName();

      if (PAN.equals(fieldName)) {
        String panLenHex = rawMessage.substring(currentIndex, currentIndex + 4);
        currentIndex += 4;
        int panLength = Integer.parseInt(hexToAscii(panLenHex));
        String panValue = rawMessage.substring(currentIndex, currentIndex + panLength * 2);
        currentIndex += panLength * 2;
        String fieldValue = "(" + panLength + ") " + hexToAscii(panValue);
        System.out.println(deNumber + " " + fieldName + " : " + fieldValue);

      } else if (ACQUIRER_INST.equals(fieldName)) {

        String acqInstLenHex = rawMessage.substring(currentIndex, currentIndex + 4);
        currentIndex += 4;

        int acqInstLength = Integer.parseInt(hexToAscii(acqInstLenHex));
        String acqInstValue = rawMessage.substring(currentIndex, currentIndex + acqInstLength * 2);
        currentIndex += acqInstLength * 2;

        String fieldValue = "(" + acqInstLength + ") " + hexToAscii(acqInstValue);
        System.out.println(deNumber + " " + fieldName + " : " + fieldValue);

      } else if (TRACK_2.equals(fieldName)) {

        String track2Hex = rawMessage.substring(currentIndex, currentIndex + 4);
        currentIndex += 4;

        int track2Length = Integer.parseInt(hexToAscii(track2Hex));
        String track2Value = rawMessage.substring(currentIndex, currentIndex + track2Length * 2);
        currentIndex += track2Length * 2;

        String fieldValue = hexToAscii(track2Value);
        System.out.println(
            deNumber + " " + fieldName + " : " + "(" + track2Length + ") " + fieldValue);

      } else if (ICC_DATA.equals(fieldName)) {

        String iccDataLenHex = rawMessage.substring(currentIndex, currentIndex + 6);
        currentIndex += 6;

        int iccDataLength = Integer.parseInt(hexToAscii(iccDataLenHex));
        dataElement.setLength(iccDataLength * 2);

        String iccData = rawMessage.substring(currentIndex, currentIndex + iccDataLength * 2);
        currentIndex += iccDataLength * 2;

        String fieldValue = "(" + iccDataLength + ") " + iccData;
        System.out.println(deNumber + " " + fieldName + " : " + fieldValue);

        parseDE55Response(iccData);
      } else {
        String fieldValueHex =
            rawMessage.substring(currentIndex, currentIndex + dataElement.getLength() * 2);
        currentIndex += dataElement.getLength() * 2;
        String fieldValue = hexToAscii(fieldValueHex);
        System.out.println(deNumber + " " + fieldName + " : " + fieldValue);
      }
    }
  }
}
