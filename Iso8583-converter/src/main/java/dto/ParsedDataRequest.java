package main.java.dto;

import java.util.List;

public class ParsedDataRequest {
  private String msgtype;
  private String bitmap;
  private List<ParsedDataElement> parsedDataElements;

  public void addParsedDataElement(ParsedDataElement dataElement) {
    parsedDataElements.add(dataElement);
  }

  public ParsedDataRequest( String msgtype, String bitmap, List<ParsedDataElement> parsedDataElements) {
    this.msgtype = msgtype;
    this.bitmap = bitmap;
    this.parsedDataElements = parsedDataElements;
  }

  public String getMsgtype() {
    return msgtype;
  }

  public void setMsgtype(String msgtype) {
    this.msgtype = msgtype;
  }

  public String getBitmap() {
    return bitmap;
  }

  public void setBitmap(String bitmap) {
    this.bitmap = bitmap;
  }

  public List<ParsedDataElement> getParsedDataElements() {
    return parsedDataElements;
  }

  public void setParsedDataElements(List<ParsedDataElement> parsedDataElements) {
    this.parsedDataElements = parsedDataElements;
  }
}
