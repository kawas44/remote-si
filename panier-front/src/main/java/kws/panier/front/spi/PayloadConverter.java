package kws.panier.front.spi;

import kws.panier.front.api.RequestPayload;
import kws.panier.front.api.ResponsePayload;


public interface PayloadConverter {

    byte[] requestToBytes(RequestPayload payload) throws
            PayloadConversionException;

    RequestPayload requestFromBytes(byte[] payload) throws
            PayloadConversionException;

    byte[] responseToBytes(ResponsePayload payload) throws
            PayloadConversionException;

    ResponsePayload responseFromBytes(byte[] payload) throws
            PayloadConversionException;

}
