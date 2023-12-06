package main.java;

import static main.java.service.Iso8583MessageService.processISO8583RequestAndResponse;
import static main.java.util.Iso8583Util.REQUEST_DATA;

public class Iso8583Converter {

  public static void main(String[] args) throws InterruptedException {

    processISO8583RequestAndResponse(REQUEST_DATA);
  }
}
