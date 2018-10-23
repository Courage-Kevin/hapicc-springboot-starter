package com.hapicc.common.json;

import java.io.IOException;

public interface JsonFormatter {

    String format(Object object) throws IOException;

    byte[] formatAsBytes(Object object) throws IOException;
}
