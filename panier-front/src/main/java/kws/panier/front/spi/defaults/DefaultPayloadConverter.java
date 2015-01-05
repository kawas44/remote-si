package kws.panier.front.spi.defaults;

import kws.panier.front.api.RequestPayload;
import kws.panier.front.api.ResponsePayload;
import kws.panier.front.spi.PayloadConversionException;
import kws.panier.front.spi.PayloadConverter;

import java.io.*;


public class DefaultPayloadConverter implements PayloadConverter {

    @Override
    public byte[] requestToBytes(RequestPayload payload) throws
            PayloadConversionException {
        return toBytes(payload.getValue());
    }

    @Override
    public RequestPayload requestFromBytes(byte[] payload) throws
            PayloadConversionException {
        return RequestPayload.instance(fromBytes(payload));
    }

    @Override
    public byte[] responseToBytes(ResponsePayload payload) throws
            PayloadConversionException {
        return toBytes(payload.getValue());
    }

    @Override
    public ResponsePayload responseFromBytes(byte[] payload) throws
            PayloadConversionException {
        return ResponsePayload.instance(fromBytes(payload));
    }

    private byte[] toBytes(Object value) throws PayloadConversionException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(value);
            return baos.toByteArray();
        } catch (IOException ex) {
            throw new PayloadConversionException(ex);
        }
    }

    private Object fromBytes(byte[] payload) throws PayloadConversionException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(payload);
             ObjectInputStream ois = new ObjectInputStream(bais)) {

            return ois.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            throw new PayloadConversionException(ex);
        }
    }

}
