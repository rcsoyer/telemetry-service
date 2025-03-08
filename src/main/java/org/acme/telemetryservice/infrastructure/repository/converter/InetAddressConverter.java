package org.acme.telemetryservice.infrastructure.repository.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.net.InetAddress;
import lombok.SneakyThrows;

@Converter(autoApply = true)
class InetAddressConverter implements AttributeConverter<InetAddress, String> {

    @Override
    public String convertToDatabaseColumn(final InetAddress remoteAddress) {
        return remoteAddress.toString();
    }

    @Override
    @SneakyThrows
    public InetAddress convertToEntityAttribute(final String remoteAddress) {
        return InetAddress.getByAddress(remoteAddress.getBytes());
    }
}
