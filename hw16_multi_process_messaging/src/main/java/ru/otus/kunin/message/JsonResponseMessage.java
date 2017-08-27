package ru.otus.kunin.message;

import ru.otus.kunin.front.AddressableJsonRequest;
import ru.otus.kunin.messageSystem.AcyclicVisitorMessage;
import ru.otus.kunin.messageSystem.Address;

public class JsonResponseMessage extends AcyclicVisitorMessage<AddressableJsonRequest> {

  private final Object result;
  private final String error;

  public JsonResponseMessage(final Address from,
                             final Address to,
                             final Object result,
                             final String error) {
    super(from, to);
    this.result = result;
    this.error = error;
  }

  @Override
  protected void visit(final AddressableJsonRequest addressableJsonRequest) {
    addressableJsonRequest.sendResponse(result, error);
  }
}
