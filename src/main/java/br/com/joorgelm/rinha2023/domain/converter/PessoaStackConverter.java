package br.com.joorgelm.rinha2023.domain.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Converter
public class PessoaStackConverter implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ";";

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    @Override
    public String convertToDatabaseColumn(List<String> stringList) {

        if (Objects.isNull(stringList)) return Strings.EMPTY;

//        if (stringList.stream().anyMatch(PessoaStackConverter::isNumeric)) {
//            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY);
//        }

        return String.join(SPLIT_CHAR, stringList);
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        return string != null ? Arrays.asList(string.split(SPLIT_CHAR)) : emptyList();
    }
}
